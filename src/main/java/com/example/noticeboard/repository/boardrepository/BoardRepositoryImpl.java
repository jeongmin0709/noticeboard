package com.example.noticeboard.repository.boardrepository;

import com.example.noticeboard.dto.PagingBoardDTO;
import com.example.noticeboard.dto.QPagingBoardDTO;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.QBoard;
import com.example.noticeboard.entity.QComment;
import com.example.noticeboard.entity.QImage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private QBoard board = QBoard.board;
    private QComment comment = QComment.comment;
    private QImage image = QImage.image;

    @Override
    public Map<String, Object> getBoard(Long id) {

        Tuple tuple = queryFactory.select(board, comment.count())
                .from(board)
                .leftJoin(comment).on(comment.board.eq(board))
                .where(board.id.eq(id))
                .fetchOne();
        Map<String, Object> result = new HashMap<>();
        result.put("board",tuple.get(0, Board.class));
        result.put("commentCount", tuple.get(1, Long.class));
        return result;
    }

    @Override
    public Optional<Board> getBoardWithImage(Long id){
        Board result = queryFactory.selectFrom(board)
                .leftJoin(board.imageList).fetchJoin()
                .where(board.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Map<String,Board> getPrevAndNextBoard(Long id){
        QBoard prevBoard = new QBoard("prevBoard");
        QBoard nextBoard = new QBoard("nextBoard");
        List<Board> boardList = queryFactory.selectFrom(board)
                .where(
                        board.id.eq(queryFactory.select(prevBoard.id.max()).from(prevBoard).where(prevBoard.id.lt(id)))
                                .or(board.id.eq(queryFactory.select(nextBoard.id.min()).from(nextBoard).where(nextBoard.id.gt(id))))
                ).fetch();
        Map<String, Board> prevAndNextBoard = new HashMap<>();
        boardList.forEach(findBoard->{
            if(findBoard.getId() > id) prevAndNextBoard.put("next", findBoard);
            else prevAndNextBoard.put("prev", findBoard);
        });
        return prevAndNextBoard;
    }

    @Override
    public Page<PagingBoardDTO> getBoardPage(String type, String keyword, String my, String username, Pageable pageable) {

        QBoard subBoard = new QBoard("subBoard");
        QComment subComment = new QComment("subComment");

        JPAQuery<PagingBoardDTO> query = queryFactory.select(
                new QPagingBoardDTO(
                        board.id,
                        board.title,
                        board.member.username,
                        board.recomendNum,
                        board.viewNum,
                        board.createDate,
                        queryFactory.selectFrom(image).where(image.board.eq(board)).exists(),
                        queryFactory.select(subComment.count()).from(subComment).where(subComment.board.eq(board))
                )).from(board);

        if(StringUtils.hasText(my)) query.leftJoin(comment).on(comment.board.eq(board)).groupBy(board);

        List<PagingBoardDTO> result = query.where(
                searchCondition(type, keyword)
                ,myBoardOrComment(my, username)
                )
                .orderBy(
                        getOrderSpecifier(pageable.getSort())
                                .stream().toArray(OrderSpecifier[]::new)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 페이지 처리를 위한 게시글 수를 알아오는 쿼리
        JPAQuery<Long> countQuery = queryFactory.select(board.countDistinct()).from(board);
        if(StringUtils.hasText(my)) countQuery.leftJoin(comment).on(comment.board.eq(board));
        Long count = countQuery.where(
                searchCondition(type, keyword),
                myBoardOrComment(my, username)
        ).fetchOne();

        return new PageImpl<>(result, pageable, count);
    }

    private BooleanBuilder searchCondition(String type, String keyword){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(type != null && keyword != null){
            if(type.contains("t")) booleanBuilder.or(board.title.containsIgnoreCase(keyword)); // 제목 검색
            if(type.contains("c")) booleanBuilder.or(board.content.containsIgnoreCase(keyword)); // 내용 검색
            if(type.contains("w")) booleanBuilder.or(board.member.username.containsIgnoreCase(keyword)); // 작성자 검색
        }
        return booleanBuilder;
    }

    private BooleanExpression myBoardOrComment(String my, String username){
        if(!StringUtils.hasText(my)) return null;
        else if (my.equals("board")) return board.member.username.eq(username);
        else if (my.equals("comment")) return comment.member.username.eq(username);
        else return null;
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();
        // Sort
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });
        return orders;
    }

}
