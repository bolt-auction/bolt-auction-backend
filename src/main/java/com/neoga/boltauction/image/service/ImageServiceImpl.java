package com.neoga.boltauction.image.service;

import com.neoga.boltauction.exception.custom.CNotImageException;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.service.ItemService;
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

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ItemService itemService;

    @Override
    public String saveItemImages(Long itemId, MultipartFile... images) throws IOException {

        // get item entity
        Item findItem = itemService.getItem(itemId);

        // field for json
        JSONObject pathJson = new JSONObject();
        ArrayList pathList = new ArrayList();

        // make directory
        File folder = new File("src/main/resources/image/item/" + itemId);
        if (!folder.exists()){
            folder.mkdir();
        }

        // save images
        for (MultipartFile file : images) {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

            if (bufferedImage != null) { // when image
                byte[] bytes = file.getBytes();
                Path path = Paths.get("src/main/resources/image/item/" + itemId + "/" + file.getOriginalFilename());
                Files.write(path, bytes);
                pathList.add(path.toString());
            } else {
                throw new CNotImageException();
            }
        }

        pathJson.put("path", pathList);
        findItem.setImagePath(pathJson.toJSONString());

        return pathJson.toJSONString();
    }

    @Override
    public String updateItemImages(Long itemId, MultipartFile... images) throws IOException {
        return null;
    }

    @Override
    public String saveStoreImage(Long storeId, MultipartFile image) throws IOException {
        return null;
    }
}
