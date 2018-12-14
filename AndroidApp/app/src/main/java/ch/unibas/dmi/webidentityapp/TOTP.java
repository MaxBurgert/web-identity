package ch.unibas.dmi.webidentityapp;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base32;

class TOTP {

  private byte[] secret;

  TOTP(String secret) {
    Base32 test = new Base32();
    this.secret = test.decode(secret.getBytes());
  }

  int getKey(byte[] counter)
      throws NoSuchAlgorithmException, InvalidKeyException {
    Mac mac = Mac.getInstance("HmacSha1");
    SecretKeySpec k = new SecretKeySpec(secret, "bla");
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

  void updateKey(String secret) {
    this.secret = secret.getBytes();
  }
}
