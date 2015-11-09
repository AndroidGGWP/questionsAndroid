package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import hk.ust.cse.hunkim.questionroom.R;
import hk.ust.cse.hunkim.questionroom.databinding.ReplyBinding;
import hk.ust.cse.hunkim.questionroom.question.Reply;

/**
 * Created by Yuxuan on 10/30/2015.
 */
public class ReplyAdapter extends ArrayAdapter<Reply> {
    private List<Reply> mReplies;
    private ReplyBinding mBinding;
    private LayoutInflater mInflater;
    public void addReply(Reply reply) {
        mReplies.add(reply);
        notifyDataSetChanged();
    }

    ReplyAdapter(Context context, List<Reply> replies){
        super(context, R.layout.reply,replies);
        mInflater = LayoutInflater.from(context);
        this.mReplies = replies;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null)
            mBinding = DataBindingUtil.inflate(mInflater, R.layout.reply, parent, false);
        else
            mBinding = DataBindingUtil.getBinding(convertView);
        Reply reply = getItem(position);
        mBinding.setReply(reply);
        convertView = mBinding.getRoot();
        return convertView;
        /*
        //LayoutInflater replyInflater = LayoutInflater.from(getContext());
        View replyView = mInflater.inflate(R.layout.reply, parent, false);
        mReplyBinding = DataBindingUtil.inflate(mInflater, R.layout.reply, parent, false);
        Reply reply = (Reply) getItem(position);

        String replyContent = ((Reply)getItem(position)).getContent();
        TextView replyContentView = (TextView) replyView.findViewById(R.id.replyContent);
        replyContentView.setText(replyContent);

        String replyTime = ((Reply)getItem(position)).getTime();
        TextView replyTimeView = (TextView) replyView.findViewById(R.id.replyTime);
        replyTimeView.setText(replyTime);
        return replyView;
        */
    }
}
