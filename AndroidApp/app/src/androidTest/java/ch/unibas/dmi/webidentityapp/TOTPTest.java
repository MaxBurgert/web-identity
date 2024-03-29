package ch.unibas.dmi.webidentityapp;

import static org.junit.Assert.assertEquals;
import android.support.test.runner.AndroidJUnit4;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TOTPTest {

  @Test
  public void test(){
    TOTP totp = new TOTP("test");
    try {
      int key = totp.getKey(new byte[]{0x00,0x00,0x00,0x01});
      assertEquals(482727,key);
    } catch ( NoSuchAlgorithmException | InvalidKeyException e) {
      e.printStackTrace();
    }
  }

}
