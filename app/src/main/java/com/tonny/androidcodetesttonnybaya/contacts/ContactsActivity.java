package com.tonny.androidcodetesttonnybaya.contacts;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tonny.androidcodetesttonnybaya.App;
import com.tonny.androidcodetesttonnybaya.R;
import com.tonny.androidcodetesttonnybaya.base.BaseActivity;
import com.tonny.androidcodetesttonnybaya.base.BaseFragment;
import com.tonny.androidcodetesttonnybaya.contactdetails.ContactDetailsFragment;
import com.tonny.androidcodetesttonnybaya.contactedit.ContactEditFragment;
import com.tonny.androidcodetesttonnybaya.databinding.ActivityContactsBinding;
import com.tonny.androidcodetesttonnybaya.util.Injection;

/**
 * Application main launcher activity
 *
 * @author tonnbaya@yahoo.co.uk
 */
public class ContactsActivity extends BaseActivity implements IContactsListView, View.OnClickListener {

    private static final String _TAG = ContactsActivity.class.getSimpleName();
    public static final String CONTACT_ID_KEY = "contactId";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityContactsBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_contacts);

        binding.setClickListener(this);
        setFragmentByAction(App.FragmentAction.INDEX);
    }


    @Override
    public void setFragmentByAction(String action) {
        setFragmentByAction(action, null);
    }

    @Override
    public void setFragmentByAction(String action, Bundle bundle) {
        BaseFragment fragment = null;
        switch (action) {
            case App.FragmentAction.INDEX:
                fragment = ContactsFragment.newInstance();
                fragment.setViewModel(new ContactsViewModel(
                        Injection.provideContactRepository(getApplicationContext()),
                        getApplicationContext()));
                break;
            case App.FragmentAction.EDIT:
                fragment = ContactEditFragment.newInstance();
                break;
            case App.FragmentAction.SHOW:
                fragment = ContactDetailsFragment.newInstance();
                break;
        }

        String tag = fragment != null ? fragment.getFragmentTag() : "";
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                setFragmentByAction(App.FragmentAction.EDIT);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            BaseFragment fragment = (BaseFragment) getCurrentFragment();
            if (fragment != null) {
                fragment.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
