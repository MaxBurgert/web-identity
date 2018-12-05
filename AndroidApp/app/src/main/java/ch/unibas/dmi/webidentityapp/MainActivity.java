package ch.unibas.dmi.webidentityapp;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import ch.unibas.dmi.webidentityapp.barcode.BarcodeCaptureActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

  private static final int BARCODE_READER_REQUEST_CODE = 1;
  private static final String TAG = MainActivity.class.getSimpleName();
  private TextView textViewKey;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button buttonAddTotp = this.findViewById(R.id.button_add_key);
    Button buttonScanTotp = this.findViewById(R.id.button_scan_key);
    Button buttonLoadKey = this.findViewById(R.id.button_load_stored_key);
    textViewKey = this.findViewById(R.id.textView);

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
      textViewKey.setText(FileIO.loadFromFile(getApplicationContext()));
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
        }
      } else {
        super.onActivityResult(requestCode, resultCode, data);
      }
    }
  }
}
