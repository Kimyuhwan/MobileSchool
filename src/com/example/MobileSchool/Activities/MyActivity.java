package com.example.MobileSchool.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Service.ManagerRegistrationService;
import com.example.MobileSchool.Utils.Constants;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private String TAG = Constants.TAG;

    private Button startServiceButton;
    private Button stopServiceButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.d(TAG, "MyActivity: onCreate");

        _initUI();
    }

    private void _initUI() {
        startServiceButton = (Button) findViewById(R.id.btn_startService);
        stopServiceButton = (Button) findViewById(R.id.btn_stopSerive);

        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click: startServiceButton");
                startService(new Intent(getApplicationContext(), ManagerRegistrationService.class));
            }
        });

        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click: stopServiceButton");
                stopService(new Intent(getApplicationContext(), ManagerRegistrationService.class));
            }
        });
    }

}
