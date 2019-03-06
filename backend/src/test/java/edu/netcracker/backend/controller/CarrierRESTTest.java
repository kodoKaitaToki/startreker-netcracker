package edu.netcracker.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.netcracker.backend.BackendApplication;
import edu.netcracker.backend.dao.RoleDAO;
import edu.netcracker.backend.message.response.UserDTO;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.service.UserService;
import edu.netcracker.backend.utils.AuthorityUtils;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.http.*;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles(profiles = "test")
public class CarrierRESTTest {

    private static final String CREATE_TEST_DB_SCRIPT = "schema.sql";

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("dataSourceTestH2")
    private DataSource dataSource;

    @Autowired
    private RoleDAO roleDAO;

    @Before
    public void beforeTest() throws SQLException, IOException {
        headers = new HttpHeaders();
        startDB();
    }

    @Test
    public void getCarrierByUsernameTestGood() throws Exception {
        String actual = getResponse("/api/v1/admin/carrier-by-username?username=vitya", HttpMethod.GET, null);

        UserDTO userDTO = UserDTO.from(userService.findByUsernameWithRole("vitya", AuthorityUtils.ROLE_CARRIER));

        String expected = objectMapper.writeValueAsString(userDTO);

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getCarrierByUsernameTestWithAbsentUsername() throws Exception {
        String actual = getResponse("/api/v1/admin/carrier-by-username?username=absentUserName", HttpMethod.GET, null);

        actual = deleteNodes(actual, "timestamp");

        String expected = "{\n" +
                "    \"status\": 404,\n" +
                "    \"error\": \"NOT_FOUND\",\n" +
                "    \"message\": \"Carrier absentUserName not found\"\n" +
                "}";

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getCarrierByEmailTestGood() throws Exception {
        String actual = getResponse("/api/v1/admin/carrier-by-email?email=a@gmail.com",
                HttpMethod.GET,
                null);

        UserDTO userDTO = UserDTO.from(userService.findByEmailWithRole("a@gmail.com", AuthorityUtils.ROLE_CARRIER));

        String expected = objectMapper.writeValueAsString(userDTO);

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getCarrierByEmailTestWithAbsentEmail() throws Exception {
        String actual = getResponse("/api/v1/admin/carrier-by-email?email=absentUserName@gmail.com",
                HttpMethod.GET,
                null);

        actual = deleteNodes(actual, "timestamp");

        String expected = "{\n" +
                "    \"status\": 404,\n" +
                "    \"error\": \"NOT_FOUND\",\n" +
                "    \"message\": \"Carrier with email absentUserName@gmail.com not found\"\n" +
                "}";

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getCarrierByEmailTestWithInvalidEmail() throws Exception {
        String actual = getResponse("/api/v1/admin/carrier-by-email?email=aasfasdasd",
                HttpMethod.GET,
                null);

        actual = deleteNodes(actual, "timestamp");

        String expected = "{\n" +
                "    \"status\": 400,\n" +
                "    \"error\": \"BAD_REQUEST\",\n" +
                "    \"message\": \"getCarrierByEmail.email: must be a well-formed email address\"\n" +
                "}";

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getCarrierByIdTestGood() throws Exception {
        User user = userService.findByUsernameWithRole("eeee", AuthorityUtils.ROLE_CARRIER);

        String actual = getResponse("/api/v1/admin/carrier/" + user.getUserId(), HttpMethod.GET, null);

        String expected = objectMapper.writeValueAsString(UserDTO.from(user));

        JSONAssert.assertEquals(expected, actual, false);
    }


    @Test
    public void getCarrierByIdTestWithAbsentId() throws Exception {
        String actual = getResponse("/api/v1/admin/carrier/12412",
                HttpMethod.GET,
                null);
        actual = deleteNodes(actual, "timestamp");

        String expected = "{\n" +
                "    \"status\": 404,\n" +
                "    \"error\": \"NOT_FOUND\",\n" +
                "    \"message\": \"Carrier with id 12412 not found\"\n" +
                "}";

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getAllCarrierTestGood() throws Exception {
        List<User> users = userService.findAllWithRole(AuthorityUtils.ROLE_CARRIER);

        List<UserDTO> userDTOS = users.stream().map(UserDTO::from).collect(Collectors.toList());

        String expected = objectMapper.writeValueAsString(userDTOS);

        String actual = getResponse("/api/v1/admin/carrier", HttpMethod.GET, null);

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getAllCarrierTestEmptyCarrierList() throws Exception {
        executeScript(CREATE_TEST_DB_SCRIPT);

        String actual = getResponse("/api/v1/admin/carrier", HttpMethod.GET, null);
        actual = deleteNodes(actual, "timestamp");

        String expected = "{\n" +
                "    \"status\": 404,\n" +
                "    \"error\": \"NOT_FOUND\",\n" +
                "    \"message\": \"No carriers yet\"\n" +
                "}";

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getAllCarrierInRangeTestGood() throws Exception {
        User firstUser = userService.findByUsernameWithRole("aaaa", AuthorityUtils.ROLE_CARRIER);
        User lastUser = userService.findByUsernameWithRole("eeee", AuthorityUtils.ROLE_CARRIER);

        List<User> users = userService.findByRangeIdWithRole(firstUser.getUserId(),
                lastUser.getUserId(),
                AuthorityUtils.ROLE_CARRIER);

        List<UserDTO> userDTOS = users.stream().map(UserDTO::from).collect(Collectors.toList());

        String expected = objectMapper.writeValueAsString(userDTOS);

        String actual = getResponse("/api/v1/admin/carrier-in-range-id?startId="+
                firstUser.getUserId()+
                "&endId=" + lastUser.getUserId(), HttpMethod.GET, null);

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getAllCarrierInRangeTestEmptyCarrierList() throws Exception {
        executeScript(CREATE_TEST_DB_SCRIPT);

        String actual = getResponse("/api/v1/admin/carrier-in-range-id?startId=1&endId=1000", HttpMethod.GET, null);
        actual = deleteNodes(actual, "timestamp");

        String expected = "{\n" +
                "    \"status\": 404,\n" +
                "    \"error\": \"NOT_FOUND\",\n" +
                "    \"message\": \"No carriers in id range yet\"\n" +
                "}";

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getPaginationTestGood() throws Exception {
        List<User> users = userService.paginationWithRole(0,
                100,
                AuthorityUtils.ROLE_CARRIER);

        List<UserDTO> userDTOS = users.stream().map(UserDTO::from).collect(Collectors.toList());

        String expected = objectMapper.writeValueAsString(userDTOS);

        String actual = getResponse("/api/v1/admin/pagination?from=0&number=100", HttpMethod.GET, null);

        JSONAssert.assertEquals(expected, actual, false);


        users = userService.paginationWithRole(2,
                1,
                AuthorityUtils.ROLE_CARRIER);

        userDTOS = users.stream().map(UserDTO::from).collect(Collectors.toList());

        expected = objectMapper.writeValueAsString(userDTOS);

        actual = getResponse("/api/v1/admin/pagination?from=2&number=1", HttpMethod.GET, null);

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getPaginationTestEmptyCarrierList() throws Exception {
        executeScript(CREATE_TEST_DB_SCRIPT);

        String actual = getResponse("/api/v1/admin/pagination?from=0&number=100", HttpMethod.GET, null);
        actual = deleteNodes(actual, "timestamp");

        String expected = "{\n" +
                "    \"status\": 404,\n" +
                "    \"error\": \"NOT_FOUND\",\n" +
                "    \"message\": \"No carriers in range\"\n" +
                "}";

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getPaginationTestInvalidRange() throws Exception {
        String actual = getResponse("/api/v1/admin/pagination?from=-1&number=100", HttpMethod.GET, null);
        actual = deleteNodes(actual, "timestamp");

        String expected = "{\n" +
                "    \"status\": 400,\n" +
                "    \"error\": \"BAD_REQUEST\",\n" +
                "    \"message\": \"Invalid range\"\n" +
                "}";

        JSONAssert.assertEquals(expected, actual, false);

        actual = getResponse("/api/v1/admin/pagination?from=1&number=-100", HttpMethod.GET, null);
        actual = deleteNodes(actual, "timestamp");
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getAllCarrierInRangeTestInvalidRange() throws Exception {
        String actual = getResponse("/api/v1/admin/carrier-in-range-id?startId=100&endId=1", HttpMethod.GET, null);
        actual = deleteNodes(actual, "timestamp");

        String expected = "{\n" +
                "    \"status\": 400,\n" +
                "    \"error\": \"BAD_REQUEST\",\n" +
                "    \"message\": \"Invalid range\"\n" +
                "}";
        JSONAssert.assertEquals(expected, actual, false);

        actual = getResponse("/api/v1/admin/carrier-in-range-id?startId=-1&endId=20", HttpMethod.GET, null);
        actual = deleteNodes(actual, "timestamp");
        JSONAssert.assertEquals(expected, actual, false);

        actual = getResponse("/api/v1/admin/carrier-in-range-id?startId=1&endId=-100", HttpMethod.GET, null);
        actual = deleteNodes(actual, "timestamp");
        JSONAssert.assertEquals(expected, actual, false);

        actual = getResponse("/api/v1/admin/carrier-in-range-id?startId=-1&endId=-10", HttpMethod.GET, null);
        actual = deleteNodes(actual, "timestamp");
        JSONAssert.assertEquals(expected, actual, false);

        actual = getResponse("/api/v1/admin/carrier-in-range-id?startId=-10&endId=-1", HttpMethod.GET, null);
        actual = deleteNodes(actual, "timestamp");
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void createCarrierTestGood() throws Exception {
        String body = "{\n" +
                "\"username\":\"masha\",\n" +
                "\"password\":\"qwe123asd\",\n" +
                "\"email\":\"mq@gmail.com\",\n" +
                "\"telephone_number\":\"12344\",\n" +
                "\"is_activated\":\"false\"\n" +
                "}";

        headers.setContentType(MediaType.APPLICATION_JSON);

        String actual = getResponse("/api/v1/admin/carrier", HttpMethod.POST, body);

        User user = userService.findByUsernameWithRole("masha", AuthorityUtils.ROLE_CARRIER);

        if (user == null) {
            throw new RuntimeException();
        }

        String expected = objectMapper.writeValueAsString(UserDTO.from(user));

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void createCarrierTestNotUniqueData() throws Exception {
        String body1 = "{\n" +
                "\"username\":\"vitya\",\n" +
                "\"password\":\"qwe123asd\",\n" +
                "\"email\":\"mq@gmail.com\",\n" +
                "\"telephone_number\":\"12344\",\n" +
                "\"is_activated\":\"false\"\n" +
                "}";

        String body2 = "{\n" +
                "\"username\":\"absentUser\",\n" +
                "\"password\":\"qwe123asd\",\n" +
                "\"email\":\"a@gmail.com\",\n" +
                "\"telephone_number\":\"12344\",\n" +
                "\"is_activated\":\"false\"\n" +
                "}";

        String expected1 = "{\n" +
                "    \"status\": 409,\n" +
                "    \"error\": \"CONFLICT\",\n" +
                "    \"message\": \"Username already exist\"\n" +
                "}";

        String expected2 = "{\n" +
                "    \"status\": 409,\n" +
                "    \"error\": \"CONFLICT\",\n" +
                "    \"message\": \"Email already exist\"\n" +
                "}";

        headers.setContentType(MediaType.APPLICATION_JSON);

        String actual = getResponse("/api/v1/admin/carrier", HttpMethod.POST, body1);
        actual = deleteNodes(actual, "timestamp");
        JSONAssert.assertEquals(expected1, actual, false);

        actual = getResponse("/api/v1/admin/carrier", HttpMethod.POST, body2);
        actual = deleteNodes(actual, "timestamp");
        JSONAssert.assertEquals(expected2, actual, false);
    }

    @Test
    public void createCarrierTestInvalidData() throws Exception {
        String body1 = "{\n" +
                "}";

        String expected1 = "{\n" +
                "    \"status\": 400,\n" +
                "    \"error\": \"BAD_REQUEST\",\n" +
                "    \"message\": \"Validation exception\",\n" +
                "    \"errors\": {\n" +
                "        \"password\": \"must not be blank\",\n" +
                "        \"is_activated\": \"must not be null\",\n" +
                "        \"telephone_number\": \"must not be blank\",\n" +
                "        \"email\": \"must not be blank\",\n" +
                "        \"username\": \"must not be blank\"\n" +
                "    }\n" +
                "}";

        headers.setContentType(MediaType.APPLICATION_JSON);

        String actual = getResponse("/api/v1/admin/carrier", HttpMethod.POST, body1);
        actual = deleteNodes(actual, "timestamp");
        JSONAssert.assertEquals(expected1, actual, false);


        String body2 = "{\n" +
                "\t\"username\":\"a\",\n" +
                "\t\"password\":\"a\",\n" +
                "\t\"telephone_number\":\"asfas\",\n" +
                "\t\"email\":\"dgsdg.com\",\n" +
                "\t\"is_activated\":\"false\"\n" +
                "}";

        String expected2 = "{\n" +
                "    \"status\": 400,\n" +
                "    \"error\": \"BAD_REQUEST\",\n" +
                "    \"message\": \"Validation exception\",\n" +
                "    \"errors\": {\n" +
                "        \"password\": \"size must be between 6 and 64\",\n" +
                "        \"email\": \"must be a well-formed email address\",\n" +
                "        \"username\": \"size must be between 3 and 24\"\n" +
                "    }\n" +
                "}";

        headers.setContentType(MediaType.APPLICATION_JSON);

        actual = getResponse("/api/v1/admin/carrier", HttpMethod.POST, body2);
        actual = deleteNodes(actual, "timestamp");
        JSONAssert.assertEquals(expected2, actual, false);

        String body3 = "{\n" +
                "\t\"username\":\"aasdasfasfasfasfasfasfasfasfasfaasfasfsfasfasfasfasfasfasf\",\n" +
                "\t\"password\":\"aasdasfasfasfasfasfasfasfasfasfaasfasfsfasfasfasfasfasfasfsdfasfasfasfasffgasdfsadfgwavfsgdfsdfg\",\n" +
                "\t\"telephone_number\":\"asfas\",\n" +
                "\t\"email\":\"dgsdg.com\",\n" +
                "\t\"is_activated\":\"false\"\n" +
                "}";

        String expected3 = "{\n" +
                "    \"status\": 400,\n" +
                "    \"error\": \"BAD_REQUEST\",\n" +
                "    \"message\": \"Validation exception\",\n" +
                "    \"errors\": {\n" +
                "        \"password\": \"size must be between 6 and 64\",\n" +
                "        \"email\": \"must be a well-formed email address\",\n" +
                "        \"username\": \"size must be between 3 and 24\"\n" +
                "    }\n" +
                "}";

        headers.setContentType(MediaType.APPLICATION_JSON);

        actual = getResponse("/api/v1/admin/carrier", HttpMethod.POST, body3);
        actual = deleteNodes(actual, "timestamp");
        JSONAssert.assertEquals(expected3, actual, false);
    }

    @Test
    public void deleteCarrierTestGood() throws JSONException, JsonProcessingException {
        User user = userService.findByUsernameWithRole("dddd", AuthorityUtils.ROLE_CARRIER);

        String actual = getResponse("/api/v1/admin/carrier/" + user.getUserId(), HttpMethod.DELETE, null);

        User deletedUser = userService.findByUsernameWithRole("dddd", AuthorityUtils.ROLE_CARRIER);

        if (deletedUser != null) {
            throw new RuntimeException();
        }

        String expected = objectMapper.writeValueAsString(UserDTO.from(user));

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void deleteCarrierTestGoodTestWithAbsentId() throws Exception {
        String actual = getResponse("/api/v1/admin/carrier/1124124",
                HttpMethod.DELETE,
                null);
        actual = deleteNodes(actual, "timestamp");

        String expected = "    {\n" +
                "        \"status\": 404,\n" +
                "            \"error\": \"NOT_FOUND\",\n" +
                "            \"message\": \"Carrier 1124124 not found \"\n" +
                "    }";

        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void updateCarrierTestGood() throws JSONException, JsonProcessingException {
        User user = userService.findByUsernameWithRole("vitya", AuthorityUtils.ROLE_CARRIER);
        String body = "{\n" +
                "\"id\":\"" + user.getUserId() + "\",\n" +
                "\"username\":\"masha\",\n" +
                "\"email\":\"masha@gmail.com\",\n" +
                "\"telephone_number\":\"12344\",\n" +
                "\"is_activated\":\"false\"\n" +
                "}";

        headers.setContentType(MediaType.APPLICATION_JSON);

        String actual = getResponse("/api/v1/admin/carrier", HttpMethod.PUT, body);

        User updatedUser = userService.findByIdWithRole(user.getUserId(), AuthorityUtils.ROLE_CARRIER);

        String expected = objectMapper.writeValueAsString(UserDTO.from(updatedUser));

        JSONAssert.assertEquals(expected, actual, false);

        user = userService.findByUsernameWithRole("dddd", AuthorityUtils.ROLE_CARRIER);
        body = "{\n" +
                "\"id\":\"" + user.getUserId() + "\",\n" +
                "\"username\":\"dddd\",\n" +
                "\"email\":\"d@gmail.com\",\n" +
                "\"telephone_number\":\"88888888888888\",\n" +
                "\"is_activated\":\"false\"\n" +
                "}";

        actual = getResponse("/api/v1/admin/carrier", HttpMethod.PUT, body);

        updatedUser = userService.findByIdWithRole(user.getUserId(), AuthorityUtils.ROLE_CARRIER);

        expected = objectMapper.writeValueAsString(UserDTO.from(updatedUser));

        JSONAssert.assertEquals(expected, actual, false);
    }

    private String getResponse(String uri, HttpMethod httpMethod, String body) {
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(uri), httpMethod, entity, String.class);

        return response.getBody();
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private void startDB() throws SQLException, IOException {
        executeScript(CREATE_TEST_DB_SCRIPT);

        Role role = new Role();
        role.setRoleName("ROLE_ADMIN");
        roleDAO.save(role);

        role = new Role();
        role.setRoleName("ROLE_USER");
        roleDAO.save(role);

        role = new Role();
        role.setRoleName("ROLE_CARRIER");
        roleDAO.save(role);

        userService.save(createUser(
                "vitya",
                "vitya",
                "vitya@gmail.ua",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER, AuthorityUtils.ROLE_CARRIER)
        ));

        userService.save(createUser(
                "aaaa",
                "1111111111",
                "a@gmail.com",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER, AuthorityUtils.ROLE_CARRIER)
        ));

        userService.save(createUser(
                "bbbb",
                "1111111111",
                "b@gmail.com",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER)
        ));

        userService.save(createUser(
                "cccc",
                "111111111",
                "c@gmail.com",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER)
        ));

        userService.save(createUser(
                "dddd",
                "11111111",
                "d@gmail.com",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER, AuthorityUtils.ROLE_CARRIER)
        ));

        userService.save(createUser(
                "eeee",
                "1111111",
                "e@gmail.com",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER, AuthorityUtils.ROLE_CARRIER)
        ));

        userService.save(createUser(
                "ffff",
                "11111111",
                "f@gmail.com",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER, AuthorityUtils.ROLE_ADMIN)
        ));

        userService.save(createUser(
                "admin",
                "admin123",
                "admin@gmail.com",
                "093",
                true,
                Arrays.asList(AuthorityUtils.ROLE_USER, AuthorityUtils.ROLE_ADMIN)
        ));

        String body = "{\n" +
                "\"username\":\"admin\",\n" +
                "\"password\":\"admin123\"\n" +
                "}";

        headers.setContentType(MediaType.APPLICATION_JSON);

        String response = getResponse("/api/auth/sign-in", HttpMethod.POST, body);

        JsonNode rootNode = objectMapper.readTree(response);

        headers.add("Authorization", rootNode.get("type").asText() + " " +
                rootNode.get("access_token").asText());
        headers.add("Authorization-Refresh", rootNode.get("type").asText() + " " +
                rootNode.get("refresh_token").asText());
    }

    private User createUser(String userName,
                            String password,
                            String mail,
                            String telephone,
                            boolean isActivated,
                            List<Role> roles) {
        User user = new User();
        user.setUserName(userName);
        user.setUserIsActivated(isActivated);
        user.setUserPassword(passwordEncoder.encode(password));
        user.setUserEmail(mail);
        user.setUserTelephone(telephone);
        user.setRegistrationDate(LocalDateTime.now());
        user.setUserRoles(roles);

        return user;
    }

    private void executeScript(String scriptName) throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(),
                new EncodedResource(new ClassPathResource(scriptName),
                        StandardCharsets.UTF_8));
    }

    private String deleteNodes(String json, String ... nodeNames) throws IOException {
        JsonNode rootNode = objectMapper.readTree(json);

        Arrays.stream(nodeNames).forEach(nodeName -> ((ObjectNode) rootNode).remove("nodeName"));

        return rootNode.toString();
    }
}
