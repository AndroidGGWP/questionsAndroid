package hk.ust.cse.hunkim.questionroom;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import hk.ust.cse.hunkim.questionroom.question.Question;
import hk.ust.cse.hunkim.questionroom.question.Reply;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Teman on 11/6/2015.
 */
public class RESTfulAPI {
    private static RESTfulAPI instance = new RESTfulAPI();
    private String serverURL = "http://54.169.201.112";
    //private String serverURL = "http://54.254.251.203";
    private Retrofit retrofit;
    private APIService service;
    private Socket mSocket;

    public Socket getSocket() {
        return mSocket;
    }

    private RESTfulAPI() {
        String baseURL = serverURL + ":5000/api/";
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(APIService.class);
        try {
            mSocket = IO.socket(serverURL + ":3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static RESTfulAPI getInstance(){
        return instance;
    }

    public Call<List<Question>> getQuestionList(Map<String, String> query) {
        return service.getQuestionList(query);
    }

    public void addLike(Question question) {

    }

    public void addDislike(Question question) {

    }

    Call<Question> getQuestion(String id) {
        return service.getQuestion(id);
    }

    public Call<Question> saveQuesion(Question question) {
        return service.saveQuestion(question);
    }

    public Call<Question> updateQuestion(Question question) {
        return service.updateQuestion(question.getKey(), question);
    }

    public void deleteQuestion(Question question) {
        service.deleteQuestion(question.getKey());
    }

    public Call<List<Reply>> getReplies(String questionKey) {
        return service.getReplyList(questionKey);
    }

    public Call<Reply> saveReply(Reply reply) {
        return service.saveReply(reply);
    }

    public Call<Reply> updateReply(Reply reply) {
        return service.updateReply(reply.getKey(), reply);
    }

    public void deleteReply(Reply reply) {
        service.deleteReply(reply.getKey());
    }

}
