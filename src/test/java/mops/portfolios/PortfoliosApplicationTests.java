package mops.portfolios;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
class PortfoliosApplicationTests {

	@Autowired
	private MockMvc mockMvc;


	@Test
	void contextLoads() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}