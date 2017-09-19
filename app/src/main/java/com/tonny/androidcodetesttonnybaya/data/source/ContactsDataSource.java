package com.tonny.androidcodetesttonnybaya.data.source;

import android.support.annotation.NonNull;

import com.tonny.androidcodetesttonnybaya.data.Contact;

import java.util.List;

/**
 * The main Entry point of accessing contacts data.
 */
public interface ContactsDataSource {

    interface OnLoadContactsListener {

        void onContactsLoaded(List<Contact> contacts);

        void onDataNotAvailable();
    }

    interface GetContactListener {

        void onContactLoaded(Contact contact);

        void onDataNotAvailable();
    }

    void getContacts(@NonNull OnLoadContactsListener callback);

    void getContact(@NonNull GetContactListener callback);

    void saveContact(@NonNull Contact contact);

    void deleteContact(@NonNull String contactId);

    void deleteAllContacts();

    void refreshContacts();
}
