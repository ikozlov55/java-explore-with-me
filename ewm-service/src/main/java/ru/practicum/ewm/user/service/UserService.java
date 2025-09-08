package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {
    Collection<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto createUser(UserDto userDto);

    void deleteUser(long userId);
}
