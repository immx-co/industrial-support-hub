package com.immx.industrialsupport.supportservice.mappers;

import com.immx.industrialsupport.supportservice.dto.user.UserResponse;
import com.immx.industrialsupport.supportservice.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;

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
                user.getUpdatedAt());
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
