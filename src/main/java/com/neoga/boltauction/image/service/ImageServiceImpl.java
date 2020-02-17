package com.neoga.boltauction.image.service;

import com.neoga.boltauction.exception.custom.CItemNotFoundException;
import com.neoga.boltauction.exception.custom.CNotImageException;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.repository.ItemRepository;
import com.neoga.boltauction.memberstore.store.domain.Store;
import com.neoga.boltauction.memberstore.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ItemRepository itemRepository;
    private final StoreRepository storeRepository;

    @Override
    public void saveItemImages(Item item, MultipartFile... images) throws IOException {

        // field for json
        JSONObject pathJson = new JSONObject();
        ArrayList pathList = new ArrayList();

        // make directory
        File folder = new File("src/main/resources/image/item/" + item.getId());
        if (!folder.exists()) {
            folder.mkdir();
        }

        // save images
        for (MultipartFile file : images) {

            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

            if (bufferedImage != null) { // when image
                byte[] bytes = file.getBytes();
                Path path = Paths.get("src/main/resources/image/item/" + item.getId() + "/" + file.getOriginalFilename());
                Files.write(path, bytes);
                pathList.add(path.toString());
            } else {
                throw new CNotImageException();
            }
        }

        pathJson.put("path", pathList);
        item.setImagePath(pathJson.toJSONString());

        itemRepository.save(item);

        return;
    }

    @Override
    public void updateItemImages(Item item, MultipartFile... images) throws IOException {
        return;
    }

    @Override
    public void saveStoreImage(Store store, MultipartFile image) throws IOException {

        // field for json
        JSONObject pathJson = new JSONObject();
        ArrayList pathList = new ArrayList();

        if (image == null) {
            return;
        }

        // make directory
        File folder = new File("src/main/resources/image/store/" + store.getId());
        if (!folder.exists()) {
            folder.mkdir();
        }

        // save images
        BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

        if (bufferedImage != null) { // when image
            byte[] bytes = image.getBytes();
            Path path = Paths.get("src/main/resources/image/store/" + store.getId() + "/" + image.getOriginalFilename());
            Files.write(path, bytes);
            pathList.add(path.toString());
        } else {
            throw new CNotImageException();
        }

        pathJson.put("path", pathList);
        store.setImagePath(pathJson.toJSONString());

        storeRepository.save(store);

        return ;
    }
}
