package net.args.mydailylook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import net.args.mydailylook.adapter.ReplyListAdpater;
import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.model.Reply;
import net.args.mydailylook.model.ReplyList;
import net.args.mydailylook.network.MyDailyLookService;
import net.args.mydailylook.network.ServiceGenerator;
import net.args.mydailylook.utils.DevLog;
import net.args.mydailylook.utils.OsUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016-09-25.
 */
public class ReplyActivity extends Activity implements View.OnClickListener {
    private EditText mReplyEdit;
    private ImageButton mSendBtn;
    private ImageView mCloseView;
    private RecyclerView mRecyclerView;

    private ReplyListAdpater mAdapter;
    private MyDailyLookService mService;
    private String mPostId;
    private boolean mLoading;
    private int mPrevTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        mService = ServiceGenerator.getService();
        mPostId = getIntent().getStringExtra("postId");

        initLayout();
        initListener();
        requestReplyList(true);
    }

    private void initLayout() {
        mReplyEdit = (EditText) findViewById(R.id.et_activity_reply);
        mSendBtn = (ImageButton) findViewById(R.id.ibtn_activity_reply_send);
        mCloseView = (ImageView) findViewById(R.id.iv_activity_reply_close);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_reply);
    }

    private void initListener() {
        mCloseView.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new ReplyListAdpater(this, new ArrayList<Reply>());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean lastItemVisibleFlag = false;
            boolean firstItemVisibleFlag = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
//                    //TODO 화면이 바닦에 닿을때 처리
//                    App.toast("스크롤이 맨끝에 옴");
//                    requestReplyList(false);
//                }

//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && firstItemVisibleFlag) {
//                    //TODO 화면이 맨위로 갔을때 처리
//                    App.toast("스크롤이 맨위에 옴!!");
//                    requestReplyList(false);
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

//                firstItemVisibleFlag = (firstVisibleItem == 0);
//                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);

                if (mLoading) {
                    if (totalItemCount > mPrevTotal) {
                        if (mPrevTotal > 0) {
                            mLoading = false;
                        }
                        mPrevTotal = totalItemCount;
                    }
                }

                if (!mLoading && (firstVisibleItem == 2)) {
                    requestReplyList(false);
                    mLoading = true;
                }

            }
        });
    }

    private void requestReplyList(final boolean isNew) {

        String lastId = isNew ? "" + 0 : mAdapter.getFirstItemId();
        DevLog.w(this, "requestReplyList lastId >> " + lastId);
        final Call<ReplyList> replyList = mService.replyList(Preferences.getInstance(this).getAccessToken()
                , OsUtils.getDeviceHashId(this)
                , mPostId
                , lastId);

        replyList.enqueue(new Callback<ReplyList>() {
            @Override
            public void onResponse(Call<ReplyList> call, Response<ReplyList> response) {
                ReplyList list = response.body();
                setData(isNew, list.getData());
            }

            @Override
            public void onFailure(Call<ReplyList> call, Throwable t) {

            }
        });
    }

    private void setData(boolean isNew, ArrayList<Reply> list) {
        if (mAdapter != null) {
            mAdapter.setReplyList(isNew, list);
        }
    }

    private void setList() {
        ArrayList<Reply> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Reply reply = new Reply("username" + i, "usercomment " + i);
            list.add(reply);
        }

        mAdapter = new ReplyListAdpater(this, list);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_activity_reply_close:
                finish();
                break;
            case R.id.ibtn_activity_reply_send:
                String content = mReplyEdit.getText().toString();
                if (content == null || content.trim().length() == 0)
                    return;

                sendReply(content);
                break;
        }
    }

    private void sendReply(String content) {
        final Call<Reply> reply = mService.reply(Preferences.getInstance(this).getAccessToken()
                , OsUtils.getDeviceHashId(this)
                , mPostId
                , content);

        reply.enqueue(new Callback<Reply>() {
            @Override
            public void onResponse(Call<Reply> call, Response<Reply> response) {
                if (response == null || response.body() == null) {
                    App.toast(R.string.response_fail);
                    return;
                }

                Reply result = response.body();
                if (result.getCode().equals("1")) {
                    App.toast(R.string.success_reply);
                    mReplyEdit.setText("");
                    closeSoftKeyboard();
                    getReplyItem(result.getId());
                }
            }

            @Override
            public void onFailure(Call<Reply> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getReplyItem(String replyId) {
        final Call<Reply> replyItem = mService.replyitem(Preferences.getInstance(this).getAccessToken()
                , OsUtils.getDeviceHashId(this)
                , replyId);

        replyItem.enqueue(new Callback<Reply>() {
            @Override
            public void onResponse(Call<Reply> call, Response<Reply> response) {
                if (response == null || response.body() == null) {
                    return;
                }

                Reply result = response.body();
                mAdapter.setReplyItem(result);
                mRecyclerView.smoothScrollToPosition(mAdapter.getLastItemPosition());
            }

            @Override
            public void onFailure(Call<Reply> call, Throwable t) {
            }
        });
    }

    private void closeSoftKeyboard() {
        // 키보드 닫기
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
