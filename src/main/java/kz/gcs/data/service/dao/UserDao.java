package kz.gcs.data.service.dao;

import kz.gcs.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by kaydar on 10/17/16.
 */
@Mapper
public interface UserDao {
    @Select("SELECT id, name, admin, hashedpassword, salt, (select deviceid from user_device where userid=id) as deviceid from users where upper(name)=upper(#{name}) and hashedpassword=#{hashedPassword}")
    User getUser(@Param("name") String name, @Param("hashedPassword") String hashedPassword);

    @Select("SELECT id, name, email, phone, admin, hashedpassword, salt, (select deviceid from user_device where userid=id) as deviceid from users where upper(name)=upper(#{name})")
    User getUserByLogin(@Param("name") String name);


}
