package com.cartoonize.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name ="image")
@Data
public class ImageEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String imageName;
    @Column
    private String imagePath;
    @Column
    private String createdDate;
    @Column
    private String createdBy;
    @Column
    private Boolean expired;
}
