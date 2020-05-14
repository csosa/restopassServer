package restopass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restopass.mongo.UsersRepository;

@Service
public class UserService {

    @Autowired
    UsersRepository usersRepository;


}
