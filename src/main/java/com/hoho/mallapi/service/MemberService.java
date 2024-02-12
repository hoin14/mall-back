package com.hoho.mallapi.service;

import com.hoho.mallapi.dto.MemberDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MemberService {

    MemberDTO getKakaoMember(String accessToken);

}
