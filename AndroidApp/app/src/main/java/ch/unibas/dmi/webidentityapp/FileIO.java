package ch.unibas.dmi.webidentityapp;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

class FileIO {

  static void saveToFile(Context context, String data) {
    try {
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
          context.openFileOutput("config.txt", Context.MODE_PRIVATE));
      outputStreamWriter.write(data);
      outputStreamWriter.close();
    } catch (IOException e) {
      Log.e("Exception", "File write failed: " + e.toString());
    }
  }

  static String loadFromFile(Context context) {
    String ret = "";

    try {
      InputStream inputStream = context.openFileInput("config.txt");

      if (inputStream != null) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String receiveString = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ((receiveString = bufferedReader.readLine()) != null) {
          stringBuilder.append(receiveString);
        }

        inputStream.close();
        ret = stringBuilder.toString();
      }
    } catch (FileNotFoundException e) {
      Log.e("login activity", "File not found: " + e.toString());
    } catch (IOException e) {
      Log.e("login activity", "Can not read file: " + e.toString());
    }

    return ret;
  }
}
