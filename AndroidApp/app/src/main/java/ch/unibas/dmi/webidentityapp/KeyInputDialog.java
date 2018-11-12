package ch.unibas.dmi.webidentityapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KeyInputDialog extends DialogFragment {
  public KeyInputDialog() {
    // Empty constructor is required for DialogFragment
    // Make sure not to add arguments to the constructor
    // Use `newInstance` instead as shown below
  }
  public static KeyInputDialog newInstance(String title) {
    KeyInputDialog frag = new KeyInputDialog();
    Bundle args = new Bundle();
    args.putString("title", title);
    frag.setArguments(args);
    return frag;
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return getActivity().getLayoutInflater().inflate(R.layout.dialog_key_input, container);
  }
}
