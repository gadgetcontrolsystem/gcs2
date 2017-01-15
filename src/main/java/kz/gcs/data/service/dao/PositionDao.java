package kz.gcs.data.service.dao;

import kz.gcs.domain.Position;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by kaydar on 10/17/16.
 */
@Mapper
public interface PositionDao {
    @Select("SELECT * FROM positions order by devicetime desc limit 1")
    Position getLastPosition();


    @Select("SELECT * FROM positions")
    List<Position> getAllPositions();

}
