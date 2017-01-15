package kz.gcs.data.service.impl;

import kz.gcs.data.service.dao.PositionDao;
import kz.gcs.data.service.PositionService;
import kz.gcs.domain.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by kaydar on 10/17/16.
 */
@Component
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionDao positionDao;

    @Override
    public Position getLastPosition(long deviceId) {
        return positionDao.getLastPosition();
    }

    @Override
    public List<Position> getPositions(long deviceId) {
        return positionDao.getAllPositions();
    }
}
