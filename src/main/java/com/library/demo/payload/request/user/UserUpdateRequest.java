package com.library.demo.payload.request.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

    @NotNull(message = "First name is required")
    @Size(min = 2, max = 30)
    private String firstName;

    @NotNull(message = "Last name is required")
    @Size(min = 2, max = 30)
    private String lastName;

    @NotNull(message = "Address is required")
    @Size(min = 10, max = 100)
    private String address;

    @NotNull(message = "Phone is required")
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "Phone format must be 999-999-9999")
    private String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
