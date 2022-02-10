package id.go.kebumenkab.retribusipasar.handler;

/**
 * Created by harysay on 16/05/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import id.go.kebumenkab.retribusipasar.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "logSession";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
//(usrID,usrName,usrReal,usrGroup,usrStatus,usrPhoto,usrLasLogin,usrSubGroup,returnTokenCode);
    public static final String key_usrID = "usrID";
    public static final String key_usrName = "usrName";
    public static final String key_usrRealName = "usrReal";
    public static final String key_usrGroup = "usrGroup";
    public static final String key_usrStatus = "usrStatus";
    public static final String key_usrPhoto = "usrPhoto";
    public static final String key_usrLastLogin = "usrLasLogin";
    public static final String key_usrSubGroup = "usrSubGroup";
    public static final String key_usrTokenCode = "returnTokenCode";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String usrID, String usrName, String usrReal, String usrGroup, String usrStatus, String usrPhoto, String usrLasLogin, String usrSubGroup, String returnTokenCode){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(key_usrID, usrID);
        editor.putString(key_usrName, usrName);
        editor.putString(key_usrRealName, usrReal);
        editor.putString(key_usrGroup, usrGroup);
        editor.putString(key_usrStatus, usrStatus);
        editor.putString(key_usrPhoto, usrPhoto);
        editor.putString(key_usrLastLogin, usrLasLogin);
        editor.putString(key_usrSubGroup, usrSubGroup);
        editor.putString(key_usrTokenCode, returnTokenCode);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        //user id
        user.put(key_usrID, pref.getString(key_usrID, null));
        // user name
        user.put(key_usrName, pref.getString(key_usrName, null));
        user.put(key_usrRealName, pref.getString(key_usrRealName, null));
        user.put(key_usrGroup, pref.getString(key_usrGroup, null));
        user.put(key_usrStatus, pref.getString(key_usrStatus, null));
        user.put(key_usrPhoto, pref.getString(key_usrPhoto, null));
        user.put(key_usrLastLogin, pref.getString(key_usrLastLogin, null));
        user.put(key_usrSubGroup, pref.getString(key_usrSubGroup, null));
        user.put(key_usrTokenCode, pref.getString(key_usrTokenCode, null));
        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login NamaPekerjaan
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
