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
    FrameLayout layoutforwait;

    private DatabaseReference mDatabase;

    String name_str;
    String result_after_scan=null;
    int today_count=0;
    int total_count=0;
    String level = "level1";
    String result_from_firebase=null;
    boolean check=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        today_count_tv = (TextView)findViewById(R.id.todayc);
        total_count_tv = (TextView)findViewById(R.id.totalc);
        update_Layout_for_counts();

        layoutforwait  =(FrameLayout)findViewById(R.id.layoutforwait);
        hide_layout_for_wait();

        read_name_from_internal();
        read_count_from_internal();
        name_tv = (TextView)findViewById(R.id.namee);
        name_tv.setText("Hi "+name_str);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        donate_more = (ImageButton)findViewById(R.id.donate);
        donate_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opencam();
                show_layout_for_wait();

            }
        });


    }


    private void read_count_from_internal() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        today_count = prefs.getInt("today_count",0);
        total_count = prefs.getInt("total_count",0);
        Log.d("###today_count_internal",today_count+"");
        Log.d("###total_count_internal",total_count+"");
    }


    private void hide_layout_for_wait() {
        layoutforwait.setVisibility(View.GONE);
    }

    private void check_for_condition() {



            if (result_after_scan != null) {
                Log.d("$$$$$", "got in");
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                DatabaseReference rf = db.child(result_after_scan).child("condition");

                rf.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Object res = dataSnapshot.getValue();
                        result_from_firebase = String.valueOf(res);
                        Log.d("####value from firebase", result_from_firebase);
                        add_points();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        hide_layout_for_wait();

                    }
                });
            } else {
                Log.d("$$$$$", "scan quickly_timeout");
            }


    }

    private void update_in_firebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(result_after_scan).child("condition").setValue("closed");
    }

    private void show_layout_for_wait() {
        layoutforwait.setVisibility(View.VISIBLE);
    }


    private void read_name_from_internal() {
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
                check = true;
                check_for_condition();

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void add_points() {
        if (result_from_firebase.equals("open"))
        {
            today_count = today_count + 1;
            total_count = total_count + 1;
            update_Layout_for_counts();
            hide_layout_for_wait();
            update_in_firebase();

        } else {
            Toast.makeText(getApplicationContext(), "Slow internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void update_Layout_for_counts() {
        total_count_tv.setText(total_count+"");
        today_count_tv.setText(today_count+"");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString("today_count", today_count+"");
        edit.putString("total_count",total_count+"");
    }
}
