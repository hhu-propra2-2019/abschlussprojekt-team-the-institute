package mops.portfolios;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class AccessRestrictionTest {

  @Autowired
  private transient MockMvc mockMvc;

  @Test
  @WithMockUser(username = "studentin", roles = {"studentin"}, password = "studentin")
  void testErrorWhenWrongRole () throws Exception {
    mockMvc.perform(get("/upload")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
  }

  @Test
  @WithMockUser(username = "orga", roles = {"orga"}, password = "orga")
  void testRightAccess () throws Exception {
    mockMvc.perform(get("/upload")).andExpect(MockMvcResultMatchers.status().isOk());
  }

}
