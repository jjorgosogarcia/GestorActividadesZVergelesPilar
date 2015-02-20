package com.example.sadarik.gestoractividadeszvergeles;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;


public class Splash extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv1 = (TextView)findViewById(R.id.tvIntro);
        String fuente = "fonts/3Dumb.ttf";
        // Cargamos la fuente:
        Typeface tf = Typeface.createFromAsset(getAssets(), fuente);
        // Aplicamos la fuente:
        tv1.setTypeface(tf);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this,Principal.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
