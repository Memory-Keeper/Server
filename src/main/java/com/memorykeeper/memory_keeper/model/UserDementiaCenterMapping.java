package com.memorykeeper.memory_keeper.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_dementia_center_mapping")
public class UserDementiaCenterMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dementia_center_id", nullable = false)
    private DementiaCenter dementiaCenter;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DementiaCenter getDementiaCenter() {
        return dementiaCenter;
    }

    public void setDementiaCenter(DementiaCenter dementiaCenter) {
        this.dementiaCenter = dementiaCenter;
    }
}
