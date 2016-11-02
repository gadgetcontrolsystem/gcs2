package kz.gcs.data.service.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by kaydar on 10/17/16.
 */
@Mapper
public interface LocationDao {
    @Select("SELECT name FROM demo")
    String name();

}
