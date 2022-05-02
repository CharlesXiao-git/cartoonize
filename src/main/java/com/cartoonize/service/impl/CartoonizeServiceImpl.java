package com.cartoonize.service.impl;

import com.cartoonize.service.ICartoonizeService;
import com.cartoonize.service.IImageService;
import com.cartoonize.service.IUserService;
import com.cartoonize.service.ProcessImageFunction;
import com.cartoonize.util.ImageUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class CartoonizeServiceImpl  implements ICartoonizeService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IImageService imageService;

    /**
     * get original image
     * @param bufferedImage
     * @return
     * @throws Exception
     */
    private BufferedImage getOriginalImage(BufferedImage bufferedImage) throws Exception{
        int picWidth = 500;
        BufferedImage compactImage = Thumbnails.of(bufferedImage).size(picWidth, 2000).asBufferedImage();
        return compactImage;
    }

    /**
     * curve process the image
     * @param compactImage
     * @return
     */
    private BufferedImage curveProcess(BufferedImage compactImage) {
        return ImageUtil.curveProcess(compactImage);
    }

    /**
     * graying the image
     * @param bufferedImage
     * @return
     */
    private BufferedImage grayingImage(BufferedImage bufferedImage) {
        return ImageUtil.grayingImage(bufferedImage);
    }

    /**
     * binary the image
     * @param bufferedImage
     * @return
     */
    private BufferedImage binaryImage(BufferedImage bufferedImage) {
        return ImageUtil.binaryImage(bufferedImage);
    }

    /**
     * get image border
     * @param bufferedImage
     * @return
     */
    private BufferedImage getImageBorder(BufferedImage bufferedImage) {
        return ImageUtil.getImageBorder(bufferedImage);
    }

    /**
     * use functional program to graving image
     * @param imagePath
     * @throws Exception
     */
    public void cartoonizeGrayingImage(String imagePath) throws Exception{
        // create graying image
        BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
        ProcessImageFunction getOriginalImage = this::getOriginalImage;
        BufferedImage grayImage = getOriginalImage
                .thenCompose(this::curveProcess)
                .thenCompose(this::grayingImage)
                .process(bufferedImage);
        // create file path
        String username = this.userService.getUserByToken();
        String newFilePath = username+"/cartoonize/" ;
        if(!new File(newFilePath).exists()){
            new File(newFilePath).mkdirs();
        }
        File newFile = new File(newFilePath + "grayImage_"+new File(imagePath).getName());
        ImageIO.write(grayImage, "jpg", newFile);
        // save carttoonize image to db, and then download
        this.imageService.saveAndDownloadCarttoonizeImage(newFile,username);
    }

    /**
     * use functional program to border image
     * @param imagePath
     * @throws Exception
     */
    public void cartoonizeBorderingImage(String imagePath) throws Exception{
        // create bordering image
        BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
        ProcessImageFunction getOriginalImage = this::getOriginalImage;
        BufferedImage grayImage = getOriginalImage
                .thenCompose(this::curveProcess)
                .thenCompose(this::grayingImage)
                .thenCompose(this::binaryImage)
                .thenCompose(this::getImageBorder)
                .process(bufferedImage);
        // create file path
        String username = this.userService.getUserByToken();
        String newFilePath = username+"/cartoonize/" ;
        if(!new File(newFilePath).exists()){
            new File(newFilePath).mkdirs();
        }
        File newFile = new File(newFilePath + "borderImage_"+new File(imagePath).getName());
        ImageIO.write(grayImage, "jpg", newFile);
        // save carttoonize image to db, and then download
        this.imageService.saveAndDownloadCarttoonizeImage(newFile,username);
    }
}
