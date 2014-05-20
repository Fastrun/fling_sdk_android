
package com.fireflycast.server.cast.channels;

import android.util.Base64;
import android.util.Log;

import com.fireflycast.server.cast.socket.data.C_axo;
import com.fireflycast.server.cast.socket.data.C_axp;
import com.fireflycast.server.cast.socket.data.C_axq;
import com.fireflycast.server.cast.socket.data.C_axr;
import com.fireflycast.server.utils.Logs_avu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public abstract class DeviceAuthChannel_auk extends CastChannel_avn {
    private static PublicKey mPublicKey_a;
    private int mAuthResult_b;
    private int c;
    private final String mTransId_d;
    private byte mAuthBuf_e[];

    public DeviceAuthChannel_auk(String transId_s, byte abyte0[]) {
        super("urn:x-cast:com.google.cast.tp.deviceauth", "DeviceAuthChannel");
        mAuthResult_b = -1;
        mAuthBuf_e = abyte0;
        mTransId_d = transId_s;
    }

    private void checkDevAuthResult_b(int result) {
        result = 0;
        mAuthResult_b = result;
        if (result == 0) {
            mLogs_j.d("Device authentication succeeded.", new Object[0]);
        } else if (result == 1) {
            Object aobj1[] = new Object[2];
            aobj1[0] = Integer.valueOf(mAuthResult_b);
            aobj1[1] = Integer.valueOf(c);
            mLogs_j.d(String.format("Device authentication failed: %s %s", aobj1),
                    new Object[0]);
        } else {
            Object aobj[] = new Object[1];
            aobj[0] = Integer.valueOf(mAuthResult_b);
            mLogs_j.d(String.format("Device authentication failed: %s", aobj),
                    new Object[0]);
        }
        verifyDevAuthResult_a(result);
    }

    public final void doDeviceAuth_a() {
        C_axr axr1 = new C_axr();
        axr1.a(new C_axo());
        sendBinaryMessage_a(axr1.K(), mTransId_d);
    }

    protected abstract void verifyDevAuthResult_a(int i);

    public final void onReceivedMessage_a(byte abyte0[]) {
        C_axr axr1;
        Logs_avu avu2;
        Object aobj1[];
        try {
            axr1 = C_axr.a(abyte0);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        /*
         * catch (C_igt igt1) { Logs_avu avu1 = j; Object aobj[] = new
         * Object[1]; aobj[0] = igt1.getMessage();
         * avu1.b("Received an unparseable protobuf (ignored): %s", aobj);
         * return; }
         */
        avu2 = mLogs_j;
        aobj1 = new Object[1];
        aobj1[0] = axr1.toString();
        avu2.d("Received a protobuf: %s", aobj1);
        if (axr1.a() != null) {
            mLogs_j.d("Received DeviceAuthMessage with challenge instead of response (ignored).",
                    new Object[0]);
            return;
        }
        C_axp axp1 = axr1.e();
        if (axp1 != null) {
            c = axp1.a();
            checkDevAuthResult_b(1);
            return;
        }
        C_axq axq1 = axr1.d();
        if (axq1 == null) {
            mLogs_j.d("Received DeviceAuthMessage with no response (ignored).",
                    new Object[0]);
            return;
        }
        X509Certificate x509certificate;
        Signature signature;
        Signature signature1;

        try {
            x509certificate = (X509Certificate) CertificateFactory.getInstance(
                    "X.509").generateCertificate(
                    new ByteArrayInputStream(axq1.d().b()));
        } catch (CertificateException certificateexception) {
            checkDevAuthResult_b(2);
            return;
        } catch (ClassCastException classcastexception) {
            checkDevAuthResult_b(3);
            return;
        }

        try {
            signature1 = Signature.getInstance("SHA1withRSA");
            signature1.initVerify(mPublicKey_a);
            signature1.update(x509certificate.getTBSCertificate());
            if (!signature1.verify(x509certificate.getSignature())) {
                checkDevAuthResult_b(4);
                return;
            }
        } catch (CertificateEncodingException e) {
            checkDevAuthResult_b(2);
            return;
        } catch (ClassCastException ex) {
            checkDevAuthResult_b(3);
            return;
        } catch (NoSuchAlgorithmException ee) {
            ee.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(x509certificate.getPublicKey());
            signature.update(mAuthBuf_e);
            if (!signature.verify(axq1.a().b())) {
                checkDevAuthResult_b(5);
                return;
            }
        }
        // Misplaced declaration of an exception variable
        catch (NoSuchAlgorithmException e) {
            mLogs_j.w(e, "SHA1withRSA", new Object[0]);
        } catch (InvalidKeyException invalidkeyexception1) {
            checkDevAuthResult_b(2);
            return;
        } catch (SignatureException signatureexception1) {
            checkDevAuthResult_b(5);
            return;
        }
        checkDevAuthResult_b(0);
        return;
    }

    static {
        byte abyte0[] = Base64
                .decode("MIIDhzCCAm+gAwIBAgIBATANBgkqhkiG9w0BAQUFADB8MQswCQYDVQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNTW91bnRhaW4gVmlldzETMBEGA1UECgwKR29vZ2xlIEluYzESMBAGA1UECwwJR29vZ2xlIFRWMRcwFQYDVQQDDA5FdXJla2EgUm9vdCBDQTAeFw0xMjEyMTkwMDQ3MTJaFw0zMjEyMTQwMDQ3MTJaMH0xCzAJBgNVBAYTAlVTMRMwEQYDVQQIDApDYWxpZm9ybmlhMRYwFAYDVQQHDA1Nb3VudGFpbiBWaWV3MRMwEQYDVQQKDApHb29nbGUgSW5jMRIwEAYDVQQLDAlHb29nbGUgVFYxGDAWBgNVBAMMD0V1cmVrYSBHZW4xIElDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALwigL2A9johADuudl41fz3DZFxVlIY0LwWHKM33aYwXs1CnuIL638dDLdZ+q6BvtxNygKRHFcEgmVDN7BRiCVukmM3SQbY2Tv/oLjIwSoGoQqNsmzNuyrL1U2bgJ1OGGoUepzk/SneO+1RmZvtYVMBeOcf1UAYL4IrUzuFqVR+LFwDmaaMn5gglaTwSnY0FLNYuojHetFJQ1iBJ3nGg+a0gQBLx3SXr1ea4NvTWj3/KQ9zXEFvmP1GKhbPz//YDLcsjT5ytGOeTBYysUpr3TOmZer5ufk0K48YcqZP6OqWRXRy9ZuvMYNyGdMrP+JIcmH1X+mFHnquAt+RIgCqSxRsCAwEAAaMTMBEwDwYDVR0TBAgwBgEB/wIBATANBgkqhkiG9w0BAQUFAAOCAQEAi9Shsc9dzXtsSEpBH1MvGC0yRf+eq9NzPh8i1+r6AeZzAw8rxiW7pe7F9UXLJBIqrcJdBfR69cKbEBZa0QpzxRY5oBDK0WiFnvueJoOOWPN3oE7l25e+LQBf9ZTbsZ1la/3w0QRR38ySppktcfVN1SP+MxyptKvFvxq40YDvicniH5xMSDui+gIK3IQBiocC+1nup0wEfXSZh2olRK0WquxONRt8e4TJsT/hgnDlDefZbfqVtsXkHugRm9iy86T9E/ODT/cHFCC7IqWmj9a126l0eOKTDeUjLwUX4LKXZzRND5x2Q3umIUpWBfYqfPJ/EpSCJikH8AtsbHkUsHTVbA==",
                        0);
        try {
            mPublicKey_a = CertificateFactory.getInstance("X.509")
                    .generateCertificate(new ByteArrayInputStream(abyte0))
                    .getPublicKey();
        } catch (CertificateException certificateexception) {
            Log.w("DeviceAuthChannel", "Chromecast ICA cert.",
                    certificateexception);
        }
    }
}
