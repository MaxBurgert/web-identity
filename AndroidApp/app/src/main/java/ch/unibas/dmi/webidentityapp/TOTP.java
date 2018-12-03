package ch.unibas.dmi.webidentityapp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TOTP {


  public static int getKey(byte[] counter,String secret)
      throws NoSuchAlgorithmException,InvalidKeyException {
    Mac mac = Mac.getInstance("HmacSha1");
    SecretKeySpec k = new SecretKeySpec(secret.getBytes(),"bla");
    mac.init(k);
    mac.update(counter);
    byte[] result =  mac.doFinal();
    byte offset = result[result.length-1];
    offset &= 15;
    byte[] trunc =  Arrays.copyOfRange(result,offset,offset+4);
    trunc[0]&=127;
    int truncint = ByteBuffer.wrap(trunc).getInt();
    truncint %= 1000000;
    return truncint;
  }

}
