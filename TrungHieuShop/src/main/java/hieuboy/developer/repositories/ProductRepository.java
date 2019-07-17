package hieuboy.developer.repositories;

import hieuboy.developer.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("select p from Product p where (:name is null or LOWER(p.name) like %:name%) " +
            "and (:category = 0 or :category is null or p.category.id = :category) " +
            "and (:manufacturer = 0 or :manufacturer is null or p.manufacturer.id = :manufacturer)" +
            "and (:code is null or LOWER(p.code) = :code ) " +
            "and (:status = 0 or :status is null or p.status = :status) ")
    Page<Product> getListProductAndSearch(@Param("name") String name,
                                          @Param("code") String code,
                                          @Param("status") Integer status,
                                          @Param("category") Integer category,
                                          @Param("manufacturer") Integer manufacturer,
                                          Pageable pageable);

    /* Tìm thông tin theo name */
    @Query("select p from Product p where LOWER(p.name) = LOWER(:name)")
    Product findByName(@Param("name") String name);

    /* Tìm thông tin sản phẩm chi tiết */
    @Query("select p from Product p where p.id = :id")
    Product findByProductId(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("delete from Product p where p.id = :id")
    void deleteByProductId(@Param("id") Integer id);

}
