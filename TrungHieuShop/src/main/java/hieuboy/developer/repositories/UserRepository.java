package hieuboy.developer.repositories;

import hieuboy.developer.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u " +
            "where (:username is null or LOWER(u.username) like %:username%) " +
            "and (:email is null or LOWER(u.email) like %:email%) " +
            "and (:phone is null or LOWER(u.phone) like %:phone%) ")
    Page<User> getListUserAndSearch(@Param("username") String username,
                                    @Param("email") String email,
                                    @Param("phone") String phone,
                                    Pageable pageable);

    /*Tìm thông tin người đăng nhập theo username */
    @Query("select u from User u where LOWER(u.username) = LOWER(:username)")
    User findByUsername(@Param("username") String username);

    /*Tìm thông tin địa chỉ email */
    @Query("SELECT u from User u where LOWER(u.email) = LOWER(:email)")
    User findByEmail(@Param("email") String email);

    /* Tìm thông tin người dùng chi tiết */
    @Query("select u from User u where u.id = :id")
    User findByUserId(@Param("id") Integer id);

    @Query("select u from User u where u.id in :ids")
    List<User> findByUserIds(@Param("ids") List<Integer> ids);
}
