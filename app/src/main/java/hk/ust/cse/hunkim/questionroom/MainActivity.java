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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.firebase.client.ValueEventListener;



import hk.ust.cse.hunkim.questionroom.db.DBHelper;
import hk.ust.cse.hunkim.questionroom.db.DBUtil;
import hk.ust.cse.hunkim.questionroom.question.Question;

public class MainActivity extends ListActivity {

    // TODO: change this to your own Firebase URL
    private static final String FIREBASE_URL = "https://fiery-heat-97.firebaseio.com/";

    private String roomName;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private QuestionListAdapter mChatListAdapter;
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
        //initialized once with an Android context.
        Firebase.setAndroidContext(this);
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

        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(FIREBASE_URL).child(roomName).child("questions");

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
        mChatListAdapter = new QuestionListAdapter(
                mFirebaseRef.orderByChild("order").limitToFirst(200),
                this, R.layout.question, roomName);
        listView.setAdapter(mChatListAdapter);

        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(MainActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        ListView listView = getListView();
        QuestionListAdapter temChatListAdapter = new QuestionListAdapter(
                mFirebaseRef.orderByChild("echo").limitToFirst(200),
                this, R.layout.question, roomName, StartTime, EndTime, Content);
        listView.setAdapter(temChatListAdapter);
        temChatListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Question question = new Question(input);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(question);
            inputText.setText("");
        }
    }

    public void updateEcho(String key) {
        if (dbutil.contains(key)) {
            Log.e("Dupkey", "Key is already in the DB!");
            return;
        }

        final Firebase echoRef = mFirebaseRef.child(key).child("echo");
        echoRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long echoValue = (Long) dataSnapshot.getValue();
                        Log.e("Echo update:", "" + echoValue);

                        echoRef.setValue(echoValue + 1);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                }
        );

        final Firebase orderRef = mFirebaseRef.child(key).child("order");
        orderRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long orderValue = (Long) dataSnapshot.getValue();
                        Log.e("Order update:", "" + orderValue);

                        orderRef.setValue(orderValue + 1);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                }
        );

        // Update SQLite DB
        dbutil.put(key);

    }

    public void updateDislikes(String key) {
        if (dbutil.contains(key)) {
            Log.e("Dupkey", "Key is already in the DB!");
            return;
        }

        final Firebase dislikesRef = mFirebaseRef.child(key).child("dislikes");
        dislikesRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long dislikesValue = (Long) dataSnapshot.getValue();
                        Log.e("Echo update:", "" + dislikesValue);

                        dislikesRef.setValue(dislikesValue + 1);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                }
        );

        final Firebase orderRef = mFirebaseRef.child(key).child("order");
        orderRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long orderValue = (Long) dataSnapshot.getValue();
                        Log.e("Order update:", "" + orderValue);

                        orderRef.setValue(orderValue - 1);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                }
        );

        // Update SQLite DB
        dbutil.put(key);

    }

    public void enterReply(String key) {
        Intent intent = new Intent(this, ReplyActivity.class);
        intent.putExtra("questionRef", FIREBASE_URL+roomName+"/questions/"+key);
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
        QuestionListAdapter temChatListAdapter = new QuestionListAdapter(
                mFirebaseRef.orderByChild("echo").limitToFirst(200),
                this, R.layout.question, roomName);
        listView.setAdapter(temChatListAdapter);
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



