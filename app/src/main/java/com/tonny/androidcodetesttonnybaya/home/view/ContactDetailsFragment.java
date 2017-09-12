package com.tonny.androidcodetesttonnybaya.home.view;

import com.tonny.androidcodetesttonnybaya.base.BaseFragment;

/**
 * @author tonnbaya@yahoo.co.uk
 */

public class ContactDetailsFragment extends BaseFragment {

    public static ContactDetailsFragment newInstance() {
        return new ContactDetailsFragment();
    }

    public ContactDetailsFragment() {
        // Recommended Empty Constructor.
    }

    @Override
    public String getFragmentTag() {
        return "ContactDetailsFragment";
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }
}
