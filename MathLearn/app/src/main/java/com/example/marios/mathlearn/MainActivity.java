package com.example.marios.mathlearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToDialekseis(View view){
        Intent intent = new Intent(this, Dialekseis.class);
        startActivity(intent);
    }

    public void goToAskhseis(View view){
        Intent intent = new Intent(this, Askhseis.class);
        startActivity(intent);
    }

    public void goToAnakoinwseis(View view){
        Intent intent = new Intent(this, Anakoinwseis.class);
        startActivity(intent);
    }

}
