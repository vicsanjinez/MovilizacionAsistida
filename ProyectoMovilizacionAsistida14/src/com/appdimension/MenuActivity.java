package com.appdimension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

//import com.usfx.sistemas.MapaActivity.MyLocationListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends Activity implements OnInitListener{

	//datos de la BD
	String[] datosGPSlatitud=new String[10000];
	String[] datosGPSlongitud=new String[10000];
	int indice1 = 0;
	int indice2 = 0;
	
	//voice
	private static final int VOICE_RECOGNITION_REQUEST = 1;
	Button bRecord;
	
	//insertar y listar
	DBAdapter db=new DBAdapter(this);
	//insertar y listar
	
	//CODIGO GPS
	double gpsLatitud=0.0,gpsLongitud=0.0;
	int datosObtenidos=0;
	
	private Timer myTimer;
    
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    protected LocationManager locationManager;
    //CODIGO GPS
    
	//text to speech
	TextToSpeech talker;
	
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	//ocultar el nombre del proyecto
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    
	setContentView(R.layout.main);
	
	//voice
	bRecord = (Button)this.findViewById(R.id.bh1);
	bRecord.setOnClickListener(new View.OnClickListener() {	
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			say("Hablar");
			
		}
	});
	//boton largo tiempo sonido vibrate
    bRecord.setOnLongClickListener(new View.OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			
			ToneGenerator sound = new ToneGenerator(AudioManager.STREAM_SYSTEM, 100);
			 sound.startTone(ToneGenerator.TONE_PROP_BEEP);
			 
			((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(50);
			
			recordSpeech(v);
			//recordSpeech();
			
			return true;
		}
	});
	
	//CODIGO GPS
    myTimer = new Timer();
	myTimer.schedule(new TimerTask() {
		@Override
		public void run() {
			TimerMethod();
		}

	}, 0, 6000);
	
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MINIMUM_TIME_BETWEEN_UPDATES,
            MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
            new MyLocationListener());
    //CODIGO GPS
	
	//text to speech
	talker = new TextToSpeech(this, this);
	
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
			 
			 say("Bienvenidos, Movilización Asistida");
					    
	   }
	/*
	@Override
	public void onStart()
	{
		super.onStart();
		say("Movilización asistida");
	}
	@Override
	public void onRestart()
	{
		super.onRestart();
		say("Movilización asistida");
	}
	@Override
	public void onResume()
	{
		super.onResume();
		say("Movilización asistida");
	}
	*/
	@Override
	   public void onDestroy() {
	      if (talker != null) {
	         talker.stop();
	         talker.shutdown();
	      }
	      super.onDestroy();
	   }
	
	//creando menus
	@Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	CreateMenu(menu);
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	return MenuChoice(item);
    }
    private void CreateMenu(Menu menu)
    {
    	menu.setQwertyMode(true);
    	
    	//MenuItem mnu1 = menu.add(0,0,0,"Donde estoy");
    	MenuItem mnu1 = menu.add(0,0,0,"estoy");
    	{
    		mnu1.setAlphabeticShortcut('y');
    		//mnu1.setIcon(R.drawable.vistas);
    	}
    	//MenuItem mnu2 = menu.add(0,1,1,"Donde he estado");
    	MenuItem mnu2 = menu.add(0,1,1,"estuve");
    	{
    		mnu2.setAlphabeticShortcut('e');
    		
    	}
    	//MenuItem mnu3 = menu.add(0,2,2,"Como voy");
    	MenuItem mnu3 = menu.add(0,2,2,"voy");
    	{
    		mnu3.setAlphabeticShortcut('o');
    		
    	}
    }
    private boolean MenuChoice(MenuItem item)
    {
    	switch(item.getItemId()){
    	case 0:
    		Toast.makeText(this, "Donde estoy", Toast.LENGTH_LONG).show();
    		//say("Donde estoy, Usted esta en");
    		say("Donde estoy");
    		say("Usted esta en");
    		say(NameStreet(gpsLatitud,gpsLongitud));
    		return true;
    	case 1:
    		Toast.makeText(this, "Donde estuve", Toast.LENGTH_LONG).show();
    		
    		say("Donde estuve, seleccione dia");
    		
    		Intent intento10 = new Intent(MenuActivity.this,CalendarioActivity.class);
			startActivity(intento10);
			
    		return true;
    	case 2:
    		Toast.makeText(this, "Como voy", Toast.LENGTH_LONG).show();
    		
    		say("Como voy");
    		
    		Intent intento11 = new Intent(MenuActivity.this,MapaActivity.class);
			
			intento11.putExtra("gpsLatitud", gpsLatitud);
			intento11.putExtra("gpsLongitud", gpsLongitud);
			
			startActivity(intento11);
    		
    		return true;
    	}
    	return false;
    }
    
  //codigo del chino........!!!!!!!!  PARA EL NOMBRE DE LA CALLE
    private String NameStreet(double latitud,double longitud) 
    { 
	    //connect to map web service 
    	String resp="";
	    StringBuilder urlString = new StringBuilder(); 
	    //http://maps.googleapis.com/maps/api/geocode/xml?latlng=-19.048750,-65.255620&sensor=false 
	    urlString.append("http://maps.googleapis.com/maps/api/geocode/xml?latlng=");
	    urlString.append(latitud);//latitud  
	    urlString.append(","); 
	    urlString.append(longitud);//longitud 
	    urlString.append("&sensor=false");
	    Log.d("xxx","URL="+urlString.toString()); 
	    // get the kml (XML) doc. And parse it to get the coordinates(direction route). 
	    Document doc = null; 
	    HttpURLConnection urlConnection= null; 
	    URL url = null; 
	    try 
	    { 
		    url = new URL(urlString.toString()); 
		    urlConnection=(HttpURLConnection)url.openConnection(); 
		    urlConnection.setRequestMethod("GET"); 
		    urlConnection.setDoOutput(true); 
		    urlConnection.setDoInput(true); 
		    urlConnection.connect(); 
		
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
		    DocumentBuilder db = dbf.newDocumentBuilder(); 
		    doc = db.parse(urlConnection.getInputStream()); 
	
		    if(doc.getElementsByTagName("formatted_address").getLength()>0) 
			{ 
			    //String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getNodeName();
			    String name = doc.getElementsByTagName("formatted_address").item(0).getFirstChild().getNodeValue();
			    Log.d("xxx","nameStreet="+ name); 
			    Toast.makeText(getApplicationContext(),"Usted esta en: \n"+ name, Toast.LENGTH_LONG).show(); 
			    resp = name.toString();
			    return resp;  
			} 
	    } 
	    catch (MalformedURLException e) 
	    { 
	    	e.printStackTrace(); 
	    } 
	    catch (IOException e) 
	    { 
	    	e.printStackTrace(); 
	    } 
	    catch (ParserConfigurationException e) 
	    { 
	    	e.printStackTrace(); 
	    } 
	    catch (SAXException e) 
	    { 
	    	e.printStackTrace(); 
	    }
		return resp; 
    }
    
  //CODIGO GPS
    private void TimerMethod()
	{
		//This method is called directly by the timer
		//and runs in the same thread as the timer.

		//We call the method that will work with the UI
		//through the runOnUiThread method.
		this.runOnUiThread(Timer_Tick);
	}

	private Runnable Timer_Tick = new Runnable() {
		public void run() {

		//This method runs in the same thread as the UI.    	       

		//Do something to the UI thread here
			//Toast.makeText(getBaseContext(), "contador", Toast.LENGTH_SHORT).show();
			
			//no se muestra la latitud y longitud actual porque perjudica a la aplicacion
			//showCurrentLocation();
		}
	};
	protected void showCurrentLocation() {
		
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            String message = String.format("Actual Ubicacion \n Longitud: %1$s \n Latitud: %2$s",location.getLongitude(), location.getLatitude());
            
          //no se muestra la latitud y longitud actual porque perjudica a la aplicacion
			//Toast.makeText(MenuActivity.this, message,Toast.LENGTH_LONG).show();
        }

    }  
    private class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
        	
        	gpsLatitud = location.getLatitude();
        	gpsLongitud = location.getLongitude();
        	
        	if (datosObtenidos == 0)
        	{  
        		say("Se tiene datos del GPS");
        		Toast.makeText(MenuActivity.this, "Se tiene datos del GPS",Toast.LENGTH_SHORT).show();
        		datosObtenidos=1;
        	}
        	
        	//insertar
        	Calendar cal = new GregorianCalendar();

		    Date date = cal.getTime();

		    //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		    String formatteDate = df.format(date);
		    
			//e1.setText(formatteDate);
			
	        // para a;adir un contacto
	        db.open();
	        
	        long id=db.insertContact(String.valueOf(gpsLatitud),String.valueOf(gpsLongitud),formatteDate);
	        
	        db.close();
	        
			//Toast.makeText(getBaseContext(), "Se introdujo el dato", Toast.LENGTH_SHORT).show();
			
        	//insertar
        	
            //String message = String.format("Nueva Ubicacion \n Longitud: %1$s \n Latitud: %2$s",location.getLongitude(), location.getLatitude());
            
          //no se muestra la latitud y longitud actual porque perjudica a la aplicacion
            //Toast.makeText(MenuActivity.this, message, Toast.LENGTH_LONG).show();
        }
        public void onStatusChanged(String s, int i, Bundle b) {
			
        	//no se muestra la latitud y longitud actual porque perjudica a la aplicacion
        	//Toast.makeText(MenuActivity.this, "Estado del proveedor cambiado",Toast.LENGTH_LONG).show();
        }
        public void onProviderDisabled(String s) {
        	
        	//no se muestra la latitud y longitud actual porque perjudica a la aplicacion
            //Toast.makeText(MenuActivity.this,"Proveedor desactivado por el usuario. GPS apagado",Toast.LENGTH_LONG).show();
        }
        public void onProviderEnabled(String s) {
        	
        	//no se muestra la latitud y longitud actual porque perjudica a la aplicacion
            //Toast.makeText(MenuActivity.this,"Proveedor activado por el usuario. GPS prendido",Toast.LENGTH_LONG).show();
        }
    }
    //CODIGO GPS
    
    //voice
    public void recordSpeech(View view) {
    	//public void recordSpeech() {
    		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    		//intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please speak slowly and clearly");
    		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Por favor hable lento y claro");
    		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST);
    	}
    	@Override
    	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    		
    		if (requestCode==VOICE_RECOGNITION_REQUEST && resultCode==RESULT_OK) {
    			ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    			/*
donde estoy
dónde estoy
donde voy
donde estoy yo
donde estoy y
donde esta hoy
donde estoy no
donde soy



donde estuve
donde tuve
donde tube
donde tú
donde es pobre
donde es tube
donde sube
donde es tu be
donde estuviste
donde se tube



como voy
cómo voy
como boy
como hoy
cómo boy
como soy
como hoy
como voy a
como boyle
    			 */
    			//textSaid = (TextView) findViewById(R.id.tTextRecognition);
    			//textSaid.setText(matches.get(0));
    			if (matches.size() == 1)
    			{
    				//if (matches.get(0).compareTo("como voy"))
    			    if (matches.get(0).equals("donde estoy"))
    				{
    			    	Toast.makeText(this, "Donde estoy", Toast.LENGTH_LONG).show();
    		    		//say("Donde estoy, Usted esta en");
    		    		say("Donde estoy");
    		    		say("Usted esta en");
    		    		say(NameStreet(gpsLatitud,gpsLongitud));
    		    		
    				}else if(matches.get(0).equals("donde estuve"))
    				{
    					Toast.makeText(this, "Donde estuve", Toast.LENGTH_LONG).show();
    					//say("Donde estuve, seleccione dia");
    		    		
    					String fechaactual="";
    					fechaactual=getFechaActual();
    					getDondeEstuve(fechaactual);
    		    		
    					//Intent intento10 = new Intent(MenuActivity.this,CalendarioActivity.class);
    					//startActivity(intento10);
    					
    				}else if(matches.get(0).equals("como voy"))
    				{
    					Toast.makeText(this, "Como voy", Toast.LENGTH_LONG).show();
    		    		
    		    		say("Como voy");
    		    		
    		    		Intent intento11 = new Intent(MenuActivity.this,MapaActivity.class);

    					intento11.putExtra("gpsLatitud", gpsLatitud);
    					intento11.putExtra("gpsLongitud", gpsLongitud);
    					
    					startActivity(intento11);
    					
    				}
    			}else if (matches.size() == 2)
    			{
    				if (matches.get(0).equals("donde estoy") || matches.get(1).equals("dónde estoy"))
    				{
    					Toast.makeText(this, "Donde estoy", Toast.LENGTH_LONG).show();
    		    		//say("Donde estoy, Usted esta en");
    		    		say("Donde estoy");
    		    		say("Usted esta en");
    		    		say(NameStreet(gpsLatitud,gpsLongitud));
    		    		
    				}else if(matches.get(0).equals("donde estuve") || matches.get(1).equals("donde tuve"))
    				{
    					Toast.makeText(this, "Donde estuve", Toast.LENGTH_LONG).show();
    					//say("Donde estuve, seleccione dia");
    		    		
    					String fechaactual="";
    					fechaactual=getFechaActual();
    					getDondeEstuve(fechaactual);
    		    		//Intent intento10 = new Intent(MenuActivity.this,CalendarioActivity.class);
    					//startActivity(intento10);
    					
    				}else if(matches.get(0).equals("como voy") || matches.get(1).equals("cómo voy"))
    				{
    					Toast.makeText(this, "Como voy", Toast.LENGTH_LONG).show();
    		    		
    		    		say("Como voy");
    		    		
    		    		Intent intento11 = new Intent(MenuActivity.this,MapaActivity.class);

    					intento11.putExtra("gpsLatitud", gpsLatitud);
    					intento11.putExtra("gpsLongitud", gpsLongitud);
    					
    					startActivity(intento11);
    					
    				}
    			}else if (matches.size() == 3)
    			{
    				if (matches.get(0).equals("donde estoy") || matches.get(1).equals("dónde estoy")|| matches.get(2).equals("donde voy"))
    				{
    					Toast.makeText(this, "Donde estoy", Toast.LENGTH_LONG).show();
    		    		//say("Donde estoy, Usted esta en");
    		    		say("Donde estoy");
    		    		say("Usted esta en");
    		    		say(NameStreet(gpsLatitud,gpsLongitud));
    		    		
    				}else if(matches.get(0).equals("donde estuve") || matches.get(1).equals("donde tuve")|| matches.get(2).equals("donde tube"))
    				{
    					Toast.makeText(this, "Donde estuve", Toast.LENGTH_LONG).show();
    					//say("Donde estuve, seleccione dia");
    		    		
    					String fechaactual="";
    					fechaactual=getFechaActual();
    					getDondeEstuve(fechaactual);
    		    		//Intent intento10 = new Intent(MenuActivity.this,CalendarioActivity.class);
    					//startActivity(intento10);
    					
    				}else if(matches.get(0).equals("como voy") || matches.get(1).equals("cómo voy")|| matches.get(2).equals("como boy"))
    				{
    					Toast.makeText(this, "Como voy", Toast.LENGTH_LONG).show();
    		    		
    		    		say("Como voy");
    		    		
    		    		Intent intento11 = new Intent(MenuActivity.this,MapaActivity.class);

    					intento11.putExtra("gpsLatitud", gpsLatitud);
    					intento11.putExtra("gpsLongitud", gpsLongitud);
    					
    					startActivity(intento11);
    					
    				}
    			}else
    			{
    				if (matches.get(0).equals("donde estoy") || matches.get(1).equals("dónde estoy") || matches.get(2).equals("donde voy") || matches.get(3).equals("donde estoy yo"))
    				{
    					Toast.makeText(this, "Donde estoy", Toast.LENGTH_LONG).show();
    		    		//say("Donde estoy, Usted esta en");
    		    		say("Donde estoy");
    		    		say("Usted esta en");
    		    		say(NameStreet(gpsLatitud,gpsLongitud));
    					
    				}else if(matches.get(0).equals("donde estuve") || matches.get(1).equals("donde tuve") || matches.get(2).equals("donde tube") || matches.get(3).equals("donde es tu be"))
    				{
    					Toast.makeText(this, "Donde estuve", Toast.LENGTH_LONG).show();
    					//say("Donde estuve, seleccione dia");
    		    		
    					String fechaactual="";
    					fechaactual=getFechaActual();
    					getDondeEstuve(fechaactual);
    		    		//Intent intento10 = new Intent(MenuActivity.this,CalendarioActivity.class);
    					//startActivity(intento10);
    					
    				}else if(matches.get(0).equals("como voy") || matches.get(1).equals("cómo voy") || matches.get(2).equals("como boy") || matches.get(3).equals("cómo boy"))
    				{
    					Toast.makeText(this, "Como voy", Toast.LENGTH_LONG).show();
    		    		
    		    		say("Como voy");
    		    		
    		    		Intent intento11 = new Intent(MenuActivity.this,MapaActivity.class);

    					intento11.putExtra("gpsLatitud", gpsLatitud);
    					intento11.putExtra("gpsLongitud", gpsLongitud);
    					
    					startActivity(intento11);
    				}
    			}
    		}
    		super.onActivityResult(requestCode, resultCode, data);
    	}
    	
    	public String getFechaActual()
    	{
    		Calendar cal = new GregorianCalendar();

		    Date date = cal.getTime();

		    //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		    String formatteDate = df.format(date);
		    
			//e1.setText(formatteDate);
		    
    		return formatteDate;
    	}
    	public void getDondeEstuve(String fecha)
    	{
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
	        
			Intent intento2 = new Intent(MenuActivity.this,MapaDatos.class);
			
			intento2.putExtra("datosGPSlatitud", datosGPSlatitud);
	        intento2.putExtra("datosGPSlongitud", datosGPSlongitud);
	        intento2.putExtra("indice", indice1-1);
			
			startActivity(intento2);
			
		    Toast.makeText(getBaseContext(), "Selecciono Mostrar Mapa", Toast.LENGTH_SHORT).show();
    	}
    	
    	//mensaje para confirmar que se ha de salir
    	
    	public boolean onKeyUp(int keyCode, KeyEvent event) {
    		// TODO Auto-generated method stub
     
    		switch(keyCode){
    			case KeyEvent.KEYCODE_BACK:
    				String cadena="Estás seguro de querer salir?";
    				//Toast.makeText(this, "Boton Atras presionado",Toast.LENGTH_SHORT).show();
    				say("salir?");
    				Intent intento15 = new Intent(MenuActivity.this,SalirActivity.class);
    				intento15.putExtra("cadena", cadena);
    				startActivity(intento15);
    		}
    		return super.onKeyUp(keyCode, event);
    	}
}
