/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mendhak.gpslogger.chart;

import android.content.Intent;

import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import com.mendhak.gpslogger.GpsMainActivity;
import com.mendhak.gpslogger.R;
import com.mendhak.gpslogger.common.Utilities;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;


public class GraphViewActivity extends SherlockActivity
{

    private GraphicalView mChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // enable the home button so you can go back to the main screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            //requestWindowFeature(com.actionbarsherlock.view.Window.FEATURE_ACTION_BAR_OVERLAY | com.actionbarsherlock.view.Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        }

        setContentView(R.layout.graph);

    }

    /**
     * Called when one of the menu items is selected.
     */
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int itemId = item.getItemId();
        Utilities.LogInfo("Option item selected - " + String.valueOf(item.getTitle()));

        switch (itemId)
        {
            case android.R.id.home:
                Intent intent = new Intent(this, GpsMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            mChartView = (new AverageTemperatureChart()).execute(this);
            layout.addView(mChartView, new ActionBar.LayoutParams
                    (ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT));
        } else {
            mChartView.repaint();
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {

            mChartView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int vis) {

                    if(vis == 0){

                        Thread t = new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                runOnUiThread(showNav);
                                try
                                {
                                    Thread.sleep(4000);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                }

                                runOnUiThread(hideNavAgain);
                            }
                        });

                        t.start();

                    }

                }
            });
        }

    }


    Runnable hideNavAgain = new Runnable()
    {
        @Override
        public void run()
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            getSupportActionBar().hide();
        }
    };

    Runnable showNav = new Runnable()
    {
        @Override
        public void run()
        {

            getSupportActionBar().show();
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    };


}