package com.tonny.androidcodetesttonnybaya.home.view;

import com.tonny.androidcodetesttonnybaya.base.BaseFragment;

/**
 * @author tonnbaya@yahoo.co.uk
 */
public class ContactEditFragment extends BaseFragment {

    public static ContactEditFragment newInstance() {
        return new ContactEditFragment();
    }

    public ContactEditFragment() {
        // Recommended Empty Constructor.
    }

    @Override
    public String getFragmentTag() {
        return "ContactEditFragment";
    }

    @Override
    public void onBackPressed() {

    }
}
