package com.hoho.mallapi.repository;

import com.hoho.mallapi.domain.Cart;
import com.hoho.mallapi.domain.CartItem;
import com.hoho.mallapi.domain.Member;
import com.hoho.mallapi.domain.Product;
import com.hoho.mallapi.dto.CartItemListDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class CartRepositoryTest {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    @Commit
    @Test
    public void testInsertByProduct(){
        String email = "user1@aaa.com";
        Long pno = 70L;
        int qty = 3;

        //이메일 상품번호로 장바구니 아이템 확인 없으면 추가 있으면 수량 변경해서 저장
        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

        //이미 사용자의 장바구니에 담겨있는 상품
        if(cartItem != null){
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);
            return;
        }

        //사용자의 장바구니에 장바구니 아이템 만들어서 저장
        //장바구니 자체가 없을 수 도 있음
        Optional<Cart> result = cartRepository.getCartOfMember(email);

        Cart cart = null;

        if(result.isEmpty()){
            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();

            cart = cartRepository.save(tempCart);
        }else{ //장바구니는 있으나 해당 상품의 장바구니 아이템은 없는 경우
            cart = result.get();
        }

        //if(cartItem == null){
            Product product = Product.builder().pno(pno).build();
            cartItem = CartItem.builder().cart(cart).product(product).qty(qty).build();
        //}

        cartItemRepository.save(cartItem);

    }

    @Test
    public void testListOfMember(){
        String email = "user1@aaa.com";

        List<CartItemListDTO> cartItemListDTOList = cartItemRepository.getItemsOfCartDTOByEmail(email);

        for (CartItemListDTO dto:cartItemListDTOList) {
            log.info(dto);
        }
    }

    @Transactional
    @Commit
    @Test
    public void testUpdateByCino(){
        Long cino = 1L;
        int qty = 5;

        Optional<CartItem> result = cartItemRepository.findById(cino);
        CartItem cartItem = result.orElseThrow();
        cartItem.changeQty(qty);
        cartItemRepository.save(cartItem);
    }

    @Test
    public void testDeleteThenList(){
        Long cino = 4L;
        Long cno = cartItemRepository.getCartFromItem(cino);

        cartItemRepository.deleteById(cino);
        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOByCart(cno);

        for (CartItemListDTO dto : cartItemList){
            log.info(dto);
        }
    }

}
