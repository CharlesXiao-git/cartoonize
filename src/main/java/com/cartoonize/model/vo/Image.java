package com.cartoonize.model.vo;

import lombok.Data;

@Data
public class Image {
    private String imageName;
    private String imagePath;
    private String createdBy;
    private Boolean expired;
}
