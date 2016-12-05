package net.args.mydailylook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.args.mydailylook.R;
import net.args.mydailylook.model.DaumSearchItem;
import net.args.mydailylook.utils.DevLog;

import java.util.ArrayList;

/**
 * Created by arseon on 2016-10-28.
 */
public class PlaceSearchAdapter extends RecyclerView.Adapter<PlaceSearchAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<DaumSearchItem> mList;
    private OnPlaceSearchListener mListener;

    public interface OnPlaceSearchListener {
        void onItemClick(DaumSearchItem item);
    }

    public void setOnPlaceSearchListener(OnPlaceSearchListener listener) {
        mListener = listener;
    }

    public PlaceSearchAdapter(Context context, ArrayList<DaumSearchItem> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public PlaceSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_search_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PlaceSearchAdapter.ViewHolder holder, int position) {
        DevLog.d(mContext, "onBindViewHolder() position >> " + position);
        if (mList == null || mList.isEmpty())
            return;

        DaumSearchItem item = mList.get(position);
        if (item == null)
            return;

        setListener(holder, position);
        holder.placeNameView.setText(item.getTitle());
        holder.placeAddrView.setText(item.getNewAddress());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rootLayout;
        TextView placeNameView;
        TextView placeAddrView;

        ViewHolder(View view) {
            super(view);

            rootLayout = (LinearLayout) view.findViewById(R.id.ll_item_place_search_list);
            placeNameView = (TextView) view.findViewById(R.id.tv_item_place_search_list_name);
            placeAddrView = (TextView) view.findViewById(R.id.tv_item_place_search_list_addr);
        }
    }

    private void setListener(ViewHolder holder, final int position) {
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(mList.get(position));
                }
            }
        });
    }

    public void setSearchList(ArrayList<DaumSearchItem> list) {
        if (mList == null)
            mList = new ArrayList<>();

        mList.addAll(list);
        notifyItemRangeInserted(mList.size() - list.size(), mList.size());
    }

}
