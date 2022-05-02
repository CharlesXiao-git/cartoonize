package com.cartoonize.model.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name ="user")
@Data
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String userName;
    @Column
    private String password;
}
