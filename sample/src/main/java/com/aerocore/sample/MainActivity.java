package com.aerocore.sample;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.aerocore.AeroCore;
import com.aerocore.AeroConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        setContentView(tv);

        AeroConfig config = new AeroConfig.Builder()
                .licenseKey("AERO_SAMPLE_LICENSE_KEY")
                .build();

        AeroCore.getInstance().init(getApplicationContext(), config);

        boolean isReady = AeroCore.getInstance().isReady();
        String version = AeroCore.getInstance().sdkVersion();

        tv.setText("AeroCore Initialized: " + isReady + "\nSDK Version: " + version);
    }
}
