package app.qualidade.apicadastro.integration;

import app.qualidade.apicadastro.model.Pet;
import app.qualidade.apicadastro.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PetIntegrationTest {

    @Autowired
    private PetRepository petRepository;

    @Test
    public void testSaveAndFindPet() {
        // Cria um pet
        Pet pet = new Pet("Rex", "Labrador", 3);

        // Salva no banco
        Pet savedPet = petRepository.save(pet);

        // Verifica se foi salvo com ID
        assertNotNull(savedPet.getId());

        // Busca pelo ID
        Optional<Pet> foundPet = petRepository.findById(savedPet.getId());

        // Verifica se encontrou
        assertTrue(foundPet.isPresent());
        assertEquals("Rex", foundPet.get().getNome());
        assertEquals("Labrador", foundPet.get().getRaca());
        assertEquals(3, foundPet.get().getIdade());
    }

    @Test
    public void testFindAllPets() {
        // Limpa dados anteriores (se necessário)
        List<Pet> allPets = petRepository.findAll();
        allPets.forEach(pet -> petRepository.deleteById(pet.getId()));

        // Cria alguns pets
        Pet pet1 = new Pet("Rex", "Labrador", 3);
        Pet pet2 = new Pet("Mimi", "Siames", 5);

        petRepository.save(pet1);
        petRepository.save(pet2);

        // Busca todos
        List<Pet> pets = petRepository.findAll();

        // Verifica se encontrou todos
        assertEquals(2, pets.size());
    }

    @Test
    public void testDeletePet() {
        // Cria um pet
        Pet pet = new Pet("Rex", "Labrador", 3);
        Pet savedPet = petRepository.save(pet);

        // Verifica se foi salvo
        assertNotNull(savedPet.getId());

        // Deleta o pet
        petRepository.deleteById(savedPet.getId());

        // Tenta encontrar o pet deletado
        Optional<Pet> foundPet = petRepository.findById(savedPet.getId());

        // Verifica que não encontrou
        assertFalse(foundPet.isPresent());
    }
}