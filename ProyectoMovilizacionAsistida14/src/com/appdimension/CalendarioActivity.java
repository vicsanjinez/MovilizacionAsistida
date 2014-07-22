package com.appdimension;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class CalendarioActivity extends Activity implements OnInitListener {
    /** Called when the activity is first created. */
	Button b1;
	DatePicker d1;
	String fecha="";
	
	//insertar y listar
	DBAdapter db=new DBAdapter(this);
	//insertar y listar
	
	String[] datosGPSlatitud=new String[10000];
	String[] datosGPSlongitud=new String[10000];
	int indice1 = 0;
	int indice2 = 0;
	
	//text to speech
	TextToSpeech talker;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //ocultar el nombre del proyecto
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.calendario);
        
      //text to speech
    	talker = new TextToSpeech(this, this);
        
        b1 = (Button)this.findViewById(R.id.bc1);
        d1 = (DatePicker)this.findViewById(R.id.datePicker1);
        
        b1.setOnClickListener(new View.OnClickListener() {	
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			
    			say("Ir al mapa");
    		}
    	});
    	//boton largo tiempo sonido vibrate
        b1.setOnLongClickListener(new View.OnLongClickListener() {
    		
    		@Override
    		public boolean onLongClick(View v) {
    			// TODO Auto-generated method stub
    			
    			ToneGenerator sound = new ToneGenerator(AudioManager.STREAM_SYSTEM, 100);
    			 sound.startTone(ToneGenerator.TONE_PROP_BEEP);
    			 
    			((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(50);
    			
    			// TODO Auto-generated method stub
				//fecha=getFechaCalendario();
				if(d1.getDayOfMonth()<10)
				{
					if((d1.getMonth() + 1)<10)
					{
						fecha="0"+d1.getDayOfMonth() + "/0" + (d1.getMonth() + 1) +  "/" + d1.getYear();
					}
					else
					{
						fecha="0"+d1.getDayOfMonth() + "/" + (d1.getMonth() + 1) +  "/" + d1.getYear();
					}
				}
				else
				{
					if((d1.getMonth() + 1)<10)
					{
						fecha=d1.getDayOfMonth() + "/0" + (d1.getMonth() + 1) +  "/" + d1.getYear();
					}
					else
					{
						fecha=d1.getDayOfMonth() + "/" + (d1.getMonth() + 1) +  "/" + d1.getYear();
					}
					
				}
				
				db.open();
		        //Cursor c=db.getAllContacts();
		        
		        //Cursor c=db.getAllContactsWhere("08/09/2012");
				
				//este esta bien
				Cursor c=db.getAllContactsWhere(fecha);
		        
		        //Cursor c=db.getContact(5);
		        if(c.moveToFirst())
		        {
		        	do{
		        		//DisplayContact(c);
		        		//datos de la BD 
		        		//String dbLatitud = c.getString(1);
		        		//String dbLongitud = c.getString(2);
		        		datosGPSlatitud[indice1]=c.getString(1);
		        		datosGPSlongitud[indice2]=c.getString(2);
		        		
		        		//Toast.makeText(getBaseContext(), datosGPSlatitud[indice1]+datosGPSlongitud[indice2], Toast.LENGTH_SHORT).show();
		        		
		        		indice1=indice1+1;
		        		indice2=indice2+1;
		        		//datos de la BD
		        	}while (c.moveToNext());
		        }
		        db.close();
				
		        say("Donde estuve, mapa");
		        
				Intent intento22 = new Intent(CalendarioActivity.this,MapaDatos.class);
				
				intento22.putExtra("datosGPSlatitud", datosGPSlatitud);
		        intento22.putExtra("datosGPSlongitud", datosGPSlongitud);
		        intento22.putExtra("indice", indice1-1);
				
				startActivity(intento22);
				
			    Toast.makeText(getBaseContext(), "Selecciono Mostrar Mapa", Toast.LENGTH_SHORT).show();
    			
    			return true;
    		}
    	});
    }
    
  //text to speech
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
			 
			 //say("Movilización asistida");
					    
	   }
	//@Override
	public void onStart()
	{
		super.onStart();
		//say("Movilización asistida");
	}
	@Override
	   public void onDestroy() {
	      if (talker != null) {
	         talker.stop();
	         talker.shutdown();
	      }
	      super.onDestroy();
	   }
	public String getFechaCalendario()
	{
		if(d1.getDayOfMonth()<10)
		{
			if((d1.getMonth() + 1)<10)
			{
				fecha="0"+d1.getDayOfMonth() + "/0" + (d1.getMonth() + 1) +  "/" + d1.getYear();
			}
			else
			{
				fecha="0"+d1.getDayOfMonth() + "/" + (d1.getMonth() + 1) +  "/" + d1.getYear();
			}
		}
		else
		{
			if((d1.getMonth() + 1)<10)
			{
				fecha=d1.getDayOfMonth() + "/0" + (d1.getMonth() + 1) +  "/" + d1.getYear();
			}
			else
			{
				fecha=d1.getDayOfMonth() + "/" + (d1.getMonth() + 1) +  "/" + d1.getYear();
			}
			
		}
		return "";
	}
}