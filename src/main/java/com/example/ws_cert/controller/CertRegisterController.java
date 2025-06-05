package com.example.ws_cert.controller;

import com.example.ws_cert.dto.ClientInfo;
import com.example.ws_cert.entity.CertRequest;
import com.example.ws_cert.entity.SubjectDN;
import com.example.ws_cert.service.EjbcaPkcs10enrollService;
import com.example.ws_cert.service.GenerateCSRService;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;

@RestController
public class CertRegisterController {

    @Autowired
    private EjbcaPkcs10enrollService ejbcaPkcs10enrollService;

    @Autowired
    private GenerateCSRService generateCSRService;


    @PostMapping("/cert/register")
    public ResponseEntity<String> certRegister(@RequestBody ClientInfo clientInfo) {
        try {

            KeyPair keyPair = generateCSRService.generateKeyPair();
            SubjectDN subjectDN = new SubjectDN();
            subjectDN.setCn(clientInfo.getCn());
            PKCS10CertificationRequest csr = generateCSRService.generateCSR(keyPair, subjectDN);
            String pem = generateCSRService.pemCSR(csr);


            CertRequest certRequest = new CertRequest(null, "SUBCA", "TLSClientProfile", "ManagementCA", null, null, "DER");
            certRequest.setUsername(clientInfo.getUsername());
            certRequest.setPassword(clientInfo.getPassword());
            System.out.println("name: " + clientInfo.getUsername() + " - password: " + clientInfo.getPassword());
            certRequest.setCertificate_request(pem);

            String cert = ejbcaPkcs10enrollService.getCertResponse(certRequest);
            return ResponseEntity.ok(cert);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Exception: " + e.getMessage());
        }
    }
}
