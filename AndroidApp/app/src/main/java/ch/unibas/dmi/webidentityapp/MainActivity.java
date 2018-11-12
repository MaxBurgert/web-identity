package ch.unibas.dmi.webidentityapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  private Button buttonAddTotp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    buttonAddTotp = this.findViewById(R.id.button);

    buttonAddTotp.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(getApplicationContext(), "Hello",Toast.LENGTH_LONG).show();
      }
    });
  }
}
