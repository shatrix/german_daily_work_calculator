package com.dailyworktime.smousa.shatrixdailyworktime;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class DailyTimeActivity extends AppCompatActivity {

    TextView textView, textViewReal, textViewBreak ;
    Button start, startAt, reset, lap ;
    long MillisecondTime, currentSavedStartTime, StartTime, UpdateTime = 0L ;
    Handler handler;
    int Seconds, SecondsReal, Minutes, Hours, MilliSeconds ;
    ListView listView ;
    String[] ListElements = new String[] {  };
    List<String> ListElementsArrayList ;
    ArrayAdapter<String> adapter ;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TimePickerDialog mTimePicker;
    long elapsedMills;
    Calendar calander;
    SimpleDateFormat format;
    Date date1, date2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_time);
        textView = (TextView)findViewById(R.id.textView);
        textViewReal = (TextView)findViewById(R.id.textViewReal);
        textViewBreak = (TextView)findViewById(R.id.textViewBreak);
        start = (Button)findViewById(R.id.button);
        startAt = (Button)findViewById(R.id.buttonat);
        reset = (Button)findViewById(R.id.button3);
        lap = (Button)findViewById(R.id.button4) ;
        listView = (ListView)findViewById(R.id.listview1);
        AlertDialog.Builder builder;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        format = new SimpleDateFormat("HH:mm");
        date1 = null;
        date2 = null;

        handler = new Handler() ;
        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));
        adapter = new ArrayAdapter<String>(DailyTimeActivity.this,
                android.R.layout.simple_list_item_1,
                ListElementsArrayList
        );

        listView.setAdapter(adapter);

        currentSavedStartTime = preferences.getLong("CurrentStartTime", 0L);
        if(currentSavedStartTime > 0){
            StartTime = currentSavedStartTime;
            handler.postDelayed(runnable, 0);
            start.setEnabled(false);
            startAt.setEnabled(false);

//            Date timesaved = new Date(currentSavedStartTime);
//            SimpleDateFormat dateformatnew = new SimpleDateFormat("HH:mm:ss");
//            System.out.println(dateformatnew.format(timesaved));
//            Toast.makeText(DailyTimeActivity.this, dateformatnew.format(timesaved),
//                    Toast.LENGTH_LONG).show();
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StartTime = SystemClock.uptimeMillis();

                editor = preferences.edit();
                editor.putLong("CurrentStartTime", StartTime);
                editor.apply();

                handler.postDelayed(runnable, 0);
                start.setEnabled(false);
                startAt.setEnabled(false);
            }
        });

        startAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mTimePicker = new TimePickerDialog(DailyTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        calander = Calendar.getInstance();
                        try {
                            date1 = format.parse(format.format(calander.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            date2 = format.parse(selectedHour+":"+selectedMinute);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        elapsedMills = date1.getTime() - date2.getTime();

                        if(elapsedMills >= 0) {
                            StartTime = SystemClock.uptimeMillis() - elapsedMills;

                            editor = preferences.edit();
                            editor.putLong("CurrentStartTime", StartTime);
                            editor.apply();

                            handler.postDelayed(runnable, 0);
                            start.setEnabled(false);
                            startAt.setEnabled(false);
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(DailyTimeActivity.this);
                            builder.setTitle("Error")
                                    .setMessage("Selected time is incorrect!\nPlease, set a correct time.")
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setPositiveButton("Confirmed", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }
                    }
                }, 7, 0, true);//Yes 24 hour time
                mTimePicker.setTitle("When did you start working today?");
                mTimePicker.show();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DailyTimeActivity.this);
                builder.setTitle("Reset")
                       .setMessage("Do you really want to reset Today's Timer?")
                       .setIcon(android.R.drawable.ic_dialog_alert)
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                handler.removeCallbacks(runnable);

                                MillisecondTime = 0L ;
                                StartTime = 0L ;
                                UpdateTime = 0L ;
                                Seconds = 0 ;
                                Minutes = 0 ;
                                Hours = 0 ;
                                MilliSeconds = 0 ;
                                textView.setText("00:00:00");
                                textViewReal.setText("00:00:00");
                                textViewBreak.setText("Break: 00 Minutes");
                                ListElementsArrayList.clear();
                                adapter.notifyDataSetChanged();

                                editor = preferences.edit();
                                editor.putLong("CurrentStartTime", 0L);
                                editor.apply();
                                start.setEnabled(true);
                                startAt.setEnabled(true);
                            }
                        })
                       .setNegativeButton("No", null)						//Do nothing on no
                       .show();
            }
        });

        lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(DailyTimeActivity.this, "Will be implemented later!",
                        Toast.LENGTH_LONG).show();
                //ListElementsArrayList.add(textViewReal.getText().toString());
                //adapter.notifyDataSetChanged();

            }
        });
    }

    public Runnable runnable = new Runnable() {

        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            SecondsReal = Seconds;
            Minutes = Seconds / 60;
            Hours = Minutes / 60;
            Minutes = Minutes % 60;
            Seconds = Seconds % 60;

            setTextTime(textView, Hours, Minutes, Seconds);


            Minutes = SecondsReal / 60;
            if (Minutes >= 360 && Minutes < 540){
                Minutes = Minutes - 30;
                textViewBreak.setText("Break: 30 Minutes");
            }
            else if (Minutes >= 540){
                Minutes = Minutes - 45;
                textViewBreak.setText("Break: 45 Minutes");
            }

            Hours = Minutes / 60;
            Minutes = Minutes % 60;
            SecondsReal = SecondsReal % 60;

            setTextTime(textViewReal, Hours, Minutes, SecondsReal);

            handler.postDelayed(this, 0);
        }

        public void setTextTime(TextView textToSet, int H, int M, int S){
            if (M < 10){
                textToSet.setText("" + H + ":0" + M + ":"
                        + String.format("%02d", S));
            }
            else{
                textToSet.setText("" + H + ":" + M + ":"
                        + String.format("%02d", S));
            }
        }

    };
}
