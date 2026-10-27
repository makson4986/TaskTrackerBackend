package org.makson.tasktrackerbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.makson.tasktrackerbackend.exceptions.TaskNotFoundException;
import org.makson.tasktrackerbackend.models.User;
import org.makson.tasktrackerbackend.services.TaskService;
import org.makson.tasktrackerbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TaskServiceTest extends IntegrationTestBase {
    final TaskService taskService;
    final UserService userService;

    @Autowired
    TaskServiceTest(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @Test
    @DisplayName("Retrieving a task by ID should not return another user's task")
    void shouldNotReturnTaskAntherUsers() {
        int testTaskId = 20;
        User user1 = (User) userService.loadUserByUsername("alexander.ivanov@gmail.com");
        User user2 = (User) userService.loadUserByUsername("maria.petrova@yandex.ru");

        Assertions.assertNotNull(taskService.getById(user2, testTaskId));
        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.getById(user1, testTaskId));
    }
}
