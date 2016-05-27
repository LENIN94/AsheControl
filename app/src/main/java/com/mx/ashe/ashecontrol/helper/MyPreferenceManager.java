package com.mx.ashe.ashecontrol.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mx.ashe.ashecontrol.model.User;


/**
 * Created by OmarLenin on 03/03/2016.
 */
public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "androidhive_gcm";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_NOTIFICATIONS = "notifications";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());

        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getName() + ", " );
    }

    public void storeFolio(User user){

    }

    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);


            User user = new User(id, name,null);
            return user;
        }
        return null;
    }


    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}