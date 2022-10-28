package com.vn.service.impl;

import com.vn.entities.Member;
import com.vn.repository.MemberRepository;
import com.vn.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Override
    public Integer save(Member member) {


        return memberRepository.save(member).getId();
    }

    @Override
    public Member findUserByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
