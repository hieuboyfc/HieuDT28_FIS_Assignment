package hieuboy.developer.repositories;

import hieuboy.developer.entities.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {

    @Query("select m from Manufacturer m where (:name is null or LOWER(m.name) like %:name%)")
    Page<Manufacturer> getListManufacturerAndSearch(@Param("name") String name,
                                                    Pageable pageable);

    /* Tìm thông tin theo name */
    @Query("select m from Manufacturer m where LOWER(m.name) = LOWER(:name)")
    Manufacturer findByName(@Param("name") String name);

    /* Tìm thông tin hãng sản xuất chi tiết */
    @Query("select m from Manufacturer m where m.id = :id")
    Manufacturer findByManufacturerId(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("delete from Manufacturer m where m.id = :id")
    void deleteByManufacturerId(@Param("id") Integer id);

}
