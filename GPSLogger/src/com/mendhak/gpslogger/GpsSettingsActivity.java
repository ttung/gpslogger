/*
*    This file is part of GPSLogger for Android.
*
*    GPSLogger for Android is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 2 of the License, or
*    (at your option) any later version.
*
*    GPSLogger for Android is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with GPSLogger for Android.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.mendhak.gpslogger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.*;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.mendhak.gpslogger.common.Utilities;
import com.mendhak.gpslogger.senders.osm.OSMHelper;

public class GpsSettingsActivity extends SherlockPreferenceActivity
{

    private final Handler handler = new Handler();
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // enable the home button so you can go back to the main screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addPreferencesFromResource(R.xml.settings);

        if (getIntent().getBooleanExtra("autosend_preferencescreen", false))
        {
            PreferenceScreen screen = (PreferenceScreen) findPreference("gpslogger_preferences");
            int pos = findPreference("autosend_preferencescreen").getOrder();
            screen.onItemClick(null, null, pos, 0);
        }


        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean useImperial = prefs.getBoolean("useImperial", false);

        EditTextPreference accuracyBeforeLogging = (EditTextPreference) findPreference("accuracy_before_logging");

        if (useImperial)
        {
            accuracyBeforeLogging.setDialogTitle(R.string.settings_accuracy_in_feet);
            accuracyBeforeLogging.getEditText().setHint(R.string.settings_enter_feet);
        }
        else
        {
            accuracyBeforeLogging.setDialogTitle(R.string.settings_accuracy_in_meters);
            accuracyBeforeLogging.getEditText().setHint(R.string.settings_enter_meters);
        }


        EditTextPreference accuracyBeforeImmediateLogging = (EditTextPreference) findPreference("accuracy_before_immediate_logging");

        if (useImperial)
        {
            accuracyBeforeImmediateLogging.setDialogTitle(R.string.settings_accuracy_in_feet);
            accuracyBeforeImmediateLogging.getEditText().setHint(R.string.settings_enter_feet);
        }
        else
        {
            accuracyBeforeImmediateLogging.setDialogTitle(R.string.settings_accuracy_in_meters);
            accuracyBeforeImmediateLogging.getEditText().setHint(R.string.settings_enter_meters);
        }




        CheckBoxPreference imperialCheckBox = (CheckBoxPreference) findPreference("useImperial");
        imperialCheckBox.setOnPreferenceChangeListener(new ImperialPreferenceChangeListener(prefs, accuracyBeforeLogging));


        Preference enableDisablePref = findPreference("enableDisableGps");
        enableDisablePref.setOnPreferenceClickListener(new AndroidLocationPreferenceClickListener());

        Preference osmSetupPref = findPreference("osm_setup");
        osmSetupPref.setOnPreferenceClickListener(new OSMPreferenceClickListener());

        CheckBoxPreference chkLog_opengts = (CheckBoxPreference) findPreference("log_opengts");
        chkLog_opengts.setOnPreferenceClickListener(new LogOpenGTSPreferenceClickListener(prefs));


        ListPreference newFilePref = (ListPreference) findPreference("new_file_creation");
        newFilePref.setOnPreferenceChangeListener(new FileCreationPreferenceChangeListener());


        if(!newFilePref.getValue().equals("static"))
        {
            Preference staticPref = (Preference)findPreference("new_file_static_name");
            staticPref.setEnabled(false);
        }


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



    private final Runnable updateResults = new Runnable()
    {
        public void run()
        {
            finish();

            startActivity(getIntent());
        }

    };


    /**
     * Opens the Android Location preferences screen
     */
    private class AndroidLocationPreferenceClickListener implements OnPreferenceClickListener
    {
        public boolean onPreferenceClick(Preference preference)
        {
            startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            return true;
        }
    }

    /**
     * Opens the OpenStreetMap preferences screen
     */
    private class OSMPreferenceClickListener implements OnPreferenceClickListener
    {

        public boolean onPreferenceClick(Preference preference)
        {
            startActivity(OSMHelper.GetOsmSettingsIntent(getApplicationContext()));

            return true;
        }
    }

    private class FileCreationPreferenceChangeListener implements Preference.OnPreferenceChangeListener
    {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            System.out.print(newValue.toString());
            Preference staticPref = (Preference)findPreference("new_file_static_name");
            if(newValue.equals("static"))
            {
                staticPref.setEnabled(true);
            }
            else
            {
                staticPref.setEnabled(false);
            }

            return true;
        }
    }


    private class ImperialPreferenceChangeListener implements Preference.OnPreferenceChangeListener
    {
        EditTextPreference accuracyBeforeLogging;
        SharedPreferences prefs;

        public ImperialPreferenceChangeListener(SharedPreferences prefs, EditTextPreference accuracyBeforeLogging)
        {
            this.prefs = prefs;
            this.accuracyBeforeLogging = accuracyBeforeLogging;
        }

        public boolean onPreferenceChange(Preference preference, final Object newValue)
        {

            Utilities.ShowProgress(GpsSettingsActivity.this, getString(R.string.settings_converting_title),
                    getString(R.string.settings_converting_description));

            new Thread()
            {

                public void run()
                {

                    try
                    {
                        sleep(3000); // Give user time to read the message
                    }
                    catch (InterruptedException e)
                    {

                        Log.e("Settings", e.getMessage());

                    }

                    boolean useImp = Boolean.parseBoolean(newValue.toString());

                    String minimumAccuracyString = prefs.getString("accuracy_before_logging", "0");

                    int minimumAccuracy;

                    if (minimumAccuracyString != null && minimumAccuracyString.length() > 0)
                    {
                        minimumAccuracy = Integer.valueOf(minimumAccuracyString);
                    }
                    else
                    {
                        minimumAccuracy = 0;
                    }

                    SharedPreferences.Editor editor = prefs.edit();

                    if (useImp)
                    {
                        accuracyBeforeLogging.setDialogTitle(R.string.settings_accuracy_in_feet);
                        accuracyBeforeLogging.getEditText().setHint(R.string.settings_enter_feet);

                        minimumAccuracy = Utilities.MetersToFeet(minimumAccuracy);
                    }
                    else
                    {
                        minimumAccuracy = Utilities.FeetToMeters(minimumAccuracy);
                        accuracyBeforeLogging.setDialogTitle(R.string.settings_accuracy_in_meters);
                        accuracyBeforeLogging.getEditText().setHint(R.string.settings_enter_meters);
                    }

                    if (minimumAccuracy >= 9999)
                    {
                        minimumAccuracy = 9999;
                    }

                    editor.putString("accuracy_before_logging", String.valueOf(minimumAccuracy));
                    editor.commit();

                    handler.post(updateResults);
                    Utilities.HideProgress();
                }
            }.start();

            return true;
        }

    }

    /**
     * Opens the OpenGTS preferences
     * Listener to ensure that the server is configured when the user wants to enable OpenGTS logging logger
     */
    private class LogOpenGTSPreferenceClickListener implements OnPreferenceClickListener
    {
        private SharedPreferences prefs;

        public LogOpenGTSPreferenceClickListener(SharedPreferences prefs)
        {
            this.prefs = prefs;
        }

        public boolean onPreferenceClick(Preference preference)
        {
            CheckBoxPreference chkLog_opengts = (CheckBoxPreference) findPreference("log_opengts");
            boolean opengts_enabled = prefs.getBoolean("opengts_enabled", false);

            if (chkLog_opengts.isChecked() && !opengts_enabled)
            {
                startActivity(new Intent("com.mendhak.gpslogger.OPENGTS_SETUP"));
            }
            return true;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        Utilities.LogDebug("GpsSettingsActivity.onWindowFocusChanged");
        if (hasFocus)
        {

            CheckBoxPreference chkLog_opengts = (CheckBoxPreference) findPreference("log_opengts");
            boolean opengts_enabled = prefs.getBoolean("opengts_enabled", false);

            if (chkLog_opengts.isChecked() && !opengts_enabled)
            {
                chkLog_opengts.setChecked(false);
            }

        }
    }


}
