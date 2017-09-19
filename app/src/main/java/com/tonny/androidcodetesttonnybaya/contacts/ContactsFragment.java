package com.tonny.androidcodetesttonnybaya.contacts;

import android.content.Context;
import android.databinding.BaseObservable;
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
import com.tonny.androidcodetesttonnybaya.base.RecyclerBaseAdapter;
import com.tonny.androidcodetesttonnybaya.data.Contact;
import com.tonny.androidcodetesttonnybaya.data.source.ContactsRepository;
import com.tonny.androidcodetesttonnybaya.databinding.FragmentContactsBinding;
import com.tonny.androidcodetesttonnybaya.databinding.ItemContactBinding;
import com.tonny.androidcodetesttonnybaya.util.Injection;

import java.util.ArrayList;
import java.util.List;

/**
 * Contacts Fragment
 *
 * @author tonnbaya@yahoo.co.uk
 */
public class ContactsFragment extends BaseFragment implements OnClickListener {

    private FragmentContactsBinding mBinding;

    private IContactsListView mHomeActivity;

    private ContactsViewModel mContactsViewModel;

    public ContactsFragment() {
        // Recommended Empty Constructor.
    }

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mContactsViewModel.start();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHomeActivity = (IContactsListView) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentContactsBinding.inflate(inflater,
                container, false);

        mBinding.setView(this);

        mBinding.setViewmodel(mContactsViewModel);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _setAutoCompleteTVAdapter();

        _setupListAdapter();
    }

    @Override
    public String getFragmentTag() {
        return "ContactsFragment";
    }

    @Override
    public void onBackPressed() {
        getActivity().finish();
    }

    @Override
    public void setViewModel(BaseObservable observable) {
        mContactsViewModel = (ContactsViewModel) observable;
    }

    private void _setAutoCompleteTVAdapter() {
        String[] NAMES_SUGGESTION = new String[]{
                "Tonny", "Salva", "Tom", "Ken", "Kenneth"
        };
        // Array Adapter for name Suggestion
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_dropdown_item_1line, NAMES_SUGGESTION);
        mBinding.searchEditText.setAdapter(adapter);
    }

    private void _setupListAdapter() {
        RecyclerView recyclerView = mBinding.recyclerView;

        // Set Recycler LayoutManager
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(manager);

        // Set Recycler Adapter
        ContactsListAdapter listAdapter = new ContactsListAdapter(new ArrayList<Contact>(0),
                Injection.provideContactRepository(getContext().getApplicationContext()));
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void click(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(ContactsActivity.CONTACT_ID_KEY, id);
        mHomeActivity.setFragmentByAction(App.FragmentAction.SHOW, bundle);
    }


    public static class ContactsListAdapter extends RecyclerView.Adapter
            <ContactsListAdapter.ContactViewHolder> {

        private List<Contact> m_contactsList;
        private ContactsRepository mContactsRepository;

        ContactsListAdapter(List<Contact> contactList, ContactsRepository repository) {
            this.mContactsRepository = repository;
            _setList(contactList);
        }

        void replaceData(List<Contact> data) {
            _setList(data);
        }

        @Override
        public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            ItemContactBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.item_contact, parent,
                    false);
            final ContactItemViewModel viewModel = new ContactItemViewModel(
                    parent.getContext().getApplicationContext(),
                    mContactsRepository);
            return new ContactViewHolder(binding, viewModel);
        }

        @Override
        public void onBindViewHolder(ContactViewHolder holder, int position) {
            final Contact contact = m_contactsList.get(position);
            if (contact != null) {
                holder.bind(contact);
            }
        }

        @Override
        public int getItemCount() {
            return m_contactsList.size();
        }

        private void _setList(List<Contact> contacts) {
            this.m_contactsList = contacts;
            notifyDataSetChanged();
        }

        static class ContactViewHolder extends RecyclerView.ViewHolder {
            private final ItemContactBinding mItemContactBinding;
            private final ContactItemViewModel mViewModel;

            ContactViewHolder(ItemContactBinding binding, ContactItemViewModel viewModel) {
                super(binding.getRoot());
                this.mItemContactBinding = binding;
                this.mViewModel = viewModel;
            }

            void bind(final Contact contact) {
                mViewModel.setContact(contact);
                mItemContactBinding.setData(mViewModel);
            }
        }
    }

    public static class ConAdapter extends RecyclerBaseAdapter {

        private final int mLayoutId;
        private List<Contact> m_contactsList;

        public ConAdapter(int layoutId, List<Contact> contactList) {
            this.mLayoutId = layoutId;
            _setList(contactList);
        }

        void replaceData(List<Contact> data) {
            _setList(data);
        }

        @Override
        public int getLayoutIdForPosition(int position) {
            return this.mLayoutId;
        }

        @Override
        public Object getObjectForPosition(int position) {
            return m_contactsList.get(position);
        }

        @Override
        public int getItemCount() {
            return this.m_contactsList.size();
        }

        private void _setList(List<Contact> contacts) {
            this.m_contactsList = contacts;
            notifyDataSetChanged();
        }
    }
}