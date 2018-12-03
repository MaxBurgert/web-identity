package ch.unibas.dmi.webidentityapp;

import static org.junit.Assert.assertEquals;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import javax.crypto.spec.SecretKeySpec;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TOTPTest {

  @BeforeClass
  public static void setup(){
    try {
      KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
      ks.load(null);
      SecretKeyEntry key = new SecretKeyEntry(new SecretKeySpec(new byte[]{0x43,0x3a},"bla"));
      ks.setEntry("test",key,null);
    } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void test(){
    try {
      TOTP totp = new TOTP("test");
      int key = totp.getKey(new byte[]{0x00,0x00,0x00,0x01},"test");
      assertEquals(50,key);
    } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | InvalidKeyException | UnrecoverableEntryException e) {
      e.printStackTrace();
    }
  }

}
