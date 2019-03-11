package ch.uzh.ifi.seal.soprafs19.Response;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;

import java.time.LocalDate;

public class UserIdResponse {
    private long id;
    private String username;
    private LocalDate date;
    private UserStatus status;
    private String birthday;
    private String token;

    public UserIdResponse(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.date = user.getDate();
        this.status = user.getStatus();
        this.birthday = user.getBirthday();
        this.token = user.getToken();
    }

    public long getId(){ return id; }

    public void setId(long id){ this.id = id; }

    public String getUsername(){ return username; }

    public void setUsername(String username){ this.username = username; }

    public LocalDate getDate(){ return date; }

    public void setDate(String creationDate){ this.date = date; }

    public UserStatus getStatus(){ return status; }

    public void setStatus(UserStatus status){ this.status = status; }

    public String getBirthday(){ return birthday; }

    public void setBirthday(String birthday){ this.birthday = birthday; }

    public String getToken(){ return token; }

    public void setToken(String token){ this.token = token; }

}
