package net.args.mydailylook.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;

import com.melnykov.fab.FloatingActionButton;

import net.args.mydailylook.App;
import net.args.mydailylook.PostingActivity;
import net.args.mydailylook.R;
import net.args.mydailylook.adapter.MainListAdapter;
import net.args.mydailylook.common.Const;
import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.model.BaseModel;
import net.args.mydailylook.model.MainList;
import net.args.mydailylook.model.MainListModel;
import net.args.mydailylook.network.MyDailyLookService;
import net.args.mydailylook.network.ServiceGenerator;
import net.args.mydailylook.utils.DevLog;
import net.args.mydailylook.utils.OsUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016-07-10.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private FloatingActionButton mFloatingBtn;
    private MyDailyLookService mService;
    private MainListAdapter mMainAdapter;

    public interface OnMainListListener {
        void onLikeChecked(String postId, boolean isChecked);

        void onMore(int position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mService = ServiceGenerator.getService();

        initLayout();
        initListener();
        setList(true);

        DevLog.i(Const.TAG, "onCreateView() ===================");
        return mView;
    }

    private View findViewById(int resId) {
        return mView.findViewById(resId);
    }

    private void initLayout() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_fragment_home);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_fragment_home_refresh);
        mFloatingBtn = (FloatingActionButton) findViewById(R.id.fab_fragment_home);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);

        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
        mFloatingBtn.attachToRecyclerView(mRecyclerView);
    }

    private void initListener() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setList(true);
            }
        });

        mFloatingBtn.setOnClickListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean lastItemVisibleFlag = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    //TODO 화면이 바닦에 닿을때 처리
                    App.toast("스크롤이 맨끝에 옴");
                    setList(false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });
    }

    private void setList(final boolean isNew) {
        final Call<MainList> postList = mService.postList(
                Preferences.getInstance(getActivity()).getAccessToken(), OsUtils.getDeviceHashId(getActivity()),
                isNew ? "0" : mMainAdapter.getLastId());
        postList.enqueue(new Callback<MainList>() {
            @Override
            public void onResponse(Call<MainList> call, Response<MainList> response) {
                stopRefreshing();

                MainList list = response.body();
                ArrayList<MainListModel> mainList = list.getData();

                if (isNew) {
                    mMainAdapter = new MainListAdapter(getActivity(), mainList);
                    mMainAdapter.setOnMainListListener(new OnMainListListener() {
                        @Override
                        public void onLikeChecked(String postId, boolean isChecked) {
                            requestLike(postId, isChecked);
                        }

                        @Override
                        public void onMore(int position) {
                            showMoreDialog();
                        }
                    });
                    mRecyclerView.setAdapter(mMainAdapter);
                } else {
                    if (mMainAdapter != null) {
                        mMainAdapter.setMainList(mainList);
                    }
                }
            }

            @Override
            public void onFailure(Call<MainList> call, Throwable t) {
                stopRefreshing();
            }
        });
    }

    private void stopRefreshing() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    private void showMoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.toolbar_menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_fragment_home:
                Intent intent = new Intent(getActivity(), PostingActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void requestLike(String postId, boolean isChecked) {
        String checked = isChecked ? "y" : "n";
        final Call<BaseModel> like = mService.like(
                Preferences.getInstance(getActivity()).getAccessToken(),
                Preferences.getInstance(getActivity()).getDeviceID(),
                postId,
                checked);
        like.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel model = response.body();
                App.toast("like response code >> " + model.getCode());
                if (model.getCode().equals("1")) {
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
            }
        });
    }
}
