package com.namankhurpia.smart_garbage_box;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skyfishjy.library.RippleBackground;

public class loading_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading_activity);
        final String value = getIntent().getExtras().getString("bin_no");
        Log.d("%%% TAG intent val-" , value);

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        rippleBackground.startRippleAnimation();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference rf = db.child(value).child("condition");

        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Object res = dataSnapshot.getValue();
                String result_from_firebase = String.valueOf(res);
                Log.d("####value from firebase", result_from_firebase);
                if(result_from_firebase.equals("OPEN"))
                {
                    Intent  i = new Intent(loading_activity.this, home.class);
                    i.putExtra("add","1");
                    startActivity(i);
                }
                //add_points();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // hide_layout_for_wait();

            }});



    }
}
