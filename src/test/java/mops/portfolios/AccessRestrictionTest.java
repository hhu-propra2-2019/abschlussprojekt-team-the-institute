package mops.portfolios;

import com.c4_soft.springaddons.test.security.context.support.WithMockKeycloackAuth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class AccessRestrictionTest {

  @Autowired
  private transient MockMvc mockMvc;

  /* TODO: enable again

  @Test
  @WithMockKeycloackAuth(name = "studentin", roles = {"studentin"})
  void testErrorWhenWrongRole () throws Exception {
    mockMvc.perform(get("/upload")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
  }

  @Test
  @WithMockKeycloackAuth(name = "orga", roles = {"orga"})
  void testRightAccess () throws Exception {
    mockMvc.perform(get("/upload")).andExpect(MockMvcResultMatchers.status().isOk());
  }

  */
}
