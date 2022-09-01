package com.stopwatchfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DataReading extends AppCompatActivity  implements RecyclerViewInterface{

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ArrayList<TimeData> data;
    private DatabaseReference DB;
    private DataAdapter adapter;

    private String year, month, week, day;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),StopWatch.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_reading);

        mAuth= FirebaseAuth.getInstance();
        currentUser= mAuth.getCurrentUser();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String date = dateFormat.format(calendar.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String time = timeFormat.format(calendar.getTime());

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
        day = dayFormat.format(calendar.getTime());

        //String[] partDate = date.split("-");
        //weekCalendar = Calendar.WEEK_OF_MONTH;
        //week= "Week"+String.valueOf(weekCalendar-1);
        year= getIntent().getExtras().getString("year");
        month= getIntent().getExtras().getString("month");
        week= getIntent().getExtras().getString("week");
        day= getIntent().getExtras().getString("day");

        DB= FirebaseDatabase.getInstance().getReference("Running time").child("Users").child(currentUser.getUid()).child("date");

        recyclerView= findViewById(R.id.recyclerview);
        data= new ArrayList<>();
        layoutManager= new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        adapter= new DataAdapter(DataReading.this, data,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        DB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TimeData timeData = dataSnapshot.getValue(TimeData.class);
                    data.add(timeData);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    @Override
    public void onItemClick(int position, boolean isDeleted) {
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        FirebaseUser currentUser= mAuth.getCurrentUser();
        DatabaseReference DBday= FirebaseDatabase.getInstance().getReference("Running time").child("Users").child(currentUser.getUid()).child("date");

        if(isDeleted) {
            DBday.child(data.get(position).getDate()).child(data.get(position).getKey()).removeValue();
            data.remove(position);
            adapter.notifyDataSetChanged();
        }
    }
}