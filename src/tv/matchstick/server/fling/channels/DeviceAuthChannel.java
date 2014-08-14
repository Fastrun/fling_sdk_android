
package tv.matchstick.server.fling.channels;

import android.util.Base64;
import android.util.Log;

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

import tv.matchstick.server.fling.socket.data.C_axo;
import tv.matchstick.server.fling.socket.data.C_axp;
import tv.matchstick.server.fling.socket.data.C_axq;
import tv.matchstick.server.fling.socket.data.C_axr;
import tv.matchstick.server.utils.LOG;

public abstract class DeviceAuthChannel extends FlingChannel {
    private static PublicKey mPublicKey;
    private int mAuthResult;
    private int c;
    private final String mTransId;
    private byte mAuthBuf[];

    public DeviceAuthChannel(String transId_s, byte abyte0[]) {
        super("urn:x-cast:com.google.cast.tp.deviceauth", "DeviceAuthChannel");
        mAuthResult = -1;
        mAuthBuf = abyte0;
        mTransId = transId_s;
    }

    private void checkDevAuthResult(int result) {
        result = 0;
        mAuthResult = result;
        if (result == 0) {
            mLogs.d("Device authentication succeeded.");
        } else if (result == 1) {
            mLogs.d(String.format("Device authentication failed: %s %s", mAuthResult, c));
        } else {
            Object aobj[] = new Object[1];
            aobj[0] = Integer.valueOf(mAuthResult);
            mLogs.d(String.format("Device authentication failed: %s", mAuthResult));
        }
        verifyDevAuthResult(result);
    }

    public final void doDeviceAuth() {
        C_axr axr1 = new C_axr();
        axr1.a(new C_axo());
        sendBinaryMessage(axr1.build(), mTransId);
    }

    protected abstract void verifyDevAuthResult(int i);

    public final void onReceivedMessage(byte abyte0[]) {
//        C_axr axr1;
//        LOG avu2;
//        Object aobj1[];
//        try {
//            axr1 = C_axr.a(abyte0);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//
//        mLogs.d("Received a protobuf: %s", axr1.toString());
//
//        if (axr1.a() != null) {
//            mLogs.d("Received DeviceAuthMessage with challenge instead of response (ignored).");
//            return;
//        }
//        C_axp axp1 = axr1.e();
//        if (axp1 != null) {
//            c = axp1.a();
//            checkDevAuthResult(1);
//            return;
//        }
//        C_axq axq1 = axr1.d();
//        if (axq1 == null) {
//            mLogs.d("Received DeviceAuthMessage with no response (ignored).");
//            return;
//        }
//        X509Certificate x509certificate;
//        Signature signature;
//        Signature signature1;
//
//        try {
//            x509certificate = (X509Certificate) CertificateFactory.getInstance(
//                    "X.509").generateCertificate(
//                    new ByteArrayInputStream(axq1.d().b()));
//        } catch (CertificateException certificateexception) {
//            checkDevAuthResult(2);
//            return;
//        } catch (ClassCastException classcastexception) {
//            checkDevAuthResult(3);
//            return;
//        }
//
//        try {
//            signature1 = Signature.getInstance("SHA1withRSA");
//            signature1.initVerify(mPublicKey);
//            signature1.update(x509certificate.getTBSCertificate());
//            if (!signature1.verify(x509certificate.getSignature())) {
//                checkDevAuthResult(4);
//                return;
//            }
//        } catch (CertificateEncodingException e) {
//            checkDevAuthResult(2);
//            return;
//        } catch (ClassCastException ex) {
//            checkDevAuthResult(3);
//            return;
//        } catch (NoSuchAlgorithmException ee) {
//            ee.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            signature = Signature.getInstance("SHA1withRSA");
//            signature.initVerify(x509certificate.getPublicKey());
//            signature.update(mAuthBuf);
//            if (!signature.verify(axq1.a().b())) {
//                checkDevAuthResult(5);
//                return;
//            }
//        }
//        // Misplaced declaration of an exception variable
//        catch (NoSuchAlgorithmException e) {
//            mLogs.w(e, "SHA1withRSA", new Object[0]);
//        } catch (InvalidKeyException invalidkeyexception1) {
//            checkDevAuthResult(2);
//            return;
//        } catch (SignatureException signatureexception1) {
//            checkDevAuthResult(5);
//            return;
//        }
        checkDevAuthResult(0);
        return;
    }

    static {
        byte abyte0[] = Base64
                .decode("MIIDhzCCAm+gAwIBAgIBATANBgkqhkiG9w0BAQUFADB8MQswCQYDVQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNTW91bnRhaW4gVmlldzETMBEGA1UECgwKR29vZ2xlIEluYzESMBAGA1UECwwJR29vZ2xlIFRWMRcwFQYDVQQDDA5FdXJla2EgUm9vdCBDQTAeFw0xMjEyMTkwMDQ3MTJaFw0zMjEyMTQwMDQ3MTJaMH0xCzAJBgNVBAYTAlVTMRMwEQYDVQQIDApDYWxpZm9ybmlhMRYwFAYDVQQHDA1Nb3VudGFpbiBWaWV3MRMwEQYDVQQKDApHb29nbGUgSW5jMRIwEAYDVQQLDAlHb29nbGUgVFYxGDAWBgNVBAMMD0V1cmVrYSBHZW4xIElDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALwigL2A9johADuudl41fz3DZFxVlIY0LwWHKM33aYwXs1CnuIL638dDLdZ+q6BvtxNygKRHFcEgmVDN7BRiCVukmM3SQbY2Tv/oLjIwSoGoQqNsmzNuyrL1U2bgJ1OGGoUepzk/SneO+1RmZvtYVMBeOcf1UAYL4IrUzuFqVR+LFwDmaaMn5gglaTwSnY0FLNYuojHetFJQ1iBJ3nGg+a0gQBLx3SXr1ea4NvTWj3/KQ9zXEFvmP1GKhbPz//YDLcsjT5ytGOeTBYysUpr3TOmZer5ufk0K48YcqZP6OqWRXRy9ZuvMYNyGdMrP+JIcmH1X+mFHnquAt+RIgCqSxRsCAwEAAaMTMBEwDwYDVR0TBAgwBgEB/wIBATANBgkqhkiG9w0BAQUFAAOCAQEAi9Shsc9dzXtsSEpBH1MvGC0yRf+eq9NzPh8i1+r6AeZzAw8rxiW7pe7F9UXLJBIqrcJdBfR69cKbEBZa0QpzxRY5oBDK0WiFnvueJoOOWPN3oE7l25e+LQBf9ZTbsZ1la/3w0QRR38ySppktcfVN1SP+MxyptKvFvxq40YDvicniH5xMSDui+gIK3IQBiocC+1nup0wEfXSZh2olRK0WquxONRt8e4TJsT/hgnDlDefZbfqVtsXkHugRm9iy86T9E/ODT/cHFCC7IqWmj9a126l0eOKTDeUjLwUX4LKXZzRND5x2Q3umIUpWBfYqfPJ/EpSCJikH8AtsbHkUsHTVbA==",
                        0);
        try {
            mPublicKey = CertificateFactory.getInstance("X.509")
                    .generateCertificate(new ByteArrayInputStream(abyte0))
                    .getPublicKey();
        } catch (CertificateException certificateexception) {
            Log.w("DeviceAuthChannel", "Fling ICA cert.",
                    certificateexception);
        }
    }
}
