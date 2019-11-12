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



    String name_str;
    String result_after_scan=null;
    int today_count=0;
    int total_count=0;

    ImageButton buynote,buyoctane;
    TextView ava_note,ava_octane,unava_note, unava_octane;

    private DatabaseReference mDatabase;

    String value="bin9876";

    @Override
    protected void onPostResume() {
        super.onPostResume();

        SharedPreferences prefss = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String today_count_str_def = prefss.getString("today_count", "");
        String  total_count_str_def = prefss.getString("total_count", "");
        today_count_tv.setText(today_count_str_def);
        total_count_tv.setText(total_count_str_def);
        if(Integer.parseInt(today_count_str_def)>=10)
        {
            ava_note.setVisibility(View.VISIBLE);
            unava_note.setVisibility(View.INVISIBLE);
        }
        else
        {
            unava_note.setVisibility(View.VISIBLE);
            ava_note.setVisibility(View.INVISIBLE);
        }
        if(Integer.parseInt(today_count_str_def)>=5)
        {
            ava_octane.setVisibility(View.VISIBLE);
            unava_octane.setVisibility(View.INVISIBLE);
        }
        else
        {
            ava_octane.setVisibility(View.INVISIBLE);
            unava_octane.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        today_count_tv = (TextView) findViewById(R.id.todayc);
        total_count_tv = (TextView) findViewById(R.id.totalc);
        buynote = (ImageButton)findViewById(R.id.notebuy);
        buyoctane = (ImageButton)findViewById(R.id.octanebuy);
        ava_note = (TextView)findViewById(R.id.ava_notebook);
        ava_octane = (TextView)findViewById(R.id.ava_octane);
        unava_note =(TextView)findViewById(R.id.unava_notebook);
        unava_octane = (TextView)findViewById(R.id.unava_octane);

        ava_note.setVisibility(View.INVISIBLE);
        ava_octane.setVisibility(View.INVISIBLE);
        unava_note.setVisibility(View.INVISIBLE);
        unava_octane.setVisibility(View.INVISIBLE);

        //default val
        SharedPreferences prefss = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String today_count_str_def = prefss.getString("today_count", "");
        String  total_count_str_def = prefss.getString("total_count", "");
        today_count_tv.setText(today_count_str_def);
        total_count_tv.setText(total_count_str_def);

        //default

        if(Integer.parseInt(today_count_str_def)>=10)
        {
            ava_note.setVisibility(View.VISIBLE);
            unava_note.setVisibility(View.INVISIBLE);
        }
        else
        {
            unava_note.setVisibility(View.VISIBLE);
            ava_note.setVisibility(View.INVISIBLE);
        }
        if(Integer.parseInt(today_count_str_def)>=5)
        {
            ava_octane.setVisibility(View.VISIBLE);
            unava_octane.setVisibility(View.INVISIBLE);
        }
        else
        {
            ava_octane.setVisibility(View.INVISIBLE);
            unava_octane.setVisibility(View.VISIBLE);
        }

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

        buyoctane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor edit = prefs.edit();

                String today_count_str = prefs.getString("today_count", "");
                today_count = Integer.parseInt(today_count_str);
                String  total_count_str = prefs.getString("total_count", "");
                total_count = Integer.parseInt(total_count_str);

                Log.d("$$$$ buy octane", today_count + "");
                Log.d("$$$$ buy octanee", total_count + "");

                if(today_count>=5)
                {
                    today_count = (today_count) -5;
                    total_count = (total_count) -5;

                    today_count_tv.setText(today_count + "");
                    total_count_tv.setText(total_count + "");
                    edit.putString("today_count",today_count+"");
                    edit.putString("total_count",total_count+"");
                    edit.commit();

                    Toast.makeText(getApplicationContext(),"CONGRATULATIONS!! CLASSMATE OCTANE PURCHASED",Toast.LENGTH_LONG).show();
                    if(Integer.parseInt(today_count_str_def)>=10)
                    {
                        ava_note.setVisibility(View.VISIBLE);
                        unava_note.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        unava_note.setVisibility(View.VISIBLE);
                        ava_note.setVisibility(View.INVISIBLE);
                    }
                    if(Integer.parseInt(today_count_str_def)>=5)
                    {
                        ava_octane.setVisibility(View.VISIBLE);
                        unava_octane.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        ava_octane.setVisibility(View.INVISIBLE);
                        unava_octane.setVisibility(View.VISIBLE);
                    }
                    pushinfirebase();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"NOT ENOUGH POINTS",Toast.LENGTH_LONG).show();
                }


            }
        });

        buynote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor edit = prefs.edit();


                Log.d("$$$$ buy note", today_count + "");
                Log.d("$$$$ buy note", total_count + "");

                if(today_count>=10)
                {
                    int updatetoday = (today_count) -10;
                    int updatetotal = (today_count) -10;



                    today_count_tv.setText(updatetoday + "");
                    total_count_tv.setText(updatetotal + "");
                    edit.putString("today_count",updatetoday+"");
                    edit.putString("total_count",updatetotal+"");
                    edit.commit();

                    Toast.makeText(getApplicationContext(),"CONGRATULATIONS!! CLASSMATE NOTEBOOK PURCHASED",Toast.LENGTH_LONG).show();
                    if(Integer.parseInt(today_count_str_def)>=10)
                    {
                        ava_note.setVisibility(View.VISIBLE);
                        unava_note.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        unava_note.setVisibility(View.VISIBLE);
                        ava_note.setVisibility(View.INVISIBLE);
                    }
                    if(Integer.parseInt(today_count_str_def)>=5)
                    {
                        ava_octane.setVisibility(View.VISIBLE);
                        unava_octane.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        ava_octane.setVisibility(View.INVISIBLE);
                        unava_octane.setVisibility(View.VISIBLE);
                    }
                    pushinfirebase();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"NOT ENOUGH POINTS",Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    private void pushinfirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference rf = db.child(value).child("condition");
        mDatabase.child("purchase").child("name").setValue("NAMAN KHURPIA purchased OCTANE");
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
