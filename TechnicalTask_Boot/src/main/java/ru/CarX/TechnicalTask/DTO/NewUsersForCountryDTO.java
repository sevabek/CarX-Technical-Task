package ru.CarX.TechnicalTask.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewUsersForCountryDTO {

    private String country;

    private long usersCount;
}
