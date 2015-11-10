package hk.ust.cse.hunkim.questionroom;

import android.util.Log;

import hk.ust.cse.hunkim.questionroom.question.Question;
import hk.ust.cse.hunkim.questionroom.question.Reply;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    private String baseURL = "http://52.74.132.232:5000/api/";
    //public List<Question> questionList;
    private Retrofit retrofit;
    private APIService service;
    private Map<String, Question> idQuestionMap;

    private RESTfulAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(APIService.class);
        idQuestionMap = new HashMap<>();
    }

    public static RESTfulAPI getInstance(){
        return instance;
    }

    public List<Question> getQuestionList(Map<String, String> query) {
        try {
            List<Question> questions = service.getQuestionList(query).execute().body();
            for(Question q : questions) {
                idQuestionMap.put(q.getKey(), q);
            }
            return questions;
        }catch (IOException e) {
            // error handling
            return new ArrayList<Question>();
        }
    }

    public void addLike(Question question) {

    }

    public void addDislike(Question question) {

    }

    Question getQuestion(String id) {
        try{
            return service.getQuestion(id).execute().body();
        }catch (IOException e){
            return new Question("");
        }
    }

    public Question saveQuesion(Question question) {
        try{
            return service.saveQuestion(question).execute().body();
        }catch (IOException e){
            return new Question("");
        }
    }

    public void updateQuestion(Question question) {
        Call<Question> call = service.updateQuestion(question.getKey(), question);
        call.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Response<Question> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void deleteQuestion(Question question) {
        service.deleteQuestion(question.getKey());
    }

    public List<Reply> getReplies(String questionKey) {
        try{
            return service.getReplyList(questionKey).execute().body();
        }catch (IOException e){
            return new ArrayList<>();
        }
    }

    public Reply saveReply(Reply reply) {
        try{
            return service.saveReply(reply).execute().body();
        }catch (IOException e){
            return new Reply("");
        }
    }

    public void updateReply(Reply reply) {
        Call<Reply> call = service.updateReply(reply.getKey(), reply);
        call.enqueue(new Callback<Reply>() {
            @Override
            public void onResponse(Response<Reply> response, Retrofit retrofitf) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void deleteReply(Reply reply) {
        service.deleteReply(reply.getKey());
    }

}
