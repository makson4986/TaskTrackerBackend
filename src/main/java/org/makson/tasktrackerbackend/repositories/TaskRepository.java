package org.makson.tasktrackerbackend.repositories;

import org.makson.tasktrackerbackend.models.Task;
import org.makson.tasktrackerbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByOwner(User owner);

    Optional<Task> findByOwnerAndId(User owner, Integer id);

    @Query("""
            select t from Task t where 
            (t.status = "NOT_DONE" or t.status = "IN_PROGRESS") or
            (t.status = "DONE" and t.performedAt = :period)
            """)
    List<Task> getTasksForReport(@Param("period") LocalDate period);
}
