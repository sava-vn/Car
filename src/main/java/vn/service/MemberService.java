package vn.service;

import com.vn.entitiess.Member;

public interface MemberService {
    Integer save(Member member);

    Member findUserByEmail(String email);
}
