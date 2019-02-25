//package edu.netcracker.backend.dao;
//
//import edu.netcracker.backend.model.Role;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.equalTo;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureTestDatabase
//@ActiveProfiles(profiles = "test")
//public class RoleDAOTest {
//
//    @Autowired
//    private RoleDAO roleDAO;
//
//    @Test
//    public void daoTest(){
//        // Basically a CrudDAOImpl test
//        Role role = new Role();
//        role.setRoleName("testrole");
//        roleDAO.save(role);
//        Role check = roleDAO.find(role.getRoleId()).get();
//        assertThat(check.getRoleName(), equalTo("testrole"));
//
//        role.setRoleName("updatedname");
//        roleDAO.save(role);
//        check = roleDAO.find(role.getRoleId()).get();
//        assertThat(check.getRoleName(), equalTo("updatedname"));
//
//        roleDAO.delete(check);
//        check = roleDAO.find(role.getRoleId()).orElse(null);
//        assertThat(check, equalTo(null));
//    }
//}
