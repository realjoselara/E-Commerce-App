package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;


public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();

        UtilityTest.injectObjects(userController, "userRepository", userRepository);
        UtilityTest.injectObjects(userController, "cartRepository", cartRepository);
        UtilityTest.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserHappyPath(){
        when(encoder.encode("superawesomepassword")).thenReturn("encodedpassword");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("joselara12345");
        request.setPassword("superawesomepassword");
        request.setConfirmPassword("superawesomepassword");

        ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);

        assertEquals(0, user.getId());
        assertEquals("joselara12345", user.getUsername());
        assertEquals("encodedpassword", user.getPassword());

    }

    @Test
    public void TestFindById(){
        long id = 1L;
        User user = new User();
        user.setUsername("joselara12345");
        user.setPassword("superawesomepassword");
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(id);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User actualUser = response.getBody();

        assertNotNull(actualUser);

        assertEquals(id, actualUser.getId());
        assertEquals("joselara12345", actualUser.getUsername());
        assertEquals("superawesomepassword", actualUser.getPassword());
    }

    @Test
    public void TestFindByUserName(){
        long id = 1L;
        User user = new User();
        user.setUsername("joselara12345");
        user.setPassword("superawesomepassword");
        user.setId(id);

        when(userRepository.findByUsername("joselara12345")).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName("joselara12345");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User actualUser = response.getBody();

        assertNotNull(actualUser);

        assertEquals(id, actualUser.getId());
        assertEquals("joselara12345", actualUser.getUsername());
        assertEquals("superawesomepassword", actualUser.getPassword());
    }

}