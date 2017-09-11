package com.phicomm.remotecontrol.request;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okio.Buffer;

/**
 * Created by zhongfei.sun on 2017/4/11.
 */

public class OKHttpManager {
    private static final String CER_PHICOMM = "-----BEGIN CERTIFICATE-----\n" +
            "MIIE9zCCA9+gAwIBAgIQTDCgCe8MJ7mFTbdoR2l/GzANBgkqhkiG9w0BAQsFADBS\n" +
            "MQswCQYDVQQGEwJDTjEaMBgGA1UEChMRV29TaWduIENBIExpbWl0ZWQxJzAlBgNV\n" +
            "BAMTHldvU2lnbiBDbGFzcyAzIE9WIFNlcnZlciBDQSBHMjAeFw0xNTA4MjQwMzE1\n" +
            "MDBaFw0xODEwMjQwMzE1MDBaMIGCMQswCQYDVQQGEwJDTjESMBAGA1UECAwJ5LiK\n" +
            "5rW35biCMRIwEAYDVQQHDAnkuIrmtbfluIIxMzAxBgNVBAoMKuS4iua1t+aWkOiu\n" +
            "r+aVsOaNrumAmuS/oeaKgOacr+aciemZkOWFrOWPuDEWMBQGA1UEAwwNKi5waGlj\n" +
            "b21tLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAK6ftNTJs2Ov\n" +
            "2W66DwA0efSOCyYrgjHxliAtCIaqWEAQ+eBOfTzsYv5O8J2bl5+mz10uoCoV13Yg\n" +
            "AQwxQ/jlP/kBsS6R7EAjQ9ksGmHcBOFfEj9OQPF+tbeGlJCqOJrpVd/Uk+IY4wzC\n" +
            "mELkxg0Mdt+Sbnt8YaGbNlCPvdLDjWlIBTlMfuuMDtRtPCegNvCT7+EcrqbF8a67\n" +
            "5ySG2UGfswlTudBYdum+Lgp181q1XLkNZKrqGXIV2HfOKMP1BGtyBL+iGlqfJEjY\n" +
            "+FsF/dxKpcKGQ8wu+Usupr4PySPMvA+ypR4SHudwjAEzj6KIEao0W8on0R3E40IN\n" +
            "u2BdHOkBlOsCAwEAAaOCAZYwggGSMAsGA1UdDwQEAwIFoDAdBgNVHSUEFjAUBggr\n" +
            "BgEFBQcDAgYIKwYBBQUHAwEwCQYDVR0TBAIwADAdBgNVHQ4EFgQUV0SUmcdxSGHe\n" +
            "R6TjiXuHdLymFq4wHwYDVR0jBBgwFoAU+YvsBDhqP6oGxpStc5UqsMjmuPswcwYI\n" +
            "KwYBBQUHAQEEZzBlMC8GCCsGAQUFBzABhiNodHRwOi8vb2NzcDYud29zaWduLmNv\n" +
            "bS9jYTYvc2VydmVyMzAyBggrBgEFBQcwAoYmaHR0cDovL2FpYTYud29zaWduLmNv\n" +
            "bS9jYTYuc2VydmVyMy5jZXIwOAYDVR0fBDEwLzAtoCugKYYnaHR0cDovL2NybHM2\n" +
            "Lndvc2lnbi5jb20vY2E2LXNlcnZlcjMuY3JsMBgGA1UdEQQRMA+CDSoucGhpY29t\n" +
            "bS5jb20wUAYDVR0gBEkwRzAIBgZngQwBAgIwOwYMKwYBBAGCm1EGAwIBMCswKQYI\n" +
            "KwYBBQUHAgEWHWh0dHA6Ly93d3cud29zaWduLmNvbS9wb2xpY3kvMA0GCSqGSIb3\n" +
            "DQEBCwUAA4IBAQDVcAtR0mWwqjUtnaM1wUAHTQ7HpMijvzxb4VX8s8w95Tvh/fEv\n" +
            "b5UoGRhmQkJE5THrmp//H5RXuC1+mc6mVAxrhuoAAj2o+MFBSZDTEYfKht5BfEQb\n" +
            "w8boA2IsnOTt6OqbrK+yo27g6TAnhwaQlKwcX1xOQn9WdavuOoAN15pgDho6J+Tp\n" +
            "CmKJ8ZWZkU8+G5qlU7H7qRVGPi910TRXbtXg3xRlDnvUC0IOr7m0FL5XgcFQTn9U\n" +
            "bcGK5fpDdgdvM3k1l+CSleHfmIYOVQj40YuFwTxSskUluEarEhDhdQmRQd/zflB9\n" +
            "7Srz4vBf+QDPfTQyQUD8IVLI/OjhzytAHcx+\n" +
            "-----END CERTIFICATE-----\n";

    public static final long DEFAULT_TIMEOUT = 3 * 1000;

    public static OkHttpClient getNewOkHttpClient() {
        return OkHttpClientHolder.Instance;
    }

    public static OkHttpClient getNewOkHttpClientSSL() {
        return OkHttpClientHolder.InstanceSSL;
    }

    private static class OkHttpClientHolder {
        private static final OkHttpClient Instance = createNormalClient();
        private static final OkHttpClient InstanceSSL = createSSlClient();

        private static OkHttpClient createNormalClient() {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
            okHttpClient.setWriteTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
            return okHttpClient;
        }

        private static OkHttpClient createSSlClient() {
            OkHttpClient okHttpClient = createNormalClient();
            setCertificates(okHttpClient, new Buffer().writeUtf8(CER_PHICOMM).inputStream());
            return okHttpClient;
        }
    }

    /**
     * 设置https证书
     *
     * @param certificate 证书文件输入流
     */
    public static void setCertificates(OkHttpClient okHttpClient, InputStream certificate) {
        if (certificate == null) {
            okHttpClient.setSslSocketFactory(null);
            return;
        }
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

            try {
                if (certificate != null)
                    certificate.close();
            } catch (IOException e) {
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
