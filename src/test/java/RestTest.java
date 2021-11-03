import main.Main;
import main.ToDoController;
import main.dao.ToDoDao;
import main.model.ToDo;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {RestTest.Initializer.class})
public class RestTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("test")
            .withUsername("sa")
            .withPassword("sa");
    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());

        }
    }

    @LocalServerPort
    private Integer port;

    private String host = postgreSQLContainer.getHost();
    private String toDoDescription = "added via test";
    private int toDoToAddId = 1;
    private ToDo toDoToAdd = new ToDo(toDoDescription);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ToDoDao toDoDao;
    @Autowired
    private ToDoController controller;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void correctIndexStarted() throws Exception {
        assertThat(this.restTemplate.getForObject("http://" + host + ":" + port + "/",
                String.class), containsString("Список дел"));
    }

    @Test
    public void addToDo() throws Exception {
        this.mockMvc.perform(post("http://" + host + ":" + port + "/todos/?description=" + toDoDescription))
                .andExpect(status().is(201));
    }

    @Test
    public void getToDo() throws Exception {
        String toDoId = this.restTemplate.postForObject("http://" + host + ":" + port +
                        "/todos/?description=" +
                        toDoDescription,
                null, String.class).trim();

        System.out.println("http://" + host + ":" + port + "/todos/" + toDoId);
        assertThat(this.restTemplate.getForObject("http://" + host + ":" + port + "/todos/" + toDoId,
                String.class), containsString(toDoDescription));
    }

    @Test
    public void listToDos() throws Exception {
        this.restTemplate.postForObject("http://" + host + ":" + port + "/todos/?description=" +
                        toDoDescription,null,
                String.class);
        assertThat(this.restTemplate.getForObject("http://" + host + ":" + port + "/todos/",
                String.class), containsString(toDoDescription));
    }

    @Test
    public void deleteToDos() throws Exception {
        this.mockMvc.perform(delete("http://" + host + ":" + port + "/todos/"))
                .andExpect(status().isOk())
                .andExpect(content().string("All ToDos was successfully deleted!"));
    }

    @Test
    public void deleteToDo() throws Exception {
        String toDoId = this.restTemplate.postForObject("http://" + host + ":" + port +
                        "/todos/?description=" +
                        toDoDescription,
                null, String.class).trim();
        this.mockMvc.perform(delete("http://" + host + ":" + port + "/todos/" + toDoId))
                .andExpect(status().isOk());
    }

    @Test
    public void updateToDo() throws Exception {
        String toDoId = this.restTemplate.postForObject("http://" + host + ":" + port +
                        "/todos/?description=" +
                        toDoDescription,
                null, String.class).trim();
        this.mockMvc.perform(put("http://" + host + ":" + port + "/todos/" + toDoId +
                "?description=" + toDoDescription + "-updated"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "ToDo id = " + toDoId + " was successfully updated!"
                )));
    }
    @Test
    public void contextLoads() throws Exception {
        assertThat(controller, notNullValue());
    }

    @Test
    @Transactional
    public void toDoAddToDbTest() {

        toDoDao.add(toDoToAdd);
        assertEquals(toDoDescription, toDoDao.get(toDoToAdd.getId()).getDescription());

    }
}