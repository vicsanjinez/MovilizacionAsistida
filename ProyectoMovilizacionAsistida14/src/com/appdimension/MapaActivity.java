package com.appdimension;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.view.View;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.Window;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import com.google.android.maps.MapView;

import com.google.android.maps.GeoPoint;

import android.view.MotionEvent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class MapaActivity extends MapActivity implements OnInitListener{
	//insertar y listar
	DBAdapter db=new DBAdapter(this);
	//insertar y listar
	
	Double[] datosRutalatitud1=new Double[1000];
	Double[] datosRutalongitud1=new Double[1000];
	Double[] datosRutalatitud2=new Double[1000];
	Double[] datosRutalongitud2=new Double[1000];
	int indiceRuta = 0;
	
	//direccion final hablada
	String direccionFinalHablada="";
	
	//direcciones de la ruta trazada
	String direccionRutaTrazada="";
	
	//voice
	private static final int VOICE_RECOGNITION_REQUEST = 1;
	
	Button bRecord,bTra;
	
	//CODIGO GPS
	double gpsLatitud,gpsLongitud;
	int datosObtenidos=0;
	
	private Timer myTimer;
    
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    protected LocationManager locationManager;
    //CODIGO GPS
    
	int capa1=0,capa2=0;
	
	TextToSpeech talker;
	
	static Context cont;
	double src_lat = -19.042322405609326; // the testing source sucre
	double src_long = -65.25537729263306; 
	double dest_lat = -19.0286305044111; // the testing destination 
	double dest_long = -65.25685787200928;
	
	int opcionElegida;
	private Button routeButton;
    String TAG = "GPStest";
    // Set up the array of GeoPoints defining the route
    int numberRoutePoints;	
    GeoPoint routePoints [];   // Dimension will be set in class RouteLoader below
    int routeGrade [];               // Index for slope of route from point i to point i+1
    RouteSegmentOverlay route;   // This will hold the route segments
    boolean routeIsDisplayed = false;

	
	//
	private MapController mapControl;
    private GeoPoint gp;
	
    //coodenadas sucre
    private static double lat = -19.043965357882502;
    private static double lon = -65.24507761001587;
    private int latE6;
    private int lonE6;
	private List<Overlay> mapOverlays;
    private Drawable drawable1,drawable2;
    private CapaPuntos itemizedOverlay1,itemizedOverlay2;

    //coodenadas sucre
    private OverlayItem[] foodItem = {
            new OverlayItem(new GeoPoint((int) (-19.043965357882502 * 1E6),(int) (-65.24507761001587 * 1E6)), "Restaurant","COSTA"),
            new OverlayItem(new GeoPoint((int) (-19.0463587654258 * 1E6),(int) (-65.26509761810303 * 1E6)),"Bar","CONGA"),
            new OverlayItem(new GeoPoint((int) (-19.04749460777634 * 1E6),(int) (-65.25561332702637 * 1E6)),"Bar ","PRAZA"),
            new OverlayItem(new GeoPoint((int) (-19.040659153411507 * 1E6),(int) (-65.2599048614502 * 1E6)), "Pizzeria","FREI"),
            new OverlayItem(new GeoPoint((int) (-19.03284975874036 * 1E6),(int) (-65.25198698043823 * 1E6)),"Restaurant","PEDRO") 
        };
    private OverlayItem[] farmaciaItem = {
            new OverlayItem(new GeoPoint((int) (-19.037676 * 1E6),(int) (-65.252285 * 1E6)), "Farmacia","San Juan"),
            new OverlayItem(new GeoPoint((int) (-19.043559 * 1E6),(int) (-65.25991 * 1E6)),"Farmacia","Copacabana"),
            new OverlayItem(new GeoPoint((int) (-19.034269 * 1E6),(int) (-65.259478 * 1E6)),"Farmacia","El Sol"),
            new OverlayItem(new GeoPoint((int) (-19.04149 * 1E6),(int) (-65.252627 * 1E6)), "Farmacia","Sucre") 
        };
	
	private List<Address> addresses;
	private Geocoder gc;
	
	EditText tTexto1,tTexto2;
	Button bGo,bGo2,bGo3;
	MapView mapView;
	MapController mc;
	GeoPoint p;
	String direccion;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //ocultar el nombre del proyecto
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.mapa);
      
        Bundle extras=getIntent().getExtras();
        if(extras != null){
        	gpsLatitud=extras.getDouble("gpsLatitud");
        	gpsLongitud=extras.getDouble("gpsLongitud");
        	
        }
        
        //voice
    	bRecord = (Button)this.findViewById(R.id.bh2);
    	bTra= (Button)this.findViewById(R.id.btra1);
    	
    	
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
    	bTra.setOnClickListener(new View.OnClickListener() {	
    		public void onClick(View v) {
    			
    			say("Trazar Ruta");
    		}
    	});
    	//boton largo tiempo sonido vibrate
        bTra.setOnLongClickListener(new View.OnLongClickListener() {
    		
    		@Override
    		public boolean onLongClick(View v) {
    			// TODO Auto-generated method stub
    			
    			ToneGenerator sound = new ToneGenerator(AudioManager.STREAM_SYSTEM, 100);
    			 sound.startTone(ToneGenerator.TONE_PROP_BEEP);
    			 
    			((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(50);
    			

    			Toast.makeText(getApplicationContext(), "Trazar Ruta", Toast.LENGTH_LONG).show();
    			
        		//capturar coordenadas de inicio del GPS
    			src_lat=gpsLatitud;
    			src_long=gpsLongitud;
    		
    			String direcciones="RUTA TRAZADA, \n";
    			//GOOGLE DIRECTIONS
    			
    			direcciones=direcciones+getGoogleDirections5(String.valueOf(src_lat), String.valueOf(src_long), direccionFinalHablada);
    	    	direccionRutaTrazada=direcciones;
    			//graficar ruta
    			//cuando hagamos la iteracion desde cero hasta indiceGPSdatos
    	        
    	        
    	        GeoPoint startGP = new GeoPoint((int)(datosRutalatitud1[0]*1E6),(int)(datosRutalongitud1[0]*1E6)); 
    	        mapView.getOverlays().add(new MyOverLay(startGP,startGP,1)); 
    		    GeoPoint gp1; 
    		    GeoPoint gp2 = startGP; 
    		    for(int i=1;i<indiceRuta;i++) // the last one would be crash 
    		    {  
    			    gp1 = gp2;  
    			    gp2 = new GeoPoint((int)(datosRutalatitud1[i]*1E6),(int)(datosRutalongitud1[i]*1E6)); 
    			    mapView.getOverlays().add(new MyOverLay(gp1,gp2,2,Color.GREEN)); 
    			    //Log.d("xxx","pair:" + pairs[i]); 
    		    } 
    		    GeoPoint endmenos1GP = new GeoPoint((int)(datosRutalatitud1[indiceRuta-1]*1E6),(int)(datosRutalongitud1[indiceRuta-1]*1E6));
    		    
    		    GeoPoint endGP = new GeoPoint((int)(datosRutalatitud1[indiceRuta]*1E6),(int)(datosRutalongitud1[indiceRuta]*1E6));
    		    
    		    mapView.getOverlays().add(new MyOverLay(endmenos1GP,endGP,2,Color.GREEN));
    		    mapView.getOverlays().add(new MyOverLay(endGP,endGP, 3)); // use the default color
    		    
    	//cuando hagamos la iteracion desde cero hasta indiceGPSdatos
    	        
    	        
    	        GeoPoint startGP1 = new GeoPoint((int)(datosRutalatitud2[0]*1E6),(int)(datosRutalongitud2[0]*1E6)); 
    	        mapView.getOverlays().add(new MyOverLay(startGP1,startGP1,1)); 
    		    GeoPoint gp11; 
    		    GeoPoint gp22 = startGP1; 
    		    for(int i=1;i<indiceRuta;i++) // the last one would be crash 
    		    {  
    			    gp11 = gp22;  
    			    gp22 = new GeoPoint((int)(datosRutalatitud2[i]*1E6),(int)(datosRutalongitud2[i]*1E6)); 
    			    mapView.getOverlays().add(new MyOverLay(gp11,gp22,2,Color.GREEN)); 
    			    //Log.d("xxx","pair:" + pairs[i]); 
    		    } 
    		    GeoPoint endmenos1GP1 = new GeoPoint((int)(datosRutalatitud2[indiceRuta-1]*1E6),(int)(datosRutalongitud2[indiceRuta-1]*1E6));
    		    
    		    GeoPoint endGP1 = new GeoPoint((int)(datosRutalatitud2[indiceRuta]*1E6),(int)(datosRutalongitud2[indiceRuta]*1E6));
    		    
    		    mapView.getOverlays().add(new MyOverLay(endmenos1GP1,endGP1,2,Color.GREEN));
    		    mapView.getOverlays().add(new MyOverLay(endGP1,endGP1, 3)); // use the default color
    			//graficar ruta
    			
    	
    			Intent intento1 = new Intent(MapaActivity.this,RutaActivity.class);
				intento1.putExtra("cadena", direcciones);
				startActivity(intento1);
    			
    			say(direcciones);
    			
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
        
        talker = new TextToSpeech(this, this);
        
        //cont=getBaseContext();
        cont=getApplicationContext();
        
        //debemos crear el editText y el Button aqui
        //para evitar errores con las otras opciones
        
        gc = new Geocoder(this, Locale.getDefault());
        
        
        //boton menu
        /*
        Button btn = (Button) findViewById(R.id.bTrazarRuta);
        btn.setOnCreateContextMenuListener(this);
        btn.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//capturar coordenadas de inicio del GPS
					src_lat=gpsLatitud;
					src_long=gpsLongitud;
				
				//capturar coordenadas del destino
				if(capa1==1)
				{
					dest_lat=itemizedOverlay1.latEnviar;
					dest_long=itemizedOverlay1.longEnviar;
				}
				if(capa2==1)
				{
					dest_lat=itemizedOverlay2.latEnviar;
					dest_long=itemizedOverlay2.longEnviar;
				}
				
				String direcciones="Ruta Trazada, \n";
				//GOOGLE DIRECTIONS
				direcciones=getGoogleDirections3(String.valueOf(src_lat),String.valueOf(src_long),String.valueOf(dest_lat),String.valueOf(dest_long));
				Toast.makeText(getApplicationContext(),direcciones, Toast.LENGTH_LONG).show();
				say(direcciones);
			}
		});
        */
        
        mapView = (MapView)findViewById(R.id.mapView);
        //mapView.setBuiltInZoomControls(true);
    
        mapView.setSatellite(false);
        mapView.setTraffic(false);
        
        /*
        int maxZoom = mapView.getMaxZoomLevel();
        int initZoom = maxZoom - 2;
        
        mc = mapView.getController();
        mc.setZoom(initZoom);
        latE6 = (int) (lat * 1e6);
        lonE6 = (int) (lon * 1e6);
        
        p = new GeoPoint(latE6, lonE6);
        
        mc.animateTo(p);
        */
        
        lugaresDirecto();
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
	
	
	public void mostrarDialog()
	{
		final CharSequence[] items = {"Ubicacion Actual", "Lugar Destacado", "Punto en el Mapa"};
		//final CharSequence[] items = {"Ubicacion", "Lugar", "Punto"};
		
        AlertDialog.Builder builder = new AlertDialog.Builder(MapaActivity.this);
        builder.setTitle("Elegir un Punto");
        builder.setItems(items, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int item) {
                //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                opcionElegida=item;
                return;
            }
        });
        builder.create().show();
	}
	public int getOpcionElegida()
	{
		return opcionElegida;
	}
	public void setOpcionElegida(int valor)
	{
		opcionElegida=valor;
	}
	public GeoPoint asignarCoordenadas(int opcion)
	{	
		switch (opcion) {
		case 1:
			/*
			//parece que es para conseguir las coordenadas actuales de mi posicion con el GPS
	        Double lat=MapaActivity.this. .myLocation.getLatitude() * 1E6;
	        Double lng=MapaActivity.this.myLocation.getLongitude() * 1E6;
	        */
			break;
		case 2:
			
			break;
		case 3:
			
			break;
		}
		GeoPoint punto = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
		return punto;
	}
	
	/*
	//@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView)
	{
		if(event.getAction() == 1){
			GeoPoint p = mapView.getProjection().fromPixels(
					(int) event.getX(),
					(int) event.getY());
			
					Toast.makeText(getBaseContext(), "Location: "+
							p.getLatitudeE6()/1E6 + "," +
							p.getLongitudeE6()/1E6,
							Toast.LENGTH_SHORT).show();

					//REVERSE GEOCODING
					Geocoder geoCoder = new Geocoder(
							getBaseContext(), Locale.getDefault());
					try{
						List<Address> addresses = geoCoder.getFromLocation(
								p.getLatitudeE6()/1E6,
								p.getLongitudeE6()/1E6,
								1);
						String add = "";
						if (addresses.size() > 0)
						{
							for (int i=0;i<addresses.get(0).getMaxAddressLineIndex();i++)
								add += addresses.get(0).getAddressLine(i)+ "\n";
						}
						Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
					}
					catch(IOException e){
						e.printStackTrace();
					}
					return true;
					
					//GEOCODING
					Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
					try{
						List<Address> addresses = geoCoder.getFromLocationName(
								"empire state building",5);
						
						String add = "";
						if (addresses.size()>0){
							p=new GeoPoint(
									(int)(addresses.get(0).getLatitude()*1E16),
									(int)(addresses.get(0).getLongitude()*1E16));
						
							mc.animateTo(p);
							mapView.invalidate();
							//mapView.postInvalidate();
						}
					}
					catch(IOException e){
						e.printStackTrace();
					}
					
		}
		return false;
	}
	*/
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
    	MenuItem mnu1 = menu.add(0,0,0,"Trazar Ruta");
    	{
    		mnu1.setAlphabeticShortcut('t');
    		//mnu1.setIcon(R.drawable.vistas);
    	}
    	
    }
    private boolean MenuChoice(MenuItem item)
    {
    	switch(item.getItemId()){
    	case 0:
    		Toast.makeText(this, "Trazar Ruta", Toast.LENGTH_LONG).show();
    		//capturar coordenadas de inicio del GPS
			src_lat=gpsLatitud;
			src_long=gpsLongitud;
		
			//capturar coordenadas del destino
			if(capa1==1)
			{
				dest_lat=itemizedOverlay1.latEnviar;
				dest_long=itemizedOverlay1.longEnviar;
			}
			if(capa2==1)
			{
				dest_lat=itemizedOverlay2.latEnviar;
				dest_long=itemizedOverlay2.longEnviar;
			}
		
			String direcciones="RUTA TRAZADA, \n";
			//GOOGLE DIRECTIONS
			direcciones=direcciones+getGoogleDirections3(String.valueOf(src_lat),String.valueOf(src_long),String.valueOf(dest_lat),String.valueOf(dest_long));
			direccionRutaTrazada=direcciones;
			
			//graficar ruta
			//cuando hagamos la iteracion desde cero hasta indiceGPSdatos
	        
	        
	        GeoPoint startGP = new GeoPoint((int)(datosRutalatitud1[0]*1E6),(int)(datosRutalongitud1[0]*1E6)); 
	        mapView.getOverlays().add(new MyOverLay(startGP,startGP,1)); 
		    GeoPoint gp1; 
		    GeoPoint gp2 = startGP; 
		    for(int i=1;i<indiceRuta;i++) // the last one would be crash 
		    {  
			    gp1 = gp2;  
			    gp2 = new GeoPoint((int)(datosRutalatitud1[i]*1E6),(int)(datosRutalongitud1[i]*1E6)); 
			    mapView.getOverlays().add(new MyOverLay(gp1,gp2,2,Color.GREEN)); 
			    //Log.d("xxx","pair:" + pairs[i]); 
		    } 
		    GeoPoint endmenos1GP = new GeoPoint((int)(datosRutalatitud1[indiceRuta-1]*1E6),(int)(datosRutalongitud1[indiceRuta-1]*1E6));
		    
		    GeoPoint endGP = new GeoPoint((int)(datosRutalatitud1[indiceRuta]*1E6),(int)(datosRutalongitud1[indiceRuta]*1E6));
		    
		    mapView.getOverlays().add(new MyOverLay(endmenos1GP,endGP,2,Color.GREEN));
		    mapView.getOverlays().add(new MyOverLay(endGP,endGP, 3)); // use the default color
		    
	        //cuando hagamos la iteracion desde cero hasta indiceGPSdatos
	        
	        
	        GeoPoint startGP1 = new GeoPoint((int)(datosRutalatitud2[0]*1E6),(int)(datosRutalongitud2[0]*1E6)); 
	        mapView.getOverlays().add(new MyOverLay(startGP1,startGP1,1)); 
		    GeoPoint gp11; 
		    GeoPoint gp22 = startGP1; 
		    for(int i=1;i<indiceRuta;i++) // the last one would be crash 
		    {  
			    gp11 = gp22;  
			    gp22 = new GeoPoint((int)(datosRutalatitud2[i]*1E6),(int)(datosRutalongitud2[i]*1E6)); 
			    mapView.getOverlays().add(new MyOverLay(gp11,gp22,2,Color.GREEN)); 
			    //Log.d("xxx","pair:" + pairs[i]); 
		    } 
		    GeoPoint endmenos1GP1 = new GeoPoint((int)(datosRutalatitud2[indiceRuta-1]*1E6),(int)(datosRutalongitud2[indiceRuta-1]*1E6));
		    
		    GeoPoint endGP1 = new GeoPoint((int)(datosRutalatitud2[indiceRuta]*1E6),(int)(datosRutalongitud2[indiceRuta]*1E6));
		    
		    mapView.getOverlays().add(new MyOverLay(endmenos1GP1,endGP1,2,Color.GREEN));
		    mapView.getOverlays().add(new MyOverLay(endGP1,endGP1, 3)); // use the default color
			
		    //graficar ruta
			
		    Intent intento1 = new Intent(MapaActivity.this,RutaActivity.class);
			intento1.putExtra("cadena", direcciones);
			startActivity(intento1);
			
			say(direcciones);
    		
    		return true;
    	}
    	return false;
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		MapController mc = mapView.getController();
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_3:
			mc.zoomIn();
			break;
		case KeyEvent.KEYCODE_1:
			mc.zoomIn();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//class for draw route
	// NOTE: This is added inside the class ShowTheMap in the file ShowTheMap.java

    /* Class to implement single task on background thread without having to manage
    the threads directly. Launch with "new RouteLoader().execute(new URL(urlString)". 
    Must be launched from the UI thread and may only be invoked once.  Adapted from 
    example in Ch.10 of Android Wireless Application Development. Use this to do data 
    load from network on separate thread from main user interface to prevent locking
    main UI if there is network delay. */
	
    private class RouteLoader extends AsyncTask<URL, String, String> {

        @Override
        protected String doInBackground(URL... params) {
            // This pattern takes more than one param but we'll just use the first
            try {
                URL text = params[0];

                XmlPullParserFactory parserCreator;

                parserCreator = XmlPullParserFactory.newInstance();

                XmlPullParser parser = parserCreator.newPullParser();

                parser.setInput(text.openStream(), null);

                publishProgress("Parsing XML...");

                int parserEvent = parser.getEventType();
                int pointCounter = -1;
                int wptCounter = -1;
                int totalWaypoints = -1;
                int lat = -1;
                int lon = -1;
                String wptDescription = "";
                int grade = -1;
                
                // Parse the XML returned on the network
                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if(tag.compareTo("number")==0){
                            numberRoutePoints = Integer.parseInt(parser.getAttributeValue(null,"numpoints"));
                            totalWaypoints = Integer.parseInt(parser.getAttributeValue(null,"numwpts"));
                            routePoints = new GeoPoint[numberRoutePoints]; 
                            routeGrade = new int[numberRoutePoints];
                            Log.i(TAG, "   Total points = "+numberRoutePoints
                                            +" Total waypoints = "+totalWaypoints);
                        }
                        if(tag.compareTo("trkpt")==0){
                            pointCounter ++;
                            lat = Integer.parseInt(parser.getAttributeValue(null,"lat"));
                            lon = Integer.parseInt(parser.getAttributeValue(null,"lon"));
                            grade = Integer.parseInt(parser.getAttributeValue(null,"grade"));
                            routePoints[pointCounter] = new GeoPoint(lat, lon);
                            routeGrade[pointCounter] = grade;
                            Log.i(TAG,"   trackpoint="+pointCounter+" latitude="+lat+" longitude="+lon
                                                +" grade="+grade);
                        } else if(tag.compareTo("wpt")==0) {
                            wptCounter ++;
                            lat = Integer.parseInt(parser.getAttributeValue(null,"lat"));
                            lon = Integer.parseInt(parser.getAttributeValue(null,"lon"));
                            wptDescription = parser.getAttributeValue(null,"description");
                            Log.i(TAG,"   waypoint="+wptCounter+" latitude="+lat+" longitude="+lon
                                            +" "+wptDescription);                      	
                        } 
                        break;
                    }

                    parserEvent = parser.next();
                }

            } catch (Exception e) {
                Log.i("RouteLoader", "Failed in parsing XML", e);
                return "Finished with failure.";
            }

            return "Done...";
        }

        protected void onCancelled() {
            Log.i("RouteLoader", "GetRoute task Cancelled");
        }

        // Now that route data are loaded, execute the method to overlay the route on the map
        protected void onPostExecute(String result) {
                Log.i(TAG, "Route data transfer complete");
                overlayRoute();
        }

        protected void onPreExecute() {
            Log.i(TAG,"Ready to load URL");
        }

        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

    }
    
    // Overlay a route.  This method is only executed after loadRouteData() completes
    // on background thread.
    
    public void overlayRoute() {
    	if(route != null) return;  // To prevent multiple route instances if key toggled rapidly
    	// Set up the overlay controller
    	route = new RouteSegmentOverlay(routePoints, routeGrade); // My class defining route overlay
    	mapOverlays = mapView.getOverlays();
    	mapOverlays.add(route);
    	
    	// Added symbols will be displayed when map is redrawn so force redraw now
        mapView.postInvalidate(); 
    }
    
    //codigo del chino........!!!!!!!!  PARA DIBUJAR LA RUTA
    private void DrawPath(GeoPoint src,GeoPoint dest, int color, MapView mMapView01) 
    { 
	    //connect to map web service 
	    StringBuilder urlString = new StringBuilder(); 
	    //urlString.append("http://maps.google.com/maps?f=d&hl=en"); 
	    urlString.append("http://maps.google.com/maps?f=d&hl=es");
	    urlString.append("&saddr=");//from 
	    urlString.append( Double.toString((double)src.getLatitudeE6()/1.0E6 )); 
	    urlString.append(","); 
	    urlString.append( Double.toString((double)src.getLongitudeE6()/1.0E6 )); 
	    urlString.append("&daddr=");//to 
	    urlString.append( Double.toString((double)dest.getLatitudeE6()/1.0E6 )); 
	    urlString.append(","); 
	    urlString.append( Double.toString((double)dest.getLongitudeE6()/1.0E6 )); 
	    //urlString.append("&ie=UTF8&0&om=0&output=kml"); 
	    urlString.append("&ie=UTF8&0&om=0&output=kml&dirflg=w");//para que nos devuelva direcciones para caminar
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
	
		    if(doc.getElementsByTagName("GeometryCollection").getLength()>0) 
			{ 
			    //String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getNodeName();
			    String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getFirstChild().getNodeValue() ; 
			    Log.d("xxx","path="+ path); 
			    String [] pairs = path.split(" ");
			    
			    String[] lngLat = pairs[0].split(","); // lngLat[0]=longitude lngLat[1]=latitude lngLat[2]=height 
			    // src 
			    GeoPoint startGP = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6)); 
			    mMapView01.getOverlays().add(new MyOverLay(startGP,startGP,1)); 
			    GeoPoint gp1; 
			    GeoPoint gp2 = startGP; 
			    for(int i=1;i<pairs.length;i++) // the last one would be crash 
			    { 
				    lngLat = pairs[i].split(","); 
				    gp1 = gp2; 
				    // watch out! For GeoPoint, first:latitude, second:longitude 
				    gp2 = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6)); 
				    mMapView01.getOverlays().add(new MyOverLay(gp1,gp2,2,color)); 
				    Log.d("xxx","pair:" + pairs[i]); 
			    } 
			    mMapView01.getOverlays().add(new MyOverLay(dest,dest, 3)); // use the default color 
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
			Toast.makeText(MapaActivity.this, message,Toast.LENGTH_LONG).show();
        }

    }  
    private class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
        	gpsLatitud = location.getLatitude();
        	gpsLongitud = location.getLongitude();
         
        	if (datosObtenidos == 0)
        	{  
        		int maxZoom = mapView.getMaxZoomLevel();
                int initZoom = maxZoom - 4;
                
                mc = mapView.getController();
                mc.setZoom(initZoom);
                latE6 = (int) (gpsLatitud * 1e6);
                lonE6 = (int) (gpsLongitud * 1e6);
                
                p = new GeoPoint(latE6, lonE6);
                
                mc.animateTo(p);
                
        		datosObtenidos=1;
        		
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
        	}
        	
        	
        	//String message = String.format("Nueva Ubicacion \n Longitud: %1$s \n Latitud: %2$s",location.getLongitude(), location.getLatitude());
            
          //no se muestra la latitud y longitud actual porque perjudica a la aplicacion
            //Toast.makeText(MapaActivity.this, message, Toast.LENGTH_LONG).show();
        }
        public void onStatusChanged(String s, int i, Bundle b) {
        	
        	//no se muestra la latitud y longitud actual porque perjudica a la aplicacion
			//Toast.makeText(MapaActivity.this, "Estado del proveedor cambiado",Toast.LENGTH_LONG).show();
        }
        public void onProviderDisabled(String s) {
        	//no se muestra la latitud y longitud actual porque perjudica a la aplicacion
            //Toast.makeText(MapaActivity.this,"Proveedor desactivado por el usuario. GPS apagado",Toast.LENGTH_LONG).show();
        }
        public void onProviderEnabled(String s) {
        	//no se muestra la latitud y longitud actual porque perjudica a la aplicacion
            //Toast.makeText(MapaActivity.this,"Proveedor activado por el usuario. GPS prendido",Toast.LENGTH_LONG).show();
        }
    }
    //CODIGO GPS
    
    //para que aparescan directo en el mapa los lugares
    public void lugaresDirecto()
    {
        	capa1=1;
        	//lugares comida
                    int foodLength = foodItem.length;
                    mapOverlays = mapView.getOverlays();
                    //drawable1 = this.getResources().getDrawable(R.drawable.fork);
                    drawable1 = cont.getResources().getDrawable(R.drawable.fork);
                    itemizedOverlay1 = new CapaPuntos(drawable1,MapaActivity.this); // Objeto de la Capa de Items
                    for (int i = 0; i < foodLength; i++){
                        itemizedOverlay1.addOverlay(foodItem[i]);
                    }
                    mapOverlays.add(itemizedOverlay1);
                    mapView.postInvalidate();
        
			capa2=1;
        	//farmacias
			int farmaciaLength = farmaciaItem.length;
            mapOverlays = mapView.getOverlays();
            drawable2 = cont.getResources().getDrawable(R.drawable.tren);
            itemizedOverlay2 = new CapaPuntos(drawable2,MapaActivity.this); // Objeto de la Capa de Items
            for (int i = 0; i < farmaciaLength; i++){
                itemizedOverlay2.addOverlay(farmaciaItem[i]);
            }
            mapOverlays.add(itemizedOverlay2);
            mapView.postInvalidate();
        
    }
    
    //GOOGLE DIRECTIONS
    public String getGoogleDirections3(String la1,String lo1,String la2,String lo2)
    {
    	String direcciones="";
    	
    	
    	//String googledirectionsapi = "http://maps.googleapis.com/maps/api/directions/json?origin=-19.043985,-65.243382&destination=-19.039035,-65.244566&mode=walking&language=es&sensor=false";
    	String googledirectionsapi = "http://maps.googleapis.com/maps/api/directions/json?origin="+la1+","+lo1+"&destination="+la2+","+lo2+"&mode=walking&language=es&sensor=false";
    	try {

    	//codigo de otra pag
    		URL twitter = new URL(googledirectionsapi);
            URLConnection tc = twitter.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
            String line;
            String str = "";
            while ((line = in.readLine()) != null) {   
                str += line;
            }
    	//codigo de otra pag
    		
    	//Tranform the string into a json object
		final JSONObject json = new JSONObject(str);
		
    	//Get the route object
		final JSONObject jsonRoute = json.getJSONArray("routes").getJSONObject(0);
		
		//Get the leg, only one leg as we don't support waypoints
		final JSONObject leg = jsonRoute.getJSONArray("legs").getJSONObject(0);
		
		//Get the steps for this leg
		final JSONArray steps = leg.getJSONArray("steps");
		
		//Number of steps for use in for loop
		final int numSteps = steps.length();
		indiceRuta=numSteps-1;
		//Set the name of this route using the start & end addresses
		direcciones="Inicio en: \n";
		direcciones=direcciones+leg.getString("start_address") + " ,\n Hasta: \n" + leg.getString("end_address");
		
		//Get google's copyright notice (tos requirement)
		//route.setCopyright(jsonRoute.getString("copyrights"));
		//Get the total length of the route.
		
		direcciones=direcciones+"\n";
		direcciones=direcciones+"La distancia en metros es: \n";
		direcciones=direcciones+leg.getJSONObject("distance").getInt("value");
		
		direcciones=direcciones+"\n El tiempo estimado es: \n";
		direcciones=direcciones+leg.getJSONObject("duration").getString("text");
		direcciones=direcciones+"\n";
		
		//Get any warnings provided (tos requirement)
		if (!jsonRoute.getJSONArray("warnings").isNull(0)) {
			//route.setWarning(jsonRoute.getJSONArray("warnings").getString(0));
		}
		/* Loop through the steps, creating a segment for each one and
		 * decoding any polylines found as we go to add to the route object's
		 * map array.
		 */
		for (int i = 0; i < numSteps; i++) {
			//segment.clearPoints();
			//Get the individual step
			final JSONObject step = steps.getJSONObject(i);
			//Set the length of this segment in metres
			//final int length = step.getJSONObject("distance").getInt("value");
			//distance += length;
			//segment.setLength(length);
			//segment.setDistance(distance/1000);
			
			//Strip html from google directions and set as turn instruction
			
			direcciones=direcciones+"\n";
			direcciones=direcciones+step.getString("html_instructions").replaceAll("<(.*?)*>", "");
			
			if ( i <= numSteps - 1 )
			{
				datosRutalatitud1[i]=step.getJSONObject("start_location").getDouble("lat");
				datosRutalongitud1[i]=step.getJSONObject("start_location").getDouble("lng");
			
				datosRutalatitud2[i]=step.getJSONObject("end_location").getDouble("lat");
				datosRutalongitud2[i]=step.getJSONObject("end_location").getDouble("lng");
			}
			//Retrieve & decode this segment's polyline and add it to the route & segment.
			//List<GeoPoint> points = decodePolyLine(step.getJSONObject("polyline").getString("points"));
			//route.addPoints(points);
			//segment.addPoints(points);
			//Push a copy of the segment to the route
			//route.addSegment(segment.copy());
		}
		//Keep a copy of the overview polyline for the route for elevation service query.
		//route.setPolyline(jsonRoute.getJSONObject("overview_polyline").getString("points"));
    	} catch (IOException e) {
    	    // TODO Auto-generated catch block
    	    //e.printStackTrace();
    	}
    	catch (JSONException e) {
    		//Log.e(e.getMessage(), "Google JSON Parser - " + );
    	}
    	
    	return direcciones;
    }
  //GOOGLE DIRECTIONS
    public String getGoogleDirections4(String la1,String lo1,String destino)
    {
    	String direcciones="";
    	
    	//formato a usar con voz y coordenadas
    	//http://maps.googleapis.com/maps/api/directions/json?origin=-19.04149,-65.252627&destination=lemoine sucre bolivia&mode=walking&language=es&sensor=false
    		
    	//String googledirectionsapi = "http://maps.googleapis.com/maps/api/directions/json?origin=-19.043985,-65.243382&destination=-19.039035,-65.244566&mode=walking&language=es&sensor=false";
    	String googledirectionsapi = "http://maps.googleapis.com/maps/api/directions/json?origin="+la1+","+lo1+"&destination="+destino+"&mode=walking&language=es&sensor=false";
    	//Log.d("xxx","pair:" + "http://maps.googleapis.com/maps/api/directions/json?origin="+la1+","+lo1+"&destination="+destino+"&mode=walking&language=es&sensor=false");
    	//Toast.makeText(getApplicationContext(),"http://maps.googleapis.com/maps/api/directions/json?origin="+la1+","+lo1+"&destination="+destino+"&mode=walking&language=es&sensor=false", Toast.LENGTH_LONG).show();
    	try {

    	//codigo de otra pag
    		URL twitter = new URL(googledirectionsapi);
            URLConnection tc = twitter.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
            String line;
            String str = "";
            while ((line = in.readLine()) != null) {   
                str += line;
            }
    	//codigo de otra pag
    		
    	//Tranform the string into a json object
		final JSONObject json = new JSONObject(str);
		
    	//Get the route object
		final JSONObject jsonRoute = json.getJSONArray("routes").getJSONObject(0);
		
		//Get the leg, only one leg as we don't support waypoints
		final JSONObject leg = jsonRoute.getJSONArray("legs").getJSONObject(0);
		
		//Get the steps for this leg
		final JSONArray steps = leg.getJSONArray("steps");
		
		//Number of steps for use in for loop
		final int numSteps = steps.length();
		
		//Set the name of this route using the start & end addresses
		direcciones="inicio en: \n";
		direcciones=direcciones+leg.getString("start_address") + " ,\n hasta: \n" + leg.getString("end_address");
		
		//Get google's copyright notice (tos requirement)
		//route.setCopyright(jsonRoute.getString("copyrights"));
		//Get the total length of the route.
		
		direcciones=direcciones+"\n";
		direcciones=direcciones+"la distancia en metros es: \n";
		direcciones=direcciones+leg.getJSONObject("distance").getInt("value");
		
		direcciones=direcciones+"\n el tiempo estimado es: \n";
		direcciones=direcciones+leg.getJSONObject("duration").getString("text");
		direcciones=direcciones+"\n";
		
		//Get any warnings provided (tos requirement)
		if (!jsonRoute.getJSONArray("warnings").isNull(0)) {
			//route.setWarning(jsonRoute.getJSONArray("warnings").getString(0));
		}
		/* Loop through the steps, creating a segment for each one and
		 * decoding any polylines found as we go to add to the route object's
		 * map array.
		 */
		for (int i = 0; i < numSteps; i++) {
			//segment.clearPoints();
			//Get the individual step
			final JSONObject step = steps.getJSONObject(i);
			//Set the length of this segment in metres
			//final int length = step.getJSONObject("distance").getInt("value");
			//distance += length;
			//segment.setLength(length);
			//segment.setDistance(distance/1000);
			
			//Strip html from google directions and set as turn instruction
			
			direcciones=direcciones+"\n";
			direcciones=direcciones+step.getString("html_instructions").replaceAll("<(.*?)*>", "");
			
			//Retrieve & decode this segment's polyline and add it to the route & segment.
			//List<GeoPoint> points = decodePolyLine(step.getJSONObject("polyline").getString("points"));
			//route.addPoints(points);
			//segment.addPoints(points);
			//Push a copy of the segment to the route
			//route.addSegment(segment.copy());
		}
		//Keep a copy of the overview polyline for the route for elevation service query.
		//route.setPolyline(jsonRoute.getJSONObject("overview_polyline").getString("points"));
    	} catch (IOException e) {
    	    // TODO Auto-generated catch block
    	    //e.printStackTrace();
    	}
    	catch (JSONException e) {
    		//Log.e(e.getMessage(), "Google JSON Parser - " + );
    	}
    	
    	return direcciones;
    }
  //GOOGLE DIRECTIONS
    public String getGoogleDirections5(String la1,String lo1,String des)
    {
    	String direcciones="";
    	
    	
    	//String googledirectionsapi = "http://maps.googleapis.com/maps/api/directions/json?origin=-19.043985,-65.243382&destination=-19.039035,-65.244566&mode=walking&language=es&sensor=false";
    	//String googledirectionsapi = "http://maps.googleapis.com/maps/api/directions/json?origin="+la1+","+lo1+"&destination="+la2+","+lo2+"&mode=walking&language=es&sensor=false";
    	//http://maps.googleapis.com/maps/api/directions/json?origin=-19.04149,-65.252627&destination=lemoine sucre bolivia&mode=walking&language=es&sensor=false
    	
    	//http://maps.googleapis.com/maps/api/directions/json?origin="+la1+","+lo1+"&destination="+la2+","+lo2+"&mode=walking&language=es&sensor=false
    	//String googledirectionsapi = "http://maps.googleapis.com/maps/api/directions/json?origin="+la1+","+lo1+"&destination="+des+"&mode=walking&language=es&sensor=false";
    	
    	//http://maps.googleapis.com/maps/api/directions/json?origin=-19.04149,-65.252627&destination=arturo borja sucre bolivia&mode=walking&language=es&sensor=false
    	
    	int lati,longi;
    	String slati="";
    	String slongi="";
    	Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		try{
			List<Address> addresses = geoCoder.getFromLocationName(des,5);

			String add = "";
			if (addresses.size()>0){
				//lati=(int)(addresses.get(0).getLatitude()*1E16);
				//longi=(int)(addresses.get(0).getLongitude()*1E16);
				
				slati=String.valueOf(addresses.get(0).getLatitude());
				slongi=String.valueOf(addresses.get(0).getLongitude());
				
				//slati=String.valueOf(lati);
				//slongi=String.valueOf(longi);
				/*
				p=new GeoPoint(
						(int)(addresses.get(0).getLatitude()*1E16),
						(int)(addresses.get(0).getLongitude()*1E16));
						*/
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
    	//Toast.makeText(getApplicationContext(), slati+" "+slongi, Toast.LENGTH_LONG).show();
    	String googledirectionsapi = "http://maps.googleapis.com/maps/api/directions/json?origin="+la1+","+lo1+"&destination="+slati+","+slongi+"&mode=walking&language=es&sensor=false";
    	
    	try {

    	//codigo de otra pag
    		URL twitter = new URL(googledirectionsapi);
            URLConnection tc = twitter.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
            String line;
            String str = "";
            while ((line = in.readLine()) != null) {   
                str += line;
            }
    	//codigo de otra pag
    		
    	//Tranform the string into a json object
		final JSONObject json = new JSONObject(str);
		
    	//Get the route object
		final JSONObject jsonRoute = json.getJSONArray("routes").getJSONObject(0);
		
		//Get the leg, only one leg as we don't support waypoints
		final JSONObject leg = jsonRoute.getJSONArray("legs").getJSONObject(0);
		
		//Get the steps for this leg
		final JSONArray steps = leg.getJSONArray("steps");
		
		//Number of steps for use in for loop
		final int numSteps = steps.length();
		indiceRuta=numSteps-1;
		
		//Set the name of this route using the start & end addresses
		direcciones="Inicio en: \n";
		direcciones=direcciones+leg.getString("start_address") + " ,\n Hasta: \n" + leg.getString("end_address");
		
		//Get google's copyright notice (tos requirement)
		//route.setCopyright(jsonRoute.getString("copyrights"));
		//Get the total length of the route.
		
		direcciones=direcciones+"\n";
		direcciones=direcciones+"La distancia en metros es: \n";
		direcciones=direcciones+leg.getJSONObject("distance").getInt("value");
		
		direcciones=direcciones+"\n El tiempo estimado es: \n";
		direcciones=direcciones+leg.getJSONObject("duration").getString("text");
		direcciones=direcciones+"\n";
		
		//Get any warnings provided (tos requirement)
		if (!jsonRoute.getJSONArray("warnings").isNull(0)) {
			//route.setWarning(jsonRoute.getJSONArray("warnings").getString(0));
		}
		/* Loop through the steps, creating a segment for each one and
		 * decoding any polylines found as we go to add to the route object's
		 * map array.
		 */
		for (int i = 0; i < numSteps; i++) {
			//segment.clearPoints();
			//Get the individual step
			final JSONObject step = steps.getJSONObject(i);
			//Set the length of this segment in metres
			//final int length = step.getJSONObject("distance").getInt("value");
			//distance += length;
			//segment.setLength(length);
			//segment.setDistance(distance/1000);
			
			//Strip html from google directions and set as turn instruction
			
			direcciones=direcciones+"\n";
			direcciones=direcciones+step.getString("html_instructions").replaceAll("<(.*?)*>", "");

			if ( i <= numSteps - 1 )
			{
				datosRutalatitud1[i]=step.getJSONObject("start_location").getDouble("lat");
				datosRutalongitud1[i]=step.getJSONObject("start_location").getDouble("lng");
			
				datosRutalatitud2[i]=step.getJSONObject("end_location").getDouble("lat");
				datosRutalongitud2[i]=step.getJSONObject("end_location").getDouble("lng");
			}
			//Retrieve & decode this segment's polyline and add it to the route & segment.
			//List<GeoPoint> points = decodePolyLine(step.getJSONObject("polyline").getString("points"));
			//route.addPoints(points);
			//segment.addPoints(points);
			//Push a copy of the segment to the route
			//route.addSegment(segment.copy());
		}
		//Keep a copy of the overview polyline for the route for elevation service query.
		//route.setPolyline(jsonRoute.getJSONObject("overview_polyline").getString("points"));
    	} catch (IOException e) {
    	    // TODO Auto-generated catch block
    	    //e.printStackTrace();
    	}
    	catch (JSONException e) {
    		//Log.e(e.getMessage(), "Google JSON Parser - " + );
    	}
    	//Toast.makeText(getBaseContext(), direcciones, Toast.LENGTH_LONG).show();
    	return direcciones;
    }
    
    
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

    			//textSaid = (TextView) findViewById(R.id.tTextRecognition);
    			//textSaid.setText(matches.get(0));
    			
    			String direccionHablada="";
    			direccionHablada = matches.get(0);
    			//setDireccionHablada(direccionHablada);
    			direccionFinalHablada=direccionHablada;
    			say(direccionHablada);
    			
    		    Toast.makeText(getBaseContext(), direccionHablada, Toast.LENGTH_LONG).show();
    			
    		}
    		super.onActivityResult(requestCode, resultCode, data);
    	}
    	
    	public String getDireccionHablada()
    	{
    		return direccionFinalHablada;
    	}
    	public void setDireccionHablada(String dir)
    	{
    		direccionFinalHablada=dir;
    	}
}

