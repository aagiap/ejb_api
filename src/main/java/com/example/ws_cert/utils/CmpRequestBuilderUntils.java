package com.example.ws_cert.utils;

import com.example.ws_cert.constant.ErrorCode;
import com.example.ws_cert.dto.ejb.request.CmpIrRequest;
import com.example.ws_cert.dto.ejb.request.CmpRevocationRequest;
import com.example.ws_cert.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.cmp.PKIBody;
import org.bouncycastle.asn1.cmp.RevDetails;
import org.bouncycastle.asn1.cmp.RevReqContent;
import org.bouncycastle.asn1.crmf.CertReqMessages;
import org.bouncycastle.asn1.crmf.CertTemplateBuilder;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.cmp.ProtectedPKIMessage;
import org.bouncycastle.cert.cmp.ProtectedPKIMessageBuilder;
import org.bouncycastle.cert.crmf.*;
import org.bouncycastle.cert.crmf.jcajce.JcePKMACValuesCalculator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.MacCalculator;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.Security;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CmpRequestBuilderUntils {
    private final KeyUtils keyUtils;

    private static final byte[] SENDER_NONCE = "12345".getBytes();
    private static final byte[] TRANSACTION_ID = "23456".getBytes();
    private static final byte[] SENDER_KID = "KeyID".getBytes();
    private static final String SIGNING_ALGORITHM = "SHA256withRSA";
    private static final AlgorithmIdentifier DIGEST_ALGORITHM = new AlgorithmIdentifier(new ASN1ObjectIdentifier("1.3.14.3.2.26")); // SHA1
    private static final AlgorithmIdentifier MAC_ALGORITHM = new AlgorithmIdentifier(new ASN1ObjectIdentifier("1.2.840.113549.2.7")); // HMAC/SHA1

    // Static block to register Bouncy Castle Provider once
    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }


    /**
     * Builds a complete CMP Initialization Request (IR) message.
     * This is the main orchestrator method.
     *
     * @param cmpIrRequest The request object containing necessary details.
     * @return The encoded byte array of the CMP PKIMessage.
     * @throws AppException if any error occurs during message building.
     */
    public byte[] buildIrMessage(CmpIrRequest cmpIrRequest) {
        try {
            // 1. Generate Key Pair
            KeyPair keyPair = keyUtils.generateKeyPair();

            // 2. Build Certificate Request Message (CRMF)
            CertificateRequestMessage certReqMsg = buildCertificateRequestMessage(cmpIrRequest, keyPair);

            // 3. Build PKIBody
            PKIBody pkiBody = buildPKIBody(certReqMsg);

            // 4. Build Protected PKIMessage Header
            ProtectedPKIMessageBuilder pkiMessageBuilder = buildProtectedPKIMessageHeader(cmpIrRequest, pkiBody);

            // 5. Create MAC Calculator for protection
            MacCalculator macCalculator = createMacCalculator(cmpIrRequest.getPassword());

            // 6. Build and encode the final Protected PKIMessage
            ProtectedPKIMessage message = pkiMessageBuilder.build(macCalculator);
            return message.toASN1Structure().getEncoded();

        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Error building IR message: " + e.getMessage());
            throw new AppException(ErrorCode.BUILD_IR_MESSAGE_FAILED);
        }
    }



    /**
     * Builds the Certificate Request Message (CRMF part of CMP).
     *
     * @param cmpIrRequest The request details.
     * @param keyPair      The generated key pair.
     * @return A CertificateRequestMessage object.
     * @throws Exception if there's an error building the certificate request.
     */
    private CertificateRequestMessage buildCertificateRequestMessage(CmpIrRequest cmpIrRequest, KeyPair keyPair) throws Exception {
        BigInteger certReqId = BigInteger.valueOf(1); // Can be made dynamic if needed

        X500Name subjectDN = new X500Name(cmpIrRequest.getSubjectDn());
        SubjectPublicKeyInfo keyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());

        CertificateRequestMessageBuilder msgBuilder = new CertificateRequestMessageBuilder(certReqId)
                .setSubject(subjectDN)
                .setPublicKey(keyInfo);

        // Optional validity override
        if (cmpIrRequest.getNotBefore() != null && cmpIrRequest.getNotAfter() != null) {
            msgBuilder.setValidity(cmpIrRequest.getNotBefore(), cmpIrRequest.getNotAfter());
        }

        // Proof of Possession (POP) using signing key
        ContentSigner signer = new JcaContentSignerBuilder(SIGNING_ALGORITHM).build(keyPair.getPrivate());
        msgBuilder.setProofOfPossessionSigningKeySigner(signer);

        return msgBuilder.build();
    }

    /**
     * Builds a complete CMP Revocation Request message.
     * This is the main orchestrator method for revocation requests.
     *
     * @param cmpRevocationRequest The request object containing necessary details for revocation.
     * @return The encoded byte array of the CMP PKIMessage.
     * @throws AppException if any error occurs during message building.
     */
    public byte[] buildRevocationRequestMessage(CmpRevocationRequest cmpRevocationRequest) {
        try {
            // 1. Build PKIBody for revocation
            PKIBody pkiBody = buildRevocationPKIBody(
                    new X500Name(cmpRevocationRequest.getIssuerDn()),
                    new X500Name(cmpRevocationRequest.getSubjectDn()),
                    cmpRevocationRequest.getSerialNumber(),
                    cmpRevocationRequest.getRevocationReason()
            );

            // 2. Build Protected PKIMessage Header
            // For revocation, sender is usually the subject of the cert to be revoked, recipient is the issuer
            ProtectedPKIMessageBuilder pkiMessageBuilder = buildProtectedPKIMessageHeader(
                    new X500Name(cmpRevocationRequest.getSubjectDn()),
                    new X500Name(cmpRevocationRequest.getIssuerDn()),
                    pkiBody
            );

            // 3. Create MAC Calculator for protection
            MacCalculator macCalculator = createMacCalculator(cmpRevocationRequest.getPassword());

            // 4. Build and encode the final Protected PKIMessage
            ProtectedPKIMessage message = pkiMessageBuilder.build(macCalculator);
            return message.toASN1Structure().getEncoded();

        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Error building revocation message: " + e.getMessage());
            throw new AppException(ErrorCode.BUILD_REVOCATION_MESSAGE_FAILED);
        }
    }

    private PKIBody buildRevocationPKIBody(X500Name issuerDN, X500Name subjectDN, BigInteger serialNumber, int revocationReason) throws Exception {
        // Cert template to tell which cert we want to revoke
        CertTemplateBuilder myCertTemplate = new CertTemplateBuilder();
        myCertTemplate.setIssuer(issuerDN);
        myCertTemplate.setSubject(subjectDN);
        myCertTemplate.setSerialNumber(new ASN1Integer(serialNumber));

        // Extension telling revocation reason
        ExtensionsGenerator extgen = new ExtensionsGenerator();
        CRLReason crlReason = CRLReason.lookup(revocationReason);
        extgen.addExtension(Extension.reasonCode, false, crlReason);
        Extensions exts = extgen.generate();

        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(myCertTemplate.build());
        v.add(exts);
        ASN1Sequence seq = new DERSequence(v);

        RevDetails myRevDetails = RevDetails.getInstance(seq);
        RevReqContent myRevReqContent = new RevReqContent(myRevDetails);

        return new PKIBody(PKIBody.TYPE_REVOCATION_REQ, myRevReqContent);
    }

    /**
     * Builds the PKIBody of the CMP message. For an IR, it contains the CertReqMessages.
     *
     * @param certReqMsg The built CertificateRequestMessage.
     * @return A PKIBody object.
     */
    private PKIBody buildPKIBody(CertificateRequestMessage certReqMsg) {
        CertReqMessages msgs = new CertReqMessages(certReqMsg.toASN1Structure());
        return new PKIBody(PKIBody.TYPE_INIT_REQ, msgs);
    }

    /**
     * Builds the header for the ProtectedPKIMessage.
     *
     * @param cmpIrRequest The request details.
     * @param pkiBody      The PKIBody of the message.
     * @return A ProtectedPKIMessageBuilder with header information set.
     */
    private ProtectedPKIMessageBuilder buildProtectedPKIMessageHeader(CmpIrRequest cmpIrRequest, PKIBody pkiBody) {
        X500Name subjectDN = new X500Name(cmpIrRequest.getSubjectDn());
        X500Name issuerDN = new X500Name(cmpIrRequest.getIssuerDn());

        GeneralName sender = new GeneralName(subjectDN);
        GeneralName recipient = new GeneralName(issuerDN);

        return new ProtectedPKIMessageBuilder(sender, recipient)
                .setMessageTime(new Date())
                .setSenderNonce(SENDER_NONCE)
                .setTransactionID(TRANSACTION_ID)
                .setSenderKID(SENDER_KID)
                .setBody(pkiBody);
    }

    /**
     * Builds the header for the ProtectedPKIMessage. This method is now more generic
     * as it takes X500Name objects directly for sender and recipient.
     *
     * @param senderDn  The X500Name of the sender.
     * @param recipientDn The X500Name of the recipient.
     * @param pkiBody   The PKIBody of the message.
     * @return A ProtectedPKIMessageBuilder with header information set.
     */

    private ProtectedPKIMessageBuilder buildProtectedPKIMessageHeader(X500Name senderDn, X500Name recipientDn, PKIBody pkiBody) {
        GeneralName sender = new GeneralName(senderDn);
        GeneralName recipient = new GeneralName(recipientDn);

        return new ProtectedPKIMessageBuilder(sender, recipient)
                .setMessageTime(new Date())
                .setSenderNonce(SENDER_NONCE)
                .setTransactionID(TRANSACTION_ID)
                .setSenderKID(SENDER_KID)
                .setBody(pkiBody);
    }

    /**
     * Creates a PKMAC (Password-Based MAC) calculator for message protection.
     *
     * @param password The password used for MAC calculation.
     * @return A MacCalculator object.
     * @throws Exception if there's an error setting up the MAC calculator.
     */
    private MacCalculator createMacCalculator(String password) throws Exception {
        JcePKMACValuesCalculator jcePkmacCalc = new JcePKMACValuesCalculator();
        jcePkmacCalc.setup(DIGEST_ALGORITHM, MAC_ALGORITHM);

        PKMACBuilder macbuilder = new PKMACBuilder(jcePkmacCalc);
        return macbuilder.build(password.toCharArray());
    }
}
