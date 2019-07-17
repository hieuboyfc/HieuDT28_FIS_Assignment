package hieuboy.developer.repositories;

import hieuboy.developer.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.*;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /* Tìm tất cả các nhóm người dùng theo id */
    @Query("select r from Role r where r.id in :ids")
    Set<Role> findAllRoleById(@Param("ids") List<Integer> ids);

    /* Tìm mã của Role */
    @Query("select r from Role r where r.id = :id")
    Role findByRoleId(@Param("id") Integer id);

    @Query("select r from Role r where (:name is null or LOWER(r.name) like %:name%) ")
    Page<Role> getListRoleAndSearch(@Param("name") String name, Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from Role r where r.id = :id")
    void deleteByRoleId(@Param("id") Integer id);
}
