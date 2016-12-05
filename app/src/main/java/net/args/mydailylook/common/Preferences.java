package net.args.mydailylook.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.args.mydailylook.utils.Crypt;
import net.args.mydailylook.utils.DevLog;
import net.args.mydailylook.utils.OsUtils;

/**
 * Created by Administrator on 2016-08-21.
 */
public class Preferences {

    private static SharedPreferences mPreference;
    private static Preferences mPreferences;
    private static Context mContext;

    private static final String KEY_ACCESS_TOKEN = "key_access_token";
    private static final String KEY_DEVICE_HASH_ID = "key_device_hash_id";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_FIRST_RUN = "key_first_run";
    private static final String KEY_PUSH_KEY = "key_push_key";

    public static Preferences getInstance(Context context) {
        if (mPreferences == null) {
            mPreferences = new Preferences(context);
        }
        mContext = context;
        return mPreferences;
    }

    private Preferences(Context context) {
        if (mPreference == null) {
            mPreference = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public boolean getIsFirstRun() {
        return mPreference.getBoolean(KEY_FIRST_RUN, true);
    }

    public void setFirstRun(boolean isFirstRun) {
        mPreference.edit().putBoolean(KEY_FIRST_RUN, isFirstRun).commit();
    }

    public String getAccessToken() {
        String accessToken = mPreference.getString(KEY_ACCESS_TOKEN, "");
        try {
            accessToken = Crypt.Decrypt(accessToken, OsUtils.getDeviceHashId(mContext));
        } catch (Exception e) {
            DevLog.e(Const.TAG, "AccessToken decryption failed");
        }
        DevLog.i(Const.TAG, "AccessToken : " + accessToken);
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        String accessTokenSecure = "";
        try {
            accessTokenSecure = Crypt.Encrypt(accessToken, OsUtils.getDeviceHashId(mContext));
        } catch (Exception e) {
            DevLog.e(Const.TAG, "AccessToken encryption failed");
        }
        mPreference.edit().putString(KEY_ACCESS_TOKEN, accessTokenSecure).commit();
    }

    public String getDeviceID() {
        return mPreference.getString(KEY_DEVICE_HASH_ID, "");
    }

    public void setDeviceID(String value) {
        mPreference.edit().putString(KEY_DEVICE_HASH_ID, value).commit();
    }

    public String getPushKey() {
        return mPreference.getString(KEY_PUSH_KEY, "");
    }

    public void setPushKey(String value) {
        mPreference.edit().putString(KEY_PUSH_KEY, value).commit();
    }

    public String getEmail() {
        return mPreference.getString(KEY_EMAIL, "");
    }

    public void setEmail(String value) {
        mPreference.edit().putString(KEY_EMAIL, value).commit();
    }

}
