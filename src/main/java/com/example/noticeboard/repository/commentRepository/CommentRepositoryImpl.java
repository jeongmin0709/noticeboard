package com.example.noticeboard.repository.commentRepository;

import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.entity.QComment;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private QComment comment = QComment.comment;

    @Override
    public Slice<Map<String, Object>> getCommentListByBoard(Board board ,Pageable pageable) {

        QComment subComment = new QComment("subComment");
        List<Tuple> result = queryFactory
                .select(
                        comment,
                        queryFactory.select(subComment.count()).from(subComment).where(subComment.parent.eq(comment))
                )
                .from(comment)
                .where(comment.board.eq(board),
                        comment.parent.isNull()
                )
                .orderBy(getOrderSpecifier(pageable.getSort())
                        .stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if(result.size() > pageable.getPageSize()){
            hasNext = true;
            result.remove(pageable.getPageSize());
        }
        List<Map<String, Object>> mapList = result.stream().map(tuple -> {
            Map<String, Object> map = new HashMap<>();
            map.put("comment", tuple.get(0, Comment.class));
            map.put("childCommentCount", tuple.get(1, Long.class));
            return map;
        }).collect(Collectors.toList());
        return new SliceImpl<>(mapList, pageable, hasNext);
    }

    @Override
    public Slice<Comment> getCommentListByParent(Comment parent, Pageable pageable) {

        List<Comment> result = queryFactory.selectFrom(comment)
                .where(comment.parent.eq(parent))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1)
                .fetch();

        boolean hasNext = false;
        if(result.size() > pageable.getPageSize()){
            hasNext = true;
            result.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(result, pageable, hasNext);
    }

    private BooleanExpression idGtOrLt(Long id, Boolean isAsc) {
        if(id == null) return null;
        return (isAsc)? comment.id.gt(id): comment.id.lt(id);
    }


    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();
        // Sort
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Comment.class, "comment");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });
        return orders;
    }
}
