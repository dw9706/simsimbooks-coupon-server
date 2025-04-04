package org.simsimbooks.couponserver.user.mapper;

import org.simsimbooks.couponserver.user.dto.UserRequestDto;
import org.simsimbooks.couponserver.user.dto.UserResponseDto;
import org.simsimbooks.couponserver.user.entity.User;

public class UserMapper {
    public static UserResponseDto toResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .gender(user.getGender())
                .build();
    }

    public static User toUser(UserRequestDto requestDto) {
        return User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .gender(requestDto.getGender())
                .build();
    }
}
