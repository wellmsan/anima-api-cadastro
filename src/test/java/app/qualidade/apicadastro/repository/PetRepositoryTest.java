package app.qualidade.apicadastro.repository;

import app.qualidade.apicadastro.model.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private PetRepository petRepository;

    private Pet pet;

    @BeforeEach
    public void setUp() {
        pet = new Pet("Rex", "Labrador", 3);
    }

    @Test
    public void testSavePet() {
        // Configuração do mock
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        when(jdbcTemplate.update(any(), any(GeneratedKeyHolder.class))).thenAnswer(invocation -> {
            GeneratedKeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(java.util.Collections.singletonMap("id", 1L));
            return 1;
        });

        // Execução
        Pet savedPet = petRepository.save(pet);

        // Verificações
        assertNotNull(savedPet);
        assertEquals(1L, savedPet.getId());
        assertEquals("Rex", savedPet.getNome());
        assertEquals("Labrador", savedPet.getRaca());
        assertEquals(3, savedPet.getIdade());

        verify(jdbcTemplate).update(any(), any(GeneratedKeyHolder.class));
    }

    @Test
    public void testFindAll() {
        // Configuração do mock
        List<Pet> expectedPets = List.of(
                new Pet("Rex", "Labrador", 3),
                new Pet("Mimi", "Siames", 5));

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedPets);

        // Execução
        List<Pet> result = petRepository.findAll();

        // Verificações
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class));
    }

    @Test
    public void testFindByIdExistente() {
        // Configuração do mock
        Pet expectedPet = new Pet("Rex", "Labrador", 3);
        expectedPet.setId(1L);

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyLong()))
                .thenReturn(expectedPet);

        // Execução
        Optional<Pet> result = petRepository.findById(1L);

        // Verificações
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Rex", result.get().getNome());
        verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), anyLong());
    }

    @Test
    public void testFindByIdNaoExistente() {
        // Configuração do mock
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyLong()))
                .thenThrow(new org.springframework.dao.EmptyResultDataAccessException(1));

        // Execução
        Optional<Pet> result = petRepository.findById(999L);

        // Verificações
        assertFalse(result.isPresent());
        verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), anyLong());
    }

    @Test
    public void testDeleteById() {
        // Configuração do mock
        when(jdbcTemplate.update(anyString(), anyLong())).thenReturn(1);

        // Execução
        petRepository.deleteById(1L);

        // Verificações
        verify(jdbcTemplate).update(anyString(), eq(1L));
    }
}