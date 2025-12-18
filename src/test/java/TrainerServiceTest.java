import org.example.repository.DAO.TrainerDAO;
import org.example.repository.entities.PokemonEntity;
import org.example.repository.entities.TrainerEntity;
import org.example.repository.entities.TrainersPokemonEntity;
import org.example.service.TrainerService;
import org.example.service.model.Pokemon;
import org.example.service.model.Trainer;
import org.example.service.model.TrainersPokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        testTrainersPokemon = new TrainersPokemon();
        testTrainersPokemon.setNickname("");
        testTrainersPokemon.setDateObtained();
        //testTrainersPokemon.setId();
        //ignoring fields that require another model
        //testTrainersPokemon.setTrainer();
        //testTrainersPokemon.setPokemon();

    }

}
