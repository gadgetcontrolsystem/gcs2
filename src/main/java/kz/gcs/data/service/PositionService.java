package kz.gcs.data.service;

import kz.gcs.domain.Position;

import java.util.List;

/**
 * Created by kaydar on 10/17/16.
 */
public interface PositionService {
    Position getLastPosition(long deviceId);

    List<Position> getPositions(long deviceId);
}
