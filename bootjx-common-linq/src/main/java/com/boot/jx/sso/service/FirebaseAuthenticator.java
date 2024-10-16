package com.boot.jx.sso.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.boot.jx.auth.AuthStateManager;
import com.boot.jx.auth.AuthStateManager.AuthState;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.model.MapModel;
import com.boot.utils.ArgUtil;
import com.boot.utils.FileUtil;
import com.boot.utils.Urly;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.javachinna.oauth2.user.CommonOAuth2UserInfo;
import com.javachinna.oauth2.user.OAuth2UserInfo;
import com.javachinna.oauth2.user.SocialEnums.ChannelPartner;

@Component
public class FirebaseAuthenticator extends AbstractAuthenticator {

	private static final long serialVersionUID = 6686144045235586188L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseAuthenticator.class);
	private static final String SCOPES = "https://www.googleapis.com/auth/firebase";

	private static final GsonFactory jacksonFactory = new GsonFactory();
	private static final NetHttpTransport netHttpTransport = new NetHttpTransport();

	@Autowired
	CommonHttpRequest commonHttpRequest;

	private GoogleCredentials googleCredentials;
	private FirebaseOptions firebaseOptions;
	private FirebaseAuth firebaseAuth;

	@Value("${firebase.db.url:}")
	String firebaseDbUrl;

	@Value("${firebase.project.id:}")
	String firebaseProjectid;

	@Value("${firebase.web.apikey:}")
	String firebaseWebApikey;

	@Value("${firebase.storage.bucket:}")
	String firebaseStorageBucket;

	@Value("${firebase.msg.senderId:}")
	String firebaseMsgSenderId;

	@Value("${firebase.auth.domain:}")
	String firebaseAuthDomain;

	@Value("${firebase.measurementId:}")
	String firebaseMeasurementId;

	@Value("${firebase.appId:}")
	String firebaseAppId;

	public GoogleCredentials googleCreds() {
		if (this.googleCredentials == null) {
			try {
				InputStream leftStream = FileUtil.getExternalOrInternalResourceAsStream(
						"providers/truelinq-firebase-adminsdk.json", FirebaseAuthenticator.class);
				if (ArgUtil.is(leftStream)) {
					googleCredentials = GoogleCredentials.fromStream(leftStream).createScoped(Arrays.asList(SCOPES));
					leftStream.close();
					googleCredentials.refresh();
				}
				return googleCredentials;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return googleCredentials;
	}

	public FirebaseOptions firebaseOptions() {
		if (this.firebaseOptions == null) {
			GoogleCredentials creds = googleCreds();
			if (ArgUtil.is(creds)) {
				this.firebaseOptions = FirebaseOptions.builder().setCredentials(creds)
						.setDatabaseUrl(firebaseDbUrl + "/").setProjectId(firebaseProjectid)

						.setStorageBucket(firebaseStorageBucket).build();
			}
		}
		return firebaseOptions;
	}

	public FirebaseAuth getInstance() {
		if (this.firebaseAuth == null) {
			this.firebaseAuth = FirebaseAuth.getInstance();
		}
		return firebaseAuth;
	}

	@PostConstruct
	public void init() {
		FirebaseOptions options = firebaseOptions();
		if (ArgUtil.is(options)) {
			FirebaseApp.initializeApp(options);
		}
	}

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner)
			throws MalformedURLException, URISyntaxException {
		return this.createAuthUrl(provider, partner, getUrl("/linq/app/v1/connect/" + provider + "/callback"));
	}

	@Override
	public String createAuthUrl(String provider, ChannelPartner partner, String redirectUri)
			throws MalformedURLException, URISyntaxException {
		AuthState state = authStateManager.createState();

		return Urly.parse(getUrl("/linq/pub/v1/connect/firebase/" + provider))//
				.queryParam("response_type", "code") //
				// .queryParam("client_id", clientId) //
				.queryParam("redirect_uri", redirectUri).queryParam("state", state.toString()) // State
				.queryParam("nonce", state.getNonce()) //
				// .queryParam("scope", scopes) //
				.queryParam("response_mode", "form_post") //
				.getURL();
	}

	public OAuth2UserInfo doAuthenticate(String provider, ChannelPartner partner, String idToken)
			throws FirebaseAuthException {
		FirebaseToken decodedToken = getInstance().verifyIdToken(idToken);
		String uid = decodedToken.getUid();
		OAuth2UserInfo userinfo = new CommonOAuth2UserInfo();
		userinfo.setProfileId(decodedToken.getUid());
		userinfo.setEmail(decodedToken.getEmail());
		userinfo.setName(decodedToken.getName());
		userinfo.setPicture(decodedToken.getPicture());

		UserRecord userRecord = getInstance().getUser(uid);
		userinfo.setPhone(userRecord.getPhoneNumber());
		// Fetch and Add more details
		userinfo.setProvider(provider);

		return userinfo;
	}

	@Override
	public OAuth2UserInfo doAuthenticate(String provider, ChannelPartner partner, MapModel body)
			throws FirebaseAuthException {
		String idToken = commonHttpRequest.get("idToken");
		return this.doAuthenticate(provider, partner, idToken);
	}

	public OAuth2UserInfo authenticate(String provider, ChannelPartner partner, String idToken)
			throws FirebaseAuthException {
		return this.doAuthenticate(provider, partner, idToken);
	}

	public FirebaseOptions getFirebaseOptions() {
		return firebaseOptions;
	}

	public String getFirebasePublicOptions() {
		return MapModel.createInstance().put("production", false).put("firebase", MapModel.createInstance() //
				.put("apiKey", this.firebaseWebApikey) //
				.put("databaseURL", this.firebaseDbUrl) //
				.put("projectId", this.firebaseProjectid) //
				.put("storageBucket", this.firebaseStorageBucket) //
				.put("messagingSenderId", this.firebaseMsgSenderId) //
				.put("authDomain", this.firebaseAuthDomain) //
				.put("appId", this.firebaseAppId) //
				.put("measurementId", this.firebaseMeasurementId) //
				.toMap()).toJson();
	}

}
