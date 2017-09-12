package com.tonny.androidcodetesttonnybaya.base;

import android.support.v4.app.Fragment;

/**
 * BaseActivity from where all classes should inherit.
 *
 * @author tonnbaya@yahoo.co.uk
 */

public abstract class BaseFragment extends Fragment {

    public abstract String getFragmentTag();

    public abstract void onBackPressed();
}
