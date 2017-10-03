package com.tonny.androidcodetesttonnybaya.contactdetails;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.BuildConfig;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tonny.androidcodetesttonnybaya.R;
import com.tonny.androidcodetesttonnybaya.base.BaseFragment;
import com.tonny.androidcodetesttonnybaya.databinding.ContactDetailItemBinding;
import com.tonny.androidcodetesttonnybaya.databinding.FragmentContactDetailBinding;
import com.tonny.androidcodetesttonnybaya.util.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author tonnbaya@yahoo.co.uk
 */

public class ContactDetailsFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // The geo Uri scheme prefix, used with Intent.ACTION_VIEW to form a geographical address
    // intent that will trigger available apps to handle viewing a location (such as Maps)
    private static final String GEO_URI_SCHEME_PREFIX = "geo:0,0?q=";

    public static final String CONTACT_URI_BUNDLE_KEY = "ContactsUriBundleKey";
    private static final String TAG = ContactDetailsFragment.class.getSimpleName();
    private Uri mContactUri;
    private ImageLoader mImageLoader;
    private FragmentContactDetailBinding mBinding;
    private MenuItem mEditContactMenuItem;

    public ContactDetailsFragment() {
        // Recommended Empty Constructor.
    }

    public static ContactDetailsFragment newInstance() {
        return new ContactDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lets this fragment contribute menu items
        setHasOptionsMenu(true);

        mImageLoader = new ImageLoader(getActivity(), getLargestScreenDimension()) {
            @Override
            protected Bitmap processBitmap(Object data) {
                return loadContactPhoto((Uri) data, getImageSize());
            }
        };

        mImageLoader.setLoadingImage(R.drawable.ic_contact_picture_holo_light);

        mImageLoader.setImageFadeIn(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_contact_detail,
                container, false);
        return mBinding.getRoot();
    }

    private Bitmap loadContactPhoto(Uri contactUri, int imageSize) {

        if (!isAdded() || getActivity() == null) {
            return null;
        }

        final ContentResolver contentResolver = getActivity().getContentResolver();

        AssetFileDescriptor afd = null;

        try {
            Uri displayImageUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);

            afd = contentResolver.openAssetFileDescriptor(displayImageUri, "r");

            if (afd != null) {
                // Reads  and decodes the file to a Bitmap and scales it to the desired size.
                return ImageLoader.decodeSampledBitmapFromDescriptor(
                        afd.getFileDescriptor(), imageSize, imageSize);
            }
        } catch (FileNotFoundException e) {
            if (BuildConfig.DEBUG) {
                // Log debug message, this is not an error message as this exception is thrown
                // when a contact is legitimately missing a contact photo (which will be quite
                // frequently in a long contacts list).
                Log.d(TAG, "Contact photo not found for contact " + contactUri.toString()
                        + ": " + e.toString());
            }
        } finally {
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // If none of the case selectors match, return null.
        return null;
    }

    private int getLargestScreenDimension() {
        // Gets a DisplayMetrics object, which is used to retrieve the display's pixel height and
        // width
        final DisplayMetrics displayMetrics = new DisplayMetrics();

        // Retrieves a displayMetrics object for the device's default display
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        // Returns the larger of the two values
        return height > width ? height : width;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Saves the contact Uri.
        outState.putParcelable(CONTACT_URI_BUNDLE_KEY, mContactUri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            // Sets the arguments extra as the currently displayed contact.
            setContact(getArguments() != null ?
                    (Uri) getArguments().getParcelable(CONTACT_URI_BUNDLE_KEY) : null);
        } else {
            // If being recreated from a saved state, sets the contact from the incoming
            // savedInstance Bundle.
            setContact((Uri) savedInstanceState.getParcelable(CONTACT_URI_BUNDLE_KEY));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Inflates the options menu for this fragment
        inflater.inflate(R.menu.contact_detail_menu, menu);

        mEditContactMenuItem = menu.findItem(R.id.menu_edit_contact);

        mEditContactMenuItem.setVisible(mContactUri != null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_edit_contact:
                // Standard system edit Contact intent
                Intent intent = new Intent(Intent.ACTION_EDIT, mContactUri);
                intent.putExtra("finishActivityOnSaveCompleted", true);

                // start the edit activity
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setContact(Uri contactLookupUri) {
        mContactUri = contactLookupUri;

        // If the Uri contains data, load the contact's image and load contacts details.
        if (contactLookupUri != null) {
            // // Asynchronously loads the contact image
            mImageLoader.loadImage(mContactUri, mBinding.contactImage);

            // Shows the contacts photo
            // Use Databinding to control visibility.
            mBinding.contactImage.setVisibility(View.VISIBLE);

            // Shows the edit contact action/menu item
            if (mEditContactMenuItem != null) {
                mEditContactMenuItem.setVisible(true);
            }

            getLoaderManager().restartLoader(ContactDetailQuery.QUERY_ID, null, this);
            getLoaderManager().restartLoader(ContactAddressQuery.QUERY_ID, null, this);
        }else {
            mBinding.contactImage.setVisibility(View.GONE);
            mBinding.contactDetailsLayout.removeAllViews();
            if(mEditContactMenuItem != null){
                mEditContactMenuItem.setVisible(false);
            }
        }
    }

    @Override
    public String getFragmentTag() {
        return "ContactDetailsFragment";
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void setViewModel(BaseObservable observable) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            // Two main Queries to load the required information
            case ContactDetailQuery.QUERY_ID:
                return new CursorLoader(
                        this.getActivity(),
                        mContactUri,
                        ContactDetailQuery.PROJECTION,
                        null, null, null);
            case ContactAddressQuery.QUERY_ID:
                // This query loads contact address details, see
                // ContactAddressQuery for more information.
                final Uri uri = Uri.withAppendedPath(mContactUri, ContactsContract.Contacts.Data.CONTENT_DIRECTORY);
                return new CursorLoader(getActivity(), uri,
                        ContactAddressQuery.PROJECTION,
                        ContactAddressQuery.SELECTION,
                        null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // If this fragment was cleared while the query was running
        // eg. from a call like setContact(uri) then don't do
        // anything.

        if (mContactUri == null) {
            return;
        }

        switch (loader.getId()) {
            case ContactDetailQuery.QUERY_ID:
                // Move to the first row in the cursor
                if (data.moveToFirst()) {
                    // For the contact details query, fetches the contact display name.
                    // ContactDetailQuery.DISPLAY_NAME maps to the appropriate display
                    // name field based on OS version.
                    final String contactName = data.getString(ContactDetailQuery.DISPLAY_NAME);

                    // the ActionBar title text.
                    getActivity().setTitle(contactName);
                }
                break;

            case ContactAddressQuery.QUERY_ID:
                // This query loads the contact address details. More than
                // one contact address is possible, so move each one to a
                // LinearLayout in a Scrollview so multiple addresses can
                // be scrolled by the user.

                // Each LinearLayout has the same LayoutParams so this can
                // be created once and used for each address.
                final LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                // Clears out the details layout first in case the details
                // layout has addresses from a previous data load still
                // added as children.
                mBinding.contactDetailsLayout.removeAllViews();

                // Loops through all the rows in the Cursor.
                if (data.moveToFirst()) {
                    do {
                        // Builds the address layout
                        final LinearLayout layout = buildAddressLayout(
                                data.getInt(ContactAddressQuery.TYPE),
                                data.getString(ContactAddressQuery.LABEL),
                                data.getString(ContactAddressQuery.ADDRESS));
                        mBinding.contactDetailsLayout.addView(layout);
                    } while (data.moveToNext());
                } else {
                    // If nothing found, adds an empty address layout
                    mBinding.contactDetailsLayout.addView(buildEmptyAddressLayout(), layoutParams);
                }

                break;
        }
    }

    /**
     * Builds an empty address layout that just shows that no addresses
     * were found for this contact.
     *
     * @return A LinearLayout to add to the contact details layout
     */
    private View buildEmptyAddressLayout() {
        return buildAddressLayout(0, null, null);
    }

    private LinearLayout buildAddressLayout(int addressType, String addressTypeLabel,
                                            final String address) {
        // Inflates the address layout
//        final LinearLayout addressLayout =
//                (LinearLayout) LayoutInflater.from(getActivity()).inflate(
//                        R.layout.contact_detail_item, mBinding.contactDetailsLayout, false);

        ContactDetailItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(getActivity()),
                R.layout.contact_detail_item,
                mBinding.contactDetailsLayout, false);

        if (addressTypeLabel == null && addressType == 0) {
            binding.contactDetailHeader.setVisibility(View.GONE);
            binding.buttonViewAddress.setVisibility(View.GONE);
            binding.contactDetailItem.setText(R.string.no_address);
        } else {
            // Get postal address label type
            CharSequence label =
                    ContactsContract.
                            CommonDataKinds.
                            StructuredPostal.
                            getTypeLabel(getResources(),
                                    addressType, addressTypeLabel);

            // Sets TextView objects in the layout
            binding.contactDetailHeader.setText(label);
            binding.contactDetailItem.setText(address);

            binding.buttonViewAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent viewIntent =
                            new Intent(Intent.ACTION_VIEW, constructGeoUri(address));

                    // A packageManager instance is needed to verify that there's a default app
                    // that handles ACTION_VIEW and a geo Uri
                    final PackageManager packageManager = getActivity().getPackageManager();

                    // Checks for an activity that can handle this intent. Preferred in this
                    // case over Intent.createChooser() as it will still let the user choose
                    // a default (or use a previously set default) for geo Uris.
                    if(packageManager.resolveActivity(viewIntent,
                            PackageManager.MATCH_DEFAULT_ONLY) != null){
                        startActivity(viewIntent);
                    }else {
                        // If no default is found, display a message that no activity can handle
                        // the view button
                        Toast.makeText(getActivity(), R.string.no_intent_found, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        return (LinearLayout) binding.getRoot();
    }

    /**
     * Constructs a geo scheme Uri from a postal address.
     *
     * @param postalAddress A postal address.
     * @return the geo:// Uri for the postal address.
     */
    private Uri constructGeoUri(String postalAddress) {
        // Concatenates the geo:// prefix to the postal address. The postal address must be
        // converted to Uri format and encoded for special characters.
        return Uri.parse(GEO_URI_SCHEME_PREFIX + Uri.encode(postalAddress));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Nothing to do here. The Cursor does not need to be released as it was never directly
        // bound to anything (like an adapter).
    }

    /**
     * This interface defines constants used by contact retrieval queries.
     */
    public interface ContactDetailQuery {
        // A unique query ID to distinguish queries being run by the
        // LoaderManager.
        final static int QUERY_ID = 1;

        // The query projection (columns to fetch from the provider)
        @SuppressLint("InlinedApi")
        final static String[] PROJECTION = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
        };

        // The query column numbers which map to each value in the projection
        final static int ID = 0;
        final static int DISPLAY_NAME = 1;
    }

    /**
     * This interface defines constants used by address retrieval queries.
     */
    public interface ContactAddressQuery {
        // A unique query ID to distinguish queries being run by the
        // LoaderManager.
        final static int QUERY_ID = 2;

        // The query projection (columns to fetch from the provider)
        final static String[] PROJECTION = {
                ContactsContract.CommonDataKinds.StructuredPostal._ID,
                ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS,
                ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.LABEL,
        };

        // The query selection criteria. In this case matching against the
        // StructuredPostal content mime type.
        final static String SELECTION =
                ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE + "'";

        // The query column numbers which map to each value in the projection
        final static int ID = 0;
        final static int ADDRESS = 1;
        final static int TYPE = 2;
        final static int LABEL = 3;
    }
}
