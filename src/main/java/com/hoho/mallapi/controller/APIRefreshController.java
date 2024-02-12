package com.hoho.mallapi.controller;

import com.hoho.mallapi.util.CustomJWTException;
import com.hoho.mallapi.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController {

    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh (@RequestHeader("Authorization") String authHeader, String refreshToken){

        log.info("Authorization:" + authHeader);
        log.info("refresh:" + refreshToken);

        if(refreshToken == null){
            throw new CustomJWTException("NULL_REFRESH");
        }

        if(authHeader == null || authHeader.length() < 7){
            throw  new CustomJWTException("INVALID STRING");
        }

        String accessToken = authHeader.substring(7);
        log.info("accessToken:" + accessToken);

        //AccessToken 만료여부 확인
        if(!checkExpiredToken(accessToken)){
            log.info("accessToken 만료");
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        //RefreshToken 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);

        log.info("refresh... claims:" + claims);

        String newAccessToken = JWTUtil.generateToken(claims, 10);

        String newRefreshToken = checkTime((Integer) claims.get("exp")) == true ? JWTUtil.generateToken(claims, 60*24) : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    private boolean checkTime(Integer exp){
        //JWT exp를 날짜로 변환
        Date expDate = new Date((long) exp * (1000));

        //현재 시간과의 차이 계산
        long gap = expDate.getTime() - System.currentTimeMillis();

        //분단위 계산
        long leftMin = gap / (1000 * 60);

        //1시간 미만으로 남앗는지 체크
        return leftMin < 60;
    }
    private boolean checkExpiredToken(String token){
        try{
            JWTUtil.validateToken(token);
        }catch (CustomJWTException e){
            if(e.getMessage().equals("Expired")){
                return true;
            }
        }
        return false;
    }
}
