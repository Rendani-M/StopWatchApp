package com.stopwatchfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StopWatch extends AppCompatActivity {
    private Button btnstart, btnstop, btnprogress;
    private Chronometer timer;
    private ImageView icanchor;
    private Animation roundingalone;
    private TextView runtime, systemTime, systemDate;

    // Database Keys
    private String year, month, month_day, day, dateTime, date, time, runningTime;
    private DatabaseReference DBday, DBweek, DBmonth, DByear;
    private String UserId, timeId;

    //Variables for week tracking
    private int weekCalendar;
    private String week;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        btnstop = findViewById(R.id.btnstop);
        btnstop.setAlpha(0);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");

        dateTime = dateTimeFormat.format(calendar.getTime());
        date = dateFormat.format(calendar.getTime());
        time = timeFormat.format(calendar.getTime());
        day = dayFormat.format(calendar.getTime());
        //Date and time
        Thread thread= new Thread(){
            @Override
            public void run() {
                try{
                    while(!isInterrupted()){
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                btnstart = findViewById(R.id.btnstart);
                                btnstop = findViewById(R.id.btnstop);
                                btnprogress= findViewById(R.id.btnprogress);
                                timer = findViewById(R.id.chronotimer);

                                runtime = findViewById(R.id.runtime);
                                systemTime = findViewById(R.id.time);
                                systemDate = findViewById(R.id.date);

                                icanchor = findViewById(R.id.icanchor);
                                roundingalone = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.roundingalon);
                                Typeface MMedium = Typeface.createFromAsset(getAssets(), "Fonts/MMedium.ttf");

                                //Weeks
                                mAuth= FirebaseAuth.getInstance();
                                currentUser= mAuth.getCurrentUser();
                                dateTime = dateTimeFormat.format(calendar.getTime());
                                date = dateFormat.format(calendar.getTime());
                                time = timeFormat.format(calendar.getTime());
                                day = dayFormat.format(calendar.getTime());

                                String[] partDate = date.split("-");

                                //Display Time and date
                                systemDate.setText(date);
                                systemTime.setText(time);
                                SimpleDateFormat month_day_Format = new SimpleDateFormat("dd-MMM");
                                month_day = month_day_Format.format(calendar.getTime());

                                UserId= currentUser.getUid();
                                DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Running time").child("Users").child(UserId);

                                btnstart.setTypeface(MMedium);
                                btnstart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        icanchor.startAnimation(roundingalone);
                                        runtime.setText("");
                                        btnstop.animate().alpha(1).setDuration(300).start();
                                        btnstart.animate().alpha(0).setDuration(300).start();
                                        btnprogress.animate().alpha(0).setDuration(300).start();

                                        //start time
                                        timer.setBase(SystemClock.elapsedRealtime());
                                        timer.start();
                                    }
                                });

                                btnstop.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        btnprogress.animate().alpha(1).setDuration(300).start();
                                        //Database Keys
                                        year= partDate[2].trim();
                                        month= partDate[1].trim();
                                        weekCalendar = Calendar.WEEK_OF_MONTH;
                                        week= "Week"+String.valueOf(weekCalendar-1);
                                        runningTime=timer.getText().toString();

                                        //Setting the running time on top
                                        runtime.setText(timer.getText().toString());

                                        // Database
                                        DByear = userDB.child("Year").child(year);
                                        DBmonth = userDB.child("Month").child(month);
                                        DBweek = userDB.child("Week").child(week);

                                        DBday = userDB.child("date").child(month_day).push();
                                        timeId= DBday.getKey();

                                        TimeData timeData = new TimeData(UserId, runningTime, month_day, time, timeId);
                                        DBday.setValue(timeData);

                                        //Log.d("New Week", String.valueOf(newWeek));
                                        //Log.d("Database Week", DBWeek);
                                        //Running time-> year-> Month-> week-> day-> (date _> time)

                                        //Restart timer
                                        timer.setBase(SystemClock.elapsedRealtime());
                                        timer.stop();

                                        icanchor.clearAnimation();
                                        btnstart.animate().alpha(1).setDuration(300).start();
                                        btnstop.animate().alpha(0).setDuration(300).start();
                                    }
                                });

                                btnprogress.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent= new Intent(StopWatch.this, DataReading.class);

                                        intent.putExtra("year", year);
                                        intent.putExtra("month", month);
                                        intent.putExtra("week", week);
                                        intent.putExtra("date", month_day);

                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                }
                catch (Exception e){

                }
            }
        }; thread.start();
    }
}