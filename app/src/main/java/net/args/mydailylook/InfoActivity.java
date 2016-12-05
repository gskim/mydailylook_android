package net.args.mydailylook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.args.mydailylook.adapter.InfoAdapter;
import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.model.PersonalInfo;
import net.args.mydailylook.model.PostingList;
import net.args.mydailylook.network.MyDailyLookService;
import net.args.mydailylook.network.ServiceGenerator;
import net.args.mydailylook.utils.OsUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arseon on 2016-10-11.
 */
public class InfoActivity extends Activity {
    private RecyclerView mRecyclerView;
    private InfoAdapter mAdapter;
    private MyDailyLookService mService;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        mService = ServiceGenerator.getService();
        mUserId = getIntent().getStringExtra("userId");

        if (mUserId == null || mUserId.trim().length() <= 0)
            mUserId = "";

        initLayout();
        initListener();
        requestInfo();
        requestPostingList();
    }

    private void initLayout() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_info);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) return 3;
                return 1;
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initListener() {
        mAdapter = new InfoAdapter(this, null);
        mAdapter.setOnInfoListener(new InfoAdapter.OnInfoListener() {
            @Override
            public void onProfileImgClick() {
            }

            @Override
            public void onSettingClick() {
                Intent intent = new Intent(InfoActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void requestInfo() {
        final Call<PersonalInfo> info = mService.info(Preferences.getInstance(this).getAccessToken()
                , OsUtils.getDeviceHashId(this), mUserId);

        info.enqueue(new Callback<PersonalInfo>() {
            @Override
            public void onResponse(Call<PersonalInfo> call, Response<PersonalInfo> response) {
                if (response == null) {
                    App.toast(R.string.response_fail);
                    return;
                }

                PersonalInfo info = response.body();
                if (mAdapter != null)
                    mAdapter.setPersonalInfo(info);
            }

            @Override
            public void onFailure(Call<PersonalInfo> call, Throwable t) {
                App.toast(R.string.response_fail);
            }
        });
    }

    private void requestPostingList() {
        final Call<PostingList> postingList = mService.postingList(Preferences.getInstance(this).getAccessToken()
                , OsUtils.getDeviceHashId(this), mUserId);

        postingList.enqueue(new Callback<PostingList>() {
            @Override
            public void onResponse(Call<PostingList> call, Response<PostingList> response) {
                if (response == null || response.body() == null) {
                    App.toast(R.string.response_fail);
                    return;
                }

                PostingList postingList = response.body();
                if (mAdapter != null)
                    mAdapter.setPostingList(postingList.getData());
            }

            @Override
            public void onFailure(Call<PostingList> call, Throwable t) {
                App.toast(R.string.response_fail);
            }
        });
    }

}
