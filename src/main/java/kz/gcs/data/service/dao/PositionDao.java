package kz.gcs.data.service.dao;

import kz.gcs.domain.Position;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by kaydar on 10/17/16.
 */
@Mapper
public interface PositionDao {
    @Select("SELECT deviceId,serverTime,deviceTime,fixTime,valid,latitude,longitude,speed,accuracy,address,attributes as allAttributes FROM positions WHERE deviceid=#{deviceid} order by devicetime desc limit 1")
    Position getLastPosition(@Param("deviceid") long deviceid);


    @Select("SELECT * FROM positions WHERE deviceid=#{deviceid}")
    List<Position> getAllPositions(@Param("deviceid") long deviceid);

}
