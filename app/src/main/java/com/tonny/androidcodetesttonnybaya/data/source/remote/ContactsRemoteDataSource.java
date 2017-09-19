package com.tonny.androidcodetesttonnybaya.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tonny.androidcodetesttonnybaya.data.Contact;
import com.tonny.androidcodetesttonnybaya.data.source.ContactsDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by tbaya on 9/13/17.
 */
public class ContactsRemoteDataSource implements ContactsDataSource {

    private static ContactsRemoteDataSource INSTANCE;

    // Prevent direct instantiation.
    private ContactsRemoteDataSource(@NonNull Context context) {
        checkNotNull(context);
    }

    public static ContactsRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ContactsRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getContacts(@NonNull OnLoadContactsListener callback) {

    }

    @Override
    public void getContact(@NonNull GetContactListener callback) {

    }

    @Override
    public void saveContact(@NonNull Contact contact) {

    }

    @Override
    public void deleteContact(@NonNull String contactId) {

    }

    @Override
    public void deleteAllContacts() {

    }

    @Override
    public void refreshContacts() {

    }
}
