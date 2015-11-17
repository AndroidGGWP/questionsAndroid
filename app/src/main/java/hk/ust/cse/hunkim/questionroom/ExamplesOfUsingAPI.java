package hk.ust.cse.hunkim.questionroom;

import android.util.ArrayMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import hk.ust.cse.hunkim.questionroom.question.Question;
import hk.ust.cse.hunkim.questionroom.question.ResponseResult;
import hk.ust.cse.hunkim.questionroom.question.User;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Teman on 11/17/2015.
 */
public class ExamplesOfUsingAPI {
    private APIService service = RESTfulAPI.getInstance().getService();

    public void signup(String username, String password) {
        User user = new User(username, password);
        service.userAuth(user, "signup").enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Response<ResponseResult> response, Retrofit retrofit) {
                ResponseResult result = response.body();
                if(result != null && result.getResult() == true) {
                    // sign up success
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
    public void login(String username, String password) {
        User user = new User(username, password);
        service.userAuth(user, "login").enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Response<ResponseResult> response, Retrofit retrofit) {
                if(response.body() != null && response.body().getResult() == true) {
                    // login success
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void getAllQuestionsOfAUser(String username) {
        Map<String, String> query = new ArrayMap<>();
        query.put("username", username);
        query.put("sortBy", "timestamp");
        query.put("order", "1"); // 1 for ascending order
        service.getQuestionList(query).enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Response<List<Question>> response, Retrofit retrofit) {
                List<Question> questions = response.body();
                if(questions != null) {
                    // retrieved user's questions
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void updateQuestion(String questionKey, Question modifiedQuestion, User loggedInUser) {
        service.updateQuestion(questionKey, modifiedQuestion, loggedInUser.getUsername()).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Response<Question> response, Retrofit retrofit) {
                //response.body(); // modified question (if not null)
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void deleteQuestion(final String questionKey, User loggedInUser) {
        service.deleteQuestion(questionKey, loggedInUser.getUsername()).enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Response<ResponseResult> response, Retrofit retrofit) {
                ResponseResult result = response.body();
                if(result != null && result.getResult() == true) {
                    // deletion success
                    // inform other clients to delete the question
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("roomName", "theRoomNameOfTheDeletedQuestion");
                        jsonObject.put("id", questionKey);
                    } catch (JSONException e) {}
                    RESTfulAPI.getInstance().getSocket().emit("del post", jsonObject);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    // updating and deleting reply are similar
}
