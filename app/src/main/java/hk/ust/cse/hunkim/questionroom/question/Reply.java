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

    private String username;
    @SerializedName("anonymous")
    private boolean isAnonymous;

    @SerializedName("postId")
    private String questionKey;
    private Long timestamp;

    public Reply (String content, String questionKey, String username, boolean isAnonymous){
        this.content = content;
        this.timestamp = new Date().getTime();
        this.questionKey = questionKey;
        this.username = username;
        this.isAnonymous = isAnonymous;
    }

    public Reply (String content){
        this.content = content;
        this.timestamp = new Date().getTime();
        this.username = "Anonymous";
        this.isAnonymous = false;
    }

    // Required default constructor for Firebase object mapping
    private Reply() {
    }

    /* -------------------- Getters ------------------- */
    public String getKey() { return key; }
    public String getContent() { return content; }
    public Long getTimestamp() { return timestamp; }

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
