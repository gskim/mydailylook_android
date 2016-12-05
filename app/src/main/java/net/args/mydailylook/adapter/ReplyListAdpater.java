package net.args.mydailylook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import net.args.mydailylook.R;
import net.args.mydailylook.common.Const;
import net.args.mydailylook.model.Reply;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-09-25.
 */
public class ReplyListAdpater extends RecyclerView.Adapter<ReplyListAdpater.ViewHolder> {
    private Context mContext;
    private ArrayList<Reply> mList;

    public ReplyListAdpater(Context context, ArrayList<Reply> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mList == null || mList.isEmpty())
            return;

        Reply item = mList.get(position);
        if (item == null)
            return;

        holder.mThumbView.setImageURI(Const.PROFILE_IMAGE_URL + "?id=" + item.getProfileImgId());
        holder.mIdView.setText(item.getUsername());
        holder.mCommentView.setText(item.getContent());
        holder.mRegDateView.setText(item.getRegdate());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView mThumbView;
        TextView mIdView;
        TextView mCommentView;
        TextView mRegDateView;

        ViewHolder(View view) {
            super(view);
            mThumbView = (SimpleDraweeView) view.findViewById(R.id.sdv_item_reply_list_thumb);
            mIdView = (TextView) view.findViewById(R.id.tv_item_reply_list_user_id);
            mCommentView = (TextView) view.findViewById(R.id.tv_item_reply_list_user_comment);
            mRegDateView = (TextView) view.findViewById(R.id.tv_item_reply_list_regdate);
        }
    }

    public String getFirstItemId() {
        if (mList != null && mList.size() > 0)
            return mList.get(0).getId();

        return "" + 0;
    }

    public int getLastItemPosition() {
        return mList.size() - 1;
    }

    public void setReplyItem(Reply replyItem) {
        if (replyItem == null)
            return;

        if (mList == null)
            mList = new ArrayList<>();

        mList.add(replyItem);
        notifyItemInserted(mList.size() - 1);
    }

    public void setReplyList(boolean isNew, ArrayList<Reply> list) {
        if (list == null || list.isEmpty())
            return;

        if (mList == null)
            mList = new ArrayList<>();

        if (isNew)
            mList.clear();

        mList.addAll(0, list);
        notifyItemRangeInserted(0, list.size());
    }

}
