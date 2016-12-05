package net.args.mydailylook.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-07-17.
 */
public class MainListModel extends BaseModel {
    private String id;
    @SerializedName("img_id")
    private ArrayList<String> imgId;
    @SerializedName("like_cnt")
    private String likeCnt;
    @SerializedName("reply_cnt")
    private String replyCnt;
    private String content;
    private String regdate;
    private String nickname;
    @SerializedName("place_name")
    private String placeName;
    @SerializedName("place_position")
    private String placePosition;
    @SerializedName("profile_img_id")
    private String profileImgId;
    private ArrayList<Reply> reply;
    private String userno;
    private String mine;
    private String like;

    public MainListModel() {
    }

//    public MainListModel(String id, String userName, String date, String content, String likeCnt, String replyCnt) {
//        this.id = id;
//        this.userName = userName;
//        this.date = date;
//        this.content = content;
//        this.likeCnt = likeCnt;
//        this.replyCnt = replyCnt;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getImgId() {
        return imgId;
    }

    public void setImgId(ArrayList<String> imgId) {
        this.imgId = imgId;
    }

    public int getTotalImgId() {
        return imgId == null ? 0 : imgId.size();
    }

    public String getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(String likeCnt) {
        this.likeCnt = likeCnt;
    }

    public String getReplyCnt() {
        return replyCnt == null ? "0" : replyCnt;
    }

    public void setReplyCnt(String replyCnt) {
        this.replyCnt = replyCnt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfilePhoto() {
        return profileImgId;
    }

    public void setProfilePhoto(String profileImgId) {
        this.profileImgId = profileImgId;
    }

    public ArrayList<Reply> getReply() {
        return reply;
    }

    public void setReply(ArrayList<Reply> reply) {
        this.reply = reply;
    }

    public int getTotalReply() {
        return reply == null ? 0 : reply.size();
    }

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public boolean getMine() {
        if (mine == null)
            mine = "";

        return mine.equalsIgnoreCase("y");
    }

    public void setMine(String mine) {
        this.mine = mine;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public boolean getIsLike() {
        return like == null ? false : like.equalsIgnoreCase("y");
    }

    public String getPlaceName() {
        return placeName == null ? "" : placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlacePosition() {
        return placePosition;
    }

    public void setPlacePosition(String placePosition) {
        this.placePosition = placePosition;
    }

}
