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
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class RutaActivity extends Activity  implements OnInitListener{
	 /** Called when the activity is first created. */
	
	//voice
	private static final int VOICE_RECOGNITION_REQUEST = 1;
	TextToSpeech talker;
	
	String cadena="";
	TextView t1;
	Button bEscuchar,bCerrar;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogruta);
      
        //ocultar el nombre del proyecto
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        talker = new TextToSpeech(this, this);
        
        Bundle extras=getIntent().getExtras();
        if(extras != null){
        	cadena=extras.getString("cadena");
        }
       
        getWindow().setLayout (LayoutParams.FILL_PARENT /* width */ , LayoutParams.WRAP_CONTENT /* height */);

        
        t1=(TextView)findViewById(R.id.t1);
        t1.setText(cadena);
        
        bEscuchar= (Button)this.findViewById(R.id.brutaescuchar);
        bCerrar= (Button)this.findViewById(R.id.brutacerrar);
    	
    	
    	bEscuchar.setOnClickListener(new View.OnClickListener() {	
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			
    			say("Escuchar");
    		}
    	});
    	//boton largo tiempo sonido vibrate
        bEscuchar.setOnLongClickListener(new View.OnLongClickListener() {
    		
    		@Override
    		public boolean onLongClick(View v) {
    			// TODO Auto-generated method stub
    			
    			ToneGenerator sound = new ToneGenerator(AudioManager.STREAM_SYSTEM, 100);
    			 sound.startTone(ToneGenerator.TONE_PROP_BEEP);
    			 
    			((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(50);
    			
    			say(cadena);
    			
    			return true;
    		}
    	});
        
        bCerrar.setOnClickListener(new View.OnClickListener() {	
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			
    			say("Cerrar");
    		}
    	});
    	//boton largo tiempo sonido vibrate
        bCerrar.setOnLongClickListener(new View.OnLongClickListener() {
    		
    		@Override
    		public boolean onLongClick(View v) {
    			// TODO Auto-generated method stub
    			
    			ToneGenerator sound = new ToneGenerator(AudioManager.STREAM_SYSTEM, 100);
    			 sound.startTone(ToneGenerator.TONE_PROP_BEEP);
    			 
    			((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(50);
    			
    			
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
}
