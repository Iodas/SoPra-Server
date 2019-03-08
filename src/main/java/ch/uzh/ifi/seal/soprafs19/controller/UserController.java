package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.Response.LoginResponse;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.entity.LoginData;
import ch.uzh.ifi.seal.soprafs19.exception.InvalidLoginDataException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }

    @PostMapping("/users/logindata")
    @ResponseStatus(HttpStatus.OK)
    LoginResponse login(@RequestBody LoginData data){
        return this.service.checkLoginData(data);
    }

//alternative: not entirely working though
    /*ResponseEntity<LoginResponse> login(@RequestBody LoginData data){
        User user = this.service.findUserByUsername(data.getUsername());
        return new ResponseEntity<>(new LoginResponse(user), HttpStatus.OK);

    }*/

}
