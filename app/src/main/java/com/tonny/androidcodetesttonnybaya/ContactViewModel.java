package com.tonny.androidcodetesttonnybaya;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Observable;
import android.databinding.ObservableField;

import com.tonny.androidcodetesttonnybaya.data.Contact;
import com.tonny.androidcodetesttonnybaya.data.source.ContactsRepository;

import java.lang.ref.WeakReference;

/**
 * Abstraction class for View Models that exposes a single {@link Contact}
 */
public class ContactViewModel extends BaseObservable {

    public final ObservableField<Contact> mContactObservable = new ObservableField<>();

    public final ObservableField<String> firstName = new ObservableField<>();

    public final ObservableField<String> lastName = new ObservableField<>();

    public final ObservableField<String> initials = new ObservableField<>();

    public final ObservableField<String> name = new ObservableField<>();

    private final ContactsRepository mContactRepository;

    private WeakReference<Context> mContextRef;

    public ContactViewModel(Context context, ContactsRepository repository) {
        this.mContextRef = new WeakReference<>(context.getApplicationContext()); // Force to use application context.
        this.mContactRepository = repository;

        this.mContactObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Contact contact = mContactObservable.get();
                if (contact != null) {
                    ContactViewModel.this.firstName.set(contact.getFirstName());
                    ContactViewModel.this.lastName.set(contact.getLastName());
                    ContactViewModel.this.initials.set(contact.getInitials());
                    String name = contact.getFirstName() + " " + contact.getLastName();
                    ContactViewModel.this.name.set(name);
                }
            }
        });
    }

    public void setContact(Contact contact) {
        mContactObservable.set(contact);
    }
}
