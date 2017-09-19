package com.tonny.androidcodetesttonnybaya.contactedit;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tonny.androidcodetesttonnybaya.R;
import com.tonny.androidcodetesttonnybaya.base.BaseFragment;
import com.tonny.androidcodetesttonnybaya.databinding.FragmentContactEditBinding;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentContactEditBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_contact_edit,
                container, false);


        return binding.getRoot();
    }

    @Override
    public String getFragmentTag() {
        return "ContactEditFragment";
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void setViewModel(BaseObservable observable) {

    }
}
