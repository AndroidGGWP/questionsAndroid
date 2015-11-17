package hk.ust.cse.hunkim.questionroom.question;

/**
 * Created by Teman on 11/16/2015.
 */
public class User {
    private String username;
    private String password;
    //private String avatar;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}

