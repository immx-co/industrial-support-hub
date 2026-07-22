package com.immx.industrialsupport.supportservice.services.user;

import com.immx.industrialsupport.supportservice.dto.role.RoleName;
import com.immx.industrialsupport.supportservice.dto.user.CreateUserRequest;
import com.immx.industrialsupport.supportservice.dto.user.UpdateUserRolesRequest;
import com.immx.industrialsupport.supportservice.entities.Department;
import com.immx.industrialsupport.supportservice.entities.Organization;
import com.immx.industrialsupport.supportservice.entities.Role;
import com.immx.industrialsupport.supportservice.entities.User;
import com.immx.industrialsupport.supportservice.exception_handling.department.NotFoundDepartmentException;
import com.immx.industrialsupport.supportservice.exception_handling.organization.NotFoundOrganizationException;
import com.immx.industrialsupport.supportservice.exception_handling.user.NotFoundUserException;
import com.immx.industrialsupport.supportservice.exception_handling.user.UserAlreadyExistsException;
import com.immx.industrialsupport.supportservice.repositories.DepartmentRepository;
import com.immx.industrialsupport.supportservice.repositories.OrganizationRepository;
import com.immx.industrialsupport.supportservice.repositories.RoleRepository;
import com.immx.industrialsupport.supportservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Сервис для работы с пользователями
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsersByDepartmentId(UUID departmentId) {
        Optional<Department> department = departmentRepository.findById(departmentId);

        if(department.isEmpty())
            throw new NotFoundDepartmentException("There is not department with ID = " + departmentId);

        return userRepository.findAllByDepartment(departmentId);
    }

    @Override
    public List<User> getAllUsersByOrganizationId(UUID organizationId) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);

        if(organization.isEmpty())
            throw new NotFoundOrganizationException("There is not organization with ID = " + organizationId);

        return userRepository.findAllByOrganization(organizationId);
    }

    @Override
    public User create(UUID departmentId,
                       CreateUserRequest createUserRequest) {
        Optional<Department> department = departmentRepository.findById(departmentId);

        if(department.isEmpty())
            throw new NotFoundDepartmentException("There is no department with ID = " + departmentId);

        boolean usernameAlreadyExists = userRepository.existsByDepartment_IdAndUsernameIgnoreCase(
                departmentId,
                createUserRequest.getUsername());

        if(usernameAlreadyExists)
            throw new UserAlreadyExistsException(
                    "There is already exists user with username = " + createUserRequest.getUsername()
                            + " в подразделении " + department.get()
                            .getName());

        User user = new User(
                department.get(),
                createUserRequest.getExternalId(),
                createUserRequest.getUsername(),
                createUserRequest.getEmail(),
                passwordEncoder.encode(createUserRequest.getPassword()),
                createUserRequest.getFirstName(),
                createUserRequest.getLastName());

        Set<RoleName> requestedRoleNames = createUserRequest.getRoles();

        if(requestedRoleNames == null || requestedRoleNames.isEmpty())
            requestedRoleNames = Set.of(RoleName.ROLE_EMPLOYEE);

        List<Role> roles = roleRepository.findAllByNameIn(requestedRoleNames);

        if(roles.size() != requestedRoleNames.size())
            throw new IllegalStateException("One or more requested roles are missing in the database");

        user.getRoles()
                .addAll(roles);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateRoles(UUID userId,
                            UpdateUserRolesRequest updateUserRolesRequest) {
        Optional<User> user = userRepository.findByIdWithRoles(userId);

        if(user.isEmpty())
            throw new NotFoundUserException("There is no user with ID = " + userId);

        List<Role> roles = roleRepository.findAllByNameIn(updateUserRolesRequest.getRoles());

        if(roles.size() != updateUserRolesRequest.getRoles()
                .size())
            throw new IllegalStateException("One or more requested roles are missing in the database");

        user.get()
                .getRoles()
                .clear();
        user.get()
                .getRoles()
                .addAll(roles);

        return userRepository.save(user.get());
    }
}
