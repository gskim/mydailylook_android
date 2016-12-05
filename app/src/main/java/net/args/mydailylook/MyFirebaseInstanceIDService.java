package net.args.mydailylook;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import net.args.mydailylook.common.Const;
import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.utils.DevLog;

/**
 * Created by Administrator on 2016-08-21.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        DevLog.d(Const.TAG, "Refreshed token: " + refreshedToken);

        Preferences.getInstance(this).setPushKey(refreshedToken);
    }

}