package com.tonny.androidcodetesttonnybaya.data.source;

import android.support.annotation.NonNull;

import com.tonny.androidcodetesttonnybaya.data.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation to load contacts from data source into a cache.
 * <p>
 * For Simplicity, this implements a dumb synchronization between locally persisted data and data
 * obtained from the server, by using the remote data source if the local database doesn't
 * exist or is empty
 * </p>
 */
public class ContactsRepository implements ContactsDataSource {

    private static ContactsRepository _INSTANCE = null;

    private final ContactsDataSource m_remoteDataSource;

    private final ContactsDataSource m_localDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Contact> mCachedContacts;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be assessed from test.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private ContactsRepository(@NonNull ContactsDataSource contactsRemoteDataSource,
                               @NonNull ContactsDataSource contactsLocalDataSource) {
        this.m_remoteDataSource = contactsRemoteDataSource;
        this.m_localDataSource = contactsLocalDataSource;
    }

    public static ContactsRepository getInstance(ContactsDataSource contactsRemoteDataSource,
                                                 ContactsDataSource contactsLocalDataSource) {
        if (_INSTANCE == null) {
            _INSTANCE = new ContactsRepository(contactsRemoteDataSource, contactsLocalDataSource);
        }
        return _INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(ContactsDataSource, ContactsDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        _INSTANCE = null;
    }

    @Override
    public void getContacts(@NonNull OnLoadContactsListener callback) {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1, "Baya", "s"));
        contacts.add(new Contact(1, "Baya", "s"));
        contacts.add(new Contact(1, "Baya", "s"));
        contacts.add(new Contact(1, "Baya", "s"));
        contacts.add(new Contact(1, "Baya", "s"));
        contacts.add(new Contact(1, "Ochieng", "t"));

        // Notify the callback.
        callback.onContactsLoaded(contacts);
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
