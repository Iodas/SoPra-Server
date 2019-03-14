package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.controller.UserController;
import ch.uzh.ifi.seal.soprafs19.Response.LoginResponse;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.entity.LoginData;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;


    //check if we are able to fetch the created user from the server
    @Test
    public void getUsers() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setName("testName");
        testUser.setPassword("testPassword");

        userService.createUser(testUser);

        //check if we dont get an error
        this.mvc.perform(get("/users")).andExpect(status().is(200));
    }

    //check if we get the user with the id of the user
    @Test
    public void userIdFunction() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername1");
        testUser.setName("testName1");
        testUser.setPassword("testPassword1");

        User createdUser = userService.createUser(testUser);
        long userId = createdUser.getId();

        //check if we dont get an error if we look for a valid id
        this.mvc.perform(get("/users/" + userId)).andExpect(status().is(200));

        //check if we get the correct error with an invalid id
        this.mvc.perform(get("/users/" + 0)).andExpect(status().is(404));
    }

    @Test
    public void updateUser() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername2");
        testUser.setName("testName2");
        testUser.setPassword("testPassword2");

        User createdUser = userService.createUser(testUser);

        //successfully changed username
        this.mvc.perform(put("/users/" + createdUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"updatedUser\", \"token\": \"" + createdUser.getToken() + "\"}"))
                .andExpect(status().is(204));

        //username exists already
        this.mvc.perform(put("/users/" + createdUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"updatedUser\", \"token\": \"" + createdUser.getToken() + "\"}"))
                .andExpect(status().is(409));

        //invalid token
        this.mvc.perform(put("/users/" + createdUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"updatedUser1\", \"token\": \" wrongToken \" }"))
                .andExpect(status().is(401));
    }

    @Test
    public void loginDataFunction() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUsername3");
        testUser.setName("testName3");
        testUser.setPassword("testPassword3");

        User createdUser = userService.createUser(testUser);

        //wrong password
        this.mvc.perform(post("/users/logindata")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUsername3\", \"password\": \"wrongPassword\"}"))
                .andExpect(status().is(404));

        //username doesn't exist
        this.mvc.perform(post("/users/logindata")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUsername99\", \"password\": \"testPassword3\"}"))
                .andExpect(status().is(404));

        //correct login credentials
        this.mvc.perform(post("/users/logindata")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUsername3\", \"password\": \"testPassword3\"}"))
                .andExpect(status().is(200));

    }

    @Test
    public void usersFunction() throws Exception{
        //create the new user
        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUsername4\", \"name\": \"testName4\", \"password\": \"testPassword4\"}"))
                .andExpect(status().is(201));

        //already existing username
        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUsername4\", \"name\": \"testName4\", \"password\": \"testPassword4\"}"))
                .andExpect(status().is(409));
    }

}