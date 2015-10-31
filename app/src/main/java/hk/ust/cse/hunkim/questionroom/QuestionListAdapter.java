package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.text.Html;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.Query;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.lang.String;
import java.util.regex.Pattern;

import hk.ust.cse.hunkim.questionroom.db.DBUtil;
import hk.ust.cse.hunkim.questionroom.question.Question;

/**
 * @author greg
 * @since 6/21/13
 * <p/>
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class QuestionListAdapter extends FirebaseListAdapter<Question> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String roomName;
    MainActivity activity;

    private String StartTime;
    private String EndTime;
    private String Content;

    public QuestionListAdapter(Query ref, Activity activity, int layout, String roomName) {
        super(ref, Question.class, layout, activity);
        StartTime="";
        EndTime="";
        Content="";
        // Must be MainActivity
        assert (activity instanceof MainActivity);

        this.activity = (MainActivity) activity;
    }

    public QuestionListAdapter(Query ref, Activity activity, int layout, String roomName, String Starttime, String Endtime, String content) {
        super(ref, Question.class, layout, activity);
        StartTime=Starttime;
        EndTime=Endtime;
        Content=content;
        // Must be MainActivity
        assert (activity instanceof MainActivity);

        this.activity = (MainActivity) activity;
    }

    private long GetTimeLimit(String Time) {
        long currentTime= System.currentTimeMillis();
        long returnTime=0;
        if (Time.contains("Now")){
            returnTime=currentTime;
        } else if (Time.contains("1 hour ago")){
            returnTime=currentTime-3600*1000;
        } else if (Time.contains("2 hours ago")){
            returnTime=currentTime-2*3600*1000;
        } else if (Time.contains("1 day ago")){
            returnTime=currentTime-24*3600*1000;
        } else if (Time.contains("1 week ago")){
            returnTime=currentTime-7*24*3600*1000;
        } else if (Time.contains("30 days ago")){
            returnTime=currentTime- 2592000000L;
        } else if (Time.contains("365 days ago")){
            returnTime=currentTime-31536000000L;
        } else if (Time.contains("The Start")){
            returnTime=0L;
        }
        return returnTime;
    }

    public boolean search_valid(Question model){
        String wholeMsg=model.getHead()+model.getDesc();
        if (Content.equals("") || (wholeMsg+" ").contains(Content)) {
            if (StartTime.equals("") && EndTime.equals("")){
                return true;
            } else {
                if (StartTime.contains("All") || EndTime.contains("All")){
                    return true;
                }
                long BeginTime = GetTimeLimit(StartTime);
                long UntilTime = GetTimeLimit(EndTime);


                long QuestionTime = model.getTimestamp();
                if (QuestionTime <= UntilTime && QuestionTime >= BeginTime) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view     A view instance corresponding to the layout we passed to the constructor.
     * @param question An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, Question question1) {
        final Question question=question1;
        DBUtil dbUtil = activity.getDbutil();
        //int echo = question.getEcho();
        //echoButton.setText("" + echo);
        //echoButton.setTextColor(Color.BLUE);
        //((TextView) view.findViewById(R.id.echonum)).setText(question.getEcho());

        ImageButton echoButton = (ImageButton) view.findViewById(R.id.echo);
        ((TextView) view.findViewById(R.id.echonum)).setText(("" + question.getEcho()));
        echoButton.setTag(question.getKey()); // Set tag for button
        echoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity m = (MainActivity) view.getContext();
                        m.updateEcho((String) view.getTag());
                    }
                }
        );

        //int dislikes = question.getDislikes();
        //echoButton.setText("" + echo);
        //echoButton.setTextColor(Color.BLUE);
        //((TextView) view.findViewById(R.id.dislikenum)).setText(question.getDislikes());
        ImageButton dislikeButton = (ImageButton) view.findViewById(R.id.dislike);
        ((TextView) view.findViewById(R.id.dislikenum)).setText(("" + question.getDislikes()));
        dislikeButton.setTag(question.getKey()); // Set tag for button
        dislikeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity m = (MainActivity) view.getContext();
                        m.updateDislikes((String) view.getTag());
                    }
                }
        );

        String msgString = "";
        question.updateNewQuestion();
        if (question.isNewQuestion()) {
            msgString += "<font color=red>NEW </font>";
        }

        msgString += "<B>" + question.getHead() + "</B>" + question.getDesc();

        ((TextView) view.findViewById(R.id.head_desc)).setText(Html.fromHtml(msgString));

        TextView hashView = (TextView) view.findViewById(R.id.head_desc);
        SpannableString ss = new SpannableString(hashView.getText().toString());
        Pattern tagPattern = Pattern.compile("[#]+[A-Za-z0-9-_]+\\b");
        //Matcher tagMatcher = tagPattern.matcher(ss);

        String newActivityURL = "tag";
        Linkify.addLinks(hashView, tagPattern, newActivityURL);


        TimeDisplay timeDisplay = new TimeDisplay(question.getTimestamp());
        ((TextView) view.findViewById(R.id.timestamp)).setText(timeDisplay.getOutputTime());


        ImageButton replyButton = (ImageButton) view.findViewById(R.id.reply);
        ((TextView) view.findViewById(R.id.replynum)).setText("" + question.getNumOfReplies());
        replyButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               MainActivity m = (MainActivity) view.getContext();
               m.enterReply(question.getKey());
               //m.updateEcho((String) view.getTag());
               //notifyDataSetChanged();
           }
        });

        // check if we already clicked
        //boolean clickable = !dbUtil.contains(question.getKey());

        //echoButton.setClickable(clickable);
        //echoButton.setEnabled(clickable);
        //view.setClickable(clickable);

        // http://stackoverflow.com/questions/8743120/how-to-grey-out-a-button
        // grey out our button

        /*
        if (clickable) {
            echoButton.getBackground().setColorFilter(null);
        } else {
            echoButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }*/


        view.setTag(question.getKey());  // store key in the view
    }

    @Override
    protected void sortModels(List<Question> mModels) {
        Collections.sort(mModels);
    }

    @Override
    protected void setKey(String key, Question model) {
        model.setKey(key);
    }


}
