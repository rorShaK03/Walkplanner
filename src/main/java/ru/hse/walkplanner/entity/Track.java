package ru.hse.walkplanner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Entity
@Table(name = "tracks")
public class Track {
    @Id
    @GeneratedValue
    @Getter
    private UUID id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private int rating;

    @Getter
    @Setter
    private int ratedUsers;

    @Getter
    @Setter
    private int walkedUsers;

    @Getter
    @Setter
    private int distanceMeters;

    @Getter
    @Setter
    private int walkMinutes;

    @Getter
    @Setter
    @OneToMany
    private List<Point> points;

    @Getter
    @Setter
    @OneToMany
    private List<KeyPoint> keyPoints;

    @Getter
    @Setter
    @ManyToOne
    private User creator;

    @Getter
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Getter
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
}