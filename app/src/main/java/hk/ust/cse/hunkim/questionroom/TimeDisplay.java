package hk.ust.cse.hunkim.questionroom;

import java.util.Date;

/**
 * Created by Yuxuan on 10/30/2015.
 */
public class TimeDisplay {
    private String outputTime;
    public TimeDisplay(long inputTime){
        long currentTime = new Date().getTime();
        long difference = currentTime - inputTime;
        difference = difference/1000;
        if (difference < 60*60*24){
            if (difference < 60){
                if (difference < 2) outputTime = "just now";
                else outputTime = difference + " seconds ago";
            }
            else if (difference < 60*60){
                outputTime = ((int)(difference/60)) + " minute" + (difference/60 > 1 ? "s": "") + " ago";
            }
            else{
                outputTime = ((int)(difference/60/60)) + " hour" + (difference/60/60 > 1 ? "s": "") + " ago";
            }
        }
        else{
            outputTime = ((int)(difference/60/60/24)) + " day" + (difference/60/60/24 > 1 ? "s": "") + " ago";
        }
    }
    public String getOutputTime(){
        return outputTime;
    }
}
