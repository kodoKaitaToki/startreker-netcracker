package edu.netcracker.backend.utils;

import edu.netcracker.backend.dao.RoleDAO;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Arrays;

@Service
public class AuthorityUtils {

    private final RoleDAO roleDAO;
    private final Environment env;
    public static Role ROLE_ADMIN;
    public static Role ROLE_USER;
    public static Role ROLE_CARRIER;
    public static Role ROLE_APPROVER;

    public static boolean DEBUG_SU = false;
    public static User DEBUG_SUPERUSER;

    @Autowired
    public AuthorityUtils(RoleDAO roleDAO, Environment env) {
        this.roleDAO = roleDAO;
        this.env = env;
    }

    @PostConstruct
    public void init() {
        ROLE_ADMIN = roleDAO.find(1).orElseThrow(
                () -> new BeanInitializationException("AuthorityUtils: ROLE_ADMIN Not Found in the Database"));
        ROLE_USER = roleDAO.find(4).orElseThrow(
                () -> new BeanInitializationException("AuthorityUtils: ROLE_USER Not Found in the Database"));
        ROLE_CARRIER = roleDAO.find(3).orElseThrow(
                () -> new BeanInitializationException("AuthorityUtils: ROLE_CARRIER Not Found in the Database"));
        ROLE_APPROVER = roleDAO.find(2).orElseThrow(
                () -> new BeanInitializationException("AuthorityUtils: ROLE_APPROVER Not Found in the Database"));

        DEBUG_SU = Arrays.asList(env.getActiveProfiles()).contains("debug_su");

        if(DEBUG_SU){
            DEBUG_SUPERUSER = new User("su", "su", "su@su.com");
            DEBUG_SUPERUSER.setUserId(0);
            DEBUG_SUPERUSER.setUserIsActivated(true);
            DEBUG_SUPERUSER.setRegistrationDate(LocalDate.now());
            DEBUG_SUPERUSER.setUserRoles(Arrays.asList(ROLE_ADMIN, ROLE_USER, ROLE_CARRIER, ROLE_APPROVER));
        }
    }
}
