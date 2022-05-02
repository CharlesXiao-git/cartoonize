package com.cartoonize.service;

import com.cartoonize.model.vo.User;
import java.util.List;

public interface IUserService {

    public List<User> findAll();

    public void saveUser(User user);

    public User findByUserName(String name);

    public String getUserByToken() throws Exception;
}
