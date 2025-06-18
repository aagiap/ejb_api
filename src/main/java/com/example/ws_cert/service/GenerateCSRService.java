package com.example.ws_cert.service;

import com.example.ws_cert.dto.SubjectDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;


@Service
public class GenerateCSRService {
    static {
        // Đăng ký BouncyCastle như một Security Provider (chỉ cần gọi 1 lần)
        Security.addProvider(new BouncyCastleProvider());
    }
    /**
     * Sinh cặp khóa RSA
     */
    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    /**
     * Tạo CSR theo chuẩn PKCS#10
     * @param keyPair cặp khóa vừa sinh
     * @return đối tượng CSR
     */
    public PKCS10CertificationRequest generateCSR(KeyPair keyPair, SubjectDN subjectDNC) throws Exception {

        // Thông tin định danh cho CSR: CN, O, C,...
        String subjectDN = subjectDNC.toString();
        X500Name x500Name = new X500Name(subjectDN);

        // Tạo CSR builder
        PKCS10CertificationRequestBuilder p10Builder =
                new JcaPKCS10CertificationRequestBuilder(x500Name, keyPair.getPublic());

        // Tạo ContentSigner dùng để ký CSR bằng private key
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        csBuilder.setProvider("BC"); // Dùng BouncyCastle
        ContentSigner signer = csBuilder.build(keyPair.getPrivate());

        // Sinh ra CSR
        return p10Builder.build(signer);
    }

    /**
     * Ghi CSR ra file dưới dạng PEM
     */
    public String pemCSR(PKCS10CertificationRequest csr) throws IOException {
        return "-----BEGIN CERTIFICATE REQUEST-----\\n"
                + Base64.getEncoder().encodeToString(csr.getEncoded())
                + "\\n-----END CERTIFICATE REQUEST-----";

    }

    public static void main(String[] args) throws Exception {
        //KeyPair keyPair = generateKeyPair();
        //PKCS10CertificationRequest csr = generateCSR(keyPair);
        //System.out.println(pemCSR(csr));
    }

}
