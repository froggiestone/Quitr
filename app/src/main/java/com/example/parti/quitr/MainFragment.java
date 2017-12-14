package com.example.parti.quitr;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.List;

import Utillities.MilestoneData;
import at.grabner.circleprogress.CircleProgressView;
import datamodels.MilestoneDataModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    double cigsPerDay;
    double yearsSmoked;
    double pricePerPack;
    double numberPerPack;
    float circleValue;

    Boolean isSpinning = false;
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

    List<MilestoneDataModel> milestoneList = new MilestoneData().Build(quitTime);

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // load icon packs
        Iconify
                .with(new FontAwesomeModule())
                .with(new MaterialCommunityModule());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // find widgets
        clock = view.findViewById(R.id.clock);
        circleView = view.findViewById(R.id.circleView);
        text_money_saved = view.findViewById(R.id.text_money_saved);
        text_milestone_time = view.findViewById(R.id.milestone_time);
        text_milestone_quote = view.findViewById(R.id.milestone_quote);
        //  text_live_saved = findViewById(R.id.text_live_saved);
        text_cigs_not_smoked = view.findViewById(R.id.text_cigs_not_smoked);

        setupSharedPreferences(false);
        nextMilestone();
        circleView.setValueAnimated(circleValue);
        //startClock();

        final Button button_timeplus = view.findViewById(R.id.timeplus);
        button_timeplus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                quitTime =  quitTime.minusHours(5);

            }
        });

        final Button button_timeminus = view.findViewById(R.id.timeminus);
        button_timeminus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                quitTime =  quitTime.plusHours(5);

            }
        });

        final Button button_reset_milestones = view.findViewById(R.id.resetmilestones);
        button_reset_milestones.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                milestoneList.clear();
                milestoneList = new MilestoneData().Build(quitTime);
                nextMilestone();

            }
        });

        final Button button_spin_the_wheel = view.findViewById(R.id.spin_me_around);
        button_spin_the_wheel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                circleView.spin();
                isSpinning = true;


            }
        });

        final Button button_one_min_minus = view.findViewById(R.id.one_min_minus);
        button_one_min_minus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                quitTime.plusMinutes(1);


            }
        });

        final Button button_one_min_plus = view.findViewById(R.id.one_min_plus);
        button_one_min_plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                quitTime.minusMinutes(1);


            }
        });

        final Button button_reset_time = view.findViewById(R.id.reset_time);
        button_reset_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setupSharedPreferences(true);


            }
        });


        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main, container, false);
        return view;

    }

    private void startClock() {
        Thread t = new Thread() {
            // final TextView clock = (TextView) findViewById(R.id.clock);
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {

                        getActivity().runOnUiThread(new Runnable() {
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
                        Thread.sleep(1000);
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

    private void setupSharedPreferences(boolean forceReset){

        SharedPreferences prefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        //editor.clear().commit();

        // on the first app boot, create the settings
        if (!prefs.getBoolean("first_app_boot", false) || forceReset) {

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

    private void checkMilestones()
    {
        DateTime now = new DateTime();
        Duration duration = new Duration(quitTime, now);

        long durationInHours = duration.getStandardHours();

        for(MilestoneDataModel item: milestoneList)
        {
            if(!item.completed)
            {
                if(item.totalhours <= durationInHours)
                {
                    item.completed = true;
                    circleView.spin();
                    Log.d("quitrDebug",item.goal);
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
                if(item.totalhours >= hours)
                {
                    text_milestone_quote.setText(item.quote);
                    text_milestone_time.setText(item.goal);

                    double circleviewdouble =  100 - ((item.totalhours - (minutes / 60)) / item.period * 100);// * item.hours;
                    // cast to long so it rounds down
                    long progress = (long)circleviewdouble;

                    // animate or not
                    if(circleValue == 0.0 || isSpinning)
                    {
                        circleView.setValueAnimated(progress);
                    }
                    else
                    {
                        circleView.setValue(progress);
                    }

                    circleValue = progress;
                    checkMilestones();
                    break;
                }

            }

        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
