package net.args.mydailylook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import net.args.mydailylook.DetailActivity;
import net.args.mydailylook.InfoActivity;
import net.args.mydailylook.R;
import net.args.mydailylook.ReplyActivity;
import net.args.mydailylook.SearchActivity;
import net.args.mydailylook.common.Const;
import net.args.mydailylook.fragment.HomeFragment;
import net.args.mydailylook.model.MainListModel;
import net.args.mydailylook.model.Reply;
import net.args.mydailylook.utils.DevLog;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-07-17.
 */
public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<MainListModel> mMainList = new ArrayList<>();
    private boolean mIsTag;
    private static HomeFragment.OnMainListListener mListener;

    public MainListAdapter(Context context, ArrayList<MainListModel> list) {
        mContext = context;
        mMainList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnMainListListener(HomeFragment.OnMainListListener listener) {
        mListener = listener;
    }

    @Override
    public MainListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        DevLog.d(Const.TAG, "onCreateViewHolder() =============");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (mMainList == null || mMainList.isEmpty())
            return;

        MainListModel listItem = mMainList.get(position);
        if (listItem == null)
            return;

        setListener(holder, position);
        holder.thumbView.setImageURI(Const.PROFILE_IMAGE_URL + "?id=" + listItem.getProfilePhoto());

        if (listItem.getImgId() != null) {
            holder.contentImgView.setImageURI(Const.IMAGE_URL + "?id=" + listItem.getImgId().get(0));
        }
        int imgLeng = listItem.getImgId().size();
        holder.moreImgView.setVisibility(imgLeng > 1 ? View.VISIBLE : View.INVISIBLE);
        holder.contentView.setText(listItem.getContent());
        holder.nickNameView.setText(listItem.getNickname());
        holder.dateView.setText(listItem.getRegdate());
        holder.commentView.setText(listItem.getReplyCnt());
        holder.likeCheckBox.setText(listItem.getLikeCnt());
        holder.likeCheckBox.setChecked(listItem.getIsLike());

        String location = listItem.getPlaceName();
        if (location.trim().length() > 0) {
            holder.locationView.setText(location);
            holder.locationView.setVisibility(View.VISIBLE);
        } else {
            holder.locationView.setVisibility(View.GONE);
        }

        if (holder.commentLayout.getChildCount() > 0)
            holder.commentLayout.removeAllViews();

        ArrayList<Reply> replyList = listItem.getReply();
        if (replyList == null || replyList.isEmpty())
            return;

        for (int i = 0; i < replyList.size(); i++) {
            LinearLayout commentLayout = (LinearLayout) mInflater.inflate(R.layout.item_main_list_comment, null);
            TextView idView = (TextView) commentLayout.findViewById(R.id.tv_list_main_list_comment_user_id);
            TextView commentView = (TextView) commentLayout.findViewById(R.id.tv_list_main_list_comment_user_comment);
            idView.setText(replyList.get(i).getUsername());
            commentView.setText(replyList.get(i).getContent());
            holder.commentLayout.addView(commentLayout);
        }
        DevLog.d(Const.TAG, "onBindViewHolder() position =============" + position);
    }

    @Override
    public int getItemCount() {
        return mMainList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        SimpleDraweeView thumbView;
        SimpleDraweeView contentImgView;
        TextView nickNameView;
        TextView locationView;
        TextView dateView;
        TextView contentView;
        CheckBox likeCheckBox;
        TextView commentView;
        //        Spinner moreSpinner;
        ImageView moreView;
        ImageView moreImgView;
        RelativeLayout itemLayout;
        LinearLayout commentLayout;

        ViewHolder(View view) {
            super(view);
            itemLayout = (RelativeLayout) view.findViewById(R.id.rl_item_main_list);
            commentLayout = (LinearLayout) view.findViewById(R.id.ll_item_main_list_comment);
            thumbView = (SimpleDraweeView) view.findViewById(R.id.sdv_item_main_list_thumb);
            contentImgView = (SimpleDraweeView) view.findViewById(R.id.sdv_item_main_list_image);
            nickNameView = (TextView) view.findViewById(R.id.tv_item_main_list_nickname);
            locationView = (TextView) view.findViewById(R.id.tv_item_main_list_location);
            dateView = (TextView) view.findViewById(R.id.tv_item_main_list_date);
            contentView = (TextView) view.findViewById(R.id.tv_item_main_list_content);
            likeCheckBox = (CheckBox) view.findViewById(R.id.cb_item_main_list_like);
            commentView = (TextView) view.findViewById(R.id.tv_item_main_list_comment);
            moreView = (ImageView) view.findViewById(R.id.iv_item_main_list_more);
            moreImgView = (ImageView) view.findViewById(R.id.iv_item_main_list_more_img);
//            moreSpinner = (Spinner) view.findViewById(R.id.spinner_item_main_list_more);
        }
    }

    private void setListener(final ViewHolder holder, final int position) {
//        holder.likeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (listener != null) {
//                    DevLog.w(Const.TAG, "[MainListAdapter] setChecked >> " + isChecked);
//                    int likeCnt = Integer.valueOf(buttonView.getText().toString());
//                    likeCnt = isChecked ? likeCnt + 1 : likeCnt - 1;
//                    holder.likeCheckBox.setText(likeCnt);
//                    listener.onLikeChecked(position, isChecked);
//                }
//            }
//        });

        HashTagHelper hashTagHelper = HashTagHelper.Creator.create(mContext.getResources().getColor(R.color.colorPrimary),
                new HashTagHelper.OnHashTagClickListener() {
                    @Override
                    public void onHashTagClicked(String hashTag) {
                        mIsTag = true;
                        Intent intent = new Intent(mContext, SearchActivity.class);
                        intent.putExtra("tag", hashTag);
                        mContext.startActivity(intent);
                    }
                });
        hashTagHelper.handle(holder.contentView);

        holder.thumbView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPersonalInfoActivity(position);
            }
        });

        holder.contentImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailActivity(position);
            }
        });

        holder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsTag) {
                    mIsTag = false;
                    return;
                }
                startDetailActivity(position);
            }
        });

        holder.likeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && v instanceof CheckBox) {
                    boolean isChecked = holder.likeCheckBox.isChecked();
                    DevLog.w(Const.TAG, "[MainListAdapter] isChecked >> " + isChecked);
                    int likeCnt = Integer.valueOf(holder.likeCheckBox.getText().toString());
                    likeCnt = isChecked ? likeCnt + 1 : likeCnt - 1;
                    holder.likeCheckBox.setText("" + likeCnt);
                    mListener.onLikeChecked(mMainList.get(position).getId(), isChecked);
                }
            }
        });

        holder.commentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReplyActivity.class);
                intent.putExtra("postId", mMainList.get(position).getId());
                mContext.startActivity(intent);
            }
        });

        holder.moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onMore(position);
            }
        });
    }

    private void startPersonalInfoActivity(int position) {
        Intent intent = new Intent(mContext, InfoActivity.class);
        intent.putExtra("userId", mMainList.get(position).getUserno());
        mContext.startActivity(intent);
    }

    private void startDetailActivity(int position) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("postId", mMainList.get(position).getId());
        intent.putExtra("content", mMainList.get(position).getContent());
        intent.putExtra("nickName", mMainList.get(position).getNickname());
        intent.putExtra("profilePhoto", mMainList.get(position).getProfilePhoto());

        mContext.startActivity(intent);
    }

    public String getLastId() {
        DevLog.w(Const.TAG, "[MainListAdapter] getLastId() >> " + mMainList.get(mMainList.size() - 1).getId());
        return mMainList.get(mMainList.size() - 1).getId();
    }

    public void setMainList(ArrayList<MainListModel> list) {
        mMainList.addAll(list);
        notifyDataSetChanged();
    }

}
