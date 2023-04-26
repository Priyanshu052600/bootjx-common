package com.boot.test;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;

import com.boot.key.JavaKeyStore;

public class KeysTest {

	private static final String KEYSTORE_PWD = "abc123";
	private static final String KEYSTORE_NAME = "myKeyStore";
	private static final String KEY_STORE_TYPE = "JCEKS";

	private static final String MY_SECRET_ENTRY = "mySecretEntry";
	private static final String DN_NAME = "CN=test, OU=test, O=test, L=test, ST=test, C=CY";
	private static final String SHA1WITHRSA = "SHA1withRSA";
	private static final String MY_PRIVATE_KEY = "myPrivateKey";
	private static final String MY_CERTIFICATE = "myCertificate";

	public static void main(String[] args) {
		try {
			JavaKeyStore keyStore = new JavaKeyStore(KEY_STORE_TYPE, KEYSTORE_PWD, KEYSTORE_NAME);
			
			
			KeyGenerator keygen = KeyGenerator.getInstance("HmacSHA256");

		} catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
			e.printStackTrace();
		}
	}
}
