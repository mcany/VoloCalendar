package volo.voloCalendar.dao;

/**
 * Created by Emin Guliyev on 04/08/2014.
 */

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import volo.voloCalendar.entity.Store;
import volo.voloCalendar.entity.User;

import java.io.Serializable;
import java.util.List;

public interface UserDAO extends CrudRepository<User, String>, Serializable {
    public User findById(String id);

    public User findByEmailAndPassword(String email, String password);

    public User findByEmail(String email);

    public List<User> findByAdmin(boolean admin);

    public List<User> findByAdminAndDeleted(boolean admin, boolean deleted);

    @Query("select user from User user where (user.admin=false)AND(user.name like ?1 or user.email like ?1 or user.telephoneNumber like ?1)")
    Page<User> search(String keywordLike, Pageable pageable);
}