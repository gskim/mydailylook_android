package net.args.mydailylook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.model.BaseModel;
import net.args.mydailylook.network.MyDailyLookService;
import net.args.mydailylook.network.ServiceGenerator;
import net.args.mydailylook.utils.DevLog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arseon on 2016-08-26.
 */
public class AuthActivity extends Activity implements View.OnClickListener {
    private MyDailyLookService mService;
    private LinearLayout mSendLayout;
    private LinearLayout mNotAuthLayout;

    private Button mResendBtn;
    private Button mCompleteBtn;
    private Button mAuthBtn;
    private Button mOtherAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mService = ServiceGenerator.createService(MyDailyLookService.class);
        initLayout();
        initListener();
    }

    private void initLayout() {
        mSendLayout = (LinearLayout) findViewById(R.id.ll_activity_auth_email_send);
        mNotAuthLayout = (LinearLayout) findViewById(R.id.ll_activity_auth_email_not_auth);

        mResendBtn = (Button) findViewById(R.id.btn_activity_auth_resend);
        mCompleteBtn = (Button) findViewById(R.id.btn_activity_auth_complete);
        mAuthBtn = (Button) findViewById(R.id.btn_activity_auth_email);
        mAuthBtn.setText(Preferences.getInstance(this).getEmail());
        mOtherAccountBtn = (Button) findViewById(R.id.btn_activity_auth_other_email);

        String authType = getIntent().getStringExtra("authType");
        if (authType != null) {
            if (authType.equals("join")) {
                mSendLayout.setVisibility(View.VISIBLE);
                mNotAuthLayout.setVisibility(View.GONE);
            } else if (authType.equals("intro")) {
                mSendLayout.setVisibility(View.GONE);
                mNotAuthLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initListener() {
        mResendBtn.setOnClickListener(this);
        mCompleteBtn.setOnClickListener(this);
        mAuthBtn.setOnClickListener(this);
        mOtherAccountBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_auth_resend:
                requestResend();
                break;
            case R.id.btn_activity_auth_complete:
                requestComplete();
                break;
            case R.id.btn_activity_auth_email:
                requestResend();
                mSendLayout.setVisibility(View.VISIBLE);
                mNotAuthLayout.setVisibility(View.GONE);
                break;
            case R.id.btn_activity_auth_other_email:
                Intent intent = new Intent(AuthActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void requestResend() {
        final Call<BaseModel> sendEmail = mService.sendEmail(Preferences.getInstance(this).getAccessToken());
        sendEmail.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel model = response.body();
                DevLog.i(AuthActivity.this, "resend email getCode >> " + model.getCode());
                if (model.getCode().equals("1")) {
                    App.toast(R.string.resend_success);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
            }
        });
    }

    private void requestComplete() {
        final Call<BaseModel> checkEmail = mService.checkEmail(Preferences.getInstance(this).getAccessToken());
        checkEmail.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel model = response.body();
                DevLog.i(AuthActivity.this, "check email getCode >> " + model.getCode());
                if (model.getCode().equals("1")) {
                    Intent intent = new Intent(AuthActivity.this, ProfileInputActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
            }
        });
    }

}
