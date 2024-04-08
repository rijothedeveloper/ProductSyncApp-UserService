package com.george.userService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.george.userService.dto.SigninRequest;
import com.george.userService.dto.SignupRequest;
import com.george.userService.entities.Role;
import com.george.userService.entities.User;
import com.george.userService.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class ProductSyncAppApplicationTests {

//	static PostgreSQLContainer<?> pgvector = new PostgreSQLContainer<>("pgvector/pgvector:pg16");

@Autowired
private MockMvc mockMvc;

@Autowired
private ObjectMapper objectMapper;

@Autowired
UserRepository userRepository;

@Autowired
PasswordEncoder passwordEncoder;

@LocalServerPort
private Integer port;

	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
			"postgres:15-alpine"
	);

	@BeforeAll
	static void beforeAll() {
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
	}
	@Test
	void shouldCeateUser() throws Exception {
		SignupRequest signupRequest = getSignUpRequest();
		String signupRequesString = objectMapper.writeValueAsString(signupRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signupRequesString))
				.andExpect(status().isCreated());

	}

	@Test
	void shouldSignIn() throws Exception {
		User user = new User();
		user.setEmail("rijouser1@gmail.com");
		user.setFirstname("Rijo");
		user.setLastname("George");
		user.setRole(Role.USER);
		user.setPassword(passwordEncoder.encode("pass"));
		user.setActive(false);
		user.setEmailVerified(false);
		var savedUser = userRepository.save(user);
		SigninRequest signinRequest = new SigninRequest();
		signinRequest.setEmail("rijouser1@gmail.com");
		signinRequest.setPassword("pass");
		String signinRequesString = objectMapper.writeValueAsString(signinRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(signinRequesString))
						.andExpect(status().isOk());
	}

	private SignupRequest getSignUpRequest() {
		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setFirstName("rijo");
		signupRequest.setLastName("george");
		signupRequest.setPassword("pass");
		signupRequest.setEmail("rijouser1332@gmail.com");
		return signupRequest;
	}

}
