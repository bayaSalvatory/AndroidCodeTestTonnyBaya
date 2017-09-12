package com.tonny.androidcodetesttonnybaya.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tonny.androidcodetesttonnybaya.App;
import com.tonny.androidcodetesttonnybaya.R;
import com.tonny.androidcodetesttonnybaya.base.BaseActivity;
import com.tonny.androidcodetesttonnybaya.base.BaseFragment;
import com.tonny.androidcodetesttonnybaya.home.api.IHomeActivity;
import com.tonny.androidcodetesttonnybaya.home.view.ContactDetailsFragment;
import com.tonny.androidcodetesttonnybaya.home.view.ContactEditFragment;
import com.tonny.androidcodetesttonnybaya.home.view.ContactsFragment;

/**
 * Application main launcher activity
 *
 * @author tonnbaya@yahoo.co.uk
 */
public class HomeActivity extends BaseActivity implements IHomeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this,
                R.layout.activity_home);

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
                .replace(R.id.content_frame, fragment)
                .addToBackStack(tag)
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
