package com.tonny.androidcodetesttonnybaya.contacts;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AlphabetIndexer;
import android.widget.CursorAdapter;
import android.widget.QuickContactBadge;
import android.widget.SearchView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.tonny.androidcodetesttonnybaya.App;
import com.tonny.androidcodetesttonnybaya.R;
import com.tonny.androidcodetesttonnybaya.base.BaseFragment;
import com.tonny.androidcodetesttonnybaya.contactdetails.ContactDetailsFragment;
import com.tonny.androidcodetesttonnybaya.data.Contact;
import com.tonny.androidcodetesttonnybaya.data.source.ContactsRepository;
import com.tonny.androidcodetesttonnybaya.databinding.FragmentContactsBinding;
import com.tonny.androidcodetesttonnybaya.databinding.ItemContactBinding;
import com.tonny.androidcodetesttonnybaya.util.ImageLoader;
import com.tonny.androidcodetesttonnybaya.util.Injection;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Contacts Fragment
 *
 * @author tonnbaya@yahoo.co.uk
 */
public class ContactsFragment extends BaseFragment implements OnClickListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ContactsFragment.class.getSimpleName();

    private static final String STATE_PREVIOUSLY_SELECTED_KEY = "previouslySelectedKey";

    private FragmentContactsBinding mBinding;

    private IContactsListView mHomeActivity;

    private ContactsViewModel mContactsViewModel;

    private String mSearchTerm;

    private ImageLoader mImageLoader;

    private ContactsAdapter mAdapter;

    // Store the previously selected search item so that on a configuration change the same item
    // can be reselected again
    private int mPreviousSelectedSearchItem = 0;

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Cursor cursor = mAdapter.getCursor();

        // Moves to the cursor row corresponding to the ListView item that was clicked
        cursor.moveToPosition(i);

        // Create a contact lookup Uri from contacts ID and lookup_up
        final Uri uri = ContactsContract.Contacts.getLookupUri(
                cursor.getLong(ContactsQuery.ID),
                cursor.getString(ContactsQuery.LOOKUP_KEY));

        Bundle bundle = new Bundle();
        bundle.putParcelable(ContactDetailsFragment.CONTACT_URI_BUNDLE_KEY, uri);
        mHomeActivity.setFragmentByAction(App.FragmentAction.SHOW, bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            // If we're restoring state after this fragment was recreated then
            // retrieve previous search term and previous search
            // result.
            mSearchTerm = savedInstanceState.getString(SearchManager.QUERY);
            mPreviousSelectedSearchItem =
                    savedInstanceState.getInt(STATE_PREVIOUSLY_SELECTED_KEY, 0);
        }

        // Let this fragment contribute menu items
        setHasOptionsMenu(true);

        mAdapter = new ContactsAdapter(getActivity(), null, false);

        mImageLoader = new ImageLoader(getActivity(), getListPreferredItemHeight()) {
            @Override
            protected Bitmap processBitmap(Object data) {
                return loadContactPhotoThumbnail((String) data, getImageSize());
            }
        };

        //  Set placeholder
        mImageLoader.setLoadingImage(R.drawable.ic_contact_picture_holo_light);

        // Add cache to image loader.
        mImageLoader.addImageCache(getActivity().getSupportFragmentManager(), 0.1f);
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

        _setupListAdapter();

        _setListAdapter();
    }

    private void _setListAdapter() {
        mBinding.listView.setAdapter(mAdapter);
        mBinding.listView.setOnItemClickListener(this);
        mBinding.listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause image loader to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    mImageLoader.setPauseWork(true);
                } else {
                    mImageLoader.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        if (mPreviousSelectedSearchItem == 0) {
            // Initialize the loader, and create a loader identified by ContactsQuery.QUERY_ID
            getLoaderManager().initLoader(ContactsQuery.QUERY_ID, null, this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);

        final SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        // Retrieve the searchView from the search menu item
        final SearchView searchView = (SearchView) searchItem.getActionView();

        // Assign searchable info to searchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // Nothing needs to happen when user submits the search string.
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // Called when the action bar search text has changed. Updates
                // the search filter, and restarts the loader to do a new query
                // using the new search string.
                String newFilter = !TextUtils.isEmpty(s) ? s : null;

                // Don't do anything if the filter is empty
                if (mSearchTerm == null && newFilter == null) {
                    return true;
                }

                // Don't do anything if the new filter is the same as the current filter
                if (mSearchTerm != null && mSearchTerm.equals(newFilter)) {
                    return true;
                }

                // Updates current filter to new filter
                mSearchTerm = newFilter;

                // Restarts the loader. This triggers onCreateLoader(), which builds the
                // necessary content Uri from mSearch
                getLoaderManager().restartLoader(
                        ContactsQuery.QUERY_ID, null, ContactsFragment.this);

                return true;
            }
        });

        if (mSearchTerm != null) {
            // If search term is already set here then this fragment is
            // being restored from a saved state and the search menu item
            // needs to be expanded and populated again.

            // Stores the search term (as it will be wiped out by
            // onQueryTextChange() when the menu item is expanded).
            final String savedSearchTerm = mSearchTerm;

            // Sets the SearchView to the previous search string.
            searchView.setQuery(savedSearchTerm, false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(mSearchTerm)) {
            // Saves the current search String
            outState.putString(SearchManager.QUERY, mSearchTerm);

            //Saves the currently selected contact.
            outState.putInt(STATE_PREVIOUSLY_SELECTED_KEY, mBinding.listView.getCheckedItemPosition());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_contact:
                final Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();

        // In the case onPause() is called during a fling the image loader is
        // un-paused to let any remaining background work complete.
        mImageLoader.setPauseWork(false);
    }

    private int getListPreferredItemHeight() {
        final TypedValue typedValue = new TypedValue();

        // Resolve list item preferred height theme attribute into typeValue
        getActivity().getTheme().resolveAttribute(
                android.R.attr.listPreferredItemHeight, typedValue, true);

        // Create a new display metrics object.
        final DisplayMetrics metrics = new DisplayMetrics();

        // Populate the DisplayMetrics
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Return the theme value based on DisplayMetrics.
        return (int) typedValue.getDimension(metrics);
    }

    private Bitmap loadContactPhotoThumbnail(String photoData, int imageSize) {
        if (!isAdded() || getActivity() == null) {
            return null;
        }

        AssetFileDescriptor afd = null;

        try {
            Uri thumbUri = Uri.parse(photoData);
            afd = getActivity().getContentResolver().openAssetFileDescriptor(thumbUri, "r");
            FileDescriptor fileDescriptor = afd.getFileDescriptor();
            if (fileDescriptor != null) {
                // Decodes a Bitmap from the image pointed to by the FileDescriptor, and scales it
                // to the specified width and height
                return ImageLoader.decodeSampledBitmapFromDescriptor(
                        fileDescriptor, imageSize, imageSize);
            }

        } catch (FileNotFoundException e) {
            Log.d(TAG, "Contact photo thumbnail not found for contact " + photoData
                    + ": " + e.toString());
        } finally {
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {
                    //Closing file descriptor might cause IOException if the file is
                    // already closed/ Nothing extra is needed to handle this.
                }
            }
        }

        // If the decoding failed, return null.
        return null;
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // If this is loader for finding contacts from the contacts provider
        // (the only one supported)
        if (id == ContactsQuery.QUERY_ID) {
            Uri contentUri;

            // There are two types of searches, one which displays all contacts and
            // one which filters contacts by search query. If mSearchTerm is set
            // then a search query has been entered and the latter should be used.

            if (mSearchTerm == null) {
                contentUri = ContactsQuery.CONTENT_URI;
            } else {
                // Since there's a search string, use the special URI that searches the
                // contacts table. The URI consists of a base Uri and the search string.
                contentUri = Uri.withAppendedPath(ContactsQuery.FILTER_URI, Uri.encode(mSearchTerm));
            }

            return new CursorLoader(getActivity(),
                    contentUri,
                    ContactsQuery.PROJECTION,
                    ContactsQuery.SELECTION,
                    null,
                    ContactsQuery.SORT_ORDER);
        }
        Log.e(TAG, "onCreateLoader - incorrect ID provided (" + id + ")");
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // This swaps the new cursor into the adapter
        if (loader.getId() == ContactsQuery.QUERY_ID) {
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == ContactsQuery.QUERY_ID) {
            // When the loader is being reset, clear the cursor from the adapter. This allows the
            // cursor resources to be freed.
            mAdapter.swapCursor(null);
        }
    }

    /**
     * This interface defines constants for the Cursor and CursorLoader, based on constants defined
     * in the {@link android.provider.ContactsContract.Contacts} class.
     */
    public interface ContactsQuery {

        // An identifier for the loader
        final static int QUERY_ID = 1;

        // A content URI for the Contacts table
        final static Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

        // The search/filter query Uri
        final static Uri FILTER_URI = ContactsContract.Contacts.CONTENT_FILTER_URI;

        // The selection clause for the CursorLoader query. The search criteria defined here
        // restrict results to contacts that have a display name and are linked to visible groups.
        // Notice that the search on the string provided by the user is implemented by appending
        // the search string to CONTENT_FILTER_URI.
        @SuppressLint("InlinedApi")
        final static String SELECTION =
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY +
                        "<>''" + " AND " + ContactsContract.Contacts.IN_VISIBLE_GROUP + "=1";

        // The desired sort order for the returned Cursor. In Android 3.0 and later, the primary
        // sort key allows for localization. In earlier versions. use the display name as the sort
        // key.
        @SuppressLint("InlinedApi")
        final static String SORT_ORDER =
                ContactsContract.Contacts.SORT_KEY_PRIMARY;

        // The projection for the CursorLoader query. This is a list of columns that the Contacts
        // Provider should return in the Cursor.
        @SuppressLint("InlinedApi")
        final static String[] PROJECTION = {

                // The contact's row id
                ContactsContract.Contacts._ID,

                // A pointer to the contact that is guaranteed to be more permanent than _ID. Given
                // a contact's current _ID value and LOOKUP_KEY, the Contacts Provider can generate
                // a "permanent" contact URI.
                ContactsContract.Contacts.LOOKUP_KEY,

                // In platform version 3.0 and later, the Contacts table contains
                // DISPLAY_NAME_PRIMARY, which either contains the contact's displayable name or
                // some other useful identifier such as an email address. This column isn't
                // available in earlier versions of Android, so you must use Contacts.DISPLAY_NAME
                // instead.
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,

                // In Android 3.0 and later, the thumbnail image is pointed to by
                // PHOTO_THUMBNAIL_URI. In earlier versions, there is no direct pointer; instead,
                // you generate the pointer from the contact's ID value and constants defined in
                // android.provider.ContactsContract.Contacts.
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,

                // The sort order column for the returned Cursor, used by the AlphabetIndexer
                SORT_ORDER,
        };

        // The query column numbers which map to each value in the projection
        final static int ID = 0;
        final static int LOOKUP_KEY = 1;
        final static int DISPLAY_NAME = 2;
        final static int PHOTO_THUMBNAIL_DATA = 3;
        final static int SORT_KEY = 4;
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

    public class ContactsAdapter extends CursorAdapter implements SectionIndexer {

        private LayoutInflater mLayoutInflater;
        private AlphabetIndexer mAlphabetIndexer;
        private TextAppearanceSpan highlightTextSpan;

        public ContactsAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
            mLayoutInflater = LayoutInflater.from(context);

            final String alphabet = context.getString(R.string.alphabet);

            mAlphabetIndexer = new AlphabetIndexer(null, ContactsQuery.SORT_KEY, alphabet);

            highlightTextSpan = new TextAppearanceSpan(context, R.style.searchTextHighlight);

        }

        private int indexOfSearchQuery(String displayName) {
            if (!TextUtils.isEmpty(mSearchTerm)) {
                return displayName.toLowerCase(Locale.getDefault()).indexOf(
                        mSearchTerm.toLowerCase(Locale.getDefault()));
            }
            return -1;
        }

        /**
         * Overides newView() to inflate the list item views.
         */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            // Inflates the list item layout.
            final View itemLayout =
                    mLayoutInflater.inflate(R.layout.contact_list_item, viewGroup, false);

            ViewHolder holder = new ViewHolder();
            holder.text1 = itemLayout.findViewById(R.id.text1);
            holder.text2 = itemLayout.findViewById(R.id.text2);
            holder.icon = itemLayout.findViewById(R.id.icon);

            itemLayout.setTag(holder);
            return itemLayout;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final ViewHolder holder = (ViewHolder) view.getTag();

            final String photoUri = cursor.getString(ContactsQuery.PHOTO_THUMBNAIL_DATA);

            final String displayName = cursor.getString(ContactsQuery.DISPLAY_NAME);

            final int startIndex = indexOfSearchQuery(displayName);

            if (startIndex == -1) {
                holder.text1.setText(displayName);
                if (TextUtils.isEmpty(mSearchTerm)) {
                    holder.text2.setVisibility(View.GONE);
                } else {
                    holder.text2.setVisibility(View.VISIBLE);
                }
            } else {

                // Wraps the display name in the spannableString
                final SpannableString highlightedName = new SpannableString(displayName);

                highlightedName.setSpan(highlightTextSpan, startIndex,
                        startIndex + mSearchTerm.length(), 0);

                holder.text1.setText(highlightedName);

                holder.text2.setVisibility(View.GONE);
            }

            // Generate the contact lookup Uri
            final Uri contactUri = ContactsContract.Contacts.getLookupUri(
                    cursor.getLong(ContactsQuery.ID),
                    cursor.getString(ContactsQuery.LOOKUP_KEY));

            // Binds the contact lookup Uri to QuickContactBadge
            holder.icon.assignContactUri(contactUri);

            mImageLoader.loadImage(photoUri, holder.icon);
        }

        /**
         * Overrides swapCursor to move the new cursor into the AlphabetIndex as well as the
         * CursorAdapter.
         */
        @Override
        public Cursor swapCursor(Cursor newCursor) {
            // Update the AlphabetIndexer with new cursor as well
            mAlphabetIndexer.setCursor(newCursor);
            return super.swapCursor(newCursor);
        }

        @Override
        public int getCount() {
            if (getCursor() == null) {
                return 0;
            }
            return super.getCount();
        }

        @Override
        public Object[] getSections() {
            return mAlphabetIndexer.getSections();
        }

        @Override
        public int getPositionForSection(int i) {
            if (getCursor() == null) {
                return 0;
            }
            return mAlphabetIndexer.getPositionForSection(i);
        }

        @Override
        public int getSectionForPosition(int i) {
            if (getCursor() == null) {
                return 0;
            }
            return mAlphabetIndexer.getSectionForPosition(i);
        }

        private class ViewHolder {
            TextView text1;
            TextView text2;
            QuickContactBadge icon;
        }
    }
}