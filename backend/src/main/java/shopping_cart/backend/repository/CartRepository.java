package shopping_cart.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shopping_cart.backend.entity.Cart;
import shopping_cart.backend.entity.CartStatus;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findFirstByUserIdAndStatus(Long userId, CartStatus status);
}
