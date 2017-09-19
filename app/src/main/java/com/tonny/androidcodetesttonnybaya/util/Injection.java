package com.tonny.androidcodetesttonnybaya.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tonny.androidcodetesttonnybaya.data.source.ContactsRepository;
import com.tonny.androidcodetesttonnybaya.data.source.local.ContactsLocalDataSource;
import com.tonny.androidcodetesttonnybaya.data.source.remote.ContactsRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enable Injection.
 * {@link com.tonny.androidcodetesttonnybaya.data.source.ContactsDataSource}
 */

public class Injection {
    public static ContactsRepository provideContactRepository(@NonNull Context context) {
        checkNotNull(context);
        return ContactsRepository.getInstance(ContactsRemoteDataSource.getInstance(context),
                ContactsLocalDataSource.getInstance(context));
    }
}
