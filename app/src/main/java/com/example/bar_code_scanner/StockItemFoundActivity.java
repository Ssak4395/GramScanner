package com.example.bar_code_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.bar_code_scanner.utilities.Helper;

import java.util.Date;
import java.util.HashMap;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StockItemFoundActivity extends AppCompatActivity {

    TableLayout tableLayout;
    TextView coilID;
    TextView Width;
    TextView Type;
    TextView color;
    TextView status;
    TextView Gauge;
    TextView Weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_item_found);
        tableLayout = findViewById(R.id.tableLayout);
        HashMap<String, String> hashMap = (HashMap) getIntent().getExtras().get("map");
        initUI();
        coilID.setText(hashMap.get("CoilID"));
        Width.setText(hashMap.get("Width"));
        color.setText(hashMap.get("Color"));
        Gauge.setText(hashMap.get("Gauge"));
        Weight.setText(hashMap.get("Weight"));
        Type.setText(hashMap.get("Type"));
        status.setText(hashMap.get("Status"));

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void initUI()
    {
     coilID = findViewById(R.id.coil_id);
     coilID.setTypeface(Typeface.DEFAULT_BOLD);
     Width = findViewById(R.id.coil_width);
     Type = findViewById(R.id.coil_type);
     color = findViewById(R.id.coil_color);
     status = findViewById(R.id.coil_status);
     Gauge = findViewById(R.id.coil_gauge);
     Weight = findViewById(R.id.coil_weight);

    }


}