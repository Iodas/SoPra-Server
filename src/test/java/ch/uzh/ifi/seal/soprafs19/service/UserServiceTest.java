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

    @Test
    public void checkLoginData(){
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

}
