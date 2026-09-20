package com.immx.industrialsupport.contracts.organization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Модель для создания организации.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrganizationRequest {

    /**
     * Внутренний идентификатор организации.
     */
    private String externalId;

    /**
     * Имя организации.
     */
    private String name;
}
