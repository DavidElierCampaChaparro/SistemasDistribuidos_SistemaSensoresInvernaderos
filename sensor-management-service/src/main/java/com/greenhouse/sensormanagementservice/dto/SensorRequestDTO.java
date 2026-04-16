package com.greenhouse.sensormanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SensorRequestDTO {

    private static final String EMPTY_SERIAL_NUMBER = "Serial number cannot be empty.";
    private static final String LENGTH_SERIAL_NUMBER = "Serial number must be between 5 and 20 characters.";
    private static final String MANDATORY_ID_FORMAT = "Format ID is mandatory.";
    private static final String POSITIVE_ID_FORMAT = "ID format must be positive.";
    private static final String MANDATORY_ID_GREENHOUSE = "Greenhouse ID is mandatory.";

    @NotBlank(message = EMPTY_SERIAL_NUMBER)
    @Size(min = 5, max = 20, message = LENGTH_SERIAL_NUMBER)
    private String serialNumber;

    @NotNull(message = MANDATORY_ID_FORMAT)
    @Positive(message = POSITIVE_ID_FORMAT)
    private Long formatId;

    @NotNull(message = MANDATORY_ID_GREENHOUSE)
    private Long greenhouseId;

}
