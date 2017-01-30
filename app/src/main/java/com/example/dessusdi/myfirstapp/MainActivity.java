package com.example.dessusdi.myfirstapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dessusdi.myfirstapp.model.AqcinObject;
import com.example.dessusdi.myfirstapp.tools.AqcinAsyncTask;


public class MainActivity extends ActionBarActivity {

    public String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AqcinObject objectAqcin = new AqcinObject(MainActivity.this);
        objectAqcin.getData();



        //TextView contentText = (TextView) findViewById(R.id.textViewAPI);
        //contentText.setText(async.getJSONStr());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
