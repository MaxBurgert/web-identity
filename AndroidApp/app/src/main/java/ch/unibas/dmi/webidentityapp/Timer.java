package ch.unibas.dmi.webidentityapp;

import java.nio.ByteBuffer;
import java.util.jar.Manifest;

class Timer{

  private long counterThirtySeconds = 0;
  private boolean started;

  Timer (){
  }

  void startTimer(){
    new Thread(() -> {
      started = true;
      int i = 0;
      while (true){
        long tmpCounter = Math.floorDiv(getTime(),30000);
        if(tmpCounter != counterThirtySeconds){
          counterThirtySeconds = tmpCounter;
          MainActivity.updateKey(longToBytes(counterThirtySeconds));
          i = 30;
        }
        i--;
        MainActivity.updateTime(i);
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  private long getTime() {
    return System.currentTimeMillis();
  }

  public byte[] longToBytes(long x) {
    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
    buffer.putLong(x);
    return buffer.array();
  }

  public boolean isStarted() {
    return started;
  }
}
