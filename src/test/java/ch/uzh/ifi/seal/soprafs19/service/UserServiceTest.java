package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
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

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void createUser() {
        userRepository.deleteAll();
        Assert.assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(createdUser.getToken());
        Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));

    }


    //this is the login function, also check if user status is online
    @Test
    public void checkLoginDataOnline(){
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        User createdUser = userService.createUser(testUser);

        LoginData testLogin = new LoginData();
        testLogin.setUsername("testUsername");
        testLogin.setPassword("testPassword");

        LoginResponse testLoginResponse = userService.checkLoginData(testLogin);
        User user = userRepository.findByUsername("testUsername");

        Assert.assertNotNull(testLoginResponse);
        Assert.assertEquals(testLoginResponse.getToken(), user.getToken());
        Assert.assertEquals(UserStatus.ONLINE, user.getStatus());

        userRepository.deleteAll();
    }

    @Test
    public void updateUser(){
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        User createdUser = userService.createUser(testUser);

        User changedUser = new User();
        changedUser.setUsername("changedUsername");
        changedUser.setBirthday("changedBirthday");
        changedUser.setToken(createdUser.getToken());

        long id = createdUser.getId();
        this.userService.updateUser(id, changedUser);

        User afterChange = this.userRepository.findById(id);

        Assert.assertNotNull(changedUser);
        Assert.assertEquals(afterChange.getUsername(), "changedUsername");
        Assert.assertEquals(afterChange.getBirthday(), "changedBirthday");

    }

    //this function is also used to logout, so here we see if the user is set to offline after logging in
    @Test
    public void updateUserOffline(){
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        User createdUser = userService.createUser(testUser);

        //here we are logging the user in
        LoginData testLogin = new LoginData();
        testLogin.setUsername("testUsername");
        testLogin.setPassword("testPassword");

        //here we are logging our user in first
        LoginResponse testLoginResponse = userService.checkLoginData(testLogin);

        long id = createdUser.getId();

        User user = this.userRepository.findById(id);
        //check if status is set to online after logging in
        Assert.assertEquals(UserStatus.ONLINE, user.getStatus());

        //logging the user out again
        String token = testLoginResponse.getToken();
        User changedUser = new User();
        changedUser.setToken(token);
        changedUser.setStatus(UserStatus.OFFLINE);
        userService.updateUser(id, changedUser);

        Assert.assertEquals(UserStatus.OFFLINE, changedUser.getStatus());
    }



    @Test
    public void getUser(){
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        User createdUser = userService.createUser(testUser);

        long id = createdUser.getId();
        User getTestUser = this.userService.getUser(id);

        Assert.assertNotNull(getTestUser);
        Assert.assertEquals(createdUser.getUsername(), getTestUser.getUsername());

    }

    @Test
    public void findUserById(){
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        User createdUser = userService.createUser(testUser);

        long id = createdUser.getId();

        User findTestUser = this.userService.findUserById(id);

        Assert.assertNotNull(findTestUser);
        Assert.assertEquals(createdUser.getUsername(), findTestUser.getUsername());

    }

}
