package com.vn.repository;

import com.vn.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByEmail(String email);
}
