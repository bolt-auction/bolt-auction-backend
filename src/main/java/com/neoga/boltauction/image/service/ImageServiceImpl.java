package com.neoga.boltauction.image.service;

import com.neoga.boltauction.exception.custom.CNotImageException;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.repository.ItemRepository;
import com.neoga.boltauction.memberstore.store.domain.Store;
import com.neoga.boltauction.memberstore.store.repository.StoreRepository;
import com.neoga.boltauction.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ItemRepository itemRepository;
    private final StoreRepository storeRepository;
    private final S3Uploader s3Uploader;
    private final static String pathUrl = "https://bolt-auction-image.s3.ap-northeast-2.amazonaws.com/";

    @Override
    public void saveItemImages(Item item, MultipartFile... images) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < images.length; i++) {
            BufferedImage bufferedImage = ImageIO.read(images[i].getInputStream());
            // when image
            if (bufferedImage != null) {
                String path = s3Uploader.upload(images[i], "image/item/" + item.getId().toString());
                stringBuilder.append(path);
                if ( i+1 < images.length)
                    stringBuilder.append(",");
            } else {
                throw new CNotImageException("이미지가 아닙니다.");
            }
        }

        item.setImagePath(stringBuilder.toString());

        itemRepository.save(item);

        return;
    }

    @Override
    public void updateItemImages(Item item, MultipartFile... images) throws IOException {

        // 기존의 이미지 삭제
        String[] pathList = item.getImagePath().split(",");
        for (int i = 0; i < pathList.length; i++) {
            String dirFile = pathList[i].substring(pathUrl.length());
            s3Uploader.removeS3File(dirFile);
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < images.length; i++) {
            BufferedImage bufferedImage = ImageIO.read(images[i].getInputStream());
            // when image
            if (bufferedImage != null) {
                String path = s3Uploader.upload(images[i], "image/store/" + item.getId().toString());
                stringBuilder.append(path);
                if ( i+1 < images.length)
                    stringBuilder.append(",");
            } else {
                throw new CNotImageException("이미지가 아닙니다.");
            }
        }

        item.setImagePath(stringBuilder.toString());

        itemRepository.save(item);

        return;
    }

    @Override
    public void saveStoreImage(Store store, MultipartFile image) throws IOException {

        // 기존의 이미지 삭제
        String oldPath = store.getImagePath();
        String dirFile = oldPath.substring(pathUrl.length());
        s3Uploader.removeS3File(dirFile);

        BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
        // when image
        if (bufferedImage != null) {
            String path = s3Uploader.upload(image, "image/store/" + store.getId().toString());
            store.setImagePath(path);
        } else {
            throw new CNotImageException("이미지가 아닙니다.");
        }

        storeRepository.save(store);

        return;
    }
}
