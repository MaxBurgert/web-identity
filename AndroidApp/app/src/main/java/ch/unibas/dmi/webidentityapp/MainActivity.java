package ch.unibas.dmi.webidentityapp;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

  private Button buttonAddTotp;
  private Button buttonScanTotp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    buttonAddTotp = this.findViewById(R.id.button_add_key);
    buttonScanTotp = this.findViewById(R.id.button_scan_key);

    buttonAddTotp.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        KeyInputDialog keyInputDialog = KeyInputDialog.newInstance("test");
        keyInputDialog.show(Objects.requireNonNull(fragmentManager),"fragment_patient_form");
      }
    });
  }
}
