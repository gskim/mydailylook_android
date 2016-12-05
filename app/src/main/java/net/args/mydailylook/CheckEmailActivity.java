package net.args.mydailylook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.model.BaseModel;
import net.args.mydailylook.network.MyDailyLookService;
import net.args.mydailylook.network.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016-09-04.
 */
public class CheckEmailActivity extends Activity {

    private MyDailyLookService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_email);

        mService = ServiceGenerator.getService();
        findViewById(R.id.btn_activity_check_email_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestEmailCheck();
            }
        });
    }

    private void requestEmailCheck() {
        final Call<BaseModel> checkEmail =
                mService.checkEmail(Preferences.getInstance(this).getAccessToken());

        checkEmail.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel result = response.body();
                if (result.getCode().equals("1")) {
                    Intent intent = new Intent(CheckEmailActivity.this, ProfileInputActivity.class);
                    startActivity(intent);
                } else {
                    App.toast("이메일 인증이 완료되지 않았습니다");
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
            }
        });
    }

}
