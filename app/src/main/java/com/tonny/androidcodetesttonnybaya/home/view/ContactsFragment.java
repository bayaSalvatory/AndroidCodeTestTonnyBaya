package com.tonny.androidcodetesttonnybaya.home.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tonny.androidcodetesttonnybaya.R;
import com.tonny.androidcodetesttonnybaya.base.BaseFragment;
import com.tonny.androidcodetesttonnybaya.databinding.FragmentContactsBinding;
import com.tonny.androidcodetesttonnybaya.databinding.ItemContactBinding;
import com.tonny.androidcodetesttonnybaya.home.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Contacts Fragment
 *
 * @author tonnbaya@yahoo.co.uk
 */
public class ContactsFragment extends BaseFragment {

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    public ContactsFragment() {
        // Recommended Empty Constructor.
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentContactsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts,
                container, false);

        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        // Set Recycler LayoutManager
        binding.recyclerView.setLayoutManager(manager);
        // Set Recycler Adapter
        binding.recyclerView.setAdapter(new ContactsListAdapter(getDummyList()));

        return binding.getRoot();
    }

    @Override
    public String getFragmentTag() {
        return "ContactsFragment";
    }

    @Override
    public void onBackPressed() {

    }

    public List<Contact> getDummyList() {
        Contact contact = new Contact("Baya", "Sal");
        Contact contact1 = new Contact("Baya", "Ema");
        Contact contact2 = new Contact("Baya", "Elphas");
        Contact contact3 = new Contact("Baya", "Thobias");
        List<Contact> contactList = new ArrayList<>();
        contactList.add(contact);
        contactList.add(contact1);
        contactList.add(contact2);
        contactList.add(contact3);
        return contactList;
    }

    private static class ContactsListAdapter extends RecyclerView.Adapter
            <ContactsListAdapter.ContactViewHolder> {

        private List<Contact> m_contactsList = new ArrayList<>();
        private ItemContactBinding mBinding;

        ContactsListAdapter(List<Contact> contactList) {
            this.m_contactsList.addAll(contactList);
        }

        @Override
        public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            mBinding = DataBindingUtil.inflate(inflater, R.layout.item_contact, parent,
                    false);
            return new ContactViewHolder(mBinding.getRoot());
        }

        @Override
        public void onBindViewHolder(ContactViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return m_contactsList.size();
        }

        class ContactViewHolder extends RecyclerView.ViewHolder {
            ContactViewHolder(View itemView) {
                super(itemView);
            }

            void bind(int position) {
                Contact contact = m_contactsList.get(position);
                if (contact != null) {
                    String displayName = contact.getFirstName() + " " + contact.getLastName();
                    mBinding.nameTextView.setText(displayName);
                    mBinding.initialsTextView.setText(contact.getInitials());
                }
            }
        }
    }
}