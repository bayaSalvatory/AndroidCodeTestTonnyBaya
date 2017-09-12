package com.tonny.androidcodetesttonnybaya.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * BaseActivity from where all classes should inherit.
 *
 * @author tonnbaya@yahoo.co.uk
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String _TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(_TAG, "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(_TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(_TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(_TAG, "onDestroy");
    }

    protected Fragment getCurrentFragment() {
        String tag = getSupportFragmentManager().getBackStackEntryAt(
                getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }
}
