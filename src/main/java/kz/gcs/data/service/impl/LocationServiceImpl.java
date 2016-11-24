package kz.gcs.data.service.impl;

import kz.gcs.data.service.dao.LocationDao;
import kz.gcs.data.service.LocationService;
import kz.gcs.domain.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by kaydar on 10/17/16.
 */
@Component
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationDao locationDao;

    @Override
    public Location getLastLocation(long gadgetId) {
        return locationDao.getLastLocation();
    }

    @Override
    public List<Location> getLocations(long gadgetId) {
        return locationDao.getAllLocations();
    }
}
