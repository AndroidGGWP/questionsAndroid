package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.Html;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import hk.ust.cse.hunkim.questionroom.databinding.QuestionBinding;
import hk.ust.cse.hunkim.questionroom.db.DBUtil;
import hk.ust.cse.hunkim.questionroom.question.Question;
import hk.ust.cse.hunkim.questionroom.question.Reply;

/**
 * Created by Teman on 11/9/2015.
 */
public class QuestionAdapter extends ArrayAdapter<Question> {
    private RESTfulAPI mAPI = RESTfulAPI.getInstance();
    private List<Question> mQuestionList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String StartTime;
    private String EndTime;
    private String Content;

    public QuestionAdapter(Context context, List<Question> questions) {
        super(context, 0, questions);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mQuestionList = questions;
        StartTime="";
        EndTime="";
        Content="";
    }

    public QuestionAdapter(Context context, List<Question> questions, String startTime, String endTime, String content) {
        super(context, 0, questions);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mQuestionList = questions;
        StartTime = startTime;
        EndTime = endTime;
        Content = content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionBinding binding;
        if(convertView == null)
            binding = DataBindingUtil.inflate(mInflater, R.layout.question, parent, false);
        else
            binding = DataBindingUtil.getBinding(convertView);
        final Question question = getItem(position);
        binding.setQuestion(question);
        convertView = binding.getRoot();

        // Display question
        String msgString = "";
        question.updateNewQuestion();
        if (question.isNewQuestion()) {
            msgString += "<font color=red>NEW </font>";
        }
        msgString += "<B>" + question.getHead() + "</B>" + question.getDesc();
        ((TextView) convertView.findViewById(R.id.head_desc)).setText(Html.fromHtml(msgString));

        // Like button
        ImageButton echoButton = (ImageButton) convertView.findViewById(R.id.questionEchoButton);
        echoButton.setTag(question.getKey()); // Set tag for button
        echoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        question.setEcho(question.getEcho() + 1);
                    }
                }
        );

        // Dislike button
        ImageButton dislikeButton = (ImageButton) convertView.findViewById(R.id.questionDislikeButton);
        dislikeButton.setTag(question.getKey()); // Set tag for button
        dislikeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        question.setDislikes(question.getDislikes() + 1);
                    }
                }
        );

        // reply button
        ImageButton replyButton = (ImageButton) convertView.findViewById(R.id.questionReplyButton);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enterReply(question.getKey());
                MainActivity m = (MainActivity) view.getContext();
                m.enterReply(question.getKey());
            }
        });


        TextView hashView = (TextView) convertView.findViewById(R.id.head_desc);
        SpannableString ss = new SpannableString(hashView.getText().toString());
        Pattern tagPattern = Pattern.compile("[#]+[A-Za-z0-9-_]+\\b");
        //Matcher tagMatcher = tagPattern.matcher(ss);

        String newActivityURL = "tag";
        Linkify.addLinks(hashView, tagPattern, newActivityURL);

        return convertView;
    }

    public void addQuestion(Question question) {
        mQuestionList.add(question);
        notifyDataSetChanged();
        mAPI.saveQuesion(question);
    }

    private void enterReply(String questionKey) {
        ((MainActivity)mContext).enterReply(questionKey);
    }

    /*
    // todo
    @Override
    protected void sortModels(List<Question> mModels) {
        Collections.sort(mModels);
    }

    @Override
    protected void setKey(String key, Question model) {
        model.setKey(key);
    }
    */

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

}
