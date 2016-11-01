package kz.gcs.data.service.impl;

import kz.gcs.data.service.dao.NameDao;
import kz.gcs.data.service.NameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by kaydar on 10/17/16.
 */
@Component
public class NameServiceImpl implements NameService {

    @Autowired
    private NameDao nameDao;

    @Override
    public String getName() {


        return nameDao.name();
    }
}
