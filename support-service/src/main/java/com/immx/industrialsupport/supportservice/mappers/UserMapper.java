package com.immx.industrialsupport.supportservice.mappers;

import com.immx.industrialsupport.contracts.user.UserResponse;
import com.immx.industrialsupport.supportservice.entities.Role;
import com.immx.industrialsupport.supportservice.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер модели пользователя в модель ответа сервиса
 */
@Component
public class UserMapper {

    /**
     * Маппает модель пользователя в модель пользователя ответа сервиса.
     *
     * @param user модель пользователя
     * @return модель пользователя ответа сервиса
     */
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getDepartment()
                        .getOrganization()
                        .getId(),
                user.getDepartment()
                        .getId(),
                user.getExternalId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.isEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()),
                user.getTelegramUsername());
    }

    /**
     * Маппает список моделей пользователей в список пользователей ответа сервиса.
     *
     * @param users список моделей пользователей
     * @return список пользователей ответа сервиса
     */
    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(this::toResponse)
                .toList();
    }
}
