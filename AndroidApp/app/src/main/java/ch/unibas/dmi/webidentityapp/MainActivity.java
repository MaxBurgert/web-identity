package ch.unibas.dmi.webidentityapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import ch.unibas.dmi.webidentityapp.barcode.BarcodeCaptureActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.apache.commons.codec.binary.Base32;

public class MainActivity extends AppCompatActivity {

  private static final int BARCODE_READER_REQUEST_CODE = 1;
  private static Handler handler;
  private static final String TAG = MainActivity.class.getSimpleName();
  @SuppressLint("StaticFieldLeak")
  private static TextView textViewTime;
  @SuppressLint("StaticFieldLeak")
  private static ProgressBar progressBar;
  @SuppressLint("StaticFieldLeak")
  private static TextView textViewKey;
  private TextView textViewSecretKey;
  private static TOTP totp;
  private Timer timer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button buttonScanTotp = this.findViewById(R.id.button_scan_key);
    Button buttonLoadKey = this.findViewById(R.id.button_load_stored_key);
    textViewSecretKey = this.findViewById(R.id.textView);
    textViewTime = this.findViewById(R.id.textViewCounter);
    progressBar = this.findViewById(R.id.progressBar);
    textViewKey = this.findViewById(R.id.textView_TOTP);

    handler = new Handler(Looper.getMainLooper());

    setTitle("Time-Based One-Time Password");

    buttonScanTotp.setOnClickListener(v -> {
      Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
      startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    });

    buttonLoadKey.setOnClickListener(
        view -> textViewSecretKey.setText(FileIO.loadFromFile(getApplicationContext())));

    String keyFromFile = FileIO.loadFromFile(getApplicationContext());

    timer = new Timer();

    if(!keyFromFile.equals("")){
      startTOTP();
    }
  }

  private void startTOTP(){
    totp = new TOTP(FileIO.loadFromFile(getApplicationContext()));
    if (!timer.isStarted()){
      timer.startTimer();
    }
  }

  static void updateTime(int counter) {
    handler.post(() -> {
      textViewTime.setText(String.valueOf(counter));
      progressBar.setProgress(counter);
    });
  }

  static void updateKey(byte[] counter) {
    handler.post(() -> {
      try {
        String tmp = String.valueOf(totp.getKey(counter));
        String tmp1 = tmp.substring(0,3);
        String tmp2 = tmp.substring(3,6);
        textViewKey.setText(String.format("%s %s", tmp1, tmp2));
        Log.d(TAG, String.valueOf(totp.getKey(counter)));
      } catch (NoSuchAlgorithmException | InvalidKeyException | StringIndexOutOfBoundsException e) {
        e.printStackTrace();
      }
    });
  }

  @SuppressLint("SetTextI18n")
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == BARCODE_READER_REQUEST_CODE) {
      if (resultCode == CommonStatusCodes.SUCCESS) {
        if (data != null) {
          Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
          textViewSecretKey.setText("Secret key: " + barcode.displayValue);
          Log.d(TAG, "Scanned barcode with value: " + barcode.displayValue);
          FileIO.saveToFile(getApplicationContext(), barcode.displayValue);
          startTOTP();
        }
      } else {
        super.onActivityResult(requestCode, resultCode, data);
      }
    }
  }
}
