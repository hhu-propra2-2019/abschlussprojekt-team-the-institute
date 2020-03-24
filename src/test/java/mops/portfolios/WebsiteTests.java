package mops.portfolios;

import com.c4_soft.springaddons.test.security.context.support.WithMockKeycloackAuth;
import mops.portfolios.demodata.DemoDataGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class WebsiteTests {

  @Autowired
  private transient MockMvc mockMvc;

  @Test
  @WithMockKeycloackAuth(name = "studentin", roles = {"studentin"})
  void testErrorWhenCorrectRoleStudentList () throws Exception {
    mockMvc.perform(get("/user/list")).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockKeycloackAuth(name = "studentin", roles = {"studentin"})
  void testErrorWhenCorrectRoleStudentCreate () throws Exception {
    mockMvc.perform(get("/user/create")).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockKeycloackAuth(name = "studentin", roles = {"studentin"})
  void testErrorWhenCorrectRoleStudentView () throws Exception {
    mockMvc.perform(get("/user/view?portfolioId=1")).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockKeycloackAuth(name = "orga", roles = {"orga"})
  void testErrorWhenCorrectRoleOrgaList () throws Exception {
    mockMvc.perform(get("/admin/list")).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockKeycloackAuth(name = "orga", roles = {"orga"})
  void testErrorWhenCorrectRoleOrgaUpload () throws Exception {
    mockMvc.perform(get("/admin/upload")).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockKeycloackAuth(name = "orga", roles = {"orga"})
  void testErrorWhenCorrectRoleOrgaView () throws Exception {
    mockMvc.perform(get("/admin/view?templateId=27")).andExpect(MockMvcResultMatchers.status().isOk());
  }
}