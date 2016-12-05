package net.args.mydailylook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Spinner;

import net.args.mydailylook.common.Preferences;
import net.args.mydailylook.model.BaseModel;
import net.args.mydailylook.model.ProfileModel;
import net.args.mydailylook.network.MyDailyLookService;
import net.args.mydailylook.network.ServiceGenerator;
import net.args.mydailylook.utils.CalendarData;
import net.args.mydailylook.utils.DevLog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016-07-02.
 */
public class ProfileInputActivity extends Activity implements View.OnClickListener {

    private ImageView mCloseBtn;
    private ImageView mDoneBtn;
    private EditText mNickNameEdit;
    private TextView mTitleView;

    private Spinner mHeightSpinner;
    private Spinner mWeightSpinner;
    private Spinner mFootSpinner;

    private Spinner mYearSpinner;
    private Spinner mMonthSpinner;
    private Spinner mDaySpinner;

    private RadioButton mMaleBtn;
    private RadioButton mFemaleBtn;

    private TextView mMinHeightView;
    private TextView mMaxHeightView;
    private RangeBar mHeightRangeBar;

    private TextView mMinWeightView;
    private TextView mMaxWeightView;
    private RangeBar mWeightRangeBar;

    private TextView mMinFootView;
    private TextView mMaxFootView;
    private RangeBar mFootRangBar;

    private MyDailyLookService mService;
    private CalendarData mCalendarData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_input);
        mService = ServiceGenerator.getService();
        mCalendarData = new CalendarData(this);
        initLayout();
        initSpinner();
        initListener();
    }

    private void initLayout() {
        mCloseBtn = (ImageView) findViewById(R.id.iv_activity_profile_input_close);
        mDoneBtn = (ImageView) findViewById(R.id.iv_activity_profile_input_done);
        mNickNameEdit = (EditText) findViewById(R.id.et_activity_profile_input_nickname);
        mTitleView = (TextView) findViewById(R.id.tv_activity_profile_input_title);

        String type = getIntent().getStringExtra("type");
        if (type != null && type.equals("edit"))
            mTitleView.setText(R.string.title_profile_edit);

        mYearSpinner = (Spinner) findViewById(R.id.spinner_activity_profile_input_year);
        mMonthSpinner = (Spinner) findViewById(R.id.spinner_activity_profile_input_month);
        mDaySpinner = (Spinner) findViewById(R.id.spinner_activity_profile_input_day);
        mHeightSpinner = (Spinner) findViewById(R.id.spinner_activity_profile_input_height);
        mWeightSpinner = (Spinner) findViewById(R.id.spinner_activity_profile_input_weight);
        mFootSpinner = (Spinner) findViewById(R.id.spinner_activity_profile_input_foot);

        mMaleBtn = (RadioButton) findViewById(R.id.rb_activity_profile_input_male);
        mMaleBtn.setChecked(true);
        mFemaleBtn = (RadioButton) findViewById(R.id.rb_activity_profile_input_female);

        mMinHeightView = (TextView) findViewById(R.id.tv_activity_profile_input_min_height);
        mMaxHeightView = (TextView) findViewById(R.id.tv_activity_profile_input_max_height);
        mHeightRangeBar = (RangeBar) findViewById(R.id.rangebar_activity_profile_input_height);

        mMinWeightView = (TextView) findViewById(R.id.tv_activity_profile_input_min_weight);
        mMaxWeightView = (TextView) findViewById(R.id.tv_activity_profile_input_max_weight);
        mWeightRangeBar = (RangeBar) findViewById(R.id.rangebar_activity_profile_input_weight);

        mMinFootView = (TextView) findViewById(R.id.tv_activity_profile_input_min_foot);
        mMaxFootView = (TextView) findViewById(R.id.tv_activity_profile_input_max_foot);
        mFootRangBar = (RangeBar) findViewById(R.id.rangebar_activity_profile_input_foot);
    }

    private void initListener() {
        mCloseBtn.setOnClickListener(this);
        mDoneBtn.setOnClickListener(this);

        mYearSpinner.setSelection(mCalendarData.getCurrentYearPosition());
        mMonthSpinner.setSelection(mCalendarData.getCurrentMonthPosition());
        mDaySpinner.setSelection(mCalendarData.getCurrentDayPosition());

        mYearSpinner.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner parent, View view, int position, long id) {
                DevLog.i(ProfileInputActivity.this, "Year Click >> " + mYearSpinner.getSelectedItem());
                return true;
            }
        });

        mMonthSpinner.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner parent, View view, int position, long id) {
                DevLog.i(ProfileInputActivity.this, "Month Click >> " + mCalendarData.getSelectedMonth(position));
                DevLog.i(ProfileInputActivity.this, "Month >> " + mMonthSpinner.getSelectedItem());

                mCalendarData.setDayList(ProfileInputActivity.this, (int) mYearSpinner.getSelectedItem(),
                        mCalendarData.getSelectedMonth(position) - 1);
                setDaySpinner();
                return true;
            }
        });

        mMaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFemaleBtn.setChecked(!mMaleBtn.isChecked());
            }
        });

        mFemaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaleBtn.setChecked(!mFemaleBtn.isChecked());
            }
        });

        mHeightRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                mMinHeightView.setText(leftPinValue);
                mMaxHeightView.setText(rightPinValue);
            }
        });

        mWeightRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                mMinWeightView.setText(leftPinValue);
                mMaxWeightView.setText(rightPinValue);
            }
        });

        mFootRangBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                mMinFootView.setText(leftPinValue);
                mMaxFootView.setText(rightPinValue);
            }
        });

        mHeightSpinner.setOnItemClickListener(onItemClickListener);
        mWeightSpinner.setOnItemClickListener(onItemClickListener);
        mFootSpinner.setOnItemClickListener(onItemClickListener);
    }

    private Spinner.OnItemClickListener onItemClickListener
            = new Spinner.OnItemClickListener() {
        @Override
        public boolean onItemClick(Spinner parent, View view, int position, long id) {
            return true;
        }
    };

    private void initSpinner() {
        // year
        ArrayList<Integer> yearList = mCalendarData.getYearList();
        Integer[] yearArr = yearList.toArray(new Integer[yearList.size()]);

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, yearArr);
        mYearSpinner.setAdapter(yearAdapter);

        // month
        ArrayList<Integer> monthList = mCalendarData.getMonthList();
        Integer[] monthArr = monthList.toArray(new Integer[monthList.size()]);

        ArrayAdapter<Integer> monthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, monthArr);
        mMonthSpinner.setAdapter(monthAdapter);

        // day
        ArrayList<Integer> dayList = mCalendarData.getDayList();
        Integer[] dayArr = dayList.toArray(new Integer[dayList.size()]);

        ArrayAdapter<Integer> dayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, dayArr);
        mDaySpinner.setAdapter(dayAdapter);

        // height
        String[] publicList = getResources().getStringArray(R.array.public_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, publicList);
        mHeightSpinner.setAdapter(adapter);
        mWeightSpinner.setAdapter(adapter);
        mFootSpinner.setAdapter(adapter);
    }

    private void setDaySpinner() {
        ArrayList<Integer> dayList = mCalendarData.getDayList();
        Integer[] dayArr = dayList.toArray(new Integer[dayList.size()]);

        ArrayAdapter<Integer> dayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, dayArr);
        mDaySpinner.setAdapter(dayAdapter);
        mDaySpinner.setSelection(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_activity_profile_input_close:
                break;
            case R.id.iv_activity_profile_input_done:
                requestProfileInput();
//                startMainActivity();
                break;
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void requestProfileInput() {
        String year = String.format("%04d", mYearSpinner.getSelectedItem());
        String month = String.format("%02d", mMonthSpinner.getSelectedItem());
        String day = String.format("%02d", mDaySpinner.getSelectedItem());

        ProfileModel profile = new ProfileModel();
        profile.setAccessToken(Preferences.getInstance(this).getAccessToken());
        profile.setType("new");
        profile.setNickname(mNickNameEdit.getText().toString());
        profile.setBirth(year + "-" + month + "-" + day);
        profile.setGender(mMaleBtn.isChecked() ? "m" : "w");
        profile.setHeightMax(mMaxHeightView.getText().toString());
        profile.setHeightMin(mMinHeightView.getText().toString());
        profile.setWeightMax(mMaxWeightView.getText().toString());
        profile.setWeightMin(mMinWeightView.getText().toString());
        profile.setFootMax(mMaxFootView.getText().toString());
        profile.setFootMin(mMinFootView.getText().toString());
        profile.setHeightPermission("" + (mHeightSpinner.getSelectedItemPosition() + 1));
        profile.setWeightPermission("" + (mWeightSpinner.getSelectedItemPosition() + 1));
        profile.setFootPermission("" + (mFootSpinner.getSelectedItemPosition() + 1));

        final Call<BaseModel> callProfile = mService.profile(profile);
        callProfile.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel model = response.body();
                App.toast("response code >> " + model.getCode());
                if (model.getCode().equals("1")) {
                    startMainActivity();
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
            }
        });
    }

    private void test_requestProfileInput() {
        ProfileModel profile = new ProfileModel();
        profile.setAccessToken(Preferences.getInstance(this).getAccessToken());
        profile.setType("new");
        profile.setNickname("냐냐");
        profile.setGender("w");
        profile.setHeightMax(mMaxHeightView.getText().toString());
        profile.setHeightMin(mMinHeightView.getText().toString());
        profile.setWeightMax(mMaxWeightView.getText().toString());
        profile.setWeightMin(mMinWeightView.getText().toString());
        profile.setFootMax(mMaxFootView.getText().toString());
        profile.setFootMin(mMinFootView.getText().toString());
        profile.setHeightPermission((String) mHeightSpinner.getTag());
        profile.setWeightPermission((String) mWeightSpinner.getTag());
        profile.setFootPermission((String) mFootSpinner.getTag());
    }

}
