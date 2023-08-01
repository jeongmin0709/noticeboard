package com.example.noticeboard;

import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.entity.Image;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.member.Role;
import com.example.noticeboard.repository.MemberRepository;
import com.example.noticeboard.repository.boardrepository.BoardRepository;
import com.example.noticeboard.repository.commentRepository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@TestComponent
public class DummyDataProvider {
    private final MemberRepository memberRepository;

    private final BoardRepository boardRepository;

    private final CommentRepository commentRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public DummyDataProvider(MemberRepository memberRepository, BoardRepository boardRepository, CommentRepository commentRepository) {
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
    }

    private void insertDummyMember(){
        List<Member> memberList = new ArrayList<>();
        for(int i=1; i<=10; i++){
            Member member = Member.builder()
                    .username("user"+i)
                    .password(passwordEncoder.encode("1111"))
                    .name("name..."+i)
                    .email("user"+i+"@gmail.com")
                    .build();
            member.addRole(Role.ROLE_USER);
            memberList.add(member);
        };
        memberRepository.saveAll(memberList);
    }

    private void insertDummyBoard(){
        List<Board> boardList = new ArrayList<>();
        for(int i=1; i<=10; i++){
            Member member = Member.builder().username("user"+i).build();
            for (int j=1; j<=10; j++) {
                Board board = Board.builder()
                        .member(member)
                        .title("title..." + ((i-1)*10+j))
                        .content("content..." + ((i-1)*10+j))
                        .build();
                for(int k=1; k<=6; k++){
                    Image image = Image.builder()
                            .uuid(UUID.randomUUID().toString())
                            .name("test.png")
                            .path("/test")
                            .board(board)
                            .build();
                    board.addImage(image);
                }
                boardList.add(board);
            }
        };
        boardRepository.saveAll(boardList);
    }
    private void insertDummyComment(){
        List<Comment> commentList = new ArrayList<>();
        for(int i = 1; i <= 10; i++){
            Member member = Member.builder().username("user"+ i).build();
            for(int j = 1; j <= 10; j++) {
                Board board = Board.builder().id((long)(i-1)*10+j).build();
                for (int k = 1; k <= 10; k++) {
                    Comment comment = Comment.builder()
                            .board(board)
                            .member(member)
                            .content("content..." + ((i-1)*100+(j-1)*10+k))
                            .build();
                    commentList.add(comment);
                }
            }
        }
        commentRepository.saveAll(commentList);
    }
    private void insertDummyChildComment(){
        List<Comment> childCommentList = new ArrayList<>();
        for(int i=1; i<=10; i++){
            Member member = Member.builder().username("user"+i).build();
            for(int j=1; j<=10; j++) {
                Board board = Board.builder().id((long)(i-1)*10+j).build();
                for (int k = 1; k <= 10; k++) {
                    Comment comment = Comment.builder().id((long)(i-1)*100+(j-1)*10+k).build();
                    for (int l = 1; l <= 10; l++) {
                        Comment childComment = Comment.builder()
                                .board(board)
                                .parent(comment)
                                .member(member)
                                .content("content..." + ((i-1)*1000+(j-1)*100+(k-1)*10+l))
                                .build();
                        childCommentList.add(childComment);
                    }
                }
            }
        }
        commentRepository.saveAll(childCommentList);
    }
    @PostConstruct
    private void insertDummyData(){
        insertDummyMember();
        insertDummyBoard();
        insertDummyComment();
        insertDummyChildComment();
    }

}
