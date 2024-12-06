package ru.CarX.TechnicalTask.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_sync_data")
public class UserSyncData implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "money")
    @Min(value= 0, message = "Money amount cannot be less than 0")
    // тип int, потому что в играх обычно нет сумм с плавающей точкой
    private int money;

    @Column(name = "country")
    private String country;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "userSyncData")
    @JsonIgnore
    private List<UserActivityData> userActivityDataList;

    public UserSyncData() {}

    public UserSyncData(String uuid, int money, String country,
                        LocalDateTime createdAt, List<UserActivityData> userActivityDataList) {
        this.uuid = uuid;
        this.money = money;
        this.country = country;
        this.createdAt = createdAt;
        this.userActivityDataList = userActivityDataList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<UserActivityData> getUserActivityDataList() {
        return userActivityDataList;
    }

    public void setUserActivityDataList(List<UserActivityData> userActivityDataList) {
        this.userActivityDataList = userActivityDataList;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
