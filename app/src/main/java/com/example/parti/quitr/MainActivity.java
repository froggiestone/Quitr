package com.example.parti.quitr;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;
import datamodels.MilestoneDataModel;

public class MainActivity extends AppCompatActivity {

    double cigsPerDay;
    double yearsSmoked;
    double pricePerPack;
    double numberPerPack;
    private float circleValue;

    String currency;

    double cigsNotSmoked;
    double moneySaved;
    double liveSaved;


    private int calcStatsCounter = 0;

    private DateTime quitTime;
    private TextView text_live_saved;
    private TextView text_money_saved;
    private TextView text_cigs_not_smoked;
    private TextView clock;
    private TextView text_milestone_time;
    private TextView text_milestone_quote;
    private CircleProgressView circleView;

    List<MilestoneDataModel> milestoneList = new ArrayList<MilestoneDataModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load icon packs
        Iconify
                .with(new FontAwesomeModule())
                .with(new MaterialCommunityModule());

        setContentView(R.layout.activity_main);

        // find widgets
        clock = findViewById(R.id.clock);
        circleView = findViewById(R.id.circleView);
        text_money_saved = findViewById(R.id.text_money_saved);
        text_milestone_time = findViewById(R.id.milestone_time);
        text_milestone_quote = findViewById(R.id.milestone_quote);
      //  text_live_saved = findViewById(R.id.text_live_saved);
        text_cigs_not_smoked = findViewById(R.id.text_cigs_not_smoked);

        setupSharedPreferences();
        setupMilestones();
        nextMilestone();
        circleView.setValueAnimated(circleValue);
        startClock();
    }

    private void startClock() {
        Thread t = new Thread() {
           // final TextView clock = (TextView) findViewById(R.id.clock);
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                DateTime now = new DateTime();
                                Period period = new Period(quitTime, now);

                                PeriodFormatter formatter = new PeriodFormatterBuilder()
                                        .appendYears().appendSuffix(" year, ", " years, ")
                                        .appendMonths().appendSuffix(" month, ", " months, ")
                                        .appendWeeks().appendSuffix(" week, ", " weeks, ")
                                        .appendDays().appendSuffix("d ", "d ")
                                        .appendHours().appendSuffix("h ", "h ")
                                        .appendMinutes().appendSuffix("m ", "m ")
                                        .appendSeconds().appendSuffix("s ", "s ")
                                        .printZeroAlways()
                                        .toFormatter();

                                //DateTime myBirthDate = new DateTime(1978, 3, 26, 12, 35, 0, 0);
                             //   System.out.println(elapsed + " ago");
                             //   circleView.setValue(42); // used to set the circlebar value
                                clock.setText(formatter.print(period));


                                if(calcStatsCounter == 5)
                                {
                                    calcStatsCounter = 0;
                                    calcStats();
                                }

                                calcStatsCounter++;
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Log.d("quitrDebug","exception in clock tick");
                }
            }
        };

        t.start();
    }

    private void calcStats() {
    // while the timer runs every second, there is no need to calc the stats and milestones that often too
    // so this function runs every x tick the timer makes (gotta save that battery)

        DateTime now = new DateTime();
        Duration duration = new Duration(quitTime, now);

        long durationInMinutes = duration.getStandardMinutes();

        DecimalFormat df = new DecimalFormat("#");
        df.setDecimalSeparatorAlwaysShown(false);

        cigsNotSmoked = ((cigsPerDay / 23) / 60 * durationInMinutes);
        text_cigs_not_smoked.setText(df.format(cigsNotSmoked));

        moneySaved = Math.floor((pricePerPack / numberPerPack) * cigsNotSmoked);
        df = new DecimalFormat("#.##");
        df.setDecimalSeparatorAlwaysShown(false);
        text_money_saved.setText(df.format(moneySaved) + " " + currency);

        liveSaved ++;
        nextMilestone();
    }

    private void setupSharedPreferences(){

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = prefs.edit();

       //editor.clear().commit();

        // on the first app boot, create the settings
        if (!prefs.getBoolean("first_app_boot", false)) {

            quitTime = new DateTime(DateTime.now());
            editor.putString("quitTime", quitTime.toString());

            editor.putBoolean("first_app_boot", true);
            editor.putLong("cigsPerDay", Double.doubleToRawLongBits(30.5));
            editor.putLong("pricePerPack", Double.doubleToRawLongBits(42));
            editor.putLong("numberPerPack", Double.doubleToRawLongBits(20));
            editor.putLong("yearsSmoked", Double.doubleToRawLongBits(14.5));
            editor.putString("currency", "Kr");

            cigsPerDay = Double.longBitsToDouble(prefs.getLong("cigsPerDay", 0));
            pricePerPack = Double.longBitsToDouble(prefs.getLong("pricePerPack", 0));
            yearsSmoked = Double.longBitsToDouble(prefs.getLong("yearsSmoked", 0));
            numberPerPack = Double.longBitsToDouble(prefs.getLong("numberPerPack", 0));
            currency = prefs.getString("currency", null);

            calcStats();

            editor.apply();

            Log.d("quitrDebug","preferences written. quit time: " + quitTime);
        }
        else
        {
            if(prefs.getString("quitTime", null) != null)
            {
                quitTime = new DateTime(prefs.getString("quitTime", null));
                Log.d("quitrDebug", "quit date loaded: " + quitTime);

                cigsPerDay = Double.longBitsToDouble(prefs.getLong("cigsPerDay", 0));
                pricePerPack = Double.longBitsToDouble(prefs.getLong("pricePerPack", 0));
                yearsSmoked =  Double.longBitsToDouble(prefs.getLong("yearsSmoked", 0));
                numberPerPack = Double.longBitsToDouble(prefs.getLong("numberPerPack", 0));
                currency = prefs.getString("currency", null);

                calcStats();

                //  editor.clear().commit();
                //  Log.d("debug","preferences cleared");
            }
            else
            {
                Log.d("quitrDebug","stored quit time returned null when it really should'nt");
            }
        }

    }

    private void setupMilestones()
    {
        MilestoneDataModel model = new MilestoneDataModel();
        model.hours = 2;
        model.description = "2 hours.";
        model.timeunit = "hours";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(2);

        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.hours = 4;
        model.description = "4 hours.";
        model.timeunit = "hours";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(4);

        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.hours = 8;
        model.description = "8 hours.";
        model.timeunit = "hours";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(8);

        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.hours = 12;
        model.description = "Last the first 12 hours.";
        model.timeunit = "hours";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(12);

        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.hours = 24;
        model.description = "day 1";
        model.timeunit = "day";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(24);

        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.hours = 48;
        model.description = "Day 2";
        model.timeunit = "days";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(48);

        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.hours = 72;
        model.description = "Day 3";
        model.timeunit = "days";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(72);

        milestoneList.add(model);
    }

    private void checkMilestones()
    {
        DateTime now = new DateTime();
        Duration duration = new Duration(quitTime, now);

        long durationInHours = duration.getStandardHours();

        for(MilestoneDataModel item: milestoneList)
        {
           if(!item.completed)
           {
                if(item.hours <= durationInHours)
                {
                    item.completed = true;
                    Log.d("quitrDebug",item.description);
                    return;
                  //  nextMilestone();
                }
           }
        }
    }

    private void nextMilestone()
    {
        DateTime now = new DateTime();
        Duration duration = new Duration(quitTime, now);

        long hours = duration.getStandardHours();
        double minutes = duration.getStandardMinutes();

        for(MilestoneDataModel item: milestoneList)
        {
            if(!item.completed)
            {
               if(item.hours >= hours)
               {
                    text_milestone_quote.setText(item.quote);
                    text_milestone_time.setText(item.description);

                    double circleviewdouble = (minutes / 60.0) / item.hours * 100;// * item.hours;
                   // cast to long so it rounds down
                    long progress = (long)circleviewdouble;

                    if(circleValue == 0.0)
                    {
                        circleView.setValueAnimated(progress);
                    }
                    else
                    {
                        circleView.setValue(progress);
                    }

                    circleValue = progress;
                    checkMilestones();
               }

            }

        }

    }

    private Boolean isMilestoneCompleted(long hours)
    {
        DateTime now = new DateTime();
        Duration duration = new Duration(quitTime, now);

        long durationHours = duration.getStandardHours();

        if(hours <= durationHours)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
}
