package com.neoga.boltauction.image.service;

import com.neoga.boltauction.exception.custom.CNotImageException;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.repository.ItemRepository;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
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
    private final S3Uploader s3Uploader;
    private final MemberRepository memberRepository;
    private static final String PATH_URI = "https://bolt-auction-image.s3.ap-northeast-2.amazonaws.com/";

    private static final String NOT_IMAGE = "이미지가 아닙니다.";

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
                throw new CNotImageException(NOT_IMAGE);
            }
        }

        String imagePath = stringBuilder.toString();

        if (imagePath.equals(""))
            item.setImagePath(null);
        else
            item.setImagePath(imagePath);

        itemRepository.save(item);
    }

    @Override
    public void updateItemImages(Item item, MultipartFile... images) throws IOException {

        // 기존의 이미지 삭제
        if (item.getImagePath() != null){
            String[] pathList = item.getImagePath().split(",");
            for (int i = 0; i < pathList.length; i++) {
                String dirFile = pathList[i].substring(PATH_URI.length());
                s3Uploader.removeS3File(dirFile);
            }
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
                throw new CNotImageException(NOT_IMAGE);
            }
        }

        String imagePath = stringBuilder.toString();

        if (imagePath.equals(""))
            item.setImagePath(null);
        else
            item.setImagePath(imagePath);

        itemRepository.save(item);
    }

    @Override
    public void saveStoreImage(Members members, MultipartFile image) throws IOException {

        // 기존의 이미지 삭제
        String oldPath = members.getImagePath();
        if (oldPath != null) {
            String dirFile = oldPath.substring(PATH_URI.length());
            s3Uploader.removeS3File(dirFile);
        }

        if (image == null){
            members.setImagePath(null);
            return;
        }


        BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
        // when image
        if (bufferedImage != null) {
            String path = s3Uploader.upload(image, "image/store/" + members.getId().toString());
            members.setImagePath(path);
        } else {
            throw new CNotImageException(NOT_IMAGE);
        }

        memberRepository.save(members);
    }
}
