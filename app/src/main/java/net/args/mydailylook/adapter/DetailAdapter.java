package net.args.mydailylook.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import net.args.mydailylook.DetailActivity;
import net.args.mydailylook.R;
import net.args.mydailylook.common.Const;
import net.args.mydailylook.utils.DevLog;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-10-02.
 */
public class DetailAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> mList = new ArrayList<>();
    private LayoutInflater mInflater;
    private DetailActivity.OnPagerListener mListener;

    public DetailAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        mList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View view = mInflater.inflate(R.layout.viewpager_imageview, null);
        view.setTag(position);

        if (mList == null || mList.isEmpty())
            return view;

        final LinearLayout layout = (LinearLayout) view.findViewById(R.id.ll_viewpager_imageview);
        layout.setTag("1");

        final ImageView imgView = (ImageView) view.findViewById(R.id.iv_viewpager_imageview);
        Glide.with(mContext).load(Const.IMAGE_URL + "?id=" + mList.get(position))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        DevLog.d(mContext, "onResourceReady call ===========");
                        if (mListener != null)
                            mListener.onLoadFinish(position);
                        return false;
                    }
                })
                .into(imgView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setImageList(ArrayList<String> list) {
        if (!mList.isEmpty())
            mList.clear();

        mList.addAll(list);
        notifyDataSetChanged();
    }

}
