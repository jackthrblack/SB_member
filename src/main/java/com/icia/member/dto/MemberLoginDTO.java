package com.icia.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class MemberLoginDTO {
    private String memberEmail;
    private String memberPassword;
}
