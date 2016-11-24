package kz.gcs.data.service.dao;

import kz.gcs.domain.Location;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by kaydar on 10/17/16.
 */
@Mapper
public interface LocationDao {
    @Select("SELECT id,time, country, city, LONGTITUDE as lon, LATITUDE as lat,GADGET_ID as gadgetId FROM locations")
    Location getLastLocation();


    @Select("SELECT * FROM locations")
    List<Location> getAllLocations();

}
