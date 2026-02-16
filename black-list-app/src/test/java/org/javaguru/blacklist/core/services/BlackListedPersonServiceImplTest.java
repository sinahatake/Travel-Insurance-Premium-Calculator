package org.javaguru.blacklist.core.services;

import org.javaguru.blacklist.core.api.command.BlackListedPersonCoreCommand;
import org.javaguru.blacklist.core.api.command.BlackListedPersonCoreResult;
import org.javaguru.blacklist.core.api.dto.BlackListedPersonDTO;
import org.javaguru.blacklist.core.api.dto.ValidationErrorDTO;
import org.javaguru.blacklist.core.domain.BlackListedPersonEntity;
import org.javaguru.blacklist.core.repositories.BlackListedPersonEntityRepository;
import org.javaguru.blacklist.core.validations.BlackListRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BlackListedPersonServiceImplTest {

    private BlackListRequestValidator validator;
    private BlackListedPersonEntityRepository repository;
    private BlackListedPersonServiceImpl service;

    @BeforeEach
    void setUp() {
        validator = mock(BlackListRequestValidator.class);
        repository = mock(BlackListedPersonEntityRepository.class);
        service = new BlackListedPersonServiceImpl(repository, validator);
    }

    @Test
    void shouldReturnErrorsWhenValidationFails() {
        BlackListedPersonDTO person = new BlackListedPersonDTO();
        List<ValidationErrorDTO> errors = List.of(new ValidationErrorDTO("ERROR_CODE_1", "Field personFirstName must not be empty!"));

        when(validator.validate(person)).thenReturn(errors);

        BlackListedPersonCoreCommand command = new BlackListedPersonCoreCommand(person);
        BlackListedPersonCoreResult result = service.check(command);

        assertThat(result.getErrors()).isEqualTo(errors);
        assertThat(result.getPersonDTO()).isNull();

        verifyNoInteractions(repository);
    }

    @Test
    void shouldMarkPersonAsBlackListedWhenFoundInRepository() {
        BlackListedPersonDTO person = new BlackListedPersonDTO();
        person.setPersonFirstName("John");
        person.setPersonLastName("Doe");
        person.setPersonCode("123");

        when(validator.validate(person)).thenReturn(List.of());
        when(repository.findByFirstNameAndLastNameAndPersonCode("John", "Doe", "123"))
                .thenReturn(Optional.of(new BlackListedPersonEntity()));

        BlackListedPersonCoreCommand command = new BlackListedPersonCoreCommand(person);
        BlackListedPersonCoreResult result = service.check(command);

        assertThat(result.getErrors()).isNull();
        assertThat(result.getPersonDTO().isBlackListed()).isTrue();

        verify(repository).findByFirstNameAndLastNameAndPersonCode("John", "Doe", "123");
    }

    @Test
    void shouldMarkPersonAsNotBlackListedWhenNotFoundInRepository() {
        BlackListedPersonDTO person = new BlackListedPersonDTO();
        person.setPersonFirstName("Jane");
        person.setPersonLastName("Smith");
        person.setPersonCode("999");

        when(validator.validate(person)).thenReturn(List.of());
        when(repository.findByFirstNameAndLastNameAndPersonCode("Jane", "Smith", "999"))
                .thenReturn(Optional.empty());

        BlackListedPersonCoreCommand command = new BlackListedPersonCoreCommand(person);
        BlackListedPersonCoreResult result = service.check(command);

        assertThat(result.getErrors()).isNull();
        assertThat(result.getPersonDTO().isBlackListed()).isFalse();

        verify(repository).findByFirstNameAndLastNameAndPersonCode("Jane", "Smith", "999");
    }
}
