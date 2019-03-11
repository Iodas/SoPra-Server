package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Response.LoginResponse;
import ch.uzh.ifi.seal.soprafs19.Response.UserIdResponse;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.LoginData;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exception.InvalidLoginDataException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.exception.UsernameTakenException;
import ch.uzh.ifi.seal.soprafs19.exception.UserIdNotFoundException;
import ch.uzh.ifi.seal.soprafs19.exception.UnauthenticatedRequestException;
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

    public User findUserById (long id) {return this.userRepository.findById(id);}
    public boolean existUserById (long id) {if (findUserById(id)!= null) return true; else return false;}

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

    public User getUser(long id){
        if(!this.userRepository.existsById(id)){
            throw new UserIdNotFoundException(id);
        }
        else return this.findUserById(id);
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

    public void updateUser(long id, User user){
        User realUser = this.userRepository.findById(id);
        if (realUser == null){
            throw new InvalidLoginDataException();
        }
        if (user.getToken().equals(realUser.getToken())){
            if (user.getUsername() != null){
                if (this.userRepository.findByUsername(user.getUsername()) != null){
                    throw new UsernameTakenException();
                }
                else realUser.setUsername(user.getUsername());
            }
            if (user.getBirthday() != null){
                realUser.setBirthday(user.getBirthday());
            }
        }
        else throw new UnauthenticatedRequestException();
        //just for testing
        //else throw new InvalidLoginDataException();
    }

}
