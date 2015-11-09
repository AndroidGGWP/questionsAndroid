package hk.ust.cse.hunkim.questionroom;

import hk.ust.cse.hunkim.questionroom.question.Question;
import hk.ust.cse.hunkim.questionroom.question.Reply;
import hk.ust.cse.hunkim.questionroom.APIService;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
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
    public static RESTfulAPI instance = new RESTfulAPI();
    private String baseURL = "http://52.74.132.232:5000/api/";
    public List<Question> questionList;
    public Retrofit retrofit;
    public APIService service;
    private Map<String, Question> idQuestionMap;
    private Question mDummyQuestion = new Question("");
    private List<Reply> mDummyReplies = new ArrayList<Reply>();

    private RESTfulAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(APIService.class);
    }

    public static RESTfulAPI getInstance(){
        return instance;
    }

    private RESTfulAPI(Map<String, String> query) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(APIService.class);
        setQuestionList(query);
    }

    public void setQuestionList(Map<String, String> query) {
        Call<List<Question>> call = service.getQuestionList(query);
        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Response<List<Question>> response, Retrofit retrofit) {
                List<Question> list = response.body();
                if (list != null) {
                    questionList = list;
                    for (final Question q : questionList) {
                        idQuestionMap.put(q.getKey(), q);
                        Call<List<Reply>> call = service.getReplyList(q.getKey());
                        call.enqueue(new Callback<List<Reply>>() {
                            @Override
                            public void onResponse(Response<List<Reply>> response, Retrofit retrofit) {
                                List<Reply> replies = response.body();
                                if (replies != null)
                                    q.setReplies(replies);
                                else
                                    q.setReplies(new ArrayList<Reply>());
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
                    }
                } else {
                    // error;
                    questionList = new ArrayList<Question>();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void addLike(Question question) {
        question.echo += 1;
        service.updateQuestion(question.getKey(), question).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Response<Question> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void addDislike(Question question) {
        question.dislikes += 1;
        service.updateQuestion(question.getKey(), question).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Response<Question> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    Holder<Question> getQuestion(String id) {
        final Holder<Question> holder = new Holder<>(mDummyQuestion);
        service.getQuestion(id).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Response<Question> response, Retrofit retrofit) {
                Question q = response.body();
                if(q != null)
                    holder.setValue(q);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        return holder;
    }

    public void saveQuesion(Question question) {
        Call<Question> call = service.saveQuestion(question);
        call.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Response<Question> response, Retrofit retrofit) {
                Question q = response.body();
                if (q != null)
                    question.setKey(q.getKey());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
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

    public Holder<List<Reply>> getReplies(String questionKey) {
        final Holder<List<Reply>> repliesHolder = new Holder<>(mDummyReplies);
        service.getReplyList(questionKey).enqueue(new Callback<List<Reply>>() {
            @Override
            public void onResponse(Response<List<Reply>> response, Retrofit retrofit) {
                List<Reply> replies = response.body();
                if(replies != null)
                    repliesHolder.setValue(replies);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        return repliesHolder;
    }

    public void saveReply(Question question, final Reply reply) {
        Call<Reply> call = service.saveReply(reply);
        call.enqueue(new Callback<Reply>() {
            @Override
            public void onResponse(Response<Reply> response, Retrofit retrofit) {
                Reply r = response.body();
                if(r != null)
                    reply.setKey(r.getKey());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
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
