package com.New.YouBooking.services.Payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRoleToUserForm {
    private String username;
    private String userRoleName;
}
