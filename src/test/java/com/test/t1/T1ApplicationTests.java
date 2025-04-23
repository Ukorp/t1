package com.test.t1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.t1.dto.TaskRequest;
import com.test.t1.model.Status;
import com.test.t1.model.Task;
import com.test.t1.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class T1ApplicationTests extends TestContainers {

    @MockitoBean
    private MailSender mock;

    @MockitoBean
    private JavaMailSender mailSender;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    int taskCount = 0;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        Task task1 = new Task(null, 59L, "one", "de", Status.RECEIVED);
        Task task2 = new Task(null, 51L, "two", "scr", Status.DONE);
        Task task3 = new Task(null, 53L, "three", "rip", Status.ARCHIVED);
        Task task4 = new Task(null, 50L, "four", "tion", Status.RECEIVED);
        taskCount = 4;
        taskRepository.saveAll(List.of(task1, task2, task3, task4));
    }

    @Test
    @DisplayName("Тест на успешное сохранение задания")
    void testSaveTask200() throws Exception {
        TaskRequest taskRequest = new TaskRequest("pep", "mep", 55L, null);

        mockMvc.perform(
                        post("/tasks/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsBytes(taskRequest))
                )
                .andExpect(status().isOk());


        List<Task> allTasks = taskRepository.findAll();

        assertEquals(taskCount + 1, allTasks.size());
        assertEquals(taskRequest.description(), allTasks.getLast().getDescription());
        assertEquals(taskRequest.title(), allTasks.getLast().getTitle());
        assertEquals(taskRequest.userId(), allTasks.getLast().getUserId());
        assertEquals(Status.RECEIVED, allTasks.getLast().getStatus());
    }

    @Test
    @DisplayName("Тест на неуспешное сохранение задания (поле null)")
    void testSaveTask400() throws Exception {
        String json = """
                {
                    "description": "one",
                    "userId": "pep",
                    "status": "RECEIVED"
                }
                """;
        mockMvc.perform(
                        post("/tasks/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest());

        assertEquals(taskCount, taskRepository.count());
    }

    @Test
    @DisplayName("Тест на успешный поиск всех заданий")
    void testFindAll200() throws Exception {
        mockMvc.perform(
                        get("/tasks/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(taskCount));
    }

    @Test
    @DisplayName("Тест на успешный поиск всех заданий (пустой лист)")
    void testFindAllEmpty200() throws Exception {
        taskRepository.deleteAll();
        mockMvc.perform(
                        get("/tasks/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @DisplayName("Тест на успешный поиск задания")
    void testFindById200() throws Exception {
        Task task1 = new Task(null, 59L, "one", "de", Status.RECEIVED);
        task1 = taskRepository.save(task1);
        mockMvc.perform(
                        get("/tasks/" + task1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(taskCount + 1))
                .andExpect(jsonPath("$.id").value(task1.getId()))
                .andExpect(jsonPath("$.title").value(task1.getTitle()))
                .andExpect(jsonPath("$.description").value(task1.getDescription()))
                .andExpect(jsonPath("$.userId").value(task1.getUserId()))
                .andExpect(jsonPath("$.status").value(task1.getStatus().name()));

    }

    @Test
    @DisplayName("Тест на неуспешный поиск задания по несуществующему id")
    void testFindById400() throws Exception {
        taskRepository.deleteById(1L);
        mockMvc.perform(
                        get("/tasks/" + 1L))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Тест на успешное обновление задания")
    void testUpdateTask200() throws Exception {
        Task task1 = new Task(null, 59L, "one", "de", Status.RECEIVED);
        task1 = taskRepository.save(task1);

        TaskRequest taskRequest = new TaskRequest("updated", "mep", 55L, Status.ARCHIVED);
        assert taskRequest.status() != null;

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        mockMvc.perform(
                        put("/tasks/" + task1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsBytes(taskRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(taskCount + 1))
                .andExpect(jsonPath("$.id").value(task1.getId()))
                .andExpect(jsonPath("$.title").value(taskRequest.title()))
                .andExpect(jsonPath("$.description").value(taskRequest.description()))
                .andExpect(jsonPath("$.userId").value(taskRequest.userId()))
                .andExpect(jsonPath("$.status").value(taskRequest.status().name()));

        Mockito.verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Тест на неуспешное обновление задания по несуществующему id")
    void testUpdateById400() throws Exception {
        taskRepository.deleteById(1L);
        TaskRequest taskRequest = new TaskRequest("updated", "mep", 55L, Status.ARCHIVED);
        mockMvc.perform(
                        put("/tasks/" + 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsBytes(taskRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Тест на успешное удаление задания")
    void testDeleteTask200() throws Exception {
        Task task1 = new Task(null, 59L, "one", "de", Status.RECEIVED);
        task1 = taskRepository.save(task1);
        mockMvc.perform(
                        delete("/tasks/" + task1.getId()))
                .andExpect(status().isOk());

        assertEquals(taskCount, taskRepository.count());
    }

    @Test
    @DisplayName("Тест на удаление несуществующего объекта (ничего не происходит)")
    void testDeleteTaskEmpty200() throws Exception {
        taskRepository.deleteById(1L);
        mockMvc.perform(
                        delete("/tasks/" + 1L))
                .andExpect(status().isOk());

        assertEquals(taskCount, taskRepository.count());
    }
}
