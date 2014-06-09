
package com.fireflycast.server.cast.auth;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public final class CastX509TrustManager implements X509TrustManager {
    public static CastX509TrustManager a[];
    private static final X509Certificate b[] = new X509Certificate[0];
    
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // TODO Auto-generated method stub
        
        try {
            throw new CertificateException(
                    "checkClientTrusted was called. Not supported.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // TODO Auto-generated method stub
        
        // try {
        // if (!"RSA".equals(authType))
        // throw new CertificateException(
        // String.format(
        // "checkServerTrusted called with authType=%s (only RSA is supported)",
        // new Object[] { authType }));
        // if (chain.length != 1) {
        // Object aobj[] = new Object[1];
        // aobj[0] = Integer.valueOf(chain.length);
        // throw new CertificateException(
        // String.format(
        // "checkServerTrusted called with chain.length of %n (should be 1)",
        // aobj));
        // } else {
        // chain[0].checkValidity();
        // return;
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // TODO Auto-generated method stub
        return b;
    }
    
    
    static {
        CastX509TrustManager aatm[] = new CastX509TrustManager[1];
        aatm[0] = new CastX509TrustManager();
        a = aatm;
    }
    
    /*


    CastX509TrustManager_atm() {
    }

    public final void checkClientTrusted(X509Certificate ax509certificate[],
            String s) {
        try {
            throw new CertificateException(
                    "checkClientTrusted was called. Not supported.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
    /*
    public final void checkServerTrusted(X509Certificate ax509certificate[],
            String s) {*/
        // try {
        // if (!"RSA".equals(s))
        // throw new CertificateException(
        // String.format(
        // "checkServerTrusted called with authType=%s (only RSA is supported)",
        // new Object[] { s }));
        // if (ax509certificate.length != 1) {
        // Object aobj[] = new Object[1];
        // aobj[0] = Integer.valueOf(ax509certificate.length);
        // throw new CertificateException(
        // String.format(
        // "checkServerTrusted called with chain.length of %n (should be 1)",
        // aobj));
        // } else {
        // ax509certificate[0].checkValidity();
        // return;
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    //}

    /*
    public final X509Certificate[] getAcceptedIssuers() {
        return b;
    }

    static {
        CastX509TrustManager_atm aatm[] = new CastX509TrustManager_atm[1];
        aatm[0] = new CastX509TrustManager_atm();
        a = aatm;
    }*/
}
