package com.cartoonize.service;

import com.cartoonize.model.vo.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface IImageService {

    public void uploadImage(byte[] bytes,String filename,String username) throws Exception;

    public String getImagePath(String imageName) throws Exception;

    public void downloadImage(String imagePath) throws Exception;

    public List<Image> listImages() throws Exception;

    public List<Image> findAll();

    public void saveImage(Image image);

    public void saveAndDownloadCarttoonizeImage(File file, String username) throws Exception;

}
