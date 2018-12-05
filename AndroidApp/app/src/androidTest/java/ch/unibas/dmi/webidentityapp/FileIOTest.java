package ch.unibas.dmi.webidentityapp;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import java.io.File;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FileIOTest {

  @Test
  public void test(){
    String key = "testkey";
    Context appContext = InstrumentationRegistry.getTargetContext();
    FileIO.saveToFile(appContext, key);
    assertEquals(key, FileIO.loadFromFile(appContext));
  }
}
