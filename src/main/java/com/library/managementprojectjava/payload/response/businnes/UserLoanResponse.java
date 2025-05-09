package com.library.managementprojectjava.payload.response.businnes;

import com.library.managementprojectjava.payload.response.user.UserResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)

public class UserLoanResponse extends UserResponse {
    private Long id;
    private Long userId;
    private Long bookId;
}
