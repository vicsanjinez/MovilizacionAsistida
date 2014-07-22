package com.appdimension;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class CapaPuntos extends ItemizedOverlay<OverlayItem>{
    Context mContexto;
	double latEnviar,longEnviar;
    private ArrayList<OverlayItem> myOverlays;

    public CapaPuntos(Drawable defaultMarker) {
        //super(defaultMarker);
    	super(boundCenterBottom(defaultMarker));
        myOverlays = new ArrayList<OverlayItem>();
        populate();
        // TODO Auto-generated constructor stub
    }
    public CapaPuntos(Drawable defaultMarker,Context context) {
        //super(defaultMarker);
    	super(boundCenterBottom(defaultMarker));
    	mContexto=context;
        myOverlays = new ArrayList<OverlayItem>();
        populate();
        // TODO Auto-generated constructor stub
    }

    public void addOverlay(OverlayItem overlay){
        myOverlays.add(overlay);
        populate();
    }
    
    public void removeItem(int i){
        if(i >= 0) myOverlays.remove(i);
        populate();
    }
    public void removeOverlay()
    {
    	myOverlays.clear();
    	
    }
    
    @Override
    protected OverlayItem createItem(int i) {
        // TODO Auto-generated method stub
        return myOverlays.get(i);
    }

    @Override
    protected boolean onTap(int i){        
        GeoPoint  gpoint = myOverlays.get(i).getPoint();
        double lat = gpoint.getLatitudeE6()/1e6;
        double lon = gpoint.getLongitudeE6()/1e6;
        String toast = "Categoria: "+myOverlays.get(i).getTitle();
        toast += "\nNombre: "+myOverlays.get(i).getSnippet();
        toast +=     "\nCoordenadas: \nLat = "+lat+" \nLon = "+lon;
        
        latEnviar=lat;	
		longEnviar=lon;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mContexto);
        builder.setTitle("Informacion");
        builder.setMessage(toast);
        
        builder.setPositiveButton("OK", new
        		DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog,int whichButton)
        		{
        		
        		}
        		});
        builder.create().show();
        return true;
    }

    @Override
    public int size(){
        return myOverlays.size();
    }
    @Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView)
	{
		if(event.getAction() == 1){
			GeoPoint p = mapView.getProjection().fromPixels(
					(int) event.getX(),
					(int) event.getY());
			
					latEnviar=p.getLatitudeE6()/1E6;
					longEnviar=p.getLongitudeE6()/1E6;
					
					//Toast.makeText(ProyectoMovilizacionAsistidaActivity.context, "Location: "+p.getLatitudeE6()/1E6 + "," +p.getLongitudeE6()/1E6,Toast.LENGTH_SHORT).show();
					//Toast.makeText(mContexto, "Location: "+p.getLatitudeE6()/1E6 + "," +p.getLongitudeE6()/1E6,Toast.LENGTH_SHORT).show();
		}
		return false;
	}

}