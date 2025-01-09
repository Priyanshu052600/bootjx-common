package com.boot.jx.sso.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.jx.http.CommonHttpRequest;
import com.boot.model.MapModel;
import com.boot.utils.ArgUtil;
import com.boot.utils.FileUtil;
import com.boot.utils.IoUtils;
import com.boot.utils.Urly;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.javachinna.oauth2.user.CommonOAuth2UserInfo;
import com.javachinna.oauth2.user.OAuth2UserInfo;
import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class AppleAuthenticator extends AbstractAuthenticator {

	private static final GsonFactory jacksonFactory = new GsonFactory();
	private static final NetHttpTransport netHttpTransport = new NetHttpTransport();

	@Autowired
	CommonHttpRequest commonHttpRequest;

	private MapModel appleCreds;

	public MapModel appleCreds() {
		if (this.appleCreds == null) {
			try {
				InputStream leftStream = FileUtil.getExternalOrInternalResourceAsStream("providers/apple-creds.json",
						AppleAuthenticator.class);
				String leftJson = IoUtils.inputstream_to_string(leftStream);
				this.appleCreds = MapModel.from(leftJson);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return appleCreds;
	}

	@Override
	public OAuth2UserInfo doAuthenticate(String provider, ChannelPartner partner, MapModel body) throws Exception {
		String code = commonHttpRequest.getRequestParam("code");
		MapModel appleCreds = appleCreds();
		String clientId = appleCreds.pathEntry("clientId").asString();
		String grantType = appleCreds.pathEntry("authorization-grant-type").asString();
		String tokenUrl = appleCreds.pathEntry("token-uri").asString();
		String token = null;

		if (ArgUtil.is(code)) {
			MapModel tokenResponse = restService.ajax(tokenUrl).field("code", code).field("client_id", clientId)
					.field("client_secret", generateSecretKey()).field("grant_type", grantType)
					.field("redirect_uri", redirectUrl(provider, partner)).submit().asMapModel();
			token = tokenResponse.keyEntry("id_token").asString();
		}
		if (ArgUtil.is(token)) {
			return getUserInfo(token);
		}
		return null;

	}

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner)
			throws MalformedURLException, URISyntaxException, IOException {
		return this.createAuthUrl(provider, partner, redirectUrl(provider, partner));
	}

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner, String redirectUrl)
			throws MalformedURLException, URISyntaxException {
		MapModel appleCreds = appleCreds();
		String appleOuthUrl = appleCreds.pathEntry("authorizationUri").asString();
		String clientId = appleCreds.pathEntry("clientId").asString();
		return Urly.parse(appleOuthUrl).queryParam("response_type", "code").queryParam("client_id", clientId)
				.queryParam("scope", "openid%20name%20email").queryParam("redirect_uri", redirectUrl).getURL();
	}

	@Override
	public OAuth2UserInfo doAuthenticateDirect(String provider, ChannelPartner partner, MapModel body)
			throws Exception {
		String token = (String) body.get("token");
		if (ArgUtil.is(token)) {
			return getUserInfo(token);
		}
		return null;
	}

	public String generateSecretKey() throws Exception {
		MapModel appleCreds = appleCreds();
		String keyId = appleCreds.pathEntry("key-id").asString();
		String issuer = appleCreds.pathEntry("team-id").asString();
		String audience = appleCreds.pathEntry("audience").asString();
		String subject = appleCreds.pathEntry("clientId").asString();

		PrivateKey pKey = generatePrivateKey();
		return Jwts.builder().setHeaderParam("kid", keyId).setIssuer(issuer).setAudience(audience).setSubject(subject)
				.setExpiration(new Date(System.currentTimeMillis() + (10000 * 60 * 5)))
				.setIssuedAt(new Date(System.currentTimeMillis())).signWith(SignatureAlgorithm.ES256, pKey).compact();
	}

	public PrivateKey generatePrivateKey() throws Exception {
		MapModel appleCreds = appleCreds();
		String pkey = appleCreds.pathEntry("private-key").asString();

		String privateKeyContent = pkey.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "");

		// Decode and generate the private key
		byte[] pkcs8EncodedKey = Base64.getDecoder().decode(privateKeyContent);
		KeyFactory factory = KeyFactory.getInstance("EC");
		return factory.generatePrivate(new PKCS8EncodedKeySpec(pkcs8EncodedKey));
	}

	public static String getKidFromIdToken(String idToken) throws Exception {
		String[] parts = idToken.split("\\.");
		String header = new String(Base64.getDecoder().decode(parts[0]));
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readTree(header).get("kid").asText();
	}

	public OAuth2UserInfo getUserInfo(String idToken) throws Exception {
		String kid = getKidFromIdToken(idToken);
		Claims claims = Jwts.parserBuilder().setSigningKey(getPublicKey(kid)).build()
				.parseClaimsJws(idToken).getBody();
		// Extract user details from the claims
		String email = claims.get("email", String.class);
		Boolean emailVerified = claims.get("email_verified", Boolean.class);
		String sub = claims.get("sub", String.class);
		String givenName = claims.get("name", String.class);

		OAuth2UserInfo userinfo = new CommonOAuth2UserInfo();
		userinfo.setEmail(email);
		userinfo.setProvider("apple");
		userinfo.setProfileId(sub);
		userinfo.setName(givenName);

		return userinfo;
	}

	public PublicKey getPublicKey(String kid) throws Exception {
		MapModel appleCreds = appleCreds();
		String appleJWKSUrl = appleCreds.pathEntry("apple-jwks-url").asString();
		// Fetch the JWKS from Apple
		URL url = new URL(appleJWKSUrl);
		InputStream inputStream = url.openStream();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jwks = objectMapper.readTree(inputStream).get("keys");

		for (JsonNode key : jwks) {
			if (kid.equals(key.get("kid").asText())) {
				// Decode the modulus and exponent
				String n = key.get("n").asText();
				String e = key.get("e").asText();

				byte[] modulusBytes = Base64.getUrlDecoder().decode(n);
				byte[] exponentBytes = Base64.getUrlDecoder().decode(e);

				BigInteger modulus = new BigInteger(1, modulusBytes);
				BigInteger exponent = new BigInteger(1, exponentBytes);

				// Create the RSA public key
				RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				return keyFactory.generatePublic(publicKeySpec);
			}
		}

		throw new IllegalArgumentException("No matching key found for kid: " + kid);
	}

}
