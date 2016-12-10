package kz.gcs.data.service;

import kz.gcs.domain.Location;
import kz.gcs.domain.User;

import java.util.List;

/**
 * Created by kaydar on 10/17/16.
 */
public interface UserService {
    User getUser(String login, String pass);
}
