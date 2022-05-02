package com.cartoonize.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.cartoonize.model.vo.User;
import java.util.Date;

public class TokenUtils {
    //token expire time
    private static final long EXPIRE_TIME= 10*60*60*1000;
    //token secret
    private static final String TOKEN_SECRET="ljdyaishijin**3nkjnj??";

    /**
     * create token
     * @param user
     * @return
     */
    public static String sign(User user) throws IllegalArgumentException, JWTCreationException {
        String token=null;
        Date expireAt=new Date(System.currentTimeMillis()+EXPIRE_TIME);
        token = JWT.create()
                .withIssuer("auth0")
                .withSubject(user.getUserName())
                .withExpiresAt(expireAt)
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
        return token;
    }

    /**
     * verify token
     * @param token
     * @return
     */
    public static Boolean verify(String token) throws IllegalArgumentException, JWTVerificationException {
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
        DecodedJWT decodedJWT=jwtVerifier.verify(token);
        return true;
    }

    public static String getUserByToken(String token) throws Exception {
        try {
            String subject = JWT.require(Algorithm.HMAC256(TOKEN_SECRET))
                    .build()
                    .verify(token)
                    .getSubject();
            return subject;
        } catch (TokenExpiredException e){
            throw new Exception("token已失效，请重新登录",e);
        } catch (JWTVerificationException e) {
            throw new Exception("token验证失败！",e);
        }
    }

}
