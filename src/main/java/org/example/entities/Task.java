package org.example.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.TaskStatus;

import java.time.LocalDateTime;

// Entity annotation tells Spring that this class should be stored in the db.
// By default, the table name is the class name
// The entity fields are mapped to table columns automatically

@Builder
@Setter
@Getter
@Entity(name= "task")
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = 0;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDateTime createdAt;
}
