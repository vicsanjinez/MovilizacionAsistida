package com.appdimension;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MapaDatos extends MapActivity {
	MapView mapView;
	MapController mc;
	private int latE6;
    private int lonE6;
    GeoPoint p;
	
	String[] datosGPSlatitud2=new String[10000];
	String[] datosGPSlongitud2=new String[10000];
	int indiceGPSdatos=0;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //ocultar el nombre del proyecto
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
        
        setContentView(R.layout.mapadatos);
        
        Bundle extras=getIntent().getExtras();
        if(extras != null){
        	datosGPSlatitud2=extras.getStringArray("datosGPSlatitud");
        	datosGPSlongitud2=extras.getStringArray("datosGPSlongitud");
        	indiceGPSdatos=extras.getInt("indice");
        }
        
        mapView = (MapView)findViewById(R.id.mapView2);
        mapView.setBuiltInZoomControls(true);
        
        //String message = String.format("DATOS \n "+datosGPSlatitud2[0]+" \n "+datosGPSlatitud2[1]+" \n "+datosGPSlatitud2[2]);
		//Toast.makeText(MapaDatos.this, message,Toast.LENGTH_LONG).show();
        
        /*
        String msj="";
        msj="el indice es: \n "+indiceGPSdatos+" \n "+datosGPSlatitud2[0];
        Log.d("xxx","pair:" + msj);
        for(int j=1;j<=indiceGPSdatos;j++)
        {
        	msj=msj+" \n "+datosGPSlatitud2[j];
        	Log.d("xxx","pair:" + msj);
        }
        Toast.makeText(MapaDatos.this,msj,Toast.LENGTH_LONG).show();
        */
        
        //cuando hagamos la iteracion desde cero hasta indiceGPSdatos
        
        
        if (indiceGPSdatos == 0)
        {
        	Toast.makeText(MapaDatos.this, "No existe una ruta almacenada",Toast.LENGTH_LONG).show();
        }
        else
        {
        
	        GeoPoint startGP = new GeoPoint((int)(Double.parseDouble(datosGPSlatitud2[0])*1E6),(int)(Double.parseDouble(datosGPSlongitud2[0])*1E6)); 
	        mapView.getOverlays().add(new MyOverLay(startGP,startGP,1)); 
		    GeoPoint gp1; 
		    GeoPoint gp2 = startGP; 
		    for(int i=1;i<indiceGPSdatos;i++) // the last one would be crash 
		    {  
			    gp1 = gp2;  
			    gp2 = new GeoPoint((int)(Double.parseDouble((datosGPSlatitud2[i]))*1E6),(int)(Double.parseDouble(datosGPSlongitud2[i])*1E6)); 
			    mapView.getOverlays().add(new MyOverLay(gp1,gp2,2,Color.GREEN)); 
			    //Log.d("xxx","pair:" + pairs[i]); 
		    } 
		    GeoPoint endmenos1GP = new GeoPoint((int)(Double.parseDouble(datosGPSlatitud2[indiceGPSdatos-1])*1E6),(int)(Double.parseDouble(datosGPSlongitud2[indiceGPSdatos-1])*1E6));
		    
		    GeoPoint endGP = new GeoPoint((int)(Double.parseDouble(datosGPSlatitud2[indiceGPSdatos])*1E6),(int)(Double.parseDouble(datosGPSlongitud2[indiceGPSdatos])*1E6));
		    
		    mapView.getOverlays().add(new MyOverLay(endmenos1GP,endGP,2,Color.GREEN));
		    mapView.getOverlays().add(new MyOverLay(endGP,endGP, 3)); // use the default color
		    
		    //ZOOM A LA ULTIMA POSICION
		    int maxZoom = mapView.getMaxZoomLevel();
	        int initZoom = maxZoom - 5;
	        
	        mc = mapView.getController();
	        mc.setZoom(initZoom);
	        latE6 = (int) (Double.parseDouble(datosGPSlatitud2[indiceGPSdatos])*1E6);
	        lonE6 = (int) (Double.parseDouble(datosGPSlongitud2[indiceGPSdatos])*1E6);
        
	        p = new GeoPoint(latE6, lonE6);
	        
	        mc.animateTo(p);
	        //ZOOM A LA ULTIMA POSICION
        }
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
