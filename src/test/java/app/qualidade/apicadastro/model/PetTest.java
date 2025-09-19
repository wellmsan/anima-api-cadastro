package app.qualidade.apicadastro.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PetTest {

    @Test
    public void testPetConstrutorVazio() {
        Pet pet = new Pet();

        assertNull(pet.getId());
        assertNull(pet.getNome());
        assertNull(pet.getRaca());
        assertNull(pet.getIdade());
    }

    @Test
    public void testPetConstrutorComParametros() {
        Pet pet = new Pet("Rex", "Labrador", 3);

        assertNull(pet.getId()); // ID é gerado automaticamente
        assertEquals("Rex", pet.getNome());
        assertEquals("Labrador", pet.getRaca());
        assertEquals(3, pet.getIdade());
    }

    @Test
    public void testSettersEGetters() {
        Pet pet = new Pet();

        pet.setId(1L);
        pet.setNome("Mimi");
        pet.setRaca("Siames");
        pet.setIdade(5);

        assertEquals(1L, pet.getId());
        assertEquals("Mimi", pet.getNome());
        assertEquals("Siames", pet.getRaca());
        assertEquals(5, pet.getIdade());
    }

    @Test
    public void testDadosNulos() {
        Pet pet = new Pet(null, null, null);

        assertNull(pet.getNome());
        assertNull(pet.getRaca());
        assertNull(pet.getIdade());
    }
}