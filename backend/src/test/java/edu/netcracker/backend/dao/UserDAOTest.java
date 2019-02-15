package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;

    @Test
    public void roleListTest(){
        User user = new User();
        user.setUserName("testuser");
        user.setUserPassword("testpassword");
        user.setUserEmail("testemail");
        user.setUserRefreshToken("testrefreshtoken");
        List<Role> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleDAO.find(1).get());
        expectedRoles.add(roleDAO.find(2).get());
        user.getUserRoles().add(expectedRoles.get(0));
        user.getUserRoles().add(expectedRoles.get(1));
        userDAO.save(user);
        User check = userDAO.find(user.getUserId()).get();
        assertThat(check.getUserRoles(), equalTo(expectedRoles));

        user.getUserRoles().remove(1);
        expectedRoles.remove(1);
        userDAO.save(user);
        check = userDAO.find(user.getUserId()).get();
        assertThat(check.getUserRoles(), equalTo(expectedRoles));

        user.getUserRoles().add(roleDAO.find(2).get());
        expectedRoles.add(roleDAO.find(2).get());
        userDAO.save(user);
        check = userDAO.find(user.getUserId()).get();
        assertThat(check.getUserRoles(), equalTo(expectedRoles));

        user.getUserRoles().clear();
        expectedRoles.clear();
        userDAO.save(user);
        check = userDAO.find(user.getUserId()).get();
        assertThat(check.getUserRoles(), equalTo(expectedRoles));

        check = userDAO.findByEmail("testemail").get();
        assertThat(check.getUserEmail(), equalTo("testemail"));

        check = userDAO.findByUsername("testuser").get();
        assertThat(check.getUsername(), equalTo("testuser"));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUsernameNotFoundException(){
        userDAO.loadUserByUsername("notexisting");
    }
}
