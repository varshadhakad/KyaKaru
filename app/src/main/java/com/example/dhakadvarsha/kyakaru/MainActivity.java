package com.example.dhakadvarsha.kyakaru;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button clickmeBtn;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clickmeBtn=(Button)findViewById(R.id.clickmeBtn);
        /*clickmeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new APICalls().placesToEat(30,17.440081, 78.348915);
            }
        });*/

        clickmeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new placeToVisit().getPlacesToVisit(30,17.440081, 78.348915);
            }
        });


    }


}
