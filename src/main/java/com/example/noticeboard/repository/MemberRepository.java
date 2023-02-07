package com.example.noticeboard.repository;

import com.example.noticeboard.entity.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Member m where m.username = :username and m.fromSocial = :fromSocial")
    public Optional<Member> findByUsername(String username, boolean fromSocial);

    @Query("select m from Member m where m.email = :email and m.fromSocial = :fromSocial")
    public Optional<Member> findByEmail(String email, boolean fromSocial);


    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
    public boolean existsByUsernameAndEmail(String username, String email);
    public boolean existsByNameAndEmail(String name, String email);
}
