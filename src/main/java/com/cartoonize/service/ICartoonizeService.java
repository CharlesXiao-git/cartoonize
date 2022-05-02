package com.cartoonize.service;

import java.awt.image.BufferedImage;

public interface ICartoonizeService {

    public void cartoonizeGrayingImage(String imagePath) throws Exception;

    public void cartoonizeBorderingImage(String imagePath) throws Exception;

}
