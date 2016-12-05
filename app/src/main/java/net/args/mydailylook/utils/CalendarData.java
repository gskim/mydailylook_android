package net.args.mydailylook.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Administrator on 2016-07-10.
 */
public class CalendarData {
    private Calendar mCalendar;
    private ArrayList<Integer> mYearList = new ArrayList<>();
    private ArrayList<Integer> mMonthList = new ArrayList<>();
    private ArrayList<Integer> mDayList = new ArrayList<>();

    public CalendarData(Context context) {
        mCalendar = Calendar.getInstance();
        setYearList();
        setMonthList();
        setDayList(context, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH));
    }

    public int getCurrentYearPosition() {
        return mYearList.indexOf(mCalendar.get(Calendar.YEAR));
    }

    public int getCurrentMonthPosition() {
        return mMonthList.indexOf(mCalendar.get(Calendar.MONTH) + 1);
    }

    public int getCurrentDayPosition() {
        return mDayList.indexOf(mCalendar.get(Calendar.DATE));
    }

    public int getSelectedMonth(int position) {
        return mMonthList.get(position);
    }

    private void setYearList() {
        mYearList.clear();
        int cYear = mCalendar.get(Calendar.YEAR);
        for (int i = cYear - 40; i < cYear + 30; i++) {
            mYearList.add(i);
        }
    }

    private void setMonthList() {
        mMonthList.clear();
        for (int i = 1; i <= 12; i++) {
            mMonthList.add(i);
        }
    }

    public void setDayList(Context context, int year, int month) {
        mDayList.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        DevLog.i(context, "LastDay >> " + lastDay);

        for (int i = 1; i <= lastDay; i++) {
            mDayList.add(i);
        }
    }

    public ArrayList<Integer> getYearList() {
        return mYearList;
    }

    public ArrayList<Integer> getMonthList() {
        return mMonthList;
    }

    public ArrayList<Integer> getDayList() {
        return mDayList;
    }

}
