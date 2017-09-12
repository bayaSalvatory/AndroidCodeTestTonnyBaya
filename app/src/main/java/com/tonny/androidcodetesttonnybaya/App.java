package com.tonny.androidcodetesttonnybaya;

import android.app.Application;

/**
 * Application class.
 *
 * @author tonnbaya@yahoo.co.uk
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize all the libs here.
    }

    public class FragmentAction {
        public static final String SHOW = "show";
        public static final String INDEX = "Index";
        public static final String EDIT = "edit";
    }
}
