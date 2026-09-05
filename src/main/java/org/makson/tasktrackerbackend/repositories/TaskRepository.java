package org.makson.tasktrackerbackend.repositories;

import org.makson.tasktrackerbackend.models.Task;
import org.makson.tasktrackerbackend.models.TaskStatus;
import org.makson.tasktrackerbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByOwner(User owner);

    Optional<Task> findByOwnerAndId(User owner, Integer id);
}
