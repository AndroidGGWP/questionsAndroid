package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hk.ust.cse.hunkim.questionroom.question.Question;
import hk.ust.cse.hunkim.questionroom.question.Reply;

/**
 * Created by Teman on 11/9/2015.
 */
public class QuestionActivity extends Activity {
    private RESTfulAPI api = RESTfulAPI.getInstance();
    private QuestionAdapter mQuestionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        assert (intent != null);

        Bundle extras = getIntent().getExtras();
        String roomName = extras.getString("roomName");
        Map<String, String> query = new HashMap<>();
        query.put("roomName", roomName);
        api.setQuestionList(query);
        mQuestionAdapter = new QuestionAdapter(getBaseContext(), api.questionList);
        /*
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mQuestionAdapter);

        //Reply to the question
        Button replyButton = (Button) findViewById(R.id.replyButton);
        final EditText replyText = (EditText) findViewById(R.id.replyInput);

        replyButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        String replyContent = replyText.getText().toString();
                        ReplyActivity r = (ReplyActivity) view.getContext();
                        Reply reply = new Reply(replyContent);
                        r.sendReply(question, reply);
                        replyText.setText("");
                    }
                }
        );
        */

    }

    public void sendReply(Question question, Reply reply){
        api.saveReply(question, reply);
        //mReplyAdapter.addReply(reply);
    }

}
