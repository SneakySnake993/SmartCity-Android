package be.happli.pos.smartcity.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import be.happli.pos.smartcity.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent navigationDrawer = new Intent(MainActivity.this, NavigationDrawerActivity.class);
        startActivity(navigationDrawer);
    }
}