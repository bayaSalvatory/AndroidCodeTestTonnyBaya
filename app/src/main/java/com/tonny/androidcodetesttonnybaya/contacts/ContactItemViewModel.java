package com.tonny.androidcodetesttonnybaya.contacts;

import android.content.Context;
import android.util.Log;

import com.tonny.androidcodetesttonnybaya.ContactViewModel;
import com.tonny.androidcodetesttonnybaya.data.source.ContactsRepository;

/**
 * Created by tbaya on 9/18/17.
 */
public class ContactItemViewModel extends ContactViewModel {

    private static final String TAG = ContactItemViewModel.class.getSimpleName();

    public ContactItemViewModel(Context context, ContactsRepository repository) {
        super(context, repository);
    }

    public void setContactsView(IContactsListView contactsView) {

    }

    public void contactClicked() {
        Log.d(TAG, "Clicked -> contactClicked() ");
    }
}
