package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Response.LoginResponse;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.LoginData;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exception.InvalidLoginDataException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.exception.UsernameTakenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


import java.time.LocalDate;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User findUserByUsername(String username){
        return this.userRepository.findByUsername(username);
    }

    public User createUser(User newUser) {
        if (this.userRepository.findByUsername(newUser.getUsername()) != null){
            throw new UsernameTakenException();
        }
        else {
            newUser.setToken(UUID.randomUUID().toString());
            newUser.setStatus(UserStatus.ONLINE);

            //getting the time of creation of new user
            newUser.setDate(LocalDate.now());


            userRepository.save(newUser);
            log.debug("Created Information for User: {}", newUser);
            return newUser;
        }

    }



    public LoginResponse checkLoginData(LoginData data){
        String username = data.getUsername();
        String password = data.getPassword();
        User user = this.userRepository.findByUsername(username);

        if (!this.userRepository.existsByUsername(username)){
            throw new InvalidLoginDataException();
        }

        else {
            if (!user.getPassword().equals(password)){
                throw new InvalidLoginDataException();
            }
        }
        String token = this.userRepository.findByUsername(username).getToken();
        LoginResponse loggedUser = new LoginResponse(user);
        loggedUser.setToken(token);
        return loggedUser;
    }



}
