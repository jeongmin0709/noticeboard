package com.example.noticeboard.repository.boardrepository;

import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.QBoard;
import com.example.noticeboard.entity.QComment;
import com.example.noticeboard.entity.QImage;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private QBoard board = QBoard.board;
    private QComment comment = QComment.comment;
    private QImage image = QImage.image;

    @Override
    public Optional<Board> getBoardWithAll(Long id) {

        Board result = queryFactory.selectFrom(board)
                .leftJoin(board.imageList).fetchJoin()
                .leftJoin(board.member).fetchJoin()
                .where(board.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    public Optional<Board> getBoardWithImage(Long id){
        Board result = queryFactory.selectFrom(board)
                .leftJoin(board.imageList).fetchJoin()
                .where(board.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Page<Object[]> getBoardPage(String type, String keyword, String my, String username, Pageable pageable) {

        QComment subComment = new QComment("subComment");

        List<Tuple> result = queryFactory
                .select(
                        board,
                        queryFactory.select(subComment.count()).
                                from(subComment).
                                where(subComment.board.eq(board))
                        )
                .from(board)
                .leftJoin(board.member)
                .leftJoin(board.imageList).fetchJoin()
                .leftJoin(comment).on(board.eq(comment.board))
                .where(
                        titleEq(type, keyword),
                        contentEq(type, keyword),
                        writerEq(type, keyword),
                        myBoardOrComment(my, username)
                )
                .orderBy(
                        getOrderSpecifier(pageable.getSort())
                                .stream().toArray(OrderSpecifier[]::new)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(board)
                .fetch();

        // 페이지 처리를 위한 게시글 수를 알아오는 쿼리
        Long count = queryFactory.select(board.countDistinct())
                .from(board).leftJoin(comment).on(comment.board.eq(board))
                .where(
                        titleEq(type, keyword),
                        contentEq(type, keyword),
                        writerEq(type, keyword),
                        myBoardOrComment(my, username)
                )
                .fetchOne();

        return new PageImpl<>(result.stream().map(tuple -> tuple.toArray()).collect(Collectors.toList()), pageable, count);
    }

    private BooleanExpression titleEq(String type, String keyword) {
        if(type != null) return type.contains("t") ? board.title.containsIgnoreCase(keyword) : null;
        else return null;
    }

    private BooleanExpression contentEq(String type, String keyword) {
        if(type != null) return type.contains("c") ? board.content.containsIgnoreCase(keyword) : null;
        else return null;
    }

    private BooleanExpression writerEq(String type, String keyword) {
        if(type != null) return type.contains("w") ? board.member.username.containsIgnoreCase(keyword) : null;
        else return null;
    }

    private BooleanExpression myBoardOrComment(String my, String username){
        if(my == null || username == null) return null;
        else if (my.equals("board")) return board.member.username.containsIgnoreCase(username);
        else if (my.equals("comment")) return comment.member.username.containsIgnoreCase(username);
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
