package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hk.ust.cse.hunkim.questionroom.databinding.ActivityReplyBinding;
import hk.ust.cse.hunkim.questionroom.question.Question;
import hk.ust.cse.hunkim.questionroom.question.Reply;

public class ReplyActivity extends Activity {

    private RESTfulAPI api = RESTfulAPI.getInstance();
    private ActivityReplyBinding mBinding;
    private ReplyAdapter mReplyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        Intent intent = getIntent();
        assert (intent != null);

        Bundle extras = getIntent().getExtras();
        String questionKey = extras.getString("questionKey");
        final Question question = api.getQuestion(questionKey).getValue();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reply);
        mBinding.setQuestion(question);

        List<Reply> replies = api.getReplies(questionKey).getValue();
        mReplyAdapter = new ReplyAdapter(getBaseContext(), replies);
        ListView replyListView = (ListView) findViewById(R.id.replyList);
        replyListView.setAdapter(mReplyAdapter);

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

    }

    public void sendReply(Question question, Reply reply){
        api.saveReply(question, reply);
        mReplyAdapter.addReply(reply);
    }

    /*
    public void updateNumOfReplies(Firebase mQuestionRef) {
        final Firebase numOfRepliesRef = mQuestionRef.child("numOfReplies");

        numOfRepliesRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long numOfRepliesValue = (Long) dataSnapshot.getValue();
                        numOfRepliesRef.setValue(numOfRepliesValue + 1);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                }
        );
    }
    */

}
