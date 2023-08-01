package com.example.noticeboard.repository.notificationrepository;

import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.notification.Notification;
import com.example.noticeboard.entity.notification.QNotification;
import com.querydsl.core.BooleanBuilder;
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
import org.springframework.data.jpa.repository.Modifying;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private QNotification notification = QNotification.notification;

    @Override
    @Modifying(clearAutomatically = true)
    public void updateNotificationIsRead(List<Long> idList){

        queryFactory.update(notification).
                set(notification.isRead, true)
                .where(notification.id.in(idList))
                .execute();
    }

    @Override
    public Slice<Notification> getNotificationListByMember(Member member, Pageable pageable){
        List<Notification> result = queryFactory.selectFrom(notification)
                .where(notification.member.eq(member))
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
        return new SliceImpl<>(result, pageable, hasNext);
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();
        // Sort
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Notification.class, "notification");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });
        return orders;
    }
}
