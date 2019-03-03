package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
public class ApproverDAOTest {
    private final int ROLE_APPROVER_ID = 4;

    @Autowired
    ApproverDAO approverDAO;

    @Autowired
    RoleDAO roleDAO;

    @Autowired UserDAO userDAO;

    @Test
    public void testApproverDao() {
        //Creating test users
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUserName("testuser" + i);
            user.setUserPassword("testpassword" + i);
            user.setUserEmail("testemail" + i);
            user.setUserTelephone("testphone" + i);
            user.setRegistrationDate(LocalDateTime.now());

            if (i % 2 == 0) {
                user.getUserRoles().add(roleDAO.find(ROLE_APPROVER_ID).get());
                user.setUserIsActivated(true);
            }
            users.add(user);
        }

        //Saving them
        for (User user : users) {
            userDAO.save(user);
        }

        //Must be approvers ONLY
        ArrayList<User> checkList = new ArrayList<>(approverDAO.findAllApprovers());

        for (User user : checkList) {
            assertThat(user.isUserIsActivated(), equalTo(true));
        }

        User user1 = checkList.get(0);
        String newName = "newTestName";
        user1.setUserName(newName);
        approverDAO.update(user1);
        User check = approverDAO.find(user1.getUserId()).get();
        assertThat(check.getUsername(), equalTo(user1.getUsername()));

        assertThat(approverDAO.count(), equalTo(BigInteger.valueOf(5L)));


    }
}
