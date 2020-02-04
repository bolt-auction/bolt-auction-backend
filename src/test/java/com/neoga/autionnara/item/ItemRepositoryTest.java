package com.neoga.autionnara.item;

import com.neoga.autionnara.item.domain.Item;
import com.neoga.autionnara.item.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testInsertItem() throws Exception {

        for (int i = 0; i < 10; i++) {
            Item item = new Item();
            item.setName("테스트 " + i);
            item.setDescription("내용 " + i);
            item.setCreateDate(LocalDateTime.now());
            item.setEndDate(LocalDateTime.now().plusDays(1));
            item.setStartPrice(i*1000);
            item.setMinBidPrice(i*100);
            item.setQuickPrice(i*10000);

            itemRepository.save(item);
        }

    }

    @Test
    public void testGetItem() throws Exception {
        //given
        Long id = 1L;
        //when
        Optional<Item> item = itemRepository.findById(id);
        //then
        System.out.println(item.toString());
    }

    @Test
    public void testUpdateItem() throws Exception {
        //given

        //when

        //then

    }

    @Test
    public void testDeleteItem() throws Exception {
        //given

        //when

        //then

    }
}
