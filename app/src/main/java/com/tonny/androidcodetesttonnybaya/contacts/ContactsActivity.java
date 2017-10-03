package com.tonny.androidcodetesttonnybaya.contacts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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
public class ContactsActivity extends BaseActivity implements IContactsListView {

    public static final String CONTACT_ID_KEY = "contactId";
    private static final String _TAG = ContactsActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ActivityContactsBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_contacts);

        // Request permission before attaching this first
        // fragment containing contacts list
        _requestSelfPermission();
    }

    private void _requestSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                Log.d(_TAG, "_requestSelfPermission() => Needs Explanation ");

            } else {
                // No Explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                Log.d(_TAG, "_requestSelfPermission() => No Explanation Needed ");
            }
        } else {
            setFragmentByAction(App.FragmentAction.INDEX);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, yay! Do the
                    // Contacts - related task you need to do.
                    Log.d(_TAG, "onRequestPermissionsResult() => Granted.");
                    setFragmentByAction(App.FragmentAction.INDEX);
                } else {
                    // Permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(_TAG, "onRequestPermissionsResult() => Denied.");
                }
            }
            break;
        }
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

        // Check if fragment is not null before we continue using it.
        if(fragment == null){
            return;
        }

        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment, fragment.getFragmentTag())
                .addToBackStack(fragment.getFragmentTag())
                .commit();
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
