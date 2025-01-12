package com.secondserve.repository.impl;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.secondserve.dto.StoreDto;
import com.secondserve.entity.QStore;
import com.secondserve.entity.Store;
import com.secondserve.repository.custom.StoreRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QStore store = QStore.store;

    @Override
    public Store findByName(String name) {
        return queryFactory
                .selectFrom(store) // 기본 엔티티 조회
                .where(store.name.eq(name)) // name과 동일한 조건
                .fetchOne(); // 단일 결과 반환
    }


    @Override
    public StoreDto.Spec findSpecById(long id) {
        return queryFactory
                .select(Projections.constructor(StoreDto.Spec.class,
                        store.name,
                        store.category,
                        store.backImage,
                        store.like,
                        store.review,
                        store.greenScore,
                        store.state,
                        store.sale,
                        store.address,
                        store.tel,
                        store.open,
                        store.end,
                        store.rest
                ))
                .from(store)
                .where(store.id.eq(id))
                .fetchOne();
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<StoreDto.Search> searchStores(String name, String category, String address, Double lat, Double lon) {
        BooleanExpression condition = store.isNotNull(); // 기본 조건 추가
        if (name != null) condition = condition.and(containsIgnoreCase(store.name, name));
        if (category != null) condition = condition.and(equalsIgnoreCase(store.category, category));
        if (address != null) condition = condition.and(containsIgnoreCase(store.address, address));
        if (lat != null && lon != null) condition = condition.and(withinDistance(lat, lon));

        return queryFactory
                .select(Projections.constructor(StoreDto.Search.class,
                        store.name,
                        store.category,
                        store.backImage,
                        store.like,
                        store.review,
                        store.greenScore,
                        store.state,
                        store.sale
                ))
                .from(store)
                .where(condition) // 단일 조건으로 변경
                .fetch();
    }



    private BooleanExpression containsIgnoreCase(StringPath path, String value) {
        return value != null ? path.containsIgnoreCase(value) : null;
    }

    // 조건 메서드: 카테고리와 정확히 일치
    private BooleanExpression equalsIgnoreCase(StringPath path, String value) {
        return value != null ? path.equalsIgnoreCase(value) : null;
    }

    // 조건 메서드: 2km 이내의 거리 검색
    private BooleanExpression withinDistance(Double lat, Double lon) {
        if (lat == null || lon == null) {
            return null;
        }
        return Expressions.booleanTemplate(
                "function('ST_Distance_Sphere', point({0}, {1}), point(store.lon, store.lat)) <= 2000",
                lon, lat
        );
    }


    @Override
    public List<StoreDto.Recent> findByIds(List<Long> storeIds) {
        return queryFactory
                .select(Projections.constructor(StoreDto.Recent.class,
                        store.name,
                        store.backImage,
                        store.like,
                        store.review,
                        store.greenScore,
                        store.state,
                        store.sale
                ))
                .from(store)
                .where(
                        store.id.in(storeIds)
                )
                .fetch();
    }

    @Override
    public List<StoreDto.Sale> findBySaleTimeBefore(LocalTime time) {
        return queryFactory
                .select(Projections.constructor(StoreDto.Sale.class,
                        store.name,
                        store.backImage,
                        store.like,
                        store.greenScore
                ))
                .from(store)
                .where(store.sale.loe(time))
                .fetch();
    }

}