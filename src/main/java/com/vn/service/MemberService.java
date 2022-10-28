package com.vn.service;

import com.vn.entities.Member;

public interface MemberService {
    Integer save(Member member);

    Member findUserByEmail(String email);
}
