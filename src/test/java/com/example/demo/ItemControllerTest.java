package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static com.example.demo.UtilityTest.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;
    @Before
    public void setup(){
        when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem(1)));
        when(itemRepository.findAll()).thenReturn(createItems());
        when(itemRepository.findByName("item")).thenReturn(Arrays.asList(createItem(1), createItem(2)));
    }

    @Test
    public void TestGetItems(){
        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();

        assertEquals(createItems(), items);
        verify(itemRepository , times(1)).findAll();
    }

    @Test
    public void TestGetItemById(){

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();
        assertEquals(createItem(1L), item);

        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    public void TestGetItemByIdInvalid(){

        ResponseEntity<Item> response = itemController.getItemById(10L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        assertNull(response.getBody());
        verify(itemRepository, times(1)).findById(10L);
    }

    @Test
    public void TestGetItemByName(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = Arrays.asList(createItem(1), createItem(2));

        assertEquals(createItems(), items);

        verify(itemRepository , times(1)).findByName("item");
    }

    @Test
    public void TestGetItemByNameInvalid(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("invalid name");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        assertNull(response.getBody());

        verify(itemRepository , times(1)).findByName("invalid name");
    }
}