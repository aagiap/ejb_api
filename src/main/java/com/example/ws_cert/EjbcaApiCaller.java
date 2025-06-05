package com.example.ws_cert;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyStore;

public class EjbcaApiCaller {

    public static void main(String[] args) throws Exception {
        String pkcsEndpoint = "https://localhost:8443/ejbca/ejbca-rest-api/v1/certificate/pkcs10enroll";
        String keystoreFile = "D:\\\\tet7\\\\ejbca-ce-r9.0.0\\\\p12\\\\superadmin.p12";
        String keystorePassword = "ejbca";

        // Load client certificate (superadmin.p12)
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            keyStore.load(fis, keystorePassword.toCharArray());
        }

        // Initialize KeyManager (for client certificate)
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keystorePassword.toCharArray());

        // Use default trust store (or load EJBCA's CA cert if needed)
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init((KeyStore) null);

        // Build SSL context with client auth
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        // Create HttpClient using SSLContext
        HttpClient client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();

        // Create JSON payload for pkcs10enroll API
        String jsonRequest = """
            {
                "certificate_request": "-----BEGIN CERTIFICATE REQUEST-----\\nMIICWTCCAUECAQAwFDESMBAGA1UEAwwJSm9obiBEb2UzMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0ulCxmzCafRN7YzkF4CftVeNRRx/ClrhiE1gR4HE3StgPoK0BMmTzOjA1oDpG+6/E8uNFWjMz7jxH0o2kJn4bEHAmTcaUhFwgtRu//taoK4HMfsRlwarpqrL5BPQKRnK75B3nK068tAiAjpsIAffnItCYZO7D98OFZwy4NS5JgKtBcZF6rGDSFUTyhIjolcJHg9fLjnjd+RdeEmA56oPZtXbGSat2Wko2i5FsNIaZHiG3NkqFhv8ogV57lYl5ilz/97NM0c+JNAt+wyFrsSBN7Bc2/zrLrkbCdOMc5hVu88vWEw54eWEeODb+633YYhTf8BclmphPuKwZoPsRSVkkwIDAQABoAAwDQYJKoZIhvcNAQELBQADggEBAM271k+/qX6ZrzmAYs/QDHA/3C8bcmgma5+9QILZ4ZENSJBhZ0qgg4O3slso3gDBN/zstqPyqbGnS4KhQ2v62Urm6CXQi4BRPzanfqfVyI0SOWPS53wk11pstq0jhbJ0RjhFh30Y/5fEhhEA3u1qG6RTaefGZ+Z3MmJW7fQTRre+HQmsFHcyqM3T/m2hPac9x+vJtkPUzW9mUMwqsSZOc7JFZwgsS5OK128aFsvJYVOTJBq6gKDcagiavOef7cMKfa4i1tGdf/d8KJu454e9L/3rrsAY1TiC471BoMlE/lOaD6lC0MDfrISyF3nvHjn+nUJbRmfwTcQDa0e3dMso01E=\\n-----END CERTIFICATE REQUEST-----",    
                "certificate_profile_name": "SUBCA",
                    "end_entity_profile_name": "TLSClientProfile",
                    "certificate_authority_name": "ManagementCA",
                    "username": "myuser2",
                    "password": "123",
                    "response_format": "DER"
            }
            """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(pkcsEndpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        // Send request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Print response
        System.out.println("Status code: " + response.statusCode());
        System.out.println("Response body:\n" + response.body());
    }
}
