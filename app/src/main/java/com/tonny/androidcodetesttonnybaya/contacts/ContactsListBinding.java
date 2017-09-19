package com.tonny.androidcodetesttonnybaya.contacts;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.tonny.androidcodetesttonnybaya.data.Contact;

import java.util.List;

/**
 * Contains {@link android.databinding.BindingAdapter}
 * for the {@link com.tonny.androidcodetesttonnybaya.data.Contact} list.
 */
public class ContactsListBinding {

    @BindingAdapter("app:items")
    public static void setItems(RecyclerView recyclerView, List<Contact> items) {
        ContactsFragment.ContactsListAdapter adapter =
                (ContactsFragment.ContactsListAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(items);
        }
    }
}
