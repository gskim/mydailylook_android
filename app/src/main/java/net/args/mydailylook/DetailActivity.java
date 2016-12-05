package net.args.mydailylook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import net.args.mydailylook.adapter.DetailAdapter;
import net.args.mydailylook.common.Const;
import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.model.MainList;
import net.args.mydailylook.model.MainListModel;
import net.args.mydailylook.model.Reply;
import net.args.mydailylook.network.MyDailyLookService;
import net.args.mydailylook.network.ServiceGenerator;
import net.args.mydailylook.utils.DevLog;
import net.args.mydailylook.utils.OsUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arseon on 2016-09-20.
 */
public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SimpleDraweeView mThumbView;
    private TextView mContentView;
    private TextView mNickNameView;
    private TextView mDateView;
    private TextView mReplyView;
    private TextView mReplyCntView;
    private TextView mPageCntView;
    private LinearLayout mReplyLayout;
    private CheckBox mLikeCheckBox;

    private DetailAdapter mAdapter;
    private HashTagHelper mTextHashTagHelper;
    private MyDailyLookService mService;

    private String mPostId;
    private int mTotalImg;
//    private HeightWrappingViewPager mViewPager;

    public interface OnPagerListener {
        void onLoadFinish(int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mService = ServiceGenerator.getService();
        mPostId = getIntent().getStringExtra("postId");
        initLayout();
        initListener();
        requestDetail();
    }

    private void initLayout() {
        mThumbView = (SimpleDraweeView) findViewById(R.id.sdv_activity_detail_thumb);
        mContentView = (TextView) findViewById(R.id.tv_activity_detail_content);
        mNickNameView = (TextView) findViewById(R.id.tv_activity_detail_nickname);
        mDateView = (TextView) findViewById(R.id.tv_activity_detail_date);
        mReplyView = (TextView) findViewById(R.id.tv_activity_detail_reply);
        mReplyCntView = (TextView) findViewById(R.id.tv_activity_detail_reply_cnt);
        mPageCntView = (TextView) findViewById(R.id.tv_activity_detail_page_indicator);
        mLikeCheckBox = (CheckBox) findViewById(R.id.cb_activity_detail_like);
        mReplyLayout = (LinearLayout) findViewById(R.id.ll_activity_detail_reply);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_detail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_activity_detail);
        mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = mViewPager.getWidth();
                if (mViewPager.getHeight() != width) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewPager.getLayoutParams();
                    params.height = width;
                    mViewPager.setLayoutParams(params);
                }
            }
        });

        try {
            String nickName = getIntent().getStringExtra("nickName");
            String profileImgId = getIntent().getStringExtra("profilePhoto");

            mNickNameView.setText(nickName);
            mThumbView.setImageURI(Const.PROFILE_IMAGE_URL + "?id=" + profileImgId);
        } catch (Exception e) {
        }
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mReplyView.setOnClickListener(this);
        mReplyCntView.setOnClickListener(this);

        mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary),
                new HashTagHelper.OnHashTagClickListener() {
                    @Override
                    public void onHashTagClicked(String hashTag) {
                        Intent intent = new Intent(DetailActivity.this, SearchActivity.class);
                        intent.putExtra("tag", hashTag);
                        startActivity(intent);
                    }
                });
        mTextHashTagHelper.handle(mContentView);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mPageCntView != null) {
                    String totalCnt = String.format(getString(R.string.page_total_count),
                            "" + (position + 1), "" + mTotalImg);
                    mPageCntView.setText(totalCnt);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_detail_reply_cnt:
            case R.id.tv_activity_detail_reply:
                Intent intent = new Intent(DetailActivity.this, ReplyActivity.class);
                intent.putExtra("postId", mPostId);
                startActivity(intent);
                break;
        }
    }

    private void requestDetail() {
        final Call<MainList> detail = mService.detail(Preferences.getInstance(this).getAccessToken(),
                mPostId,
                OsUtils.getDeviceHashId(this));

        detail.enqueue(new Callback<MainList>() {
            @Override
            public void onResponse(Call<MainList> call, Response<MainList> response) {
                MainList list = response.body();
                App.toast("requestDetail response >> " + list.getCode());
                setData(list.getData());
            }

            @Override
            public void onFailure(Call<MainList> call, Throwable t) {

            }
        });
    }

    private void setData(ArrayList<MainListModel> list) {
        MainListModel item = list.get(0);

        mContentView.setText(item.getContent());
        mNickNameView.setText(item.getNickname());
        mDateView.setText(item.getRegdate());
        mReplyCntView.setText(item.getReplyCnt());
        mLikeCheckBox.setText(item.getLikeCnt());

        mAdapter = new DetailAdapter(this, item.getImgId());
        mViewPager.setAdapter(mAdapter);
        mThumbView.setImageURI(Const.PROFILE_IMAGE_URL + "?id=" + item.getProfilePhoto());

        mTotalImg = item.getTotalImgId();
        String totalCnt = String.format(getString(R.string.page_total_count), "1", "" + mTotalImg);
        mPageCntView.setText(totalCnt);

        int replyCnt = Integer.valueOf(item.getReplyCnt());
        if (replyCnt == 0) {
            mReplyView.setVisibility(View.GONE);
            mReplyLayout.setVisibility(View.GONE);
            return;
        }

        String reply = String.format(getString(R.string.show_all_reply), "" + replyCnt);
        mReplyView.setText(reply);

        setReply(item);
    }

    private void setReply(MainListModel item) {
        ArrayList<Reply> replyList = item.getReply();
        int totalSize = item.getTotalReply();

        for (int i = 0; i < totalSize; i++) {
            if (i == 2)
                return;

            LinearLayout commentLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_main_list_comment, null);
            TextView idView = (TextView) commentLayout.findViewById(R.id.tv_list_main_list_comment_user_id);
            TextView commentView = (TextView) commentLayout.findViewById(R.id.tv_list_main_list_comment_user_comment);
            idView.setText(replyList.get(i).getUsername());
            commentView.setText(replyList.get(i).getContent());
            mReplyLayout.addView(commentLayout);
        }
    }

    private void resizeViewPager(int position) {
        View view = mViewPager.findViewWithTag(position);
        if (view == null) return;

        view.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int height = view.getMeasuredHeight();

        DevLog.i(this, "resizeViewPager() height >> " + height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        mViewPager.setLayoutParams(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            showMenuDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMenuDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.toolbar_menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

}
