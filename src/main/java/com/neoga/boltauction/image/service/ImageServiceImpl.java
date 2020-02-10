package com.neoga.boltauction.image.service;

import com.neoga.boltauction.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private ImageRepository imageRepository;

    @Override
    public String saveItemImages(Long itemId, MultipartFile... images) throws IOException {

        for (MultipartFile image : images) {
            byte[] bytes = image.getBytes();
            Path path = Paths.get("src/main/resources/image/" + image.getOriginalFilename());
            Files.write(path, bytes);
        }

        return "ok";


    }
}
