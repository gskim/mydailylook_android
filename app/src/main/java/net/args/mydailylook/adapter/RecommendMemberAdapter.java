package net.args.mydailylook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import net.args.mydailylook.App;
import net.args.mydailylook.R;
import net.args.mydailylook.common.Const;
import net.args.mydailylook.model.RecommendMember;

import java.util.ArrayList;

/**
 * Created by arseon on 2016-09-27.
 */
public class RecommendMemberAdapter extends RecyclerView.Adapter<RecommendMemberAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<RecommendMember> mList = new ArrayList<>();
    private OnRecommendListener mOnListener;

    public interface OnRecommendListener {
        void onFollow(String userId, boolean isChecked);
    }

    public RecommendMemberAdapter(Context context, ArrayList<RecommendMember> list) {
        mContext = context;
        mList = list;
    }

    public void setOnListener(OnRecommendListener onListener) {
        mOnListener = onListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_member_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mList == null || mList.isEmpty())
            return;

        RecommendMember listItem = mList.get(position);
        if (listItem == null)
            return;

        setListener(holder, position);
        holder.thumbView.setImageURI(Const.IMAGE_URL + "?id=" + listItem.getProfilePhoto());
        holder.nickNameView.setText(listItem.getUsername());
//        holder.descriptionView.setText(listItem.getDescription());
        holder.followCheckBox.setChecked(listItem.getFollow().equalsIgnoreCase("y"));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rootLayout;
        SimpleDraweeView thumbView;
        TextView nickNameView;
        TextView descriptionView;
        CheckBox followCheckBox;

        ViewHolder(View view) {
            super(view);
            rootLayout = (RelativeLayout) view.findViewById(R.id.rl_item_recommend_member_list);
            thumbView = (SimpleDraweeView) view.findViewById(R.id.sdv_item_recommend_member_list_thumb);
            nickNameView = (TextView) view.findViewById(R.id.tv_item_recommend_member_list_name);
            descriptionView = (TextView) view.findViewById(R.id.tv_item_recommend_member_list_description);
            followCheckBox = (CheckBox) view.findViewById(R.id.cb_item_recommend_member_list_follow);
            descriptionView.setVisibility(View.GONE);
        }
    }

    private void setListener(final ViewHolder holder, final int position) {
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.toast("Item Click >> " + position);
            }
        });

        holder.followCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.followCheckBox.setText(isChecked ? R.string.following : R.string.add_follow);
            }
        });

        holder.followCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mList.get(position).getId();
                if (mOnListener != null)
                    mOnListener.onFollow(userId, holder.followCheckBox.isChecked());
            }
        });
    }

    public void addList(ArrayList<RecommendMember> list) {
        if (list != null)
            mList.addAll(list);

        notifyDataSetChanged();
    }

    public void initList(ArrayList<RecommendMember> list) {
        if (mList != null)
            mList.clear();

        mList.addAll(list);
        notifyDataSetChanged();
    }

}
