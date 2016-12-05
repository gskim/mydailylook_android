package net.args.mydailylook;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.common.ResponseCode;
import net.args.mydailylook.model.GateModel;
import net.args.mydailylook.model.LoginModel;
import net.args.mydailylook.network.MyDailyLookService;
import net.args.mydailylook.network.ServiceGenerator;
import net.args.mydailylook.utils.DevLog;
import net.args.mydailylook.utils.OsUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016-06-20.
 */
public class IntroActivity extends Activity {
    private static final int HANDLE_CHECK_PUSH_KEY = 1;

    private MyDailyLookService mService;
    private IntroActivityHandler mHandler;
    private int pushRetryCount = 35;

    private static class IntroActivityHandler extends Handler {
        private final WeakReference<IntroActivity> weakActivity;

        public IntroActivityHandler(IntroActivity activity) {
            this.weakActivity = new WeakReference<IntroActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            IntroActivity activity = weakActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    private void handleMessage(Message msg) {
        switch (msg.what) {
            case HANDLE_CHECK_PUSH_KEY:
                checkPushKey();
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        mService = ServiceGenerator.getService();
        mHandler = new IntroActivityHandler(this);

        checkPermission();

//        String deviceId = Preferences.getInstance(this).getDeviceID();
//        if (deviceId == null || deviceId.trim().length() <= 0)
//            Preferences.getInstance(this).setDeviceID(OsUtils.getDeviceHashId(this));
//
//        checkPushKey();
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }

    private void checkPermission() {
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(R.string.permission_retry)
                .setDeniedCloseButtonText(R.string.exit)
                .setPermissions(Manifest.permission.READ_PHONE_STATE)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            checkPushKey();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            App.toast(R.string.permission_deny_exit);
            finish();
        }
    };

//    private void showPermissionDialog() {
//        final Dialog dialog = new Dialog(this);
//        View view = getLayoutInflater().inflate(R.layout.dialog_permission, null);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.btn_dialog_permission_go:
//                        App.toast("설정하기");
//                        break;
//                    case R.id.btn_dialog_permission_exit:
//                        dialog.dismiss();
//                        break;
//                }
//            }
//        });
//
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(view);
//        dialog.show();
//    }

    private void checkPushKey() {
        String pushKey = Preferences.getInstance(this).getPushKey();
        if (pushKey.trim().length() > 0 && !pushKey.contains("NotDeviceToken")) {
            requestGate();
        } else {
            String token;
            try {
                token = FirebaseInstanceId.getInstance().getToken();
            } catch (Exception e) {
                token = "";
                e.printStackTrace();
            }

            if (token != null && token.trim().length() > 0) {
                Preferences.getInstance(this).setPushKey(token);
                requestGate();
                return;
            }

            DevLog.w(this, "FirebaseInstanceId getToken is null");
            retryPushCheck();
        }
    }

    private void retryPushCheck() {
        DevLog.w(this, "retryPushCheck");
        if (pushRetryCount < 0) {
            String deviceId = OsUtils.getDeviceHashId(this);
            if (deviceId == null || deviceId.trim().length() <= 0) {
                App.toast("Device ID 가 없습니다.");
                return;
            }

            Preferences.getInstance(this).setPushKey("NotDeviceToken_" + deviceId);
            requestGate();
        } else {
            pushRetryCount--;
            mHandler.sendEmptyMessageDelayed(HANDLE_CHECK_PUSH_KEY, 400);
        }
    }

    private void checkNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goNextActivity();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
            }
        }, 1000);
    }

    private void goNextActivity() {
        // 처음 실행
        if (Preferences.getInstance(this).getIsFirstRun()) {
            Preferences.getInstance(this).setFirstRun(false);
            startIntroduceActivity();
            return;
        }

        // 액세스 토큰
        String accessToken = Preferences.getInstance(this).getAccessToken();
        if (accessToken != null && accessToken.trim().length() > 0) {
            DevLog.i(this, "getAccessToken() >> 0 " + accessToken);
            // 자동로그인
            requestAutoLogin();
            return;
        }

        startLoginSelectActivity();
    }

    private void requestGate() {
//        String deviceId = Preferences.getInstance(this).getDeviceID();
        String deviceId = OsUtils.getDeviceHashId(this);
        String pushKey = Preferences.getInstance(this).getPushKey();

        final Call<GateModel> gate = mService.gate(deviceId, pushKey,
                "" + OsUtils.getVersionCode(this), "Android", OsUtils.getOsVersion());

        gate.enqueue(new Callback<GateModel>() {
            @Override
            public void onResponse(Call<GateModel> call, Response<GateModel> response) {
                GateModel gateModel = response.body();
                DevLog.i(IntroActivity.this, gateModel.getCode());
                App.toast("response code >> " + gateModel.getCode());

                checkNextActivity();
            }

            @Override
            public void onFailure(Call<GateModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void requestAutoLogin() {
        DevLog.i(this, "autologin getDeviceID() >> " + Preferences.getInstance(this).getDeviceID());
        final Call<LoginModel> autoLogin = mService.autoLogin(Preferences.getInstance(this).getAccessToken(),
                OsUtils.getDeviceHashId(this));

        autoLogin.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                LoginModel loginModel = response.body();
                Preferences.getInstance(IntroActivity.this).setAccessToken(loginModel.getAccessToken());
                String code = loginModel.getCode();

                App.toast("autologin response code >> " + loginModel.getCode());

                // 성공
                if (code.equals(ResponseCode.RESPONSE_1)) {
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

                if (code.equals(ResponseCode.RESPONSE_4)) {
                    Intent intent = new Intent(IntroActivity.this, ProfileInputActivity.class);
                    startActivity(intent);
                    return;
                }
                // 이메일 인증을 안함.
                if (code.equals(ResponseCode.RESPONSE_5)) {
                    startAuthActivity();
                } else { // 로그인/회원가입 선택
                    startLoginSelectActivity();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
            }
        });
    }

    private void startIntroduceActivity() {
        Intent intent = new Intent(this, DescriptionActivity.class);
        View sharedView = findViewById(R.id.iv_activity_intro_logo);
        String transitionName = getString(R.string.logo_transition);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(IntroActivity.this, sharedView, transitionName);
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    private void startLoginSelectActivity() {
        Intent intent = new Intent(this, LoginSelectActivity.class);
        startActivity(intent);
//        View sharedView = findViewById(R.id.iv_activity_intro_logo);
//        String transitionName = getString(R.string.logo_transition);
//
//        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(IntroActivity.this, sharedView, transitionName);
//        startActivity(intent, transitionActivityOptions.toBundle());
    }

    private void startAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra("authType", "intro");
        startActivity(intent);
//        View sharedView = findViewById(R.id.iv_activity_intro_logo);
//        String transitionName = getString(R.string.logo_transition);
//
//        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(IntroActivity.this, sharedView, transitionName);
//        startActivity(intent, transitionActivityOptions.toBundle());
    }

}
