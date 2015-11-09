package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hk.ust.cse.hunkim.questionroom.R;
import hk.ust.cse.hunkim.questionroom.question.Reply;

/**
 * Created by Yuxuan on 10/30/2015.
 */
public class ReplyAdapter extends ArrayAdapter{
    private List<Reply> mReplies;
    public void addReply(Reply reply) {
        mReplies.add(reply);
        notifyDataSetChanged();
    }

    ReplyAdapter(Context context, List<Reply> replies){
        super(context, R.layout.reply,replies);
        this.mReplies = replies;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater replyInflater = LayoutInflater.from(getContext());
        View replyView = replyInflater.inflate(R.layout.reply, parent, false);

        String replyContent = ((Reply)getItem(position)).getContent();
        TextView replyContentView = (TextView) replyView.findViewById(R.id.replyContent);
        replyContentView.setText(replyContent);

        String replyTime = ((Reply)getItem(position)).getTime();
        TextView replyTimeView = (TextView) replyView.findViewById(R.id.replyTime);
        replyTimeView.setText(replyTime);

        return replyView;
    }
}
