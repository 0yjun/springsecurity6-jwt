package com.security.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    /** 사용자 계정 **/
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT-001", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, "ACCOUNT-002", "이미 존재하는 사용자 입니다."),
    USER_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "ACCOUNT-003", "회원가입중 문제가 발생하였습니다."),
    MIS_MATCH_PASSWORD(HttpStatus.BAD_REQUEST,"ACCOUNT-004","아이디와 비밀번호가 일치하지 않습니다."),
    HAS_EMAIL(HttpStatus.CONFLICT, "ACCOUNT-005", "존재하는 이메일입니다."),
    TOKEN_IS_INVALID(HttpStatus.BAD_REQUEST, "ACCOUNT-006","유효하지 않은 토큰입니다."),
    ACCESS_TOKEN_IS_NOT_EXPIRED(HttpStatus.UNAUTHORIZED, "ACCOUNT-007","토큰이 만료되지 않았습니다."),
    ACCESS_TOKEN_IS_EXPIRED(HttpStatus.UNAUTHORIZED, "ACCOUNT-008","토큰이 만료되었습니다."),
    ACCESS_TOKEN_IS_NOT_CREATED(HttpStatus.UNAUTHORIZED, "ACCOUNT-008","토큰이 생성에 실패하였습니다."),
    ACCESS_TOKEN_IS_NOT_EXIST(HttpStatus.UNAUTHORIZED, "ACCOUNT-008","토큰이 졵재하지 않습니다."),
    LOGIN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "ACCOUNT-009", "로그인에 실패하였습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ACCOUNT-009", "서버 내부 오류가 발생했습니다."),

    /* 성공처리 */
    SUCCESS(HttpStatus.OK, "success-001", ""),
    SUCCESS_LOGIN(HttpStatus.OK, "success-002", "로그인에 성공하였습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String messgae;

}
