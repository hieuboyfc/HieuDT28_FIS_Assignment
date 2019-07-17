package hieuboy.developer.repositories;

import hieuboy.developer.entities.GroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupPermissionRepository extends JpaRepository<GroupPermission, Integer> {

    List<GroupPermission> findByParentID(Integer parentId);

    @Query("select gr from GroupPermission gr where gr.id = :id")
    GroupPermission findByGroupPermissionId(@Param("id") Integer id);
}
