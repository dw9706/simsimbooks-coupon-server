package org.simsimbooks.couponserver.user;

import lombok.RequiredArgsConstructor;
import org.simsimbooks.couponserver.user.dto.UserRequestDto;
import org.simsimbooks.couponserver.user.dto.UserResponseDto;
import org.simsimbooks.couponserver.user.entity.User;
import org.simsimbooks.couponserver.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto createUser(UserRequestDto requestDto) {
        User save = userRepository.save(UserMapper.toUser(requestDto));
        return UserMapper.toResponse(save);
    }

    public UserResponseDto getUser(Long userId) {

        if (Objects.isNull(userId)) {
            throw new IllegalArgumentException("userId is null");
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("id : " + userId + "인 user가 없습니다.");
        }

        return UserMapper.toResponse(optionalUser.get());

    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UserRequestDto requestDto) {
        if (Objects.isNull(userId)) {
            throw new IllegalArgumentException("userId is null");
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("id : " + userId + "인 user가 없습니다.");
        }

        User user = optionalUser.get();
        Optional.ofNullable(requestDto.getName()).ifPresent(user::setName);
        Optional.ofNullable(requestDto.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(requestDto.getGender()).ifPresent(user::setGender);

        return UserMapper.toResponse(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (Objects.isNull(userId)) {
            throw new IllegalArgumentException("userId is null");
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("id : " + userId + "인 user가 없습니다.");
        }

        userRepository.delete(optionalUser.get());
    }
}
