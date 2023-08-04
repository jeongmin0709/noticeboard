package com.example.noticeboard.repository;

import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.entity.MemberComment;
import com.example.noticeboard.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberCommentRepository extends JpaRepository<MemberComment, Long> {

    public boolean existsByMemberAndComment(Member member, Comment comment);

    @Modifying
    @Query("delete from MemberComment mc where mc.comment = :comment")
    public void deleteByComment(Comment comment);
}
