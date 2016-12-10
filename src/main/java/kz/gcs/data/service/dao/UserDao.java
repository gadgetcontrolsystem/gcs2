package kz.gcs.data.service.dao;

import kz.gcs.domain.Location;
import kz.gcs.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by kaydar on 10/17/16.
 */
@Mapper
public interface UserDao {
    @Select("SELECT * from users where upper(login)=upper(#{login}) and password=#{pass}")
    User getUser(@Param("login") String login, @Param("pass") String pass);

    @Select("SELECT * from users where upper(login)=upper(#{login})")
    User getUserByLogin(@Param("login") String login);


}
