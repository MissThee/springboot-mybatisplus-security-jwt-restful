package com.github.missthee.db.dto.login;

import com.github.missthee.db.bo.login.LoginBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginResDTO {
    private LoginBO user;
}