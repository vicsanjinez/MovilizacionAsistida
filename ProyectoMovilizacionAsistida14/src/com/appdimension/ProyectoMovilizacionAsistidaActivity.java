package com.appdimension;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import android.view.Window;

public class ProyectoMovilizacionAsistidaActivity extends Activity {
	static Context context;
	
	TextView t1,t2;
	ImageView i1;
	TableLayout table;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //ocultar el nombre del proyecto
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.splash);
        
                
        context = getApplicationContext();
        
        //Animation spinin=AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        Animation spinin=AnimationUtils.loadAnimation(this, R.anim.fade_in);
        LayoutAnimationController controller=new LayoutAnimationController(spinin);
        table = (TableLayout)findViewById(R.id.tableLayout1);
        for(int i=0;i<table.getChildCount();i++)
        {
        	TableRow row=(TableRow)table.getChildAt(i);
        	row.setLayoutAnimation(controller);
        }
        
        Animation fadeFinal=AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeFinal.setAnimationListener(new AnimationListener() {
			
			//@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			//@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			//@Override
			public void onAnimationEnd(Animation animation) {
			}
		});
        
        //mejor manera de hacer un splash
        int timeSplashScreenShowsInMilliseconds = 3000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
        	@Override
        	public void run() {
        		finish();
        		Intent mainActivity = new Intent();
        		String activityPackageName = MenuActivity.class.getPackage().getName();
        		String activityClassName = MenuActivity.class.getName();
        		mainActivity.setClassName(activityPackageName, activityClassName);
        		startActivity(mainActivity);
        	}
        }, timeSplashScreenShowsInMilliseconds);
        
        /*
        Thread splashThread = new Thread() {
            @Override
            public void run() {
               try {
                  int waited = 0;
                  while (waited < 5000) {
                     sleep(100);
                     waited += 100;
                  }
               } catch (InterruptedException e) {
                  // do nothing
               } finally {
                  finish();
                  
                  Intent i = new Intent();
                  i.setClassName("com.usfx.sistemas",
                                 "com.usfx.sistemas.MenuActivity");
                  startActivity(i);
                  
           		}
            }
         };
         splashThread.start();
         */
    }
    @Override
    protected void onPause(){
    	super.onPause();
    	/*
    	t1.clearAnimation();
    	t2.clearAnimation();
    	*/
    	for(int i=0;i<table.getChildCount();i++)
        {
        	TableRow row=(TableRow)table.getChildAt(i);
        	row.clearAnimation();
        }
    }
    
}