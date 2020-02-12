package com.neoga.boltauction.image.service;

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

        Item findItem = itemService.getItem(itemId);

        JSONObject pathJson = new JSONObject();
        ArrayList pathList = new ArrayList();

        //폴더 생성
        File folder = new File("src/main/resources/image/item/" + itemId);
        if (!folder.exists()){
            folder.mkdir();
        }

        for (MultipartFile file : images) {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

            if (bufferedImage == null) {
                byte[] bytes = file.getBytes();
                Path path = Paths.get("src/main/resources/image/" + itemId + "/" + file.getOriginalFilename());
                Files.write(path, bytes);
                pathList.add(path.toString());
            } else {
                return null;
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
}
