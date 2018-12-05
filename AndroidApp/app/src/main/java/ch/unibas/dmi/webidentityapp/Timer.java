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
      while (true){
        long tmpTime = getTime();
        int tmpRest = (int) (tmpTime % 30000)/1000;
        if(tmpRest == 0 || counterThirtySeconds == 0){
          counterThirtySeconds = Math.floorDiv(tmpTime, 30000);
          MainActivity.updateKey(longToBytes(counterThirtySeconds));
        }
        MainActivity.updateTime(30-tmpRest);
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
