package com.tonny.androidcodetesttonnybaya.contacts;

import android.os.Bundle;

/**
 * Abstraction for homeActivity
 */
public interface IContactsListView {

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
