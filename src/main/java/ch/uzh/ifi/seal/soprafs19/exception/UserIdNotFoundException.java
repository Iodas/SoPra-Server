package ch.uzh.ifi.seal.soprafs19.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserIdNotFoundException extends RuntimeException{
    private long failedId;

    public UserIdNotFoundException(long id){ this.failedId = id; }

    public long getFailedId() { return failedId; }

    public void setFailedId(long failedId) { this.failedId = failedId; }
}