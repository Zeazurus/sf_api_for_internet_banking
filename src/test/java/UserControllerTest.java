import org.bankapi.spring.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getBalanceTest() throws Exception {
        this.mockMvc.perform(get("/users/1/getBalance"))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/users/xxx/getBalance"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void putMoney() throws Exception {
        this.mockMvc.perform(post("/users/1/putMoney").param("amount", "1"))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/users/xxx/putMoney").param("amount", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void takeMoney() throws Exception {
        this.mockMvc.perform(post("/users/1/takeMoney").param("amount", "1"))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/users/xxx/takeMoney").param("amount", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void transferMoney() throws Exception {
        this.mockMvc.perform(post("/users/1/transferMoney").param("amount", "1").param("toUserId", "2"))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/users/xxx/transferMoney").param("amount", "1").param("toUserId", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getOperationList() throws Exception {
        this.mockMvc.perform(get("/users/1/getOperationList"))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/users/xxx/getOperationList"))
                .andExpect(status().isBadRequest());
    }
}