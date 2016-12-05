package net.args.mydailylook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import net.args.mydailylook.App;
import net.args.mydailylook.DetailActivity;
import net.args.mydailylook.R;
import net.args.mydailylook.common.Const;
import net.args.mydailylook.model.PersonalInfo;
import net.args.mydailylook.model.PostingInfo;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Administrator on 2016-10-02.
 */
public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private static final int TYPE_HEADER = Integer.MIN_VALUE;
    private static final int TYPE_ADAPTEE_OFFSET = 2;

    private Context mContext;
    private ArrayList<PostingInfo> mPostingInfoList = new ArrayList<>();
    private PersonalInfo mInfo;
    private OnInfoListener mListener;

    public interface OnInfoListener {
        void onProfileImgClick();

        void onSettingClick();
    }

    public void setOnInfoListener(OnInfoListener onInfoListener) {
        mListener = onInfoListener;
    }

    public InfoAdapter(Context context, PersonalInfo info) {
        mContext = context;
        mInfo = info;
    }

    public void setPersonalInfo(PersonalInfo info) {
        mInfo = info;
        notifyDataSetChanged();
    }

    public void initPostingList(ArrayList<PostingInfo> postingInfoList) {
        if (mPostingInfoList != null)
            mPostingInfoList.clear();

        mPostingInfoList.addAll(postingInfoList);
        notifyDataSetChanged();
    }

    public void setPostingList(ArrayList<PostingInfo> postingInfoList) {
        if (mPostingInfoList == null)
            mPostingInfoList = new ArrayList<>();

        mPostingInfoList.addAll(postingInfoList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_fragment_info, parent, false);
            ViewHolder viewHolder = new ViewHolder(v, viewType);

            try {
                int height = parent.getMeasuredHeight() / 2;
                viewHolder.rootLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        parent.getMeasuredWidth(), height));
            } catch (Exception e) {
            }

            return viewHolder;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_gallery, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, viewType);

        try {
            int width = parent.getMeasuredWidth() / 3; // - (space  * 2))
            viewHolder.cardView.setLayoutParams(new FrameLayout.LayoutParams(width, width));
        } catch (Exception e) {
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mInfo == null)
            return;

        setListener(holder, position);
        if (position == 0) {
            Glide.with(mContext).load(Const.PROFILE_IMAGE_URL + "?id=" + mInfo.getProfileImgId())
                    .bitmapTransform(new BlurTransformation(mContext, 40, 2))
                    .into(holder.blurView);
            holder.profileImgView.setImageURI(Const.PROFILE_IMAGE_URL + "?id=" + mInfo.getProfileImgId());

            holder.followCntView.setText(mInfo.getFollowCount());
            holder.followingCntView.setText(mInfo.getFollowingCount());
            holder.postingCntView.setText(mInfo.getPostCount());
            holder.settingBtn.setVisibility(mInfo.getMine() ? View.VISIBLE : View.GONE);
            return;
        }

        if (mPostingInfoList.isEmpty() || mPostingInfoList.size() == 0)
            return;

        Glide.with(mContext).load(Const.IMAGE_URL + "?id=" + mPostingInfoList.get(position - 1).getImgId())
                .into(holder.thumbView);
    }

    @Override
    public int getItemCount() {
        return mPostingInfoList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;

        return TYPE_ADAPTEE_OFFSET;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView profileImgView;
        ImageView blurView;
        TextView followCntView;
        TextView followingCntView;
        TextView postingCntView;
        LinearLayout rootLayout;
        ImageButton settingBtn;

        ImageView thumbView;
        CardView cardView;

        ViewHolder(View view, int viewType) {
            super(view);
            if (viewType == TYPE_HEADER) {
                profileImgView = (SimpleDraweeView) view.findViewById(R.id.sdv_header_fragment_info_thumb);
                blurView = (ImageView) view.findViewById(R.id.iv_header_fragment_info_blur_img);
                rootLayout = (LinearLayout) view.findViewById(R.id.ll_header_fragment_info);
                followCntView = (TextView) view.findViewById(R.id.tv_header_fragment_info_follower_cnt);
                followingCntView = (TextView) view.findViewById(R.id.tv_header_fragment_info_following_cnt);
                postingCntView = (TextView) view.findViewById(R.id.tv_header_fragment_info_posting_cnt);
                settingBtn = (ImageButton) view.findViewById(R.id.ibtn_header_fragment_info_setting);
            } else {
                thumbView = (ImageView) view.findViewById(R.id.iv_item_info_gallery);
                cardView = (CardView) view.findViewById(R.id.cv_item_info_gallery);
            }
        }
    }

    private void setListener(final ViewHolder holder, final int position) {
        if (holder.profileImgView != null) {
            holder.profileImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onProfileImgClick();
                }
            });
        }

        if (holder.followCntView != null) {
            holder.followCntView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    App.toast("Follow Cnt Click");
                }
            });
        }

        if (holder.followingCntView != null) {
            holder.followingCntView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    App.toast("Following Cnt Click");
                }
            });
        }

        if (holder.thumbView != null) {
            holder.thumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("postId", mPostingInfoList.get(position - 1).getId());
                    if (mInfo != null) {
                        intent.putExtra("nickName", mInfo.getUsername());
                        intent.putExtra("profilePhoto", mInfo.getProfileImgId());
                    }
                    mContext.startActivity(intent);
                }
            });
        }

        if (holder.settingBtn != null) {
            holder.settingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onSettingClick();
                }
            });
        }
    }


    public void setProfileImg(ViewHolder holder, String imgId) {
        if (holder == null || imgId == null)
            return;

        mInfo.setProfileImgId(imgId);
        notifyItemChanged(0);
    }

}
