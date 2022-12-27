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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private QBoard board = QBoard.board;
    private QImage image = QImage.image;
    private QComment comment = QComment.comment;

    @Override
    public Optional<Board> getBoardWithMember(Long id) {

        List<Board> reuslt = queryFactory.selectFrom(board)
                .join(board.member).fetchJoin()
                .join(image).on(board.eq(image.board))
                .fetch();

        return Optional.empty();
    }

    @Override
    public Page<Object[]> getBoardPage(String type, String keyword, Pageable pageable) {

        List<Tuple> result = queryFactory.select(board, comment.count())
                .from(board).leftJoin(board.member).fetchJoin()
                .leftJoin(comment).on(board.eq(comment.board))
                .where(
                        titleEq(type, keyword),
                        contentEq(type, keyword),
                        writerEq(type, keyword)
                )
                .orderBy(
                        getOrderSpecifier(pageable.getSort())
                                .stream().toArray(OrderSpecifier[]::new)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(board)
                .fetch();
        Long count = queryFactory.select(board.count()).from(board).fetchOne();
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
        if(type != null) return type.contains("w") ? board.member.name.containsIgnoreCase(keyword) : null;
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
