// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.googlecodelabs.example.backupexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;

/**
 * Class for storing and accessing SharedPreferences
 */
class PrefUtils {

    /**
     * We store the login details such as the actual email address
     * used for logging into our app in this preferences file.
     *
     * Since this has the actual details, we exclude it from backup.
     */
    private static final String PREF_LOGIN_DETAILS =
            "com.googlecodelabs.example.backupexample.PREF_LOGIN_DETAILS";

    /**
     * This shared preferences file contains information that should be backed up.
     * Rather than actual email address, it contains a hints and other data that can
     * be helpful for end users after a backup
     */
    private static final String PREF_APP_INFO =
            "com.googlecodelabs.example.backupexample.PREF_APP_INFO";

    private static final String ACCOUNT_NAME = "ACCOUNT_NAME";
    private static final String ACCOUNT_AUTH_KEY = "ACCOUNT_AUTH_KEY";
    private static final String ACCOUNT_NAME_HINT = "ACCOUNT_NAME_HINT";

    /* Class known for its static methods*/
    private PrefUtils() {
    }

    /**
     * Whether the App should show the login screen
     * We store this in a shared preference that is <string>excluded<strong/> from
     * backup.
     *
     * @param context Context for fetching SharedPreferences
     * @return boolean indicating whether we should show the login screen
     */
    static boolean needsLogin(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(PREF_LOGIN_DETAILS, Context.MODE_PRIVATE);
        return preferences.getString(ACCOUNT_AUTH_KEY, null) == null ||
                preferences.getString(ACCOUNT_NAME, null) == null;
    }

    /**
     * Set the Login Account name once the login is successful.
     *
     * Here we also set an account name hint for the user to remember which account they
     * used once the app has been restored.
     *
     * @param context Context used for SharedPreferences
     * @param accountName The username/acountName to be set
     * @param authKey The authKey to be used for login
     */
    static void setLoginAccount(@NonNull Context context, String accountName, String authKey) {
        context.getSharedPreferences(PREF_LOGIN_DETAILS, Context.MODE_PRIVATE)
                .edit()
                .putString(ACCOUNT_NAME, accountName)
                .putString(ACCOUNT_AUTH_KEY, authKey)
                .apply();
        context.getSharedPreferences(PREF_APP_INFO, Context.MODE_PRIVATE)
                .edit()
                .putString(ACCOUNT_NAME_HINT, createHint(accountName))
                .apply();
    }

    static void logout(@NonNull Context context) {
        context.getSharedPreferences(PREF_LOGIN_DETAILS, Context.MODE_PRIVATE)
                .edit()
                .remove(ACCOUNT_NAME)
                .remove(ACCOUNT_AUTH_KEY)
                .apply();
    }

    private static String createHint(String accountName) {
        int idx = accountName.indexOf("@");
        String namePart = accountName;
        String rem = "";
        if (idx > -1) {
            namePart = accountName.substring(0, idx);
            rem = accountName.substring(idx);
        }

        return namePart.substring(0, 2) + "****" + rem;
    }

    @Nullable
    static String getLoginAccount(@NonNull Context context) {
        return context.getSharedPreferences(PREF_LOGIN_DETAILS, Context.MODE_PRIVATE)
                .getString(ACCOUNT_NAME, null);
    }

    static String getLoginAuthKey(Context context) {
        return context.getSharedPreferences(PREF_LOGIN_DETAILS, Context.MODE_PRIVATE)
                .getString(ACCOUNT_AUTH_KEY, null);
    }

    static String getLoginHint(Context context) {
        return context.getSharedPreferences(PREF_APP_INFO, Context.MODE_PRIVATE)
                .getString(ACCOUNT_NAME_HINT, null);
    }

    static Spanned getDebugText(@NonNull Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>SHARED PREFS DEBUG:<br></b>");
        sb.append(ACCOUNT_NAME_HINT);
        sb.append(": <i>");
        sb.append(PrefUtils.getLoginHint(context));
        sb.append("</i><br>\n");

        sb.append(ACCOUNT_NAME);
        sb.append(": <i>");
        sb.append(PrefUtils.getLoginAccount(context));
        sb.append("</i><br>\n");

        sb.append(ACCOUNT_AUTH_KEY);
        sb.append(": <i>");
        sb.append(PrefUtils.getLoginAuthKey(context));
        sb.append("</i><br>\n");
        return Html.fromHtml(sb.toString());
    }
}
