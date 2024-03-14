package ru.hse.walkplanner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Entity
@Table(name = "key_points")
public class KeyPoint {
    @Id
    @GeneratedValue
    private UUID id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private double latitude;

    @Getter
    @Setter
    private double longitude;

    @Getter
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
}