package net.args.mydailylook.network;

import net.args.mydailylook.model.BaseModel;
import net.args.mydailylook.model.GateModel;
import net.args.mydailylook.model.JoinModel;
import net.args.mydailylook.model.LoginModel;
import net.args.mydailylook.model.MainList;
import net.args.mydailylook.model.PersonalInfo;
import net.args.mydailylook.model.PostingList;
import net.args.mydailylook.model.ProfileModel;
import net.args.mydailylook.model.Recommend;
import net.args.mydailylook.model.Reply;
import net.args.mydailylook.model.ReplyList;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by Administrator on 2016-08-21.
 */
public interface MyDailyLookService {
    @FormUrlEncoded
    @POST("common/gate")
    Call<GateModel> gate(@Field("deviceId") String deviceId,
                         @Field("gcmId") String gcmId,
                         @Field("appVersion") String appVersion,
                         @Field("osType") String osType,
                         @Field("osVersion") String osVersion);

    @FormUrlEncoded
    @POST("user/join")
    Call<JoinModel> join(@Field("email") String id,
                         @Field("password") String pw,
                         @Field("loginType") String loginType,
                         @Field("deviceId") String deviceId);

    @FormUrlEncoded
    @POST("user/login")
    Call<LoginModel> login(@Field("email") String id,
                           @Field("password") String pw,
                           @Field("loginType") String loginType,
                           @Field("deviceId") String deviceId);

    @FormUrlEncoded
    @POST("user/auto-login")
    Call<LoginModel> autoLogin(@Field("accessToken") String accessToken,
                               @Field("deviceId") String deviceId);

    @FormUrlEncoded
    @POST("user/logout")
    Call<BaseModel> logout(@Field("accessToken") String accessToken,
                           @Field("deviceId") String deviceId);

    @FormUrlEncoded
    @POST("user/email-send")
    Call<BaseModel> sendEmail(@Field("accessToken") String accessToken);

    @POST("user/profile")
    Call<BaseModel> profile(@Body ProfileModel profile);

    @FormUrlEncoded
    @POST("user/email-check")
    Call<BaseModel> checkEmail(@Field("accessToken") String accessToken);

    @FormUrlEncoded
    @POST("post/list")
    Call<MainList> postList(@Field("accessToken") String accessToken,
                            @Field("deviceId") String deviceId,
                            @Field("lastId") String lastId);

    @Multipart
    @POST("post/posting")
    Call<ResponseBody> posting(@PartMap Map<String, RequestBody> map,
                               @Part MultipartBody.Part file1,
                               @Part MultipartBody.Part file2,
                               @Part MultipartBody.Part file3,
                               @Part MultipartBody.Part file4);

    @FormUrlEncoded
    @POST("post/detail")
    Call<MainList> detail(@Field("accessToken") String accessToken,
                          @Field("postId") String postId,
                          @Field("deviceId") String deviceId);

    @FormUrlEncoded
    @POST("post/like")
    Call<BaseModel> like(@Field("accessToken") String accessToken,
                         @Field("deviceId") String deviceId,
                         @Field("postId") String postId,
                         @Field("like") String likeYn);

    @FormUrlEncoded
    @POST("post/reply")
    Call<Reply> reply(@Field("accessToken") String accessToken,
                      @Field("deviceId") String deviceId,
                      @Field("postId") String postId,
                      @Field("content") String content);

    @FormUrlEncoded
    @POST("post/replyitem")
    Call<Reply> replyitem(@Field("accessToken") String accessToken,
                          @Field("deviceId") String deviceId,
                          @Field("replyId") String replyId);


    @FormUrlEncoded
    @POST("post/replylist")
    Call<ReplyList> replyList(@Field("accessToken") String accessToken,
                              @Field("deviceId") String deviceId,
                              @Field("postId") String postId,
                              @Field("lastId") String lastId);

    @FormUrlEncoded
    @POST("user/recommend")
    Call<Recommend> recommend(@Field("accessToken") String accessToken,
                              @Field("deviceId") String deviceId,
                              @Field("pageId") String pageId);

    @FormUrlEncoded
    @POST("user/follow")
    Call<BaseModel> follow(@Field("accessToken") String accessToken,
                           @Field("deviceId") String deviceId,
                           @Field("userId") String userId,
                           @Field("follow") String follow);

    @FormUrlEncoded
    @POST("user/info")
    Call<PersonalInfo> info(@Field("accessToken") String accessToken,
                            @Field("deviceId") String deviceId,
                            @Field("userId") String userId);

    @FormUrlEncoded
    @POST("user/posting-list")
    Call<PostingList> postingList(@Field("accessToken") String accessToken,
                                  @Field("deviceId") String deviceId,
                                  @Field("userId") String userId);

    @Multipart
    @POST("user/profile-image-update")
    Call<ResponseBody> profileImg(@PartMap Map<String, RequestBody> map,
                                  @Part MultipartBody.Part file1);

}
