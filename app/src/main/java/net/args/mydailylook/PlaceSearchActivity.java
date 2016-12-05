package net.args.mydailylook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.args.mydailylook.adapter.PlaceSearchAdapter;
import net.args.mydailylook.common.Const;
import net.args.mydailylook.model.DaumSearchChannel;
import net.args.mydailylook.model.DaumSearchItem;
import net.args.mydailylook.network.DaumApiService;
import net.args.mydailylook.network.ServiceGenerator;
import net.args.mydailylook.view.DividerItemDecoration;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arseon on 2016-10-27.
 */
public class PlaceSearchActivity extends Activity {

    private RecyclerView mRecyclerView;
    private EditText mSearchEdit;
    private ImageButton mSearchBtn;

    private DaumApiService mDaumApiService;
    private PlaceSearchAdapter mAdapter;
    private String mSearchWord;
    private int mPageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search);

        mDaumApiService = ServiceGenerator.getDaumApiService();
        initLayout();
        initListener();
    }

    private void initLayout() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_place_search);
        mSearchEdit = (EditText) findViewById(R.id.et_activity_place_search);
        mSearchBtn = (ImageButton) findViewById(R.id.ibtn_activity_place_search);


        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initListener() {
        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    requestDaumSearch(true);
                    return true;
                }
                return false;
            }
        });
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDaumSearch(true);
            }
        });

        mAdapter = new PlaceSearchAdapter(this, new ArrayList<DaumSearchItem>());
        mAdapter.setOnPlaceSearchListener(new PlaceSearchAdapter.OnPlaceSearchListener() {
            @Override
            public void onItemClick(DaumSearchItem item) {
                Intent intent = new Intent();
                intent.putExtra("searchResult", item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void requestDaumSearch(boolean isNew) {
        mSearchWord = mSearchEdit.getText().toString();
        mPageNum = isNew ? 1 : ++mPageNum;

        final Call<DaumSearchChannel> daumSearch = mDaumApiService.daumSearch(
                Const.DAUM_API_KEY,
                mSearchWord,
                "37.514322572335935,127.06283102249932",
                "20000",
                "" + mPageNum
        );

        daumSearch.enqueue(new Callback<DaumSearchChannel>() {
            @Override
            public void onResponse(Call<DaumSearchChannel> call, Response<DaumSearchChannel> response) {
                DaumSearchChannel result = response.body();
                if (result == null) {
                    App.toast("데이터가 없습니다.");
                    return;
                }

                setSearchList(result.getChannel().getItem());
            }

            @Override
            public void onFailure(Call<DaumSearchChannel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setSearchList(ArrayList<DaumSearchItem> list) {
        if (list == null)
            return;

        mAdapter.setSearchList(list);
    }

}
