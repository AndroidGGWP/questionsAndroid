package hk.ust.cse.hunkim.questionroom;

import java.util.List;
import java.util.Map;

import hk.ust.cse.hunkim.questionroom.question.Question;
import hk.ust.cse.hunkim.questionroom.question.Reply;
import hk.ust.cse.hunkim.questionroom.question.ResponseResult;
import hk.ust.cse.hunkim.questionroom.question.User;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by Teman on 11/6/2015.
 */
public interface APIService {
    @GET("posts")
    Call<List<Question>> getQuestionList(@QueryMap Map<String, String> params);

    @POST("posts")
    Call<Question> saveQuestion(@Body Question question);

    @GET("posts/{id}")
    Call<Question> getQuestion(@Path("id") String id);

    @PUT("posts/{id}")
    Call<Question> updateQuestion(@Path("id") String id, @Body Question question, @Query("username") String currentUsername);

    @DELETE("posts/{id}")
    Call<ResponseResult> deleteQuestion(@Path("id") String id, @Query("username") String currentUsername);

    @GET("replies")
    Call<List<Reply>> getReplyList(@QueryMap Map<String, String> query);

    @POST("replies")
    Call<Reply> saveReply(@Body Reply reply);

    @GET("replies/{id}")
    Call<Reply> getReply(@Path("id") String id);

    @PUT("replies/{id}")
    Call<Reply> updateReply(@Path("id") String id, @Body Reply reply, @Query("username") String currentUsername);

    @DELETE("replies/{id}")
    Call<ResponseResult> deleteReply(@Path("id") String id, @Query("username") String currentUsername);

    @POST("users")
    Call<ResponseResult> userAuth(@Body User user, @Query("option") String option);
}

