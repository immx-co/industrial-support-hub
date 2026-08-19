package com.immx.industrialsupport.supportservice.services.incident;

import com.immx.industrialsupport.contracts.incident.AssignIncidentRequest;
import com.immx.industrialsupport.contracts.incident.ChangeIncidentStatusRequest;
import com.immx.industrialsupport.contracts.incident.CreateIncidentRequest;
import com.immx.industrialsupport.contracts.role.RoleName;
import com.immx.industrialsupport.supportservice.entities.Incident;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с обращениями.
 */
@Service
public interface IIncidentService {

    /**
     * Создаёт обращение.
     *
     * @param organizationId идентификатор организации, в которой создаётся обращение.
     * @param departmentId   идентификатор подразделения организации, пользователь которой создаёт обращение.
     * @param reporterId     идентификатор пользователя, который создаёт обращение.
     * @param request        модель запроса на создание обращения.
     * @return созданное обращение.
     */
    Incident create(UUID organizationId,
                    UUID departmentId,
                    UUID reporterId,
                    CreateIncidentRequest request);

    /**
     * Получает обращение по идентификатору и по идентификатору организации.
     *
     * @param organizationId идентификатор организации.
     * @param incidentId     идентификатор обращения.
     * @return полученное обращение
     */
    Incident getById(UUID organizationId,
                     UUID incidentId);

    /**
     * Получает список обращений в организации.
     *
     * @param organizationId идентификатор организации
     * @return полученный список обращений по идентификатору организации
     */
    List<Incident> getAllByOrganization(UUID organizationId);

    /**
     * Назначает инженера на выполнение обращения.
     *
     * @param organizationId        идентификатор организации, в которой назначить инженера на выполнение обращения
     * @param incidentId            идентификатор обращения, которое назначить на инженера
     * @param assignIncidentRequest модель запроса на назначения инженера на выполнение обращения
     * @return полученное обращение с назначенным инженером на ее выполнение
     */
    Incident assignEngineer(UUID organizationId,
                            UUID incidentId,
                            AssignIncidentRequest assignIncidentRequest);

    /**
     * Изменяет статус обращения.
     *
     * @param organizationId              идентификатор организации, статус обращения в которой изменить
     * @param incidentId                  идентификатор обращения, статус которого изменить
     * @param changeIncidentStatusRequest модель запроса на изменение статуса обращения
     * @return полученное обращение с измененным статусом
     */
    Incident changeStatus(UUID organizationId,
                          UUID incidentId,
                          ChangeIncidentStatusRequest changeIncidentStatusRequest);

    /**
     * Удаляет все обращения из базы данных.
     *
     * @return количество удаленных обращений
     */
    long deleteAll();

    /**
     * Получает активные обращения, фильтруя по идентификатору организации, подразделения, пользователя и ролей.
     *
     * @param organizationId идентификатор организации
     * @param userId         идентификатор пользователя
     * @param roles          множество ролей
     * @return отфильтрованный список обращений.
     */
    List<Incident> getActiveForUser(UUID organizationId,
                                    UUID userId,
                                    Set<RoleName> roles);

}
