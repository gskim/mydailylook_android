package net.args.mydailylook;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.args.mydailylook.adapter.RecommendMemberAdapter;
import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.model.BaseModel;
import net.args.mydailylook.model.Recommend;
import net.args.mydailylook.model.RecommendMember;
import net.args.mydailylook.network.MyDailyLookService;
import net.args.mydailylook.network.ServiceGenerator;
import net.args.mydailylook.utils.OsUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arseon on 2016-09-27.
 */
public class RecommendMemberActivity extends Activity implements RecommendMemberAdapter.OnRecommendListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecommendMemberAdapter mAdapter;
    private MyDailyLookService mService;
    private int mPageId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_member);
        mService = ServiceGenerator.getService();

        initLayout();
        initListener();
        requestList(true);
    }

    private void initLayout() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_recommend_member);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_activity_recommend_member_refresh);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
    }

    private void initListener() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecommendMemberAdapter(this, new ArrayList<RecommendMember>());
        mAdapter.setOnListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestList(true);
            }
        });
    }

    private void requestList(final boolean isNew) {
        mPageId = isNew ? 1 : mPageId + 1;

        final Call<Recommend> recommend = mService.recommend(
                Preferences.getInstance(this).getAccessToken(),
                OsUtils.getDeviceHashId(this),
                "" + mPageId
        );

        recommend.enqueue(new Callback<Recommend>() {
            @Override
            public void onResponse(Call<Recommend> call, Response<Recommend> response) {
                stopRefreshing();

                Recommend result = response.body();
                ArrayList<RecommendMember> list = result.getData();
                setList(isNew, list);
            }

            @Override
            public void onFailure(Call<Recommend> call, Throwable t) {
                stopRefreshing();
            }
        });
    }

    private void stopRefreshing() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void setList(boolean isNew, ArrayList<RecommendMember> list) {
        if (mAdapter != null) {
            if (isNew) {
                mAdapter.initList(list);
            } else {
                mAdapter.addList(list);
            }
        }
    }

    @Override
    public void onFollow(String userId, boolean isChecked) {
        final Call<BaseModel> follow = mService.follow(
                Preferences.getInstance(this).getAccessToken(),
                OsUtils.getDeviceHashId(this),
                userId,
                isChecked ? "y" : "n");

        follow.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel result = response.body();
                App.toast("follow result >> " + result.getCode());
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
            }
        });
    }

}