package com.mendhak.gpslogger;

import android.app.Application;
import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

/**
 * Application class so we can initialize ACRA.
 */
@ReportsCrashes(formKey = "", // will not be used
        mailTo = "tonytung@merly.org")
public class GPSLogger extends Application {
    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        super.onCreate();
        ACRA.init(this);
    }
}
