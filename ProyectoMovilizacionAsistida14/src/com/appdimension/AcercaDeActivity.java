package com.appdimension;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class AcercaDeActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//ocultar el nombre del proyecto
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    
		setContentView(R.layout.acercade);
	}
}
