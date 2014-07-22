package com.appdimension;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SalirActivity extends Activity  implements OnInitListener{
	 /** Called when the activity is first created. */
	
	//voice
	private static final int VOICE_RECOGNITION_REQUEST = 1;
	TextToSpeech talker;
	
	String cadena="";
	TextView t2;
	Button bSI,bNO;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogsalir);
      
        //ocultar el nombre del proyecto
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        talker = new TextToSpeech(this, this);
        
        Bundle extras=getIntent().getExtras();
        if(extras != null){
        	cadena=extras.getString("cadena");
        }
       
        
        
        getWindow().setLayout (LayoutParams.FILL_PARENT /* width */ , LayoutParams.WRAP_CONTENT /* height */);

        
        t2=(TextView)findViewById(R.id.t2);
        t2.setText(cadena);
        
        
        
        bSI= (Button)this.findViewById(R.id.bsalirsi);
        bNO= (Button)this.findViewById(R.id.bsalirno);
    	
    	
    	bSI.setOnClickListener(new View.OnClickListener() {	
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			
    			say("Si");
    		}
    	});
    	//boton largo tiempo sonido vibrate
        bSI.setOnLongClickListener(new View.OnLongClickListener() {
    		
    		@Override
    		public boolean onLongClick(View v) {
    			// TODO Auto-generated method stub
    			
    			ToneGenerator sound = new ToneGenerator(AudioManager.STREAM_SYSTEM, 100);
    			 sound.startTone(ToneGenerator.TONE_PROP_BEEP);
    			 
    			((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(50);
    			
    			SalirActivity.this.finish();
    			
    			return true;
    		}
    	});
        
        bNO.setOnClickListener(new View.OnClickListener() {	
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			
    			say("No");
    		}
    	});
    	//boton largo tiempo sonido vibrate
        bNO.setOnLongClickListener(new View.OnLongClickListener() {
    		
    		@Override
    		public boolean onLongClick(View v) {
    			// TODO Auto-generated method stub
    			
    			ToneGenerator sound = new ToneGenerator(AudioManager.STREAM_SYSTEM, 100);
    			 sound.startTone(ToneGenerator.TONE_PROP_BEEP);
    			 
    			((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(50);
    			
    			
    			//finish();
    			//dismissDialog(0);
    			//finishFromChild(getParent());
    			//dismissDialog(RESULT_OK);
    			Intent intento16 = new Intent(SalirActivity.this,MenuActivity.class);
				
				startActivity(intento16);
				
				finish();
    			
    			return true;
    		}
    	});
    }
    public void say(String text2say){
	      talker.speak(text2say, TextToSpeech.QUEUE_FLUSH, null);
	}
	@Override
	   public void onInit(int status) {
		  //talker.setLanguage(Locale.US);
		   /*
		   Locale loc = new Locale("en", "","");
		    if(talker.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE){
		    	talker.setLanguage(loc);
		    
		    	//say("Hello World, I am so happy today because I love android");
		    	 * 
		    }
		   */
		    
		    Locale loc = new Locale("es", "","");
			 if(talker.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE){
			    	talker.setLanguage(loc);
			    }
			 
					    
	   }
	@Override
	   public void onDestroy() {
	      if (talker != null) {
	         talker.stop();
	         talker.shutdown();
	      }
	      super.onDestroy();
	   }
	//mensaje para confirmar que se ha de salir
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
 
		switch(keyCode){
			case KeyEvent.KEYCODE_BACK:				
				
				return true;
			case KeyEvent.KEYCODE_HOME:
				
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
}
