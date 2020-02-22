package com.neoga.boltauction.image.service;

import com.neoga.boltauction.exception.custom.CNotImageException;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.repository.ItemRepository;
import com.neoga.boltauction.memberstore.store.domain.Store;
import com.neoga.boltauction.memberstore.store.repository.StoreRepository;
import com.neoga.boltauction.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ItemRepository itemRepository;
    private final StoreRepository storeRepository;
    private final S3Uploader s3Uploader;
    private final static JSONParser parser = new JSONParser();

    @Override
    public void saveItemImages(Item item, MultipartFile... images) throws IOException {

        JSONObject pathJson = new JSONObject();
        ArrayList pathList = new ArrayList();

        for (MultipartFile image: images) {
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
            // when image
            if (bufferedImage != null) {
                String path = s3Uploader.upload(image, "image/" + item.getId().toString());
                pathList.add(path);
            } else {
                throw new CNotImageException("이미지가 아닙니다.");
            }
        }

        pathJson.put("path", pathList);
        item.setImagePath(pathJson.toJSONString());

        itemRepository.save(item);

        return;
    }

    @Override
    public void updateItemImages(Item item, MultipartFile... images) throws IOException {

        // 기존의 이미지 삭제
        try {
            JSONObject savePath = (JSONObject) parser.parse(item.getImagePath());
            String pathUrl = "https://bolt-auction-image.s3.ap-northeast-2.amazonaws.com/";
            ArrayList savePathList = (ArrayList) savePath.get("path");
            savePathList.forEach(o -> {
                String dirFile = o.toString().substring(pathUrl.length());
                s3Uploader.removeS3File(dirFile);
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject pathJson = new JSONObject();
        ArrayList pathList = new ArrayList();

        for (MultipartFile image: images) {
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
            // when image
            if (bufferedImage != null) {
                String path = s3Uploader.upload(image, "image/item/" + item.getId().toString());
                pathList.add(path);
            } else {
                throw new CNotImageException("이미지가 아닙니다.");
            }
        }

        pathJson.put("path", pathList);
        item.setImagePath(pathJson.toJSONString());

        itemRepository.save(item);

        return;
    }

    @Override
    public void saveStoreImage(Store store, MultipartFile image) throws IOException {

        // 기존의 이미지 삭제
        try {
            JSONObject savePath = (JSONObject) parser.parse(store.getImagePath());
            String pathUrl = "https://bolt-auction-image.s3.ap-northeast-2.amazonaws.com/";
            ArrayList savePathList = (ArrayList) savePath.get("path");
            savePathList.forEach(o -> {
                String dirFile = o.toString().substring(pathUrl.length());
                s3Uploader.removeS3File(dirFile);
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject pathJson = new JSONObject();
        ArrayList pathList = new ArrayList();

        BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
        // when image
        if (bufferedImage != null) {
            String path = s3Uploader.upload(image, "image/store/" + store.getId().toString());
            pathList.add(path);
        } else {
            throw new CNotImageException("이미지가 아닙니다.");
        }

        pathJson.put("path", pathList);
        store.setImagePath(pathJson.toJSONString());

        storeRepository.save(store);

        return;
    }
}
