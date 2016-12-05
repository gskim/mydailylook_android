package net.args.mydailylook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.model.BaseModel;
import net.args.mydailylook.network.MyDailyLookService;
import net.args.mydailylook.network.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arseon on 2016-11-02.
 */
public class SettingActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mLogoutView;
    private MyDailyLookService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mService = ServiceGenerator.getService();
        initLayout();
        initListener();
    }

    private void initLayout() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_setting);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        mLogoutView = (TextView) findViewById(R.id.tv_activity_setting_logout);
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        mLogoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLogout();
            }
        });
    }

    private void requestLogout() {
        Call<BaseModel> logout = mService.logout(Preferences.getInstance(this).getAccessToken(),
                Preferences.getInstance(this).getDeviceID());

        logout.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if (response == null || response.body() == null)
                    return;

                BaseModel result = response.body();
                App.toast("logout result >> " + result.getCode());

                if (result.getCode().equals("1")) {
                    Preferences.getInstance(SettingActivity.this).setAccessToken("");
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {

            }
        });
    }
}
