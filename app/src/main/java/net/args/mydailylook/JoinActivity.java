package net.args.mydailylook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.model.JoinModel;
import net.args.mydailylook.network.MyDailyLookService;
import net.args.mydailylook.network.ServiceGenerator;
import net.args.mydailylook.utils.DevLog;
import net.args.mydailylook.utils.OsUtils;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016-06-29.
 */
public class JoinActivity extends Activity implements View.OnClickListener {

    private Button mJoinBtn;
    private MaterialEditText mPwEdit;
    private MaterialEditText mIdEdit;
    private MaterialEditText mPwConfirmEdit;
    private TextView mCloseView;
    private TextView mTermsView;
    private MyDailyLookService mService;

    private AlertDialog mCheckDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        mService = ServiceGenerator.createService(MyDailyLookService.class);
        initLayout();
        initListener();
    }

    private void initLayout() {
        mJoinBtn = (Button) findViewById(R.id.btn_activity_join);
        mIdEdit = (MaterialEditText) findViewById(R.id.et_activity_join_id);
        mPwEdit = (MaterialEditText) findViewById(R.id.et_activity_join_pw);
        mPwConfirmEdit = (MaterialEditText) findViewById(R.id.et_activity_join_pw_confirm);
        mTermsView = (TextView) findViewById(R.id.tv_activity_join_terms);
        mCloseView = (TextView) findViewById(R.id.tv_activity_join_close);
    }

    private void initListener() {
        mPwEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPwEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());

        mPwConfirmEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPwConfirmEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());

        Pattern pattern = Pattern.compile("이용약관");
        Linkify.addLinks(mTermsView, pattern, "myterms:");

        mJoinBtn.setOnClickListener(this);
        mCloseView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_join:
                if (checkValid())
                    requestJoin();
                break;
            case R.id.tv_activity_join_close:
                finish();
                break;
        }
    }

    private boolean checkValid() {
        String id = mIdEdit.getText().toString();
        String pw = mPwEdit.getText().toString();
        String pwConfirm = mPwConfirmEdit.getText().toString();

        if (id == null || id.trim().length() <= 0) {
            Toast.makeText(this, "이메일 빈값", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pw == null || pw.trim().length() <= 0) {
            Toast.makeText(this, "비밀번호 빈값", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!pw.equals(pwConfirm)) {
            Toast.makeText(this, "비밀번호 확인 안맞음", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void requestJoin() {
        String id = mIdEdit.getText().toString();
        String pw = mPwEdit.getText().toString();
        final Call<JoinModel> join = mService.join(id, pw, "normal",
                OsUtils.getDeviceHashId(this));

        join.enqueue(new Callback<JoinModel>() {
            @Override
            public void onResponse(Call<JoinModel> call, Response<JoinModel> response) {
                JoinModel joinModel = response.body();
                DevLog.i(JoinActivity.this, joinModel.getCode());
                Toast.makeText(JoinActivity.this, "response code >> " + joinModel.getCode(), Toast.LENGTH_SHORT).show();

                Preferences.getInstance(JoinActivity.this).setAccessToken(joinModel.getAccessToken());

                if (joinModel.getCode().equals("1")) {
                    Intent intent = new Intent(JoinActivity.this, AuthActivity.class);
                    intent.putExtra("authType", "join");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<JoinModel> call, Throwable t) {
            }
        });
    }

}
