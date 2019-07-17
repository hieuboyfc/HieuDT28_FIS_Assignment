package hieuboy.developer.repositories;

import hieuboy.developer.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("select c from Category c where (:name is null or LOWER(c.name) like %:name%)")
    Page<Category> getListCategoryAndSearch(@Param("name") String name,
                                            Pageable pageable);

    /* Tìm thông tin theo name */
    @Query("select c from Category c where LOWER(c.name) = LOWER(:name)")
    Category findByName(@Param("name") String name);

    /* Tìm thông tin danh mục chi tiết */
    @Query("select c from Category c where c.id = :id")
    Category findByCategoryId(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("delete from Category c where c.id = :id")
    void deleteByCategoryId(@Param("id") Integer id);

}
