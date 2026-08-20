package io.github.mjh5153.complyapi.service.impl;

import io.github.mjh5153.complyapi.dto.CompanyDTO;
import io.github.mjh5153.complyapi.entity.Company;
import io.github.mjh5153.complyapi.exception.ResourceNotFoundException;
import io.github.mjh5153.complyapi.mapper.CompanyMapper;
import io.github.mjh5153.complyapi.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Pure Mockito unit tests for {@link CompanyServiceImpl}. No Spring context,
 * so they run in milliseconds and only cover business logic in the service.
 */
@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock CompanyRepository repository;
    @Mock CompanyMapper mapper;
    @InjectMocks CompanyServiceImpl service;

    private Company entity;
    private CompanyDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Company(1L, "Acme", "acme@example.com");
        dto = new CompanyDTO(1L, "Acme", "acme@example.com");
    }

    // --- reads ----------------------------------------------------------

    @Test
    void getAllCompanies_mapsEachEntityToDto() {
        given(repository.findAll()).willReturn(List.of(entity));
        given(mapper.toDTO(entity)).willReturn(dto);

        List<CompanyDTO> result = service.getAllCompanies();

        assertThat(result).containsExactly(dto);
    }

    @Test
    void getCompany_whenPresent_returnsDto() {
        given(repository.findById(1L)).willReturn(Optional.of(entity));
        given(mapper.toDTO(entity)).willReturn(dto);

        assertThat(service.getCompany(1L)).isEqualTo(dto);
    }

    @Test
    void getCompany_whenMissing_throwsResourceNotFound() {
        given(repository.findById(42L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCompany(42L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("42");
    }

    // --- writes ---------------------------------------------------------

    @Test
    void createCompany_persistsMappedEntityAndReturnsDto() {
        given(mapper.toEntity(dto)).willReturn(entity);
        given(repository.save(entity)).willReturn(entity);
        given(mapper.toDTO(entity)).willReturn(dto);

        assertThat(service.createCompany(dto)).isEqualTo(dto);

        verify(repository).save(entity);
    }

    @Test
    void updateCompany_whenMissing_throwsAndDoesNotSave() {
        given(repository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCompany(1L, dto))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void deleteCompany_whenMissing_throwsAndDoesNotDelete() {
        given(repository.existsById(1L)).willReturn(false);

        assertThatThrownBy(() -> service.deleteCompany(1L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, never()).deleteById(any());
    }

    // --- async ----------------------------------------------------------

    @Test
    void createCompanyAsync_completesNormallyOnSuccess() throws Exception {
        given(mapper.toEntity(dto)).willReturn(entity);
        given(repository.save(entity)).willReturn(entity);
        given(mapper.toDTO(entity)).willReturn(dto);

        CompletableFuture<CompanyDTO> future = service.createCompanyAsync(dto);

        assertThat(future.get()).isEqualTo(dto);
    }

    @Test
    void createCompaniesAsync_returnsAllResultsInOrder() throws Exception {
        Company e2 = new Company(2L, "Beta", "b@x.com");
        CompanyDTO d2 = new CompanyDTO(2L, "Beta", "b@x.com");
        given(mapper.toEntity(dto)).willReturn(entity);
        given(mapper.toEntity(d2)).willReturn(e2);
        given(repository.save(entity)).willReturn(entity);
        given(repository.save(e2)).willReturn(e2);
        given(mapper.toDTO(entity)).willReturn(dto);
        given(mapper.toDTO(e2)).willReturn(d2);

        List<CompanyDTO> result = service.createCompaniesAsync(List.of(dto, d2)).get();

        assertThat(result).containsExactly(dto, d2);
    }
}

