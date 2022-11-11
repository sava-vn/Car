package com.vn.service;

import com.vn.entities.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public interface MemberService {
    Member updateMember(Member member);

    Integer save(Member member);

    Member findUserByEmailAndFullName(String email, String fullName);

    void updateResetPasswordToken(String token, String email);
    Member findByResetPasswordToken(String token);
    void updatePassword(Member member, String newPassword);

    Member findByEmail(String email);

    Optional<Member> findUserById(Integer id);
}
