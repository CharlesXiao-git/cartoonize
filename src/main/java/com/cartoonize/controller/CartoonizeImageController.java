package com.cartoonize.controller;

import com.cartoonize.service.ICartoonizeService;
import com.cartoonize.service.IImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Cartoonize Image API Interfaces")
@RequestMapping("/cartoonize")
public class CartoonizeImageController {

    @Autowired
    private IImageService imageService;

    @Autowired
    private ICartoonizeService cartoonizeService;

    @GetMapping("/grayingImage/{imageName}")
    @ApiOperation("Graying Image")
    @ApiImplicitParam(name = "imageName", value = "Image Name", required = true)
    public String grayingImage(@RequestParam(required = true) String imageName) throws Exception{
        // get image by image name
        String imagePath = this.imageService.getImagePath(imageName);
        if (!StringUtils.isEmpty(imagePath)){
            cartoonizeService.cartoonizeGrayingImage(imagePath);
        }
        return "Image is not found.";
    }

    @GetMapping("/borderImage/{imageName}")
    @ApiOperation("BorderImage Image")
    @ApiImplicitParam(name = "imageName", value = "Image Name", required = true)
    public String borderImage(@RequestParam(required = true) String imageName) throws Exception{
        // get image by image name
        String imagePath = this.imageService.getImagePath(imageName);
        if (!StringUtils.isEmpty(imagePath)){
            cartoonizeService.cartoonizeBorderingImage(imagePath);
        }
        return "Image is not found.";
    }

}
