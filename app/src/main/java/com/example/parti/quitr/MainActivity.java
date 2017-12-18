package com.example.parti.quitr;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements MainFragment.OnFragmentInteractionListener, TimelineFragment.OnFragmentInteractionListener  {

    String param1 = "param1";
    String param2 = "param2";

    // hold a reference to the fragments so they don't get reloaded every time
    private HashMap<String, Fragment> fragmentHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item)
                    {
                        Fragment selectedFragment = null;
                        String tag = null;
                        switch (item.getItemId())
                        {
                            case R.id.main_fragment:
                                if(fragmentHashMap.get("R.id.main_fragment") == null)
                                {
                                    selectedFragment = MainFragment.newInstance(param1, param2);
                                    tag = "R.id.main_fragment";
                                }
                                else
                                {
                                    selectedFragment = fragmentHashMap.get("R.id.main_fragment");
                                }

                                break;
                            case R.id.timeline_fragment:
                                selectedFragment = TimelineFragment.newInstance(param1, param2);
                                tag = "timeline";
                                break;
                        }
                        if(selectedFragment != null)
                        {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout, selectedFragment, tag);
                            transaction.commit();

                        }
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        Fragment f = MainFragment.newInstance(param1, param2);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, f, "R.id.main_fragment");
        fragmentHashMap.put("R.id.main_fragment", f);
        transaction.commit();


        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);


    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

}
