package net.args.mydailylook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016-10-02.
 */
public class SearchActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initLayout();
        initListener();
    }

    private void initLayout() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_activity_search);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        mSearchView = (TextView) findViewById(R.id.tv_activity_search);
        mSearchView.setText("#" + getIntent().getStringExtra("tag"));
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
