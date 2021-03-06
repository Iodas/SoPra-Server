package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.Response.LoginResponse;
import ch.uzh.ifi.seal.soprafs19.Response.UserIdResponse;
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

    @GetMapping("/users/{userId}")
    UserIdResponse userIdFunction(@PathVariable long userId){ User user = this.service.getUser(userId); return new UserIdResponse(user); }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateUser(@PathVariable long userId, @RequestBody User user){
        this.service.updateUser(userId, user);
    }

    @PostMapping("/users/logindata")
    @ResponseStatus(HttpStatus.OK)
    LoginResponse logindataFunction(@RequestBody LoginData data){
        return this.service.checkLoginData(data);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    User usersFunction(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }




}
