package com.cartoonize.service.impl;

import com.cartoonize.model.entity.UserEntity;
import com.cartoonize.model.vo.User;
import com.cartoonize.repository.UserJpaRepository;
import com.cartoonize.service.IUserService;
import com.cartoonize.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserJpaRepository userJpaRepository;

    /**
     * find all users from db and map to user vo
     * @return
     */
    public List<User> findAll()
    {
        List<User> userList = new ArrayList<User>();
        List<UserEntity> userEntityList = userJpaRepository.findAll();
        userEntityList.forEach(
                userEntity -> {
                    User user = new User();
                    user.setUserName(userEntity.getUserName());
                    user.setPassword(userEntity.getPassword());
                    userList.add(user);
                }
        );
        return userList;
    }

    /**
     * find user by username
     * @param name
     * @return
     */
    public User findByUserName(String name)
    {
        User user = null;
        UserEntity userEntity = userJpaRepository.findByUserName(name);
        if(Objects.nonNull(userEntity)){
            user = new User();
            user.setUserName(userEntity.getUserName());
            user.setPassword(userEntity.getPassword());
        }
        return user;
    }

    /**
     * save user to db
     * @param user
     */
    public void saveUser(User user)
    {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(user.getUserName());
        userEntity.setPassword(user.getPassword());
        userJpaRepository.save(userEntity);
    }

    /**
     * get user by token
     * @return
     * @throws Exception
     */
    public String getUserByToken() throws Exception{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("token");
        String username = null;
        if (!StringUtils.isEmpty(token)){
            username = TokenUtils.getUserByToken(token);
        }
        return username;
    }

}
