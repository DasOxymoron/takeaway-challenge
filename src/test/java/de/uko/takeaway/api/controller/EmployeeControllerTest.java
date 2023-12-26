package de.uko.takeaway.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.uko.takeaway.api.rest.controller.EmployeeController;
import de.uko.takeaway.api.rest.dto.EmployeeCreationDto;
import de.uko.takeaway.api.rest.dto.EmployeeDto;
import de.uko.takeaway.api.rest.dto.EmployeeUpdateDto;
import de.uko.takeaway.api.rest.dto.FullNameDto;
import de.uko.takeaway.api.rest.mappers.EmployeeMapper;
import de.uko.takeaway.domain.domain.Employee;
import de.uko.takeaway.domain.service.EmployeeService;
import de.uko.takeaway.domain.service.CreateEmployeeUseCase;
import de.uko.takeaway.domain.service.EmployeeUpdateUseCase;
import de.uko.takeaway.api.config.ControllerAdviceConfiguration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Import(ControllerAdviceConfiguration.class)
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    public static final FullNameDto FULL_NAME = new FullNameDto("firstName", "lastName");
    public static final String EMAIL = "email@domain.com";
    public static final LocalDate BIRTHDAY = LocalDate.of(2023, 12, 19);
    public static final List<String> HOBBIES = Collections.emptyList();
    public static final UUID ID = UUID.randomUUID();
    public static final EmployeeDto EMPLOYEE_DTO = new EmployeeDto(
        ID,
        FULL_NAME,
        EMAIL,
        HOBBIES,
        BIRTHDAY
    );
    @MockBean
    private CreateEmployeeUseCase createEmployeeUseCase;
    @MockBean
    private EmployeeMapper employeeDtoMapper;
    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private EmployeeUpdateUseCase employeeUpdateUseCase;
    @Autowired
    private MockMvc mockMvc;

    @Nested
    class CreationTests {

        @Test
        @SneakyThrows
        void shouldValidateOnCreation() {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                    .contentType("application/json")
                    .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.type").value("https://www.lieferando.de/problem/constraint-violation"))
                .andExpect(jsonPath("$.violations[0].field").value("birthday"))
                .andExpect(jsonPath("$.violations[0].message").value("must not be null"))
                .andExpect(jsonPath("$.violations[1].field").value("email"))
                .andExpect(jsonPath("$.violations[1].message").value("must not be empty"))
                .andExpect(jsonPath("$.violations[2].field").value("fullName"))
                .andExpect(jsonPath("$.violations[2].message").value("must not be null"))
                .andExpect(jsonPath("$.violations[3].field").value("hobbies"))
                .andExpect(jsonPath("$.violations[3].message").value("must not be null"))
            ;
        }

        @Test
        @SneakyThrows
        void shouldValidateFUllNameOnCreation() {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                    .contentType("application/json")
                    .content(/* language=JSON */ """
                        {
                            "fullName": {
                                "firstName": null,
                                "lastName": null
                            },
                            "hobbies": [],
                            "email": "email@domain.com",
                            "birthday": "2023-12-19"
                        }"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.type").value("https://www.lieferando.de/problem/constraint-violation"))
                .andExpect(jsonPath("$.violations[0].field").value("fullName.firstName"))
                .andExpect(jsonPath("$.violations[0].message").value("must not be blank"))
                .andExpect(jsonPath("$.violations[1].field").value("fullName.lastName"))
                .andExpect(jsonPath("$.violations[1].message").value("must not be blank"))
            ;
        }

        @Test
        @SneakyThrows
        void shouldValidateEmail() {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                    .contentType("application/json")
                    .content(/* language=JSON */ """
                        {
                            "fullName": {
                                "firstName": "firstName",
                                "lastName":  "lastName"
                            },
                            "hobbies": [],
                            "email": "emaildomain.com",
                            "birthday": "2023-12-19"
                        }"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.type").value("https://www.lieferando.de/problem/constraint-violation"))
                .andExpect(jsonPath("$.violations[0].field").value("email"))
                .andExpect(jsonPath("$.violations[0].message").value("must be a well-formed email address"))
            ;
        }

        @Test
        @SneakyThrows
        void shouldCreate() {
            Employee mappedEmployee = mock(Employee.class);

            EmployeeCreationDto expectedRequestBody = new EmployeeCreationDto(
                FULL_NAME,
                EMAIL,
                HOBBIES,
                BIRTHDAY
            );

            doReturn(mappedEmployee).when(employeeDtoMapper).mapToDomain(any());
            doReturn(mappedEmployee).when(createEmployeeUseCase).create(any());
            doReturn(EMPLOYEE_DTO).when(employeeDtoMapper).mapFromDomain(any());

            mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                    .contentType("application/json")
                    .content(/* language=JSON */ """
                        {
                            "fullName": {
                                "firstName": "firstName",
                                "lastName":  "lastName"
                            },
                            "hobbies": [],
                            "email": "email@domain.com",
                            "birthday": "2023-12-19"
                        }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID.toString()))
                .andExpect(jsonPath("$.fullName.firstName").value(FULL_NAME.firstName()))
                .andExpect(jsonPath("$.fullName.lastName").value(FULL_NAME.lastName()))
                .andExpect(jsonPath("$.hobbies").isEmpty())
                .andExpect(jsonPath("$.birthday").value("2023-12-19"))
                .andExpect(jsonPath("$.email").value(EMAIL))
            ;

            verify(employeeDtoMapper).mapToDomain(expectedRequestBody);
            verify(createEmployeeUseCase).create(mappedEmployee);
            verify(employeeDtoMapper).mapFromDomain(mappedEmployee);
        }
    }

    @Nested
    class GetEmployeesTests {

        @Test
        @SneakyThrows
        void shouldGetAll() {
            Employee employee = mock(Employee.class);
            PageRequest pageRequest = PageRequest.of(0, 10);

            doReturn(EMPLOYEE_DTO).when(employeeDtoMapper).mapFromDomain(any());
            PageImpl<Employee> toBeReturned = new PageImpl<>(List.of(employee), pageRequest,
                1); //https://github.com/spring-projects/spring-data-commons/issues/2987
            doReturn(toBeReturned).when(employeeService).getAll(any());

            mockMvc.perform(MockMvcRequestBuilders.get("/api/employees")
                    .param("page", "0")
                    .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(ID.toString()))
                .andExpect(jsonPath("$.content[0].fullName.firstName").value(FULL_NAME.firstName()))
                .andExpect(jsonPath("$.content[0].fullName.lastName").value(FULL_NAME.lastName()))
                .andExpect(jsonPath("$.content[0].email").value(EMAIL))
                .andExpect(jsonPath("$.content[0].hobbies").isEmpty())
                .andExpect(jsonPath("$.content[0].birthday").value(BIRTHDAY.toString()))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.pageable.sort.empty").value(true))
                .andExpect(jsonPath("$.pageable.sort.sorted").value(false))
                .andExpect(jsonPath("$.pageable.sort.unsorted").value(true))
            ;

            verify(employeeService).getAll(pageRequest);
            verify(employeeDtoMapper).mapFromDomain(employee);
        }

        @Test
        @SneakyThrows
        void shouldGetSingle() {
            Employee employee = mock(Employee.class);

            doReturn(EMPLOYEE_DTO).when(employeeDtoMapper).mapFromDomain(any());
            doReturn(employee).when(employeeService).getById(any());

            mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID.toString()))
                .andExpect(jsonPath("$.fullName.firstName").value(FULL_NAME.firstName()))
                .andExpect(jsonPath("$.fullName.lastName").value(FULL_NAME.lastName()))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.hobbies").isEmpty())
                .andExpect(jsonPath("$.birthday").value(BIRTHDAY.toString()))
            ;

            verify(employeeService).getById(ID);
            verify(employeeDtoMapper).mapFromDomain(employee);
        }
    }

    @Nested
    class UpdateTests {

        @Test
        @SneakyThrows
        void shouldValidate() {
            mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", ID)
                    .contentType("application/json")
                    .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Constraint Violation"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.type").value("https://www.lieferando.de/problem/constraint-violation"))
                .andExpect(jsonPath("$.violations[0].field").value("email"))
                .andExpect(jsonPath("$.violations[0].message").value("must not be empty"))
                .andExpect(jsonPath("$.violations[1].field").value("hobbies"))
                .andExpect(jsonPath("$.violations[1].message").value("must not be null"))
                .andExpect(jsonPath("$.violations[2].field").value("lastName"))
                .andExpect(jsonPath("$.violations[2].message").value("must not be empty"));
        }
    }

    @Test
    @SneakyThrows
    void shouldUpdate() {
        Employee mappedEmployee = mock(Employee.class);

        doReturn(mappedEmployee).when(employeeUpdateUseCase).update(any(), any());
        doReturn(EMPLOYEE_DTO).when(employeeDtoMapper).mapFromDomain(any());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", ID)
                .contentType("application/json")
                .content("""
                    {
                        "lastName": "lastName",
                        "email": "email@domain.com",
                        "hobbies": []
                    }"""))
            .andExpect(status().isOk())
        ;

        verify(employeeUpdateUseCase).update(ID, new EmployeeUpdateDto(FULL_NAME.lastName(), EMAIL, HOBBIES));
        verify(employeeDtoMapper).mapFromDomain(mappedEmployee);
    }

    @Nested
    class DeletionTests {

        @Test
        @SneakyThrows
        void shouldDelete() {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", ID))
                .andExpect(status().isOk());

            verify(employeeService).delete(ID);
        }
    }
}