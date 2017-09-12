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
import android.widget.ArrayAdapter;

import com.tonny.androidcodetesttonnybaya.App;
import com.tonny.androidcodetesttonnybaya.R;
import com.tonny.androidcodetesttonnybaya.base.BaseFragment;
import com.tonny.androidcodetesttonnybaya.databinding.FragmentContactsBinding;
import com.tonny.androidcodetesttonnybaya.databinding.ItemContactBinding;
import com.tonny.androidcodetesttonnybaya.home.HomeActivity;
import com.tonny.androidcodetesttonnybaya.home.api.IHomeActivity;
import com.tonny.androidcodetesttonnybaya.home.api.OnClickListener;
import com.tonny.androidcodetesttonnybaya.home.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Contacts Fragment
 *
 * @author tonnbaya@yahoo.co.uk
 */
public class ContactsFragment extends BaseFragment implements OnClickListener {

    private IHomeActivity m_homeActivity;
    private static final String[] NAMES_SUGGESTION = new String[]{
            "Tonny", "Salva", "Tom", "Ken", "Kenneth"
    };

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    public ContactsFragment() {
        // Recommended Empty Constructor.
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        m_homeActivity = (IHomeActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentContactsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts,
                container, false);

        // Array Adapter for name Suggestion
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_dropdown_item_1line, NAMES_SUGGESTION);
        binding.searchEditText.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        // Set Recycler LayoutManager
        binding.recyclerView.setLayoutManager(manager);
        // Set Recycler Adapter
        binding.recyclerView.setAdapter(new ContactsListAdapter(this, getDummyList()));

        return binding.getRoot();
    }

    @Override
    public String getFragmentTag() {
        return "ContactsFragment";
    }

    @Override
    public void onBackPressed() {

    }

    /**
     * Todo We will have the know contact list and therefore just inject it
     * Todo this is only for testing purpose.
     *
     * @return
     */
    public List<Contact> getDummyList() {
        Contact contact = new Contact(0, "Baya", "Sal");
        Contact contact1 = new Contact(1, "Baya", "Ema");
        Contact contact2 = new Contact(2, "Baya", "Elphas");
        Contact contact3 = new Contact(3, "Baya", "Thobias");
        List<Contact> contactList = new ArrayList<>();
        contactList.add(contact);
        contactList.add(contact1);
        contactList.add(contact2);
        contactList.add(contact3);
        return contactList;
    }

    @Override
    public void click(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(HomeActivity.CONTACT_ID_KEY, id);
        m_homeActivity.setFragmentByAction(App.FragmentAction.SHOW, bundle);
    }

    private static class ContactsListAdapter extends RecyclerView.Adapter
            <ContactsListAdapter.ContactViewHolder> {

        private OnClickListener mClickListener;
        private List<Contact> m_contactsList = new ArrayList<>();
        private ItemContactBinding mBinding;

        ContactsListAdapter(OnClickListener listener, List<Contact> contactList) {
            this.mClickListener = listener;
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
                final Contact contact = m_contactsList.get(position);
                if (contact != null) {
                    String displayName = contact.getFirstName() + " " + contact.getLastName();
                    mBinding.nameTextView.setText(displayName);
                    mBinding.initialsTextView.setText(contact.getInitials());
                    mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mClickListener.click(contact.getId());
                        }
                    });
                }
            }
        }
    }
}