package app.qualidade.apicadastro.controller;

import app.qualidade.apicadastro.model.Pet;
import app.qualidade.apicadastro.repository.PetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetController petController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreatePet() throws Exception {
        // Configuração
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();

        Pet pet = new Pet("Rex", "Labrador", 3);
        Pet savedPet = new Pet("Rex", "Labrador", 3);
        savedPet.setId(1L);

        when(petRepository.save(any(Pet.class))).thenReturn(savedPet);

        // Execução e Verificação
        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Rex")))
                .andExpect(jsonPath("$.raca", is("Labrador")))
                .andExpect(jsonPath("$.idade", is(3)));

        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    public void testGetAllPets() throws Exception {
        // Configuração
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();

        Pet pet1 = new Pet("Rex", "Labrador", 3);
        pet1.setId(1L);
        Pet pet2 = new Pet("Mimi", "Siames", 5);
        pet2.setId(2L);

        when(petRepository.findAll()).thenReturn(List.of(pet1, pet2));

        // Execução e Verificação
        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nome", is("Rex")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nome", is("Mimi")));

        verify(petRepository, times(1)).findAll();
    }

    @Test
    public void testGetPetByIdExistente() throws Exception {
        // Configuração
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();

        Pet pet = new Pet("Rex", "Labrador", 3);
        pet.setId(1L);

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        // Execução e Verificação
        mockMvc.perform(get("/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Rex")))
                .andExpect(jsonPath("$.raca", is("Labrador")))
                .andExpect(jsonPath("$.idade", is(3)));

        verify(petRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetPetByIdNaoExistente() throws Exception {
        // Configuração
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();

        when(petRepository.findById(999L)).thenReturn(Optional.empty());

        // Execução e Verificação
        mockMvc.perform(get("/pets/999"))
                .andExpect(status().isNotFound());

        verify(petRepository, times(1)).findById(999L);
    }

    @Test
    public void testDeletePetExistente() throws Exception {
        // Configuração
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();

        Pet pet = new Pet("Rex", "Labrador", 3);
        pet.setId(1L);

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        doNothing().when(petRepository).deleteById(1L);

        // Execução e Verificação
        mockMvc.perform(delete("/pets/1"))
                .andExpect(status().isOk());

        verify(petRepository, times(1)).findById(1L);
        verify(petRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePetNaoExistente() throws Exception {
        // Configuração
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();

        when(petRepository.findById(999L)).thenReturn(Optional.empty());

        // Execução e Verificação
        mockMvc.perform(delete("/pets/999"))
                .andExpect(status().isNotFound());

        verify(petRepository, times(1)).findById(999L);
        verify(petRepository, never()).deleteById(anyLong());
    }
}