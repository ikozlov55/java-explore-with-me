package ru.practicum.ewm.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.user.model.User;

@UtilityClass
public final class UserMapper {
    public static User toModel(UserDto user) {
        return new User(null, user.name(), user.email());
    }

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
