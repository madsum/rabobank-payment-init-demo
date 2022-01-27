package com.example.rabobankpaymentdemo;

import com.example.rabobankpaymentdemo.exception.InvalidSignatureException;
import com.example.rabobankpaymentdemo.handler.CertificateHandler;
import com.example.rabobankpaymentdemo.handler.SignatureHandler;
import com.example.rabobankpaymentdemo.model.PaymentInitiationRequest;
import com.example.rabobankpaymentdemo.util.PaymentUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

public class TestData {

    public static final String PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEowIBAAKCAQEAryLyouTQr1dvMT4qvek0eZsh8g0DQQLlOgBzZwx7iInxYEAg\n" +
            "MNxCKXiZCbmWHBYqh6lpPh+BBmrnBQzB+qrSNIyd4bFhfUlQ+htK08yyL9g4nyLt\n" +
            "0LeKuxoaVWpInrB5FRzoEY5PPpcEXSObgr+pM71AvyJtQLxZbqTao4S7TRKecUm3\n" +
            "2Wwg+FWY/StSKlox3QmEaxEGU7aPkaQfQs4hrtuUePwKrbkQ2hQdMpvI5oXRWzTq\n" +
            "afvEQvND+IyLvZRqf0TSvIwsgtJd2tch2kqPoUwng3AmUFleJbMjFNzrWM7TH9Lk\n" +
            "KPItYtSuMTzeSe9o0SmXZFgcEBh5DnETZqIVuQIDAQABAoIBAAfjgxpjRCMhxRhq\n" +
            "vHIhdwOjQTwt6d+bycd7DbeukEHuNLkpKkoJIdHMZNhTS7eoJ/JEZ0EtGhq35gAM\n" +
            "OxCXcTB8xP/NhZ6nFsatRAmWRtBw5NwGLsAgBFe5LUZ7qxm6yTlyO+HOjzM6ii/H\n" +
            "0tFo4K478Bar7k/kLAMX2eFTsTwfb23J8KhbVCXK/Oh02lqlqbf8/T4MowS1px25\n" +
            "LGQlS7KFW9CkuUVQ83IyCw9EbDJSMMr+Hkw8Bo5VllJ1s8RK++Fn3KwveCjOmZub\n" +
            "6luzMAJFdeDPvCi0mNctZzkB0abOmOcJQt1fHTJQJ9x69q4JRD9N+FvG3QwqgMLL\n" +
            "z0i63IECgYEA5v6G52aBUzKTLl3kn/P0WJ85RteOo2BHsIP+0toD6RMKNREdSCLs\n" +
            "itwsF/DKH3t4id0thiAK2cLRdY2SbWp0l0jXLyXd2VcZwTkEA2iQJJ9OLrGvDka/\n" +
            "j1CzdTjAj9ZBHsZzH93OoL+xSYeOhUMnIoWRzVe1cRSz3G83HeCBhGUCgYEAwhhz\n" +
            "3AszQjpjIt2P/eBeiEZALVsmt3lFm3cSuvNlo3b05it3OP/aVSfABSS5xfi4XqGB\n" +
            "YgDk4UbiRQxGlixht8ZoQWqdTDWKnSJi56uGEFg9F361kAVfZb7zVIBxddGfEuga\n" +
            "OaigNGe0M7J0fbWdwbx8EVsZOwXWp/TbbSblJMUCgYBKc3EBtj0qlptvj1233EY+\n" +
            "Jhus5J8Zs1eH4hNI3HH0NmnMztZUQMViwDIKCVbsLLyeGsaoez1kEHG4ZMf0MiKf\n" +
            "/B83GApYGcW4TGspuhLzatElJanZfR4S0Bz3RDJ0accVZzsF41TM5Nv8ag+ajhlX\n" +
            "/BsRRxq49sY93y6xl4HHLQKBgF9E8VmIhdh0IET0y8CpaL0q/kVFAHP+KpRsldz9\n" +
            "q13Y/cwceaCYtOonYLElnan2s0h/raoVFkMdL+MEa4E6t5wk3vd9BUhq32bRggqE\n" +
            "voE3ToVBxIy0lmaym21Wvlo+Uf5NvtGeW0Rdwq29YkBx7MUzZxJ9zJyT+RDntuyU\n" +
            "stShAoGBAOQMrfyQCFcInYo0aNdtm6spUbTEfGNnMVKq4hdLsKv5Yv1LotcELeWs\n" +
            "Avx59tOuhYNOod7oAWfGLQjjKZk1FOHjTD+CUg1Iixw2gwJ8Kz8OQZsSvNjMIJX8\n" +
            "qfeCXIBPrtblS37vxqk0t+V2vREC0575yzkckQmWaGBFPEALxI3t\n" +
            "-----END RSA PRIVATE KEY-----";

    public static final String CERTIFICATE = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDwjCCAqoCCQDxVbCjIKynQjANBgkqhkiG9w0BAQsFADCBojELMAkGA1UEBhMC\n" +
            "TkwxEDAOBgNVBAgMB1V0cmVjaHQxEDAOBgNVBAcMB1V0cmVjaHQxETAPBgNVBAoM\n" +
            "CFJhYm9iYW5rMRMwEQYDVQQLDApBc3Nlc3NtZW50MSIwIAYDVQQDDBlTYW5kYm94\n" +
            "LVRQUDpleGNlbGxlbnQgVFBQMSMwIQYJKoZIhvcNAQkBFhRuby1yZXBseUByYWJv\n" +
            "YmFuay5ubDAeFw0yMDAxMzAxMzIyNDlaFw0yMTAxMjkxMzIyNDlaMIGiMQswCQYD\n" +
            "VQQGEwJOTDEQMA4GA1UECAwHVXRyZWNodDEQMA4GA1UEBwwHVXRyZWNodDERMA8G\n" +
            "A1UECgwIUmFib2JhbmsxEzARBgNVBAsMCkFzc2Vzc21lbnQxIjAgBgNVBAMMGVNh\n" +
            "bmRib3gtVFBQOmV4Y2VsbGVudCBUUFAxIzAhBgkqhkiG9w0BCQEWFG5vLXJlcGx5\n" +
            "QHJhYm9iYW5rLm5sMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAryLy\n" +
            "ouTQr1dvMT4qvek0eZsh8g0DQQLlOgBzZwx7iInxYEAgMNxCKXiZCbmWHBYqh6lp\n" +
            "Ph+BBmrnBQzB+qrSNIyd4bFhfUlQ+htK08yyL9g4nyLt0LeKuxoaVWpInrB5FRzo\n" +
            "EY5PPpcEXSObgr+pM71AvyJtQLxZbqTao4S7TRKecUm32Wwg+FWY/StSKlox3QmE\n" +
            "axEGU7aPkaQfQs4hrtuUePwKrbkQ2hQdMpvI5oXRWzTqafvEQvND+IyLvZRqf0TS\n" +
            "vIwsgtJd2tch2kqPoUwng3AmUFleJbMjFNzrWM7TH9LkKPItYtSuMTzeSe9o0SmX\n" +
            "ZFgcEBh5DnETZqIVuQIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQASFOkJiKQuL7fS\n" +
            "ErH6y5Uwj9WmmQLFnit85tjbo20jsqseTuZqLdpwBObiHxnBz7o3M73PJAXdoXkw\n" +
            "iMVykZrlUSEy7+FsNZ4iFppoFapHDbfBgM2WMV7VS6NK17e0zXcTGySSRzXsxw0y\n" +
            "EQGaOU8RJ3Rry0HWo9M/JmYFrdBPP/3sWAt/+O4th5Jyk8RajN3fHFCAoUz4rXxh\n" +
            "UZkf/9u3Q038rRBvqaA+6c0uW58XqF/QyUxuTD4er9veCniUhwIX4XBsDNxIW/rw\n" +
            "BRAxOUkG4V+XqrBb75lCyea1o/9HIaq1iIKI4Day0piMOgwPEg1wF383yd0x8hRW\n" +
            "4zxyHcER\n" +
            "-----END CERTIFICATE-----";

    public static final String SIGNATURE = "WHjIGjM9ONl40W5VPfwu63f47cGFE36ZLfl16tdkDndRF7JYauMDRj5yusfuW7aiExkk+bU3lttDLlb4lYhuX0D6Y0M3cofm0Rmz7o7pQJw4t5uyvkHtnLLATmnv+iZIRVw7XcSgQZ9nmjzpCqo8qn4bm4v6MdxuGwOzz97ipU/NRav6qAF9o1bQzsYysnsEVkDWNfQmCfNTESrY62nitVR6zeGeKtOwuaIVAgHFNDEYaIi77UNnAIZ7ZbKBCGfaFW80vKb+oJzjCjFDUWp2saUmhahxBIBZSMMzVdp6N5+2uAM4boVeYMIqqEAVuPlwOKrt8bXuxRSrAUTdrYuVSg==";

    public static final String RABO_SIGNATURE = "AlFr/WbYiekHmbB6XdEO/7ghKd0n6q/bapENAYsL86KoYHqa4eP34xfH9icpQRmTpH0qOkt1vfUPWnaqu+vHBWx/gJXiuVlhayxLZD2w41q8ITkoj4oRLn2U1q8cLbjUtjzFWX9TgiQw1iY0ezpFqyDLPU7+ZzO01JI+yspn2gtto0XUm5KuxUPK24+xHD6R1UZSCSJKXY1QsKQfJ+gjzEjrtGvmASx1SUrpmyzVmf4qLwFB1ViRZmDZFtHIuuUVBBb835dCs2W+d7a+icGOCtGQbFcHvW0FODibnY5qq8v5w/P9i9PSarDaGgYb+1pMSnF3p8FsHAjk3Wccg2a1GQ==";

    public static X509Certificate getValidX509Certificate(){
        X509Certificate certificate = null;
        try {
            CertificateFactory fact = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream is = new ByteArrayInputStream(CERTIFICATE.getBytes("UTF8"));
            certificate = (X509Certificate) fact.generateCertificate(is);
        }catch (Exception ignore){
        }
        return certificate;
    }

    public static CertificateHandler getCertificateHandler() throws InvalidSignatureException, NoSuchAlgorithmException, InvalidKeySpecException {
        UUID xRequestId = UUID.fromString("29318e25-cebd-498c-888a-f77672f66449");
        PaymentInitiationRequest paymentInitiationRequest = getPaymentInitiationRequest();
        String sha256Encoded = DigestUtils.sha256Hex(paymentInitiationRequest.toString());
        sha256Encoded = xRequestId.toString().concat(sha256Encoded);
        String signature = PaymentUtil.generateSignature(sha256Encoded);
        CertificateHandler certificateHandler  = new CertificateHandler(xRequestId, CERTIFICATE, paymentInitiationRequest, signature);
        return certificateHandler;
    }

    public static SignatureHandler geeSignatureHandler() throws InvalidSignatureException, NoSuchAlgorithmException, InvalidKeySpecException {
        UUID xRequestId = UUID.fromString("29318e25-cebd-498c-888a-f77672f66449");
        PaymentInitiationRequest paymentInitiationRequest = getPaymentInitiationRequest();
        String sha256Encoded = DigestUtils.sha256Hex(paymentInitiationRequest.toString());
        sha256Encoded = xRequestId.toString().concat(sha256Encoded);
        String signature = PaymentUtil.generateSignature(sha256Encoded);
        SignatureHandler signatureHandler = new SignatureHandler(xRequestId, CERTIFICATE, paymentInitiationRequest, signature);
        return  signatureHandler;
    }



    public static PaymentInitiationRequest getPaymentInitiationRequest(){
        PaymentInitiationRequest paymentInitiationRequest = new PaymentInitiationRequest();
        paymentInitiationRequest.setCreditorIBAN("NL94ABNA1008270121");
        paymentInitiationRequest.setDebtorIBAN("NL02RABO7134384551");
        paymentInitiationRequest.setAmount("100");
        paymentInitiationRequest.setCurrency("EUR");
        return paymentInitiationRequest;

    }



}
