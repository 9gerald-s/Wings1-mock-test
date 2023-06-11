package com.fresco.ecommerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fresco.ecommerce.repo.CategoryRepo;
import com.fresco.ecommerce.repo.ProductRepo;
import com.fresco.ecommerce.repo.UserRepo;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
class T4SpringBootJwtApplicationTests {

	@Autowired
	MockMvc mvc;
	String c_u = "jack", s_u = "apple", p = "pass_word";
	@Autowired
	CategoryRepo categoryRepo;
	@Autowired
	UserRepo userRepo;
	@Autowired
	ProductRepo productRepo;

	@Test
	@Order(10)
	public void consumerLoginWithBadCreds() throws Exception {

		mvc.perform(post("/api/public/login").contentType(MediaType.APPLICATION_JSON)
				.content(getJSONCreds(c_u, "password"))).andExpect(status().is(401));
	}

	private String getJSONCreds(String u, String p) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", u);
		map.put("password", p);
		return new JSONObject(map).toJSONString();
	}

	@Test
	@Order(11)
	public void consumerLoginWithValidCreds() throws Exception {
		assertEquals(200, loginHelper(c_u, p).getStatus());
		assertNotEquals("", loginHelper(c_u, p).getContentAsString());
	}

	private MockHttpServletResponse loginHelper(String u, String p) throws Exception {

		return mvc.perform(post("/api/public/login").contentType(MediaType.APPLICATION_JSON).content(getJSONCreds(u, p)))
				.andReturn().getResponse();
	}

}
