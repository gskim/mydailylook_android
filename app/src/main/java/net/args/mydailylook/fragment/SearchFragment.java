package net.args.mydailylook.fragment;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import net.args.mydailylook.R;

/**
 * Created by Administrator on 2016-07-10.
 */
public class SearchFragment extends Fragment {
    private View mView;
    private ColorStateList mColorStateList;
    private RadioButton mTagRadioBtn;
    private RadioButton mMemberRadioBtn;
    private TextView mSearchTypeView;
    private EditText mSearchEdit;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);

        initData();
        initLayout();
        initListener();

        return mView;
    }

    private View findViewById(int resId) {
        return mView.findViewById(resId);
    }

    private void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mColorStateList = getResources().getColorStateList(R.color.selector_search_icon, null);
        } else {
            mColorStateList = getResources().getColorStateList(R.color.selector_search_icon);
        }
    }

    private void initLayout() {
        mTagRadioBtn = (RadioButton) findViewById(R.id.rb_fragment_search_tag);
        mMemberRadioBtn = (RadioButton) findViewById(R.id.rb_fragment_search_member);
        mSearchEdit = (EditText) findViewById(R.id.et_fragment_search);
        mSearchTypeView = (TextView) findViewById(R.id.tv_fragment_search_type);
        mSearchTypeView.setText(mTagRadioBtn.isChecked() ? R.string.search_tag : R.string.search_member);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_fragment_search);
    }

    private void initListener() {
        mTagRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagRadioBtn.setChecked(mTagRadioBtn.isChecked());
                mMemberRadioBtn.setChecked(!mTagRadioBtn.isChecked());
                mSearchTypeView.setText(mTagRadioBtn.isChecked() ? R.string.search_tag : R.string.search_member);
            }
        });

        mTagRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTagRadioBtn.setBackgroundTintList(mColorStateList);
            }
        });

        mMemberRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMemberRadioBtn.setChecked(mMemberRadioBtn.isChecked());
                mTagRadioBtn.setChecked(!mMemberRadioBtn.isChecked());
                mSearchTypeView.setText(mTagRadioBtn.isChecked() ? R.string.search_tag : R.string.search_member);
            }
        });

        mMemberRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMemberRadioBtn.setBackgroundTintList(mColorStateList);
            }
        });
    }

}
