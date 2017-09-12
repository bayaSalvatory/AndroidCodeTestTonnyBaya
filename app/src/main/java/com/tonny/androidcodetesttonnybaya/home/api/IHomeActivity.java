package com.tonny.androidcodetesttonnybaya.home.api;

import android.os.Bundle;

/**
 * Abstraction for homeActivity
 */
public interface IHomeActivity {

    /**
     * Use this to set the current fragment by action.
     *
     * @param action string action.
     */
    void setFragmentByAction(String action);

    /**
     * Use this to set current fragment by action.
     *
     * @param action string action
     * @param bundle bundle key value pair
     */
    void setFragmentByAction(String action, Bundle bundle);
}
