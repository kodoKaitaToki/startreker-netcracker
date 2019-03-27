package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Role;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
public class RoleDAOTest {

    @Autowired
    private RoleDAO roleDAO;

    @Test
    public void daoTest() {
        Role role = new Role();
        role.setRoleName("testrole");
        roleDAO.save(role);
        Role check = roleDAO.find(role.getRoleId())
                            .get();
        assertThat(check.getRoleName(), equalTo("testrole"));

        role.setRoleName("updatedname");
        roleDAO.save(role);
        check = roleDAO.find(role.getRoleId())
                       .get();
        assertThat(check.getRoleName(), equalTo("updatedname"));

        roleDAO.delete(check);
        check = roleDAO.find(role.getRoleId())
                       .orElse(null);
        assertThat(check, equalTo(null));
    }

    @Test
    public void testFindByRoleNameAdmin() {
        Optional<Role> admin = roleDAO.find("ROLE_ADMIN");

        if (!admin.isPresent()) {
            throw new RuntimeException();
        }

        Assert.assertEquals(admin.get()
                                 .getRoleName(), "ROLE_ADMIN");
    }

    @Test
    public void testFindByRoleNameCarrier() {
        Optional<Role> carrier = roleDAO.find("ROLE_CARRIER");

        if (!carrier.isPresent()) {
            throw new RuntimeException();
        }

        Assert.assertEquals(carrier.get()
                                   .getRoleName(), "ROLE_CARRIER");
    }

    @Test
    public void testFindByRoleNameApprover() {
        Optional<Role> approver = roleDAO.find("ROLE_APPROVER");

        if (!approver.isPresent()) {
            throw new RuntimeException();
        }

        Assert.assertEquals(approver.get()
                                    .getRoleName(), "ROLE_APPROVER");
    }

    @Test
    public void testFindByRoleNameUser() {
        Optional<Role> user = roleDAO.find("ROLE_USER");

        if (!user.isPresent()) {
            throw new RuntimeException();
        }

        Assert.assertEquals(user.get()
                                .getRoleName(), "ROLE_USER");
    }

    @Test
    public void testFindByRoleNameException() {
        Optional<Role> approver = roleDAO.find("asdasdasd");

        Assert.assertNull(approver.orElse(null));
    }
}