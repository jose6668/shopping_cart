package shopping_cart.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shopping_cart.backend.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findFirstByCartIdAndProductId(Long cartId, Long productId);

    List<CartItem> findAllByCartIdOrderByIdAsc(Long cartId);
}
