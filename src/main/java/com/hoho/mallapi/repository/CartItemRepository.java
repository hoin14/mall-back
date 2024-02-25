package com.hoho.mallapi.repository;

import com.hoho.mallapi.domain.CartItem;
import com.hoho.mallapi.dto.CartItemListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    //특정한 사용자의 모든 장바구니 아이템들을 가져오기 -> Input : email / out -> CartItemListDTO
    @Query("select " +
            " new com.hoho.mallapi.dto.CartItemListDTO(ci.cino, ci.qty, p.pname, p.price, pi.fileName) " +
            "from CartItem ci " +
            "inner join Cart mc on ci.cart = mc " +
            "left join Product p on ci.product = p " +
            "left join p.imageList pi " +
            "where " +
            "mc.owner.email = :email and pi.ord = 0 " +
            "order by ci.cino desc")
    List<CartItemListDTO> getItemsOfCartDTOByEmail(@Param("email") String email);

    //이메일, 상품 번호로 해당 상품이 장바구니 아이템으로 존재하는지 확인
    @Query("select ci from CartItem ci left join Cart c on ci.cart = c " +
            "where c.owner.email = :email and ci.product.pno = :pno")
    CartItem getItemOfPno(@Param("email") String email, @Param("pno") Long pno);

    //장바구니 아이템 번호로 장바구니 번호를 얻어오려고 하는 경우
    @Query("select c.cno from Cart c left join CartItem ci on ci.cart = c " +
            "where ci.cino = :cino")
    Long getCartFromItem(@Param("cino") Long cino);

    //장바구니 아이템 번호로 장바구니 아이템들 조회
    @Query("select " +
            " new com.hoho.mallapi.dto.CartItemListDTO(ci.cino, ci.qty, p.pname, p.price, pi.fileName) " +
            "from CartItem ci " +
            "inner join Cart c on ci.cart = c " +
            "left join Product p on ci.product = p " +
            "left join p.imageList pi " +
            "where " +
            "pi.ord = 0 and c.cno = :cno " +
            "order by ci.cino desc")
    List<CartItemListDTO> getItemsOfCartDTOByCart(@Param("cno") Long cno);


}
