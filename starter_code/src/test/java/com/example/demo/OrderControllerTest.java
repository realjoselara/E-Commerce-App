package com.example.demo;

import static com.example.demo.UtilityTest.*;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Before
    public void setup(){
        User user = createUser();
        when(userRepository.findByUsername("joselara1234")).thenReturn(user);
        when(orderRepository.findByUser(any())).thenReturn(createOrders());
    }

    @Test
    public void TestSubmit(){

        ResponseEntity<UserOrder> response = orderController.submit("joselara1234");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();

        assertEquals(createItems(), order.getItems());
        assertEquals(createUser().getId(), order.getUser().getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void TestSubmitInvalid(){

        ResponseEntity<UserOrder> response = orderController.submit("joselara");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        assertNull( response.getBody());
        verify(userRepository, times(1)).findByUsername("joselara");
    }

    @Test
    public void TestGetOrdersForUser(){

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("joselara1234");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();
        assertEquals(createOrders().size(), orders.size());
    }

}