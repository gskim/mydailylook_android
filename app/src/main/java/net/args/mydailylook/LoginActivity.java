package net.args.mydailylook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.rengwuxian.materialedittext.MaterialEditText;

import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.model.LoginModel;
import net.args.mydailylook.network.MyDailyLookService;
import net.args.mydailylook.network.ServiceGenerator;
import net.args.mydailylook.utils.DevLog;
import net.args.mydailylook.utils.OsUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016-06-18.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private CheckBox mPwCheckBox;
    private MaterialEditText mPwEdit;
    private MaterialEditText mIdEdit;
    private ImageView mCloseView;
    private MyDailyLookService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mService = ServiceGenerator.createService(MyDailyLookService.class);

        initLayout();
        initListener();
    }

    private void initLayout() {
        mIdEdit = (MaterialEditText) findViewById(R.id.et_activity_login_id);
        mPwEdit = (MaterialEditText) findViewById(R.id.et_activity_login_pw);
        mPwCheckBox = (CheckBox) findViewById(R.id.cb_activity_login_pw_check);
        mCloseView = (ImageView) findViewById(R.id.iv_activity_login_close);
    }

    private void initListener() {
        findViewById(R.id.btn_activity_login).setOnClickListener(this);
        mCloseView.setOnClickListener(this);
        mPwEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPwEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());

        mPwCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPwEdit.setTransformationMethod(isChecked ? null : new PasswordTransformationMethod());
                mPwEdit.setSelection(mPwEdit.getText().length());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_activity_login_close:
                finish();
                break;
            case R.id.btn_activity_login:
                requestLogin();
                break;
        }
    }

    private void requestLogin() {
        final String id = mIdEdit.getText().toString();
        String pw = mPwEdit.getText().toString();

        Call<LoginModel> login = mService.login(id, pw, "normal",
                OsUtils.getDeviceHashId(this));
        login.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                LoginModel loginModel = response.body();
                DevLog.i(LoginActivity.this, loginModel.getCode());

                Preferences.getInstance(LoginActivity.this).setAccessToken(loginModel.getAccessToken());
                Preferences.getInstance(LoginActivity.this).setEmail(id);

                responseLogin(loginModel.getCode());
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
            }
        });
    }

    private void responseLogin(String code) {
        int cd = 0;
        try {
            cd = Integer.valueOf(code);
        } catch (Exception e) {
        }

        switch (cd) {
            case 1: // 성공
                App.toast(R.string.login_success);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case 2: // 등록되지 않은 이메일
                App.toast(R.string.invalid_email);
                break;
            case 3: // 이메일, 비밀번호 불일치
                App.toast(R.string.invalid_password);
                break;
            case 4: // 프로필 등록을 하지 않은 회원
                App.toast(R.string.not_register_profile);
                Intent profileIntent = new Intent(LoginActivity.this, ProfileInputActivity.class);
                startActivity(profileIntent);
                break;
            case 5: // 이메일 인증을 하지 않은 회원
                App.toast(R.string.not_register_profile);
                Intent authIntent = new Intent(LoginActivity.this, AuthActivity.class);
                startActivity(authIntent);
                break;
        }
    }

}
