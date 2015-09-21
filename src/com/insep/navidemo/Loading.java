package com.insep.navidemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Loading extends Activity{   
	    private final int SPLASH_DISPLAY_LENGHT = 1000; //延迟三秒     
	       
	    @Override    
	    public void onCreate(Bundle savedInstanceState) {    
	        super.onCreate(savedInstanceState);    
	        setContentView(R.layout.loading);    
	        this.getActionBar().hide();
	        new Handler().postDelayed(new Runnable(){    
	     
	         @Override    
	         public void run() {    
	             Intent mainIntent = new Intent(Loading.this,MainActivity.class);    
	             Loading.this.startActivity(mainIntent);    
	             Loading.this.finish();    
	         }    
	               
	        }, SPLASH_DISPLAY_LENGHT);    
	    }    
}  