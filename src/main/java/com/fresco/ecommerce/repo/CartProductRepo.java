package com.fresco.ecommerce.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fresco.ecommerce.models.CartProduct;
import com.fresco.ecommerce.models.User;

@Repository
public interface CartProductRepo extends JpaRepository<CartProduct, Integer>{
	Optional<CartProduct> findByCartUserUserIdAndProductProductId(Integer userId,Integer productId);
	
	@Transactional
	void deleteByCartUserUserIdAndProductProductId(Integer userId,Integer productId);

	List<CartProduct> findByProductProductId(Integer pid);
	

}
