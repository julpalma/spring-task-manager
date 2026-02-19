package org.example.repository;

import org.example.entities.Task;
import org.example.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

//JPA Repository is an interface that simplifies data access in Java applications.
//JPA provider (Hibernate) translates it into SQL queries for the database dialect you’re using
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findTaskByStatus(TaskStatus status);

    // Pagination example
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    @Query("SELECT t FROM task t WHERE t.status = :status AND t.createdAt >= :fromDate")
    List<Task> findTasksByStatusSince(@Param("status") TaskStatus status,
                                      @Param("fromDate") LocalDateTime fromDate);

    @Query(value = "SELECT * FROM task t WHERE t.status = :status ORDER BY t.created_at DESC LIMIT 5", nativeQuery = true)
    List<Task> findTop5RecentTasks(@Param("status") String status);

}
