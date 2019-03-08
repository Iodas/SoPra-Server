package ch.uzh.ifi.seal.soprafs19.Response;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import java.time.LocalDate;

public class LoginResponse {
    private long id;
    private String username;
    private String name;
    private LocalDate date;
    private String token;

    public LoginResponse(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.date = user.getDate();
        this.token = user.getToken();
    }

    public long getId(){ return id; }
    public void setId(long id){ this.id = id; }

    public String getUsername(){ return username; }
    public void setUsername(String username){ this.username = username; }

    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }

    public void setDate(LocalDate date) { this.date = date; }
    public LocalDate getDate() { return date; }

    public String getToken(){ return token;}
    public void setToken(String token) {this.token = token; }


}
