import org.example.repository.DAO.PokemonDAO;
import org.example.repository.DAO.TrainerDAO;
import org.example.repository.DAO.TrainersPokemonDAO;
import org.example.repository.entities.PokemonEntity;
import org.example.repository.entities.TPCompositeKey;
import org.example.repository.entities.TrainerEntity;
import org.example.repository.entities.TrainersPokemonEntity;
import org.example.service.PokemonService;
import org.example.service.TrainerService;
import org.example.service.TrainersPokemonService;
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


import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainersPokemonServiceTest {

    @Mock
    private TrainersPokemonDAO trainersPokemonDAO;

    @Mock
    private TrainerService trainerService;

    @Mock
    private PokemonService pokemonService;

    @InjectMocks
    private TrainersPokemonService trainersPokemonService;

    private TrainersPokemonEntity testEntity;
    private Trainer testTrainer;
    private Pokemon testPokemon;
    private TPCompositeKey testKey;

    @BeforeEach
    void setup() {
        testEntity = new TrainersPokemonEntity();
        testEntity.setTrainerId(1);
        testEntity.setPokemonId(25);
        testEntity.setNickname("Sparky");

        testTrainer = new Trainer();
        testTrainer.setId(1);
        testTrainer.setName("Ash");

        testPokemon = new Pokemon();
        testPokemon.setId(25);
        testPokemon.setName("Pikachu");

        testKey = new TPCompositeKey(1, 25);
    }

    //create entity
    @Test
    void createEntity_Success_ReturnsKey() throws SQLException {
        when(trainersPokemonDAO.create(testEntity)).thenReturn(testKey);

        TPCompositeKey result = trainersPokemonService.createEntity(testEntity);

        assertNotNull(result);
        assertEquals(testKey, result);
    }

    @Test
    void createEntity_SQLException_ReturnsNull() throws SQLException {
        when(trainersPokemonDAO.create(testEntity)).thenThrow(SQLException.class);

        TPCompositeKey result = trainersPokemonService.createEntity(testEntity);

        assertNull(result);
    }

    //get entity by id
    @Test
    void getEntityById_Found_ReturnsEntity() throws SQLException {
        when(trainersPokemonDAO.findById(testKey))
                .thenReturn(Optional.of(testEntity));

        Optional<TrainersPokemonEntity> result =
                trainersPokemonService.getEntityById(testKey);

        assertTrue(result.isPresent());
    }

    @Test
    void getEntityById_NotFound_ReturnsEmpty() throws SQLException {
        when(trainersPokemonDAO.findById(testKey))
                .thenReturn(Optional.empty());

        Optional<TrainersPokemonEntity> result =
                trainersPokemonService.getEntityById(testKey);

        assertTrue(result.isEmpty());
    }

    //catch pokemon
    @Test
    void catchPokemon_Success_ReturnsOne() throws SQLException {
        when(trainersPokemonDAO.catchPokemon(testEntity)).thenReturn(1);

        int result = trainersPokemonService.catchPokemon(testEntity);

        assertEquals(1, result);
    }

    @Test
    void catchPokemon_SQLException_ReturnsZero() throws SQLException {
        when(trainersPokemonDAO.catchPokemon(testEntity))
                .thenThrow(SQLException.class);

        int result = trainersPokemonService.catchPokemon(testEntity);

        assertEquals(0, result);
    }

    //see/release pokemon
    @Test
    void seePokemon_Success() throws SQLException {
        when(trainersPokemonDAO.seePokemon(testEntity)).thenReturn(1);

        int result = trainersPokemonService.seePokemon(testEntity);

        assertEquals(1, result);
    }

    @Test
    void releasePokemon_SQLException_ReturnsZero() throws SQLException {
        when(trainersPokemonDAO.releasePokemon(testEntity))
                .thenThrow(SQLException.class);

        int result = trainersPokemonService.releasePokemon(testEntity);

        assertEquals(0, result);
    }

    //convert entity to model
    @Test
    void convertEntityToModel_Success() {
        when(pokemonService.getModelById(25))
                .thenReturn(Optional.of(testPokemon));
        when(trainerService.getModelById(1))
                .thenReturn(Optional.of(testTrainer));

        Optional<TrainersPokemon> result =
                trainersPokemonService.convertEntityToModel(testEntity);

        assertTrue(result.isPresent());
        assertEquals("Sparky", result.get().getNickname());
    }

    @Test
    void convertEntityToModel_MissingPokemon_ReturnsEmpty() {
        when(pokemonService.getModelById(25))
                .thenReturn(Optional.empty());

        Optional<TrainersPokemon> result =
                trainersPokemonService.convertEntityToModel(testEntity);

        assertTrue(result.isEmpty());
    }

    //get model by id
    @Test
    void getModelById_Success() throws SQLException {
        when(trainersPokemonDAO.findById(testKey))
                .thenReturn(Optional.of(testEntity));
        when(pokemonService.getModelById(25))
                .thenReturn(Optional.of(testPokemon));
        when(trainerService.getModelById(1))
                .thenReturn(Optional.of(testTrainer));

        Optional<TrainersPokemon> result =
                trainersPokemonService.getModelById(testKey);

        assertTrue(result.isPresent());
    }

    //get all models
    @Test
    void getAllModels_Success() throws SQLException {
        when(trainersPokemonDAO.findAll())
                .thenReturn(List.of(testEntity));
        when(pokemonService.getModelById(25))
                .thenReturn(Optional.of(testPokemon));
        when(trainerService.getModelById(1))
                .thenReturn(Optional.of(testTrainer));

        List<TrainersPokemon> result =
                trainersPokemonService.getAllModels();

        assertEquals(1, result.size());
    }

}

