package ch.unibas.dmi.webidentityapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import ch.unibas.dmi.webidentityapp.barcode.BarcodeCaptureActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

  private static final int BARCODE_READER_REQUEST_CODE = 1;
  private static Handler handler;
  private static final String TAG = MainActivity.class.getSimpleName();
  private static TextView textViewTime;
  private static ProgressBar progressBar;
  private static TextView textViewKey;
  private TextView textViewSecretKey;
  private static TOTP totp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button buttonAddTotp = this.findViewById(R.id.button_add_key);
    Button buttonScanTotp = this.findViewById(R.id.button_scan_key);
    Button buttonLoadKey = this.findViewById(R.id.button_load_stored_key);
    textViewSecretKey = this.findViewById(R.id.textView);
    textViewTime = this.findViewById(R.id.textViewCounter);
    progressBar = this.findViewById(R.id.progressBar);
    textViewKey = this.findViewById(R.id.textView_TOTP);

    handler = new Handler(Looper.getMainLooper());

    buttonAddTotp.setOnClickListener(view -> {
      FragmentManager fragmentManager = getFragmentManager();
      KeyInputDialog keyInputDialog = KeyInputDialog.newInstance("test");
      keyInputDialog.show(Objects.requireNonNull(fragmentManager), "fragment_patient_form");
    });

    buttonScanTotp.setOnClickListener(v -> {
      Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
      startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    });

    buttonLoadKey.setOnClickListener(view -> {
      textViewSecretKey.setText(FileIO.loadFromFile(getApplicationContext()));
    });

    totp = new TOTP(FileIO.loadFromFile(getApplicationContext()));

    Timer timer = new Timer();
    timer.startTimer();

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
        textViewKey.setText(String.valueOf(totp.getKey(counter)));
        Log.d(TAG, String.valueOf(totp.getKey(counter)));
      } catch (NoSuchAlgorithmException | InvalidKeyException e) {
        e.printStackTrace();
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == BARCODE_READER_REQUEST_CODE) {
      if (resultCode == CommonStatusCodes.SUCCESS) {
        if (data != null) {
          Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
          textViewKey.setText(barcode.displayValue);
          Log.d(TAG, "Scanned barcode with value: " + barcode.displayValue);
          FileIO.saveToFile(getApplicationContext(),barcode.displayValue);
          totp.updateKey(barcode.displayValue);
        }
      } else {
        super.onActivityResult(requestCode, resultCode, data);
      }
    }
  }
}
