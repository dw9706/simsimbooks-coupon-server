package org.simsimbooks.couponserver.user.dto;

import lombok.Builder;
import lombok.Getter;
import org.simsimbooks.couponserver.user.entity.Gender;
@Builder
@Getter //직렬화하기 위해서 getter 필요
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private Gender gender;
}
