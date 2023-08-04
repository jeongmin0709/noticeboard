package com.example.noticeboard.repository;

import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.MemberBoard;
import com.example.noticeboard.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberBoardRepository extends JpaRepository<MemberBoard, Long> {
    public boolean existsByMemberAndBoard(Member member, Board board);

    @Modifying
    @Query("delete from MemberBoard mb where mb.board = :board")
    public void deleteByBoard(Board board);
}
