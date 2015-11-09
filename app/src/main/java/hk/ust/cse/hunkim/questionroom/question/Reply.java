package hk.ust.cse.hunkim.questionroom.question;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Yuxuan on 10/26/2015.
 */
public class Reply {
    /**
     * Must be synced with firebase JSON structure
     * Each must have getters
     */
    @SerializedName("_id")
    private String key;
    @SerializedName("wholeMsg")
    private String content;
    private Long timestamp;
    private String time;

    public Reply (String reply){
        this.content = reply;
        this.timestamp = new Date().getTime();
    }
    public Reply (String reply, String time){
        this.content = reply;
        this.time = time;
    }


    // Required default constructor for Firebase object mapping
    private Reply() {
    }

    /* -------------------- Getters ------------------- */
    public String getKey() { return key; }
    public String getContent() { return content; }
    public Long getTimestamp() { return timestamp; }
    public String getTime() { return time; }


    public void setKey(String key) {
        this.key = key;
    }
}
