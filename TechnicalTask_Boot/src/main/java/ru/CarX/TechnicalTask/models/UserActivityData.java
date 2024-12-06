package ru.CarX.TechnicalTask.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity_data")
public class UserActivityData implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity")
    @Min(value= 0, message = "Activity amount cannot be less than 0")
    private int activity;

    @Column(name = "timestamp")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "sync_uuid", referencedColumnName = "uuid")
    private UserSyncData userSyncData;

    public UserActivityData() {}

    public UserActivityData(int activity, LocalDateTime timestamp, UserSyncData userSyncData) {
        this.activity = activity;
        this.timestamp = timestamp;
        this.userSyncData = userSyncData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public UserSyncData getUserSyncData() {
        return userSyncData;
    }

    public void setUserSyncData(UserSyncData userSyncData) {
        this.userSyncData = userSyncData;
    }
}
