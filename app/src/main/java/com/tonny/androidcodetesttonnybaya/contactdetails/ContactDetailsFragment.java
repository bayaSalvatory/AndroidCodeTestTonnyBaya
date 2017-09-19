package com.tonny.androidcodetesttonnybaya.contactdetails;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tonny.androidcodetesttonnybaya.R;
import com.tonny.androidcodetesttonnybaya.base.BaseFragment;
import com.tonny.androidcodetesttonnybaya.databinding.FragmentContactDetailBinding;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentContactDetailBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_contact_detail,
                container, false);
        return binding.getRoot();
    }

    @Override
    public String getFragmentTag() {
        return "ContactDetailsFragment";
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void setViewModel(BaseObservable observable) {

    }
}
