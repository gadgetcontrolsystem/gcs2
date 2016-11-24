package kz.gcs.data.service.impl;

import kz.gcs.data.service.dao.LocationDao;
import kz.gcs.data.service.LocationService;
import kz.gcs.domain.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by kaydar on 10/17/16.
 */
@Component
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationDao locationDao;

    @Override
    public Location getLastLocation() {
        return locationDao.getLastLocation();
    }
}
