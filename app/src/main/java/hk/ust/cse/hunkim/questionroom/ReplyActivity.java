package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import hk.ust.cse.hunkim.questionroom.question.Reply;

public class ReplyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        Intent intent = getIntent();
        assert (intent != null);

        Bundle extras = getIntent().getExtras();
        String questionURLString = extras.getString("questionRef");
        final Firebase mQuestionRef = new Firebase(questionURLString);


        Log.d("debug", questionURLString);


        //Get the question in this reply room
        final Firebase questionMessageRef = mQuestionRef.child("wholeMsg");
        final TextView question = (TextView) findViewById(R.id.question);

        //time for the question
        final TextView questionTime = (TextView) findViewById(R.id.questionTime);


        mQuestionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String questionTitle = (String) dataSnapshot.child("wholeMsg").getValue();
                question.setText(questionTitle);
                long questionTimestamp = (long) dataSnapshot.child("timestamp").getValue();
                TimeDisplay questionTimeDisplay = new TimeDisplay(questionTimestamp);
                questionTime.setText(questionTimeDisplay.getOutputTime());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //Get the replies to the question
        final Firebase mReplyRef = mQuestionRef.child("replies");

        mReplyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String[][] replyTexts = new String[((int) dataSnapshot.getChildrenCount()) - 1][2];
                Reply[] replyListDisplay = new Reply[((int) dataSnapshot.getChildrenCount()) - 1];
                ArrayList<HashMap> replyList = (ArrayList<HashMap>) dataSnapshot.getValue();
                for (int i = 0; i < (int) dataSnapshot.getChildrenCount() - 1; i++) {
                    TimeDisplay timeDisplay = new TimeDisplay((long) replyList.get(i + 1).get("timestamp"));
                    //replyTexts[i][0] = replyList.get(i + 1).get("content").toString();
                    //replyTexts[i][1] = timeDisplay.getOutputTime();
                    replyListDisplay[i] = new Reply(replyList.get(i + 1).get("content").toString(), timeDisplay.getOutputTime());
                }
                ListAdapter replyListAdapter = new ReplyAdapter(getBaseContext(), replyListDisplay);
                ListView replyListView = (ListView) findViewById(R.id.replyList);
                replyListView.setAdapter(replyListAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        //Reply to the question
        Button replyButton = (Button) findViewById(R.id.replyButton);
        final EditText replyText = (EditText) findViewById(R.id.replyInput);

        replyButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        String reply = replyText.getText().toString();
                        ReplyActivity r = (ReplyActivity) view.getContext();
                        r.sendReply(mQuestionRef, reply);
                        replyText.setText("");
                    }
                }
        );

    }

    public void sendReply(Firebase mQuestionRef, final String replyContent){

        if (!replyContent.equals("")) {
            updateNumOfReplies(mQuestionRef);

            final Firebase mRepliesRef = mQuestionRef.child("replies");
            mRepliesRef.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Reply> repliesValue = (List<Reply>) dataSnapshot.getValue();
                            repliesValue.add(new Reply(replyContent));
                            mRepliesRef.setValue(repliesValue);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    }
            );
        }
    }

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
}
