package com.library.demo.payload.request.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserSaveRequest extends UserRequest {

    @NotEmpty(message = "At least one role must be assigned to the user.")
    private Set<Long> roleIdList;
}
