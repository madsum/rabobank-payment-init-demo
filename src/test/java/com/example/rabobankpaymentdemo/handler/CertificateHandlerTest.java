package com.example.rabobankpaymentdemo.handler;

import com.example.rabobankpaymentdemo.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.cert.X509Certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CertificateHandlerTest {
    private CertificateHandler certificateHandler;

    @BeforeEach
    void setUp(){
       certificateHandler = TestData.getCertificateHandler();
    }

    @Test
    void testValidCertificate(){
        X509Certificate certificate = TestData.getValidX509Certificate();
        certificateHandler.setX509Certificate(certificate);
        assertTrue(certificateHandler.verify());
    }

    @Test
    void testInvalidCertificate(){
        certificateHandler.setCertificateString("Invalid");
        assertFalse(certificateHandler.verify());
    }

    @Test
    void testDigest(){
        String expectedSha256Hex = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
        String actualSha256Hex = certificateHandler.digest("test");
        assertEquals(expectedSha256Hex, actualSha256Hex);
    }
}
