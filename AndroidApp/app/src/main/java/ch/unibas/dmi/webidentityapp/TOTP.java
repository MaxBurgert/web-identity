package ch.unibas.dmi.webidentityapp;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

class TOTP {

  static int getKey(byte[] counter, String secret)
      throws NoSuchAlgorithmException, InvalidKeyException {
    Mac mac = Mac.getInstance("HmacSha1");
    SecretKeySpec k = new SecretKeySpec(secret.getBytes(), "bla");
    mac.init(k);
    mac.update(counter);
    byte[] result = mac.doFinal();
    byte offset = result[result.length - 1];
    offset &= 15;
    byte[] trunc = Arrays.copyOfRange(result, offset, offset + 4);
    trunc[0] &= 127;
    int truncint = ByteBuffer.wrap(trunc).getInt();
    truncint %= 1000000;
    return truncint;
  }

}
