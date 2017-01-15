package kz.gcs.data.service;

import kz.gcs.domain.User;

/**
 * Created by kaydar on 10/17/16.
 */
public interface UserService {
    User getUser(String login, String pass);
    User getUserByLogin(String name);
}
