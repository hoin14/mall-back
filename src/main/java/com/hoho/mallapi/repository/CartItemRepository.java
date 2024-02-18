package com.hoho.mallapi.repository;

import com.hoho.mallapi.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    //특정한 사용자의 모든 장바구니 아이템들을 가져오기 -> Input : email / out -> CartItemListDTO

}
