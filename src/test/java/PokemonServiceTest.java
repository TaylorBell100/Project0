import org.example.repository.DAO.PokemonDAO;
import org.example.repository.DAO.TrainerDAO;
import org.example.repository.entities.PokemonEntity;
import org.example.repository.entities.TrainerEntity;
import org.example.repository.entities.TrainersPokemonEntity;
import org.example.service.PokemonService;
import org.example.service.TrainerService;
import org.example.service.model.Pokemon;
import org.example.service.model.Trainer;
import org.example.service.model.TrainersPokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTest {

    @Mock
    private PokemonDAO pokemonDAO;

    @InjectMocks
    private PokemonService pokemonService;

    private Pokemon testPokemon;
    private PokemonEntity testEntity;
    private Trainer testTrainer;
    private TrainerEntity testTrainerEntity;
    private TrainersPokemon testTrainersPokemon;
    private TrainersPokemonEntity testTrainersPokemonEntity;

    @BeforeEach
    void setup(){
        //setup test pokemon entity
        testEntity = new PokemonEntity();
        testEntity.setId(25);
        testEntity.setName("Pikachu");
        testEntity.setRegion("Kanto");
        testEntity.setType1("Electric");
        testEntity.setType2("NULL");

        //setup test pokemon model
        testPokemon = new Pokemon();
        testPokemon.setId(25);
        testPokemon.setName("Pikachu");
        testPokemon.setRegion("Kanto");
        testPokemon.setType1("Electric");
        testPokemon.setType2("NULL");

        testTrainerEntity = new TrainerEntity();
        testTrainerEntity.setId(1);
        testTrainerEntity.setName("Ash");
        testTrainerEntity.setRegion("Kanto");

        testTrainer = new Trainer();
        testTrainer.setId(1);
        testTrainer.setName("Ash");
        testTrainer.setRegion("Kanto");

        testTrainersPokemonEntity = new TrainersPokemonEntity();
        testTrainersPokemonEntity.setNickname("");
        testTrainersPokemonEntity.setDateObtained(LocalDate.now());
        testTrainersPokemonEntity.setTrainerId(1);
        testTrainersPokemonEntity.setPokemonId(25);
        testTrainersPokemonEntity.setPartySlot(1);

        testTrainersPokemon = new TrainersPokemon();
        testTrainersPokemon.setNickname("");
        testTrainersPokemon.setDateObtained(LocalDate.now());
        testTrainersPokemon.setPartySlot(1);
        //ignoring fields that require another model
        //testTrainersPokemon.setId();
        //testTrainersPokemon.setTrainer();
        //testTrainersPokemon.setPokemon();
    }

    //get entity by id
    @Test
    void getEntityById_Found_ReturnsEntity() throws SQLException {
        when(pokemonDAO.findById(25)).thenReturn(Optional.of(testEntity));

        Optional<PokemonEntity> result = pokemonService.getEntityById(25);

        assertTrue(result.isPresent());
        assertEquals(25, result.get().getId());
    }

    @Test
    void getEntityById_NotFound_ReturnsEmpty() throws SQLException {
        when(pokemonDAO.findById(25)).thenReturn(Optional.empty());

        Optional<PokemonEntity> result = pokemonService.getEntityById(25);

        assertTrue(result.isEmpty());
    }

    @Test
    void getEntityById_SQLException_ReturnsEmpty() throws SQLException {
        when(pokemonDAO.findById(25)).thenThrow(SQLException.class);

        Optional<PokemonEntity> result = pokemonService.getEntityById(25);

        assertTrue(result.isEmpty());
    }

    //get all entities
    @Test
    void getAllEntities_Success_ReturnsList() throws SQLException {
        when(pokemonDAO.findAll(1, "")).thenReturn(List.of(testEntity));

        List<PokemonEntity> result = pokemonService.getAllEntities(1, "");

        assertEquals(1, result.size());
    }

    @Test
    void getAllEntities_Empty_ReturnsEmptyList() throws SQLException {
        when(pokemonDAO.findAll(1, "")).thenReturn(new ArrayList<>());

        List<PokemonEntity> result = pokemonService.getAllEntities(1, "");

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllEntities_SQLException_ReturnsNull() throws SQLException {
        when(pokemonDAO.findAll(1, "")).thenThrow(SQLException.class);

        List<PokemonEntity> result = pokemonService.getAllEntities(1, "");

        assertNull(result);
    }

    //convert entity to model
    @Test
    void convertEntityToModel_Success() {
        Optional<Pokemon> result = pokemonService.convertEntityToModel(testEntity);

        assertTrue(result.isPresent());
        assertEquals("Pikachu", result.get().getName());
    }

    //get model by id
    @Test
    void getModelById_Found_ReturnsModel() throws SQLException {
        when(pokemonDAO.findById(25)).thenReturn(Optional.of(testEntity));

        Optional<Pokemon> result = pokemonService.getModelById(25);

        assertTrue(result.isPresent());
        assertEquals("Pikachu", result.get().getName());
    }

    @Test
    void getModelById_NotFound_ReturnsEmpty() throws SQLException {
        when(pokemonDAO.findById(25)).thenReturn(Optional.empty());

        Optional<Pokemon> result = pokemonService.getModelById(25);

        assertTrue(result.isEmpty());
    }

    //get model by name
    @Test
    void getModelByName_Found_ReturnsModel() throws SQLException {
        when(pokemonDAO.findByName("Pikachu")).thenReturn(Optional.of(testEntity));

        Optional<Pokemon> result = pokemonService.getModelByName("Pikachu");

        assertTrue(result.isPresent());
    }

    @Test
    void getModelByName_NotFound_ReturnsEmpty() throws SQLException {
        when(pokemonDAO.findByName("Pikachu")).thenReturn(Optional.empty());

        Optional<Pokemon> result = pokemonService.getModelByName("Pikachu");

        assertTrue(result.isEmpty());
    }

    //get all models
    @Test
    void getAllModels_Success_ReturnsModels() throws SQLException {
        when(pokemonDAO.findAll(1, "")).thenReturn(List.of(testEntity));

        List<Pokemon> result = pokemonService.getAllModels(1, "");

        assertEquals(1, result.size());
    }

    @Test
    void getAllModels_Empty_ReturnsEmpty() throws SQLException {
        when(pokemonDAO.findAll(1, "")).thenReturn(new ArrayList<>());

        List<Pokemon> result = pokemonService.getAllModels(1, "");

        assertTrue(result.isEmpty());
    }


}
