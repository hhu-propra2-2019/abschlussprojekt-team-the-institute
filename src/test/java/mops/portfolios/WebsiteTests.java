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
public class WebsiteTests {
  private final transient String nameSt = "studentin";
  private final transient String nameOr = "orga";

  @Autowired
  private transient MockMvc mockMvc;

  @Test
  @WithMockKeycloackAuth(name = nameSt, roles = {nameSt})
  void testErrorWhenCorrectRoleStudentList () throws Exception {
    mockMvc.perform(get("/user/list")).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockKeycloackAuth(name = nameSt, roles = {nameSt})
  void testErrorWhenCorrectRoleStudentCreate () throws Exception {
    mockMvc.perform(get("/user/create")).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockKeycloackAuth(name = nameSt, roles = {nameSt})
  void testErrorWhenCorrectRoleStudentView () throws Exception {
    mockMvc.perform(get("/user/view?portfolioId=1")).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockKeycloackAuth(name = nameOr, roles = {nameOr})
  void testErrorWhenCorrectRoleOrgaList () throws Exception {
    mockMvc.perform(get("/admin/list")).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockKeycloackAuth(name = nameOr, roles = {nameOr})
  void testErrorWhenCorrectRoleOrgaUpload () throws Exception {
    mockMvc.perform(get("/admin/upload")).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockKeycloackAuth(name = nameOr, roles = {nameOr})
  void testErrorWhenCorrectRoleOrgaView () throws Exception {
    mockMvc.perform(get("/admin/view?templateId=27")).andExpect(MockMvcResultMatchers.status().isOk());
  }
}