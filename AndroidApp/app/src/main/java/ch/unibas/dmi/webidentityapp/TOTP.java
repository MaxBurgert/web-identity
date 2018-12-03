package ch.unibas.dmi.webidentityapp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
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
  private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
  private KeyStore ks;
  private String alias;

  TOTP(String alias) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
    ks = KeyStore.getInstance(ANDROID_KEY_STORE);
    ks.load(null);
    this.alias = alias;
  }

  public int getKey(byte[] counter,String alias)
      throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException, InvalidKeyException {
    Mac mac = Mac.getInstance("HmacSha1");
    KeyStore.Entry key = ks.getEntry(alias,null);
    if (!(key instanceof Key)) {
      System.err.println("Key Store Entry with alias: "+alias+"wasn't a key");
      return -1;
    }
    mac.init((Key)key);
    mac.update(counter);
    byte[] result =  mac.doFinal();
    byte offset = result[result.length-1];
    offset &= 15;
    byte[] trunc =  Arrays.copyOfRange(result,offset,offset+3);
    trunc[0]&=127;
    int truncint = ByteBuffer.wrap(trunc).getInt();
    truncint %= 1000000;
    return truncint;
  }

}
