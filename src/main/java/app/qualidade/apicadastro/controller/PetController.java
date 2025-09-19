package app.qualidade.apicadastro.controller;

import app.qualidade.apicadastro.model.Pet;
import app.qualidade.apicadastro.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetRepository petRepository;

    @PostMapping
    public Pet createPet(@RequestBody Pet pet) {
        if (pet.getNome() == null || pet.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do pet é obrigatório");
        }
        return petRepository.save(pet);
    }

    @GetMapping
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable("id") Long id) {
        Optional<Pet> pet = petRepository.findById(id);
        return pet.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable("id") Long id) {
        Optional<Pet> pet = petRepository.findById(id);
        if (pet.isPresent()) {
            petRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
