package org.simsimbooks.couponserver.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.simsimbooks.couponserver.user.entity.Gender;

@Builder
@Setter //역직렬화를 위해선 setter와 기본생성자 필요
@Getter
public class UserRequestDto {
    private String name;
    private String email;
    private Gender gender;
}
