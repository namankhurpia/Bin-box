package com.namankhurpia.smart_garbage_box;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {


    private DatabaseReference mDatabase;

    EditText ed1,ed2;
    ImageButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ed1 = (EditText)findViewById(R.id.ed1);
        ed2 = (EditText)findViewById(R.id.ed2);
        btn = (ImageButton) findViewById(R.id.btn);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ph = ed1.getText().toString().trim();
                String name = ed2.getText().toString().trim();
                mDatabase.child("users").child(ph).child("name").setValue(name);
                Log.d("####","send");

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("name", name);
                edit.putString("phone",ph);
                edit.putString("today_count", 0+"");
                edit.putString("total_count",0+"");
                edit.commit();

                Intent  i = new Intent(Login.this,home.class);
                startActivity(i);

            }
        });

    }




}
