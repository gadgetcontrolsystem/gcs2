package kz.gcs.data.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kz.gcs.data.service.dao.PositionDao;
import kz.gcs.data.service.PositionService;
import kz.gcs.domain.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by kaydar on 10/17/16.
 */
@Component
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionDao positionDao;

    @Override
    public Position getLastPosition(long deviceId) {
        Position lastPosition = positionDao.getLastPosition(deviceId);
        lastPosition.convertJsonAttrs();
        return lastPosition;
    }

    @Override
    public List<Position> getPositions(long deviceId) {
        return positionDao.getAllPositions(deviceId);
    }
}
