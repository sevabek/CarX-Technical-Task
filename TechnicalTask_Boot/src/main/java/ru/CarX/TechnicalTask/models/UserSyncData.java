package ru.CarX.TechnicalTask.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "user_sync_data")
@AllArgsConstructor
@NoArgsConstructor
public class UserSyncData implements Serializable {

    @Id
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "money")
    @Min(value= 0, message = "Money amount cannot be less than 0")
    // тип int, потому что в играх обычно нет сумм с плавающей точкой
    private int money;

    @Column(name = "country")
    private String country;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}

