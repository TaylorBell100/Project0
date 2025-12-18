import org.example.repository.DAO.TrainerDAO;
import org.example.repository.entities.PokemonEntity;
import org.example.repository.entities.TrainerEntity;
import org.example.repository.entities.TrainersPokemonEntity;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private TrainerService trainerService;

    private Pokemon testPokemon;
    private PokemonEntity testPokemonEntity;
    private Trainer testTrainer;
    private TrainerEntity testTrainerEntity;
    private TrainersPokemon testTrainersPokemon;
    private TrainersPokemonEntity testTrainersPokemonEntity;

    @BeforeEach
    void setup(){
        //setup test pokemon entity
        testPokemonEntity = new PokemonEntity();
        testPokemonEntity.setId(25);
        testPokemonEntity.setName("Pikachu");
        testPokemonEntity.setRegion("Kanto");
        testPokemonEntity.setType1("Electric");
        testPokemonEntity.setType2("NULL");

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

    //create entity
    @Test
    void createEntity_Success_ReturnsNewId() throws SQLException{
        //AAA
        //arrange
        when(trainerDAO.create(testTrainerEntity)).thenReturn(100);

        //act
        Integer result = trainerService.createEntity(testTrainerEntity);

        //Assert
        assertEquals(100,result);
        verify(trainerDAO, times(1)).create(testTrainerEntity);
    }

    @Test
    void createEntity_ThrowsSQLException_ReturnsNegativeOne() throws SQLException{
        //AAA
        //arrange
        when(trainerDAO.create(testTrainerEntity)).thenThrow(new SQLException("Database error"));

        //act
        Integer result = trainerService.createEntity(testTrainerEntity);

        //Assert
        assertEquals(-1,result);
        verify(trainerDAO, times(1)).create(testTrainerEntity);
    }

    @Test
    void createEntity_DAOReturnsNull_ReturnsNull() throws SQLException {
        when(trainerDAO.create(testTrainerEntity)).thenReturn(null);

        Integer result = trainerService.createEntity(testTrainerEntity);

        assertNull(result);
        verify(trainerDAO).create(testTrainerEntity);
    }


    //test get entity by id
    @Test
    void getEntityById_Found_ReturnsEntity() throws SQLException {
        when(trainerDAO.findById(1)).thenReturn(Optional.of(testTrainerEntity));

        Optional<TrainerEntity> result = trainerService.getEntityById(1);

        assertTrue(result.isPresent());
        assertEquals(testTrainerEntity.getName(), result.get().getName());
        verify(trainerDAO, times(1)).findById(1);
    }

    @Test
    void getEntityById_NotFound_ReturnsEmpty() throws SQLException {
        when(trainerDAO.findById(1)).thenReturn(Optional.empty());

        Optional<TrainerEntity> result = trainerService.getEntityById(1);

        assertTrue(result.isEmpty());
        verify(trainerDAO, times(1)).findById(1);
    }

    @Test
    void getEntityById_SQLException_ReturnsEmpty() throws SQLException {
        when(trainerDAO.findById(1))
                .thenThrow(new SQLException("DB failure"));

        Optional<TrainerEntity> result = trainerService.getEntityById(1);

        assertTrue(result.isEmpty());
    }


    //test get all entities
    @Test
    void getAllEntities_ReturnsList() throws SQLException {
        when(trainerDAO.findAll()).thenReturn(List.of(testTrainerEntity));

        List<TrainerEntity> result = trainerService.getAllEntities();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(trainerDAO, times(1)).findAll();
    }

    @Test
    void getAllEntities_SQLException_ReturnsNull() throws SQLException {
        when(trainerDAO.findAll()).thenThrow(new SQLException());

        List<TrainerEntity> result = trainerService.getAllEntities();

        assertNull(result);
        verify(trainerDAO, times(1)).findAll();
    }

    @Test
    void getAllEntities_NoTrainers_ReturnsEmptyList() throws SQLException {
        when(trainerDAO.findAll()).thenReturn(List.of());

        List<TrainerEntity> result = trainerService.getAllEntities();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    //test update entity
    @Test
    void updateEntity_Success_ReturnsUpdatedCount() throws SQLException {
        when(trainerDAO.updateById(testTrainerEntity)).thenReturn(1);

        int result = trainerService.updateEntity(testTrainerEntity);

        assertEquals(1, result);
        verify(trainerDAO, times(1)).updateById(testTrainerEntity);
    }

    @Test
    void updateEntity_SQLException_ThrowsRuntimeException() throws SQLException {
        when(trainerDAO.updateById(testTrainerEntity)).thenThrow(new SQLException());

        assertThrows(RuntimeException.class,
                () -> trainerService.updateEntity(testTrainerEntity));
    }

    @Test
    void updateEntity_NoRowsUpdated_ReturnsZero() throws SQLException {
        when(trainerDAO.updateById(testTrainerEntity)).thenReturn(0);

        int result = trainerService.updateEntity(testTrainerEntity);

        assertEquals(0, result);
    }


    //convert entity to model
    @Test
    void convertEntityToModel_Success() {
        Optional<Trainer> result = trainerService.convertEntityToModel(testTrainerEntity);

        assertTrue(result.isPresent());
        assertEquals("Ash", result.get().getName());
    }

    //get model by id
    @Test
    void getModelById_Found_ReturnsModel() throws SQLException {
        when(trainerDAO.findById(1)).thenReturn(Optional.of(testTrainerEntity));

        Optional<Trainer> result = trainerService.getModelById(1);

        assertTrue(result.isPresent());
        assertEquals("Ash", result.get().getName());
    }

    @Test
    void getModelById_NotFound_ReturnsEmpty() throws SQLException {
        when(trainerDAO.findById(1)).thenReturn(Optional.empty());

        Optional<Trainer> result = trainerService.getModelById(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void getModelById_EntityNotFound_ReturnsEmpty() throws SQLException {
        when(trainerDAO.findById(1)).thenReturn(Optional.empty());

        Optional<Trainer> result = trainerService.getModelById(1);

        assertTrue(result.isEmpty());
    }


    //get model by trainer name
    @Test
    void getModelByTrainerName_Found_ReturnsModel() throws SQLException {
        when(trainerDAO.findByTrainerName("Ash"))
                .thenReturn(Optional.of(testTrainerEntity));

        Optional<Trainer> result = trainerService.getModelByTrainerName("Ash");

        assertTrue(result.isPresent());
        assertEquals("Ash", result.get().getName());
    }

    //get all models
    @Test
    void getAllModels_ReturnsConvertedModels() throws SQLException {
        when(trainerDAO.findAll()).thenReturn(List.of(testTrainerEntity));

        List<Trainer> result = trainerService.getAllModels();

        assertEquals(1, result.size());
        assertEquals("Ash", result.get(0).getName());
    }

    @Test
    void getAllModels_WhenEntitiesNull_ThrowsException() throws SQLException {
        when(trainerDAO.findAll()).thenThrow(new SQLException());

        assertThrows(NullPointerException.class,
                () -> trainerService.getAllModels());
    }


    //get entities by name
    @Test
    void getEntitiesByName_ReturnsList() throws SQLException {
        when(trainerDAO.findAllByName("Ash"))
                .thenReturn(List.of(testTrainerEntity));

        List<TrainerEntity> result = trainerService.getEntitiesByName("Ash");

        assertEquals(1, result.size());
        verify(trainerDAO, times(1)).findAllByName("Ash");
    }

    @Test
    void getEntitiesByName_NoMatches_ReturnsEmptyList() throws SQLException {
        when(trainerDAO.findAllByName("Ash"))
                .thenReturn(List.of());

        List<TrainerEntity> result =
                trainerService.getEntitiesByName("Ash");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    //get entities by region
    @Test
    void getEntitiesByRegion_ReturnsList() throws SQLException {
        when(trainerDAO.findAllByRegion("Kanto"))
                .thenReturn(List.of(testTrainerEntity));

        List<TrainerEntity> result = trainerService.getEntitiesByRegion("Kanto");

        assertEquals(1, result.size());
    }

    @Test
    void getEntitiesByRegion_NoMatches_ReturnsEmptyList() throws SQLException {
        when(trainerDAO.findAllByRegion("Kanto"))
                .thenReturn(List.of());

        List<TrainerEntity> result =
                trainerService.getEntitiesByRegion("Kanto");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    //party related methods
    @Test
    void updateNickname_CallsDAO() throws SQLException {
        when(trainerDAO.updateNickname(1,25,"Sparky")).thenReturn(1);

        int result = trainerService.updateNickname(1,25,"Sparky");

        assertEquals(1, result);
    }

    @Test
    void updateNickname_PokemonNotOwned_ReturnsZero() throws SQLException {
        when(trainerDAO.updateNickname(1, 25, "Sparky"))
                .thenReturn(0);

        int result = trainerService.updateNickname(1, 25, "Sparky");

        assertEquals(0, result);
    }


    @Test
    void addToParty_CallsDAO() throws SQLException {
        when(trainerDAO.addToParty(1,25,1)).thenReturn(1);

        int result = trainerService.addToParty(1,25,1);

        assertEquals(1, result);
    }

    @Test
    void addToParty_SlotOccupied_ReturnsZero() throws SQLException {
        when(trainerDAO.addToParty(1, 25, 1))
                .thenReturn(0);

        int result = trainerService.addToParty(1, 25, 1);

        assertEquals(0, result);
    }


    @Test
    void removeFromParty_CallsDAO() throws SQLException {
        when(trainerDAO.removeFromParty(1,25)).thenReturn(1);

        int result = trainerService.removeFromParty(1,25);

        assertEquals(1, result);
    }

    @Test
    void removeFromParty_NotInParty_ReturnsZero() throws SQLException {
        when(trainerDAO.removeFromParty(1, 25))
                .thenReturn(0);

        int result = trainerService.removeFromParty(1, 25);

        assertEquals(0, result);
    }



}//class
