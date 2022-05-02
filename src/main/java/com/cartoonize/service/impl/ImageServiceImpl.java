package com.cartoonize.service.impl;

import com.cartoonize.model.entity.ImageEntity;
import com.cartoonize.model.vo.Image;
import com.cartoonize.repository.ImageJpaRepository;
import com.cartoonize.service.IImageService;
import com.cartoonize.service.IUserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageServiceImpl implements IImageService {

    @Autowired
    private ImageJpaRepository imageJpaRepository;

    @Autowired
    private IUserService userService;

    /**
     * use threadPoolTaskExecutor to upload image
     * @param bytes
     * @param filename
     * @param username
     * @throws Exception
     */
    @Async("threadPoolTaskExecutor")
    public void uploadImage(byte[] bytes,String filename,String username) throws Exception {
        long startTime = new Date().getTime();
        log.info(" Start upload file : " + filename);
        // if folder is not exist, create folder for the user
        if(!new File(username).exists()){
            new File(username).mkdir();
        }
        File newFile = new File(username +"/" +filename);
        // use try with resources to write the bytes
        try(BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newFile));){
            out.write(bytes);
        }catch (Exception e) {
            log.error("File upload error :" + e.getMessage());
        }
        // save image metadata to db
        Image image = new Image();
        image.setImageName(newFile.getName());
        image.setImagePath(newFile.getPath());
        image.setCreatedBy(username);
        this.saveImage(image);
        long endTime = new Date().getTime();
        log.info("Cost time : " + (endTime - startTime) + " to upload " + filename);
    }

    /**
     * get ImagePath by imageName
     * @param imageName
     * @return
     * @throws Exception
     */
    public String getImagePath(String imageName) throws Exception {
        String result = null;
        // get all images,that owned by user, and find matching image.
        // return matching image path
        List<Image> images = this.listImages();
        if(!StringUtils.isEmpty(imageName)){
            Optional<Image> imapgeOpt = images.stream().filter(image ->
                    imageName.equals(image.getImageName()))
                    .findAny();
            if (imapgeOpt.isPresent()){
                Image findImage = imapgeOpt.get();
                result = findImage.getImagePath();
            }
        }
        return result;
    }

    /**
     * downloadImage by imagePath
     * @param imagePath
     * @throws Exception
     */
    public void downloadImage(String imagePath) throws Exception{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        File downloadFile = new File(imagePath);
        ServletContext context = request.getServletContext();
        // get MIME type of the file
        String mimeType = context.getMimeType(imagePath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                downloadFile.getName());
        response.setHeader(headerKey, headerValue);
        // copy the stream to the response's output stream.
        InputStream myStream = new FileInputStream(imagePath);
        IOUtils.copy(myStream, response.getOutputStream());
        response.flushBuffer();
    }

    /**
     * list all images,that owned by user
     * @return
     * @throws Exception
     */
    public List<Image> listImages() throws Exception {
        List<Image> images = null;
        // get username from token
        String username = this.userService.getUserByToken();
        // find image,that owned by user
        if(!StringUtils.isEmpty(username)){
            images = this.findAll();
            images = images.stream().filter(image ->
                    username.equals(image.getCreatedBy())
            ).collect(Collectors.toList());
        }
        return images;
    }

    /**
     * find all images from db and map to image VO
     * @return
     */
    public List<Image> findAll(){
        List<Image> imageList = new ArrayList<Image>();
        List<ImageEntity> imageEntityList = this.imageJpaRepository.findAll();
        imageEntityList.forEach(
                imageEntity -> {
                    Image image = new Image();
                    image.setImageName(imageEntity.getImageName());
                    image.setImagePath(imageEntity.getImagePath());
                    image.setCreatedBy(imageEntity.getCreatedBy());
                    image.setExpired(imageEntity.getExpired());
                    imageList.add(image);
                }
        );
        return imageList;
    }

    /**
     * saveImage
     * @param image
     */
    public void saveImage(Image image)
    {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageName(image.getImageName());
        imageEntity.setImagePath(image.getImagePath());
        imageEntity.setCreatedBy(image.getCreatedBy());
        imageEntity.setExpired(image.getExpired());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());
        imageEntity.setCreatedDate(date);
        this.imageJpaRepository.save(imageEntity);
    }

    public void saveAndDownloadCarttoonizeImage(File newFile, String username) throws Exception
    {
        // save image metadata to db
        Image image = new Image();
        image.setImageName(newFile.getName());
        image.setImagePath(newFile.getPath());
        image.setCreatedBy(username);
        this.saveImage(image);
        // download the image
        this.downloadImage(newFile.getPath());
    }


}
