package hk.ust.cse.hunkim.questionroom;


import android.app.ListActivity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import android.view.View;
import android.view.inputmethod.EditorInfo;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hk.ust.cse.hunkim.questionroom.db.DBHelper;
import hk.ust.cse.hunkim.questionroom.db.DBUtil;
import hk.ust.cse.hunkim.questionroom.question.Question;

public class MainActivity extends ListActivity {

    private String roomName;
    private QuestionAdapter mQuestionAdapter;
    private RESTfulAPI mAPI = RESTfulAPI.getInstance();
    private String StartTime;
    private String EndTime;
    private String Content;

    private DBUtil dbutil;

    public DBUtil getDbutil() {
        return dbutil;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartTime = "";
        EndTime = "";
        Content = "";
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        assert (intent != null);

        // Make it a bit more reliable
        roomName = intent.getStringExtra(JoinActivity.ROOM_NAME);
        if (roomName == null || roomName.length() == 0) {
            roomName = "all";
        }

        setTitle("Room name: " + roomName);

        Map<String, String> query = new HashMap<>();
        query.put("roomName", roomName);
        mAPI.setQuestionList(query);
        List<Question> questions = mAPI.questionList;

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        // get the DB Helper
        DBHelper mDbHelper = new DBHelper(this);
        dbutil = new DBUtil(mDbHelper);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();
        // Tell our list adapter that we only want 200 messages at a time
        Map<String, String> query = new HashMap<>();
        query.put("sortBy", "order");
        query.put("limit", "200");
        mAPI.setQuestionList(query);
        mQuestionAdapter = new QuestionAdapter(getBaseContext(), mAPI.questionList);
        listView.setAdapter(mQuestionAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        ListView listView = getListView();
        Map<String, String> query = new HashMap<>();
        query.put("sortBy", "echo");
        query.put("limit", "200");
        mAPI.setQuestionList(query);
        QuestionAdapter temQuestionAdapter = new QuestionAdapter(getBaseContext(), mAPI.questionList);
        listView.setAdapter(temQuestionAdapter);
        temQuestionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Question question = new Question(input);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mAPI.saveQuesion(question);
            inputText.setText("");
        }
    }

    public void updateEcho(String key) {
        if (dbutil.contains(key)) {
            Log.e("Dupkey", "Key is already in the DB!");
            return;
        }
        // todo
        //api.addLike();

        // Update SQLite DB
        dbutil.put(key);

    }

    public void updateDislikes(String key) {
        if (dbutil.contains(key)) {
            Log.e("Dupkey", "Key is already in the DB!");
            return;
        }

        // todo
        // api.addDislikes();

        // Update SQLite DB
        dbutil.put(key);

    }

    public void enterReply(String key) {
        Intent intent = new Intent(this, ReplyActivity.class);
        intent.putExtra("questionKey", key);
        startActivity(intent);
    }

    public void Close(View view) {
        finish();
    }

    public void Reset_Search(View view){
        StartTime="";
        EndTime="";
        Content="";
        ListView listView = getListView();
        Map<String, String> query = new HashMap<>();
        query.put("sortBy", "echo");
        query.put("limit", "200");
        mAPI.setQuestionList(query);
        QuestionAdapter temQuestionAdapter = new QuestionAdapter(getBaseContext(), mAPI.questionList);
        listView.setAdapter(temQuestionAdapter);
    }



    public void Search(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("Room Name", roomName);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String newStartTime = data.getExtras().getString("StartTime");
            String newEndTime = data.getExtras().getString("EndTime");
            String newContent = data.getExtras().getString("Content");
            StartTime = newStartTime;
            EndTime = newEndTime;
            Content = newContent;
        }
    }
    @Override
    public void startActivity(Intent intent) {
        if (TextUtils.equals(intent.getAction(), Intent.ACTION_VIEW)) {
            //Intent i = new Intent(this,MainActivity.class);
            Intent i = new Intent(this,SearchActivity.class);
            Uri uri = intent.getData();
            //strip off hashtag from the URI
            String tag=uri.toString();
           /* System.out.println(tag.substring(3));
            i.putExtra("StartTime", "The Start");
            i.putExtra("EndTime", "Now");
            i.putExtra("Content", "#test");
            i.putExtra("Room Name", roomName);


            //MainActivity.this.setResult(RESULT_OK, i);
            startActivity(i);*/
            System.out.println(tag.substring(3).length());
            i.putExtra("hash",tag.substring(3)+ " ");
            intent.putExtra("Room Name", roomName);
            startActivityForResult(i, 1);
            //startActivity(i);
        }
        else {
            super.startActivity(intent);
        }
    }
};



