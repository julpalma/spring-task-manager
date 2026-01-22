package org.example.repository;

import org.example.model.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//JPA Repository is an interface that simplifies data access in Java applications.
//JPA provider (Hibernate) translates it into SQL queries for the database dialect you’re using
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findTaskByStatus(String status);

    List<Task> findByStatusIgnoreCase(String status, Sort sort);
}
