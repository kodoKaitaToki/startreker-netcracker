package edu.netcracker.backend.util;

import edu.netcracker.backend.dao.RoleDAO;
import edu.netcracker.backend.model.Role;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AuthorityUtils {

    private final RoleDAO roleDAO;
    public static Role ROLE_ADMIN;
    public static Role ROLE_USER;
    public static Role ROLE_CARRIER;

    @Autowired
    public AuthorityUtils(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @PostConstruct
    public void init() {
        ROLE_ADMIN = roleDAO.find(1).orElseThrow(
                () -> new BeanInitializationException("AuthorityUtils: ROLE_ADMIN Not Found in the Database"));
        ROLE_USER = roleDAO.find(2).orElseThrow(
                () -> new BeanInitializationException("AuthorityUtils: ROLE_USER Not Found in the Database"));
        ROLE_CARRIER = roleDAO.find(3).orElseThrow(
                () -> new BeanInitializationException("AuthorityUtils: ROLE_CARRIER Not Found in the Database"));
    }
}
