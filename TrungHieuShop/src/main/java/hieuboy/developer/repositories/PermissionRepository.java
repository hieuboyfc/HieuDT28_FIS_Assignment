package hieuboy.developer.repositories;

import hieuboy.developer.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    /*Tìm danh sách chức năng theo danh sách id truyền vào*/
    @Query("select p from Permission p where p.id in :ids")
    Set<Permission> findByPermissionByIds(@Param("ids") List<Integer> ids);

    /* Tìm thông tin quyền chi tiết */
    @Query("select p from Permission p where p.id = :id")
    Permission findByPermissionId(@Param("id") Integer id);

    /*Tìm thông tin link */
    @Query("select p from Permission p where LOWER(p.link) = LOWER(:link)")
    Permission findByLinkCheck(@Param("link") String link);

    /*Tìm danh sách chức năng theo đường dẫn*/
    List<Permission> findByLink(String link);
}
