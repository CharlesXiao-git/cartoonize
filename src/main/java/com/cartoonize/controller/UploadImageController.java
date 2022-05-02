package com.cartoonize.controller;

import com.cartoonize.model.vo.Image;
import com.cartoonize.service.IImageService;
import com.cartoonize.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@Api(tags = "Image Upload API Interfaces")
@RequestMapping("/image")
public class UploadImageController {

    @Autowired
    private IImageService imageService;

    @Autowired
    private IUserService userService;

    @PostMapping("/upload")
    @ApiOperation("Upload Image")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        if(!file.isEmpty()){
            byte[] bytes = file.getBytes();
            // get username from token
            String username = this.userService.getUserByToken();
            this.imageService.uploadImage(bytes,file.getOriginalFilename(),username);
            return "Upload file : " + file.getOriginalFilename();
        }else{
            return "file is empty!";
        }
    }

    @GetMapping("/listImages")
    @ApiOperation(value = "List Images")
    public String listImages() throws Exception{
        List<Image> images = this.imageService.listImages();
        JSONObject json = new JSONObject();
        json.put("images",images);
        return json.toString();
    }

    @GetMapping("/downloadImage/{imageName}")
    @ApiOperation("Download image")
    @ApiImplicitParam(name = "imageName", value = "Image Name", required = true)
    public String downloadImage(@RequestParam(required = true) String imageName) throws Exception{
        String imagePath = this.imageService.getImagePath(imageName);
        if (!StringUtils.isEmpty(imagePath)){
            this.imageService.downloadImage(imagePath);
        }
        return "Image is not found.";
    }

}
