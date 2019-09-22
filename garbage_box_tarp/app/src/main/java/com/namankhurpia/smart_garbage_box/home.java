package com.namankhurpia.smart_garbage_box;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

import static java.lang.Thread.sleep;

public class home extends AppCompatActivity {

    TextView name_tv,today_count_tv,total_count_tv;
    ImageButton donate_more;

    private DatabaseReference mDatabase;

    String name_str;
    String result_after_scan=null;
    int today_count=0;
    int total_count=0;


    String value;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        today_count_tv = (TextView) findViewById(R.id.todayc);
        total_count_tv = (TextView) findViewById(R.id.totalc);


        //default val
        SharedPreferences prefss = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String today_count_str_def = prefss.getString("today_count", "");
        String  total_count_str_def = prefss.getString("total_count", "");
        today_count_tv.setText(today_count_str_def);
        total_count_tv.setText(total_count_str_def);

        //default

        try {
            value = getIntent().getExtras().getString("add");
            Log.d("^^^^", value);

        } catch (Exception e) {

        } finally {

            if(value.equals(1+"")){
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor edit = prefs.edit();
                String today_count_str = prefs.getString("today_count", "");
                today_count = Integer.parseInt(today_count_str);
                String  total_count_str = prefs.getString("total_count", "");
                total_count = Integer.parseInt(total_count_str);

                Log.d("$$$$ todaycount", today_count + "");
                Log.d("$$$$ totalcount", total_count + "");
                today_count = today_count + 1;
                total_count = total_count + 1;
                today_count_tv.setText(today_count + "");
                total_count_tv.setText(total_count + "");



                edit.putString("today_count",today_count+"");
                edit.putString("total_count",total_count+"");
                edit.commit();

            }


            read_name_from_internal();
            //read_count_from_internal();
            name_tv = (TextView) findViewById(R.id.namee);
            name_tv.setText("Hi " + name_str);




            donate_more = (ImageButton) findViewById(R.id.donate);
            donate_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    opencam();

                }
            });


        }

    }



    private void read_name_from_internal()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        name_str = prefs.getString("name","");
        Log.d("####name from internal",name_str);
    }



    private void opencam() {
        final Activity activity = this;
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                result_after_scan = result.getContents();
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
                Intent i = new Intent(home.this, loading_activity.class);
                i.putExtra("bin_no", result_after_scan);
                startActivity(i);



            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



}
