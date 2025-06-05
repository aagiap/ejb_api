package com.example.ws_cert.controller;

import com.example.ws_cert.entity.SubjectDN;
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
public class GenerateCSRController {

    @Autowired
    private GenerateCSRService generateCSR;

    SubjectDN subjectDN = new SubjectDN();

    @PostMapping("/generateCSR")
    public ResponseEntity<String> generate(@RequestBody SubjectDN request) {
        try {
            KeyPair keyPair = generateCSR.generateKeyPair();

            SubjectDN subjectDN = new SubjectDN();
            subjectDN.setCn(request.getCn());
            subjectDN.setO(request.getO());
            subjectDN.setOu(request.getOu());
            subjectDN.setL(request.getL());
            subjectDN.setSt(request.getSt());
            subjectDN.setC(request.getC());

            PKCS10CertificationRequest csr = generateCSR.generateCSR(keyPair,subjectDN);
            String pem = generateCSR.pemCSR(csr);
            return ResponseEntity.ok(pem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tạo CSR: " + e.getMessage());
        }
    }

}
