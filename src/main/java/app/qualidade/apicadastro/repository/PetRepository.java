package app.qualidade.apicadastro.repository;

import app.qualidade.apicadastro.model.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class PetRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Pet> rowMapper = (rs, rowNum) -> {
        Pet pet = new Pet();
        pet.setId(rs.getLong("id"));
        pet.setNome(rs.getString("nome"));
        pet.setRaca(rs.getString("raca"));
        pet.setIdade(rs.getInt("idade"));
        return pet;
    };

    public Pet save(Pet pet) {
        String sql = "INSERT INTO pets (nome, raca, idade) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pet.getNome());
            ps.setString(2, pet.getRaca());
            ps.setInt(3, pet.getIdade());
            return ps;
        }, keyHolder);

        pet.setId(keyHolder.getKey().longValue());
        return pet;
    }

    public List<Pet> findAll() {
        String sql = "SELECT * FROM pets";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Pet> findById(Long id) {
        String sql = "SELECT * FROM pets WHERE id = ?";
        try {
            Pet pet = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(pet);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM pets WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
