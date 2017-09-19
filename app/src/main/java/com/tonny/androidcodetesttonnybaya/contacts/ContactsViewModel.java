package com.tonny.androidcodetesttonnybaya.contacts;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.tonny.androidcodetesttonnybaya.data.Contact;
import com.tonny.androidcodetesttonnybaya.data.source.ContactsDataSource;
import com.tonny.androidcodetesttonnybaya.data.source.ContactsRepository;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Exposes the data to be used in the contacts list screen.
 * <p>
 * {@link BaseObservable} implements a listener mechanism which is notified when a
 * property changes. This is done by assigning a {@link Bindable} annotation to the property's
 * getter method.
 *
 * @author tonnybaya@yahoo.co.uk
 * @since 1.0.0
 */
public class ContactsViewModel extends BaseObservable {

    // These observable fields will update Views Automatically.
    public final ObservableList<Contact> items = new ObservableArrayList<>();

    private final ContactsRepository mContactsRepository;
    private WeakReference<Context> mContextRef;

    public ContactsViewModel(
            ContactsRepository repository,
            Context context) {
        mContextRef = new WeakReference<>(context);
        mContactsRepository = repository;
    }

    @Bindable
    public boolean isEmpty(){
        return items.isEmpty();
    }

    public void start() {
        loadContacts(false);
    }

    public void loadContacts(boolean forceUpdate) {
        loadContacts(false, true);
    }

    public void loadContacts(boolean forceUpdate, final boolean showLoadingUI) {

        if (showLoadingUI) {

        }

        if (forceUpdate) {
            mContactsRepository.refreshContacts();
        }

        mContactsRepository.getContacts(new ContactsDataSource.OnLoadContactsListener() {
            @Override
            public void onContactsLoaded(List<Contact> contacts) {
                // Dismiss progress bar if visible.

                items.clear();
                items.addAll(contacts);
//                notifyPropertyChanged(BR.empty);
            }

            @Override
            public void onDataNotAvailable() {
                // Show Error message that data is not available.
                // Dismiss progress bar if visible
            }
        });
    }
}
