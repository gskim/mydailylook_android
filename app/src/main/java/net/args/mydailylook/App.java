package net.args.mydailylook;

import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDexApplication;
import android.view.Gravity;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import net.args.mydailylook.common.Const;
import net.args.mydailylook.utils.DevLog;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016-07-17.
 */
public class App extends MultiDexApplication {
    private static App mInstance;
    private AppHandler mHandler;

    private static class AppHandler extends Handler {
        private final WeakReference<App> weakClass;

        public AppHandler(App app) {
            this.weakClass = new WeakReference<App>(app);
        }

        @Override
        public void handleMessage(Message msg) {
            App app = weakClass.get();
            if (app != null) {
                app.handleMessage(msg);
            }
        }
    }

    private void handleMessage(Message msg) {
        if (msg.arg1 == 1) {
            Toast toast = Toast.makeText(mInstance.getApplicationContext(), (String) msg.obj,
                    msg.what);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (msg.arg2 == 1) {
            Toast toast = Toast.makeText(mInstance.getApplicationContext(), (String) msg.obj,
                    msg.what);
            toast.setGravity(Gravity.TOP, 0, 100);
            toast.show();
        } else {
            Toast.makeText(mInstance.getApplicationContext(), (String) msg.obj, msg.what).show();
        }
    }

    public App() {
        mInstance = this;
    }

    public static App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new AppHandler(this);
        Fresco.initialize(this);
        DevLog.w(Const.TAG, "[App] onCreate()");
    }

    public static void toast(int resId) {
        toast(mInstance.getResources().getString(resId));
    }

    public static void toast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT, false, false);
    }

    private static void showToast(String message, int duration, boolean isCenter, boolean isTop) {
        if (message == null || message.trim().length() <= 0)
            return;

        Message msg = new Message();
        msg.what = duration;
        msg.obj = message;
        msg.arg1 = isCenter ? 1 : 0;
        msg.arg2 = isTop ? 1 : 0;
        mInstance.mHandler.sendMessage(msg);
    }

}
