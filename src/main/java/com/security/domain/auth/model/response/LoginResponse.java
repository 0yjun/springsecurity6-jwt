package com.security.domain.auth.model.response;

import com.security.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    ErrorCode errorCode;
    String jwtToken;
}
