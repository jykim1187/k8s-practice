package com.example.ordersystem.ordering.ordering.repository;

import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.ordering.ordering.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderingDetailRepository extends JpaRepository<OrderDetail,Long> {


}
