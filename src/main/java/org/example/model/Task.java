package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Entity annotation tells Spring that this class should be stored in the db.
// By default, the table name is the class name
// The entity fields are mapped to table columns automatically

@Builder
@Setter
@Getter
@Entity(name= "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Builder.Default
    private int id = 0;
    private String title;
    private String description;
    private String status;
    private LocalDateTime createdAt;

}
