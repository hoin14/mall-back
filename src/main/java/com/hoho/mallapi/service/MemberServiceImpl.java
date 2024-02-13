package com.hoho.mallapi.service;

import com.hoho.mallapi.domain.Member;
import com.hoho.mallapi.domain.MemberRole;
import com.hoho.mallapi.dto.MemberDTO;
import com.hoho.mallapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberDTO getKakaoMember(String accessToken) {
        //카카오 연동 닉네임 --- 이메이 주소에 해당
        String nickname = getEmailFromKaKaoAccessToken(accessToken);

        //기존 DB에 회원 정보가 있는 경우/ 없는 경우
        Optional<Member> result = memberRepository.findById(nickname);

        if(result.isPresent()){
            MemberDTO memberDTO = entityDTO(result.get());
            log.info("exist: " + memberDTO);
            return memberDTO;
        }

        Member socialMember = makeSocialMember(nickname);

        memberRepository.save(socialMember);

        MemberDTO memberDTO = entityDTO(socialMember);
        return memberDTO;
    }

    private Member makeSocialMember(String email){
        String tempPassword = makeTempPassword();
        log.info("tempPassword: " + tempPassword);

        Member member = Member.builder()
                .email(email)
                .pw(passwordEncoder.encode(tempPassword))
                .nickname("Social Member")
                .social(true)
                .build();

        member.addRole(MemberRole.USER);

        return member;
    }

    private String getEmailFromKaKaoAccessToken(String accessToken){

        String kaKaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kaKaoGetUserURL).build();

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(uriBuilder.toUri(), HttpMethod.GET, entity, LinkedHashMap.class);

        log.info(response);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
        log.info(bodyMap);

        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("properties");
        log.info("kakaoAccount: " + kakaoAccount);

        String nickname = kakaoAccount.get("nickname");
        log.info("nickname: " + nickname);

        return nickname;

    }

    private String makeTempPassword(){
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < 10; i++){
            buffer.append( (char) ( (int)(Math.random() * 55) + 65));
        }
        return buffer.toString();
    }

}
