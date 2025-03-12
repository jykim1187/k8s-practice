package com.example.ordersystem.ordering.ordering.domain;

import com.example.ordersystem.common.BaseTimeEntity;
import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.ordering.ordering.dtos.OrderDetailResDto;
import com.example.ordersystem.ordering.ordering.dtos.OrderListResDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.ordersystem.ordering.ordering.domain.OrderStatus.CANCELLED;
import static com.example.ordersystem.ordering.ordering.domain.OrderStatus.ORDERED;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Ordering extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus orderStatus= ORDERED;
    @OneToMany(mappedBy = "ordering", cascade = CascadeType.PERSIST) //remove는 사용하지 않는 이유는 주문을 취소시 삭제가 아니라 이넘타입을 CANCELLED로 할 꺼니까
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public OrderListResDto fromEntity(){
        List<OrderDetailResDto> orderDetailResDtos = new ArrayList<>();
            for(OrderDetail od : this.getOrderDetails()){
                OrderDetailResDto orderDetailResDto = OrderDetailResDto.builder()
                        .productName(od.getProduct().getName())
                        .detailId(od.getId())
                        .count(od.getQuantity())
                        .build();
                orderDetailResDtos.add(orderDetailResDto);
            }
            OrderListResDto orderDto =  OrderListResDto.builder()
                    .id(this.getId())
                    .orderStatus(this.getOrderStatus().toString())
                    .orderDetails(orderDetailResDtos)
                    .memberEmail(this.getMember().getEmail())
                    .build();
            return orderDto;
    }

    public void changeStatus(){
        this.orderStatus = CANCELLED;
    }
}
