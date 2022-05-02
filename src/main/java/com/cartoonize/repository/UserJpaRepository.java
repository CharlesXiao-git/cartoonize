package com.cartoonize.repository;
import com.cartoonize.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    /**
     * findByUserName
     * @param userName
     * @return
     */
    UserEntity findByUserName(String userName);

}
