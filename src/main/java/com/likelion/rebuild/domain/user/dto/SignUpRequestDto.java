package com.likelion.rebuild.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    private String loginId;
    private String nickname;
    private String password;
}
