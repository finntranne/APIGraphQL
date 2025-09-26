package vn.iotstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.iotstar.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByPriceAsc();

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :catId")
    List<Product> findByCategoryId(@Param("catId") Long catId);
}
