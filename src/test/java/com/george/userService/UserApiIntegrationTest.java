package com.george.userService;

import com.george.userService.entities.EmailVerificationToken;
import com.george.userService.entities.Role;
import com.george.userService.entities.User;
import com.george.userService.repository.EmailVerificationTokenRepository;
import com.george.userService.repository.UserRepository;
import com.george.userService.services.JwtService;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Date;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiIntegrationTest {
    @ServiceConnection //to override the application properties connection details
     static PostgreSQLContainer postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
            .withUsername("testUser")
            .withPassword("testSecret")
            .withDatabaseName("testDatabase");

    @LocalServerPort
    private Integer port;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    static {
        postgres.start();
    }
    @BeforeAll
    static void setup() {
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = port;
    }
    @BeforeEach
	void setUp() {
        emailVerificationTokenRepository.deleteAll();
		userRepository.deleteAll();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
	}

    @Test
    void shouldReturnStatus() throws Exception {
        RestAssured.when()
                .get("/api/v1/auth/status")
                .then()
                .statusCode(200)
                .body("data", Matchers.equalTo("working fine"))
                .body("error", Matchers.anEmptyMap());
    }

    @Test
    void shouldVriyUserEmail() throws Exception {
        User user = getDummyUser("rijo", "water", "rijogeorge8@gmail.com", "poopoo");
        String token ="dummyToken";
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken(token);
        emailVerificationToken.setUser(user);
        emailVerificationToken.setCreatedAt(new Date());
        userRepository.save(user);
        emailVerificationTokenRepository.save(emailVerificationToken);
        RestAssured.when()
                .get("/api/v1/auth/verify_user_email?token="+token)
                .then()
                .statusCode(200)
                .body("message", Matchers.equalTo("verified"));
    }


    @Test
	void shouldCeateUser() throws Exception {
        String requestBody = """
                {
                    "firstName":"rijo",
                    "lastName":"water",
                    "email": "rijogeorge8@gmail.com",
                    "password": "poopoo"
                }
				""";

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/auth/signup")
                .then()
                .statusCode(201)
                .body("success", Matchers.equalTo(true))
                .body("data", Matchers.equalTo("User created successfully, please verify your email address"))
                .body("error", Matchers.anEmptyMap());

	}

    @Test
    void shouldSigninUser() throws Exception {
        User user = getDummyUser("rijo", "water", "rijogeorge8@gmail.com", "poopoo");
        userRepository.save(user);
        String requestBody = """
                {
                    "email": "rijogeorge8@gmail.com",
                    "password": "poopoo"
                }
				""";

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/auth/signin")
                .then()
                .statusCode(200)
                .body("token", Matchers.notNullValue())
                .body("refreshToken", Matchers.notNullValue())
                .body("error", Matchers.nullValue());

    }

    @Test
    void shouldRefreshToken() throws Exception {
        User user = getDummyUser("rijo", "water", "rijogeorge8@gmail.com", "poopoo");
        userRepository.save(user);
        var token = jwtService.generateToken(user);
        String requestBody = """
                {
                    "token":\""""+token+"""
                \"
                }
				""";
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/auth/refresh")
                .then()
                .statusCode(200)
                .body("token", Matchers.notNullValue())
                .body("refreshToken", Matchers.notNullValue())
                .body("error", Matchers.nullValue());


    }

    private User getDummyUser(String firstName, String lastName, String email, String password) {
        User user = new User();
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(password));
        user.setActive(false);
        user.setEmailVerified(false);
        return user;
    }

}
