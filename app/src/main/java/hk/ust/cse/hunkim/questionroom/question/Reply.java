package hk.ust.cse.hunkim.questionroom.question;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import hk.ust.cse.hunkim.questionroom.TimeDisplay;

/**
 * Created by Yuxuan on 10/26/2015.
 */
public class Reply extends BaseObservable {
    /**
     * Must be synced with firebase JSON structure
     * Each must have getters
     */
    @SerializedName("_id")
    private String key;

    @SerializedName("wholeMsg")
    private String content;

    @SerializedName("postId")
    private String questionKey;
    private Long timestamp;

    public Reply (String content, String questionKey){
        this.content = content;
        this.timestamp = new Date().getTime();
        this.questionKey = questionKey;
    }

    public Reply (String content){
        this.content = content;
        this.timestamp = new Date().getTime();
    }

    // Required default constructor for Firebase object mapping
    private Reply() {
    }

    /* -------------------- Getters ------------------- */
    public String getKey() { return key; }
    public String getContent() { return content; }
    public Long getTimestamp() { return timestamp; }

    @Bindable
    public String getTimeDisplay() {
        return TimeDisplay.fromTimestamp(timestamp);
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getQuestionKey() {
        return questionKey;
    }

    public void setQuestionKey(String questionKey) {
        this.questionKey = questionKey;
    }
}
