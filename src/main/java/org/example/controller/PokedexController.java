package org.example.controller;

import org.example.repository.entities.TrainersPokemonEntity;
import org.example.service.PokemonService;
import org.example.service.TrainerService;
import org.example.service.TrainersPokemonService;
import org.example.service.model.Pokemon;
import org.example.service.model.Trainer;
import org.example.service.model.TrainersPokemon;
import org.example.util.InputHandler;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PokedexController {
    private final TrainersPokemonService trainersPokemonService = new TrainersPokemonService();
    private final PokemonService pokemonService = new PokemonService();
    private final TrainerService trainerService = new TrainerService();
    private static Integer trainerId = 0;

    public void handleInput0(){
        boolean running = true;
        while(running){
            printMenu0();
            trainerId = InputHandler.getIntInput("Please enter a valid TrainerID: ");
            switch(trainerId){
                case 0 -> {
                    System.out.println("Leaving Pokedex Services");
                    running = false; }
                default -> {
                    validateTrainerId(trainerId);
                    running = false;
                }
            }//switch
        }//while running
    }//handleinput0

    private void printMenu0(){
        System.out.println("=== POKEDEX SERVICES ===");
        System.out.println("Enter 0 to exit.");
    }

    private void validateTrainerId(Integer i){
        Optional<Trainer> trainer = trainerService.getModelById(i);
        if(trainer.isPresent()){
            handleInput1();
        }else{
            System.out.println("Invalid Trainer ID");
        }
    }

    public void handleInput1(){
        boolean running = true;
        while(running){
            printMenu1();
            int choice = InputHandler.getIntInput("Please select from the following: ");
            //take user choice and move to the appropriate method, using handle search all
            switch(choice){
                case 1 -> {
                    int i = handleSearchAllInput("ALL");
                    String x = "";
                    if (i!=1){x = handleInput2(i);}
                    if (i!=0){listAllPokemon(i,x);}
                }
                case 2 -> {
                    int i = handleSearchAllInput("UNSEEN");
                    String x = "";
                    if (i!=1){x = handleInput2(i);}
                    if (i!=0){searchPokemonByUnseen(i,x);}
                }
                case 3 -> {
                    int i = handleSearchAllInput("SEEN");
                    String x = "";
                    if (i!=1){x = handleInput2(i);}
                    if (i!=0){searchPokemonBySeen(i,x);}
                    //searchPokemonBySeen();
                }
                case 4 -> {
                    int i = handleSearchAllInput("CAUGHT");
                    String x = "";
                    if (i!=1){x = handleInput2(i);}
                    if (i!=0){searchPokemonByCaught(i,x);}
                }
                case 5 -> {
                    handleCatchInput(2);
                }
                case 6 ->{
                    handleCatchInput(1);
                }
                case 0 -> {
                    System.out.println("Leaving Pokedex Services");
                    running = false; }
                default -> System.out.println("Invalid choice");
            }//switch
        }//while running
    }//handle input1

    private void printMenu1(){
        System.out.println("=== POKEDEX SERVICES ===");
        System.out.println("1. Search All Pokemon");
        System.out.println("2. Search Pokemon by Unseen");
        System.out.println("3. Search Pokemon by Seen");
        System.out.println("4. Search Pokemon by Caught");
        System.out.println("5. Update Pokemon State by National ID");
        System.out.println("6. Update Pokemon State by Name");
        System.out.println("0. Exit");
    }

    public int handleSearchAllInput(String x){
        boolean running = true;
        while(running){
            printSearchMenu(x);
            int choice = InputHandler.getIntInput("Please select from the following: ");
            //use choice to validate input
            switch(choice){
                case 1 -> {return 1;}
                case 2 -> {return 2;}
                case 3 -> {return 3;}
                case 4 -> {return 4;}
                case 5 -> {return 5;}
                case 0 -> {
                    System.out.println("Leaving Pokedex Services");
                    running = false;
                }
                default -> System.out.println("Invalid choice");
            }//switch
        }//while running
        return 0;
    }//handle search all

    private void printSearchMenu(String x){
        System.out.println("=== SEARCHING "+ x +" POKEMON ===");
        System.out.println("1. List All");
        System.out.println("2. List by Region");
        System.out.println("3. List by Type");
        System.out.println("4. Find by National ID");
        System.out.println("5. Find by Name");
        System.out.println("0. Exit");
    }

    private String handleInput2(Integer i) {
        String x = "";
        String y = "";
        System.out.println(i);
        switch (i){
            case 2 -> {
                y = "Please enter a Region: ";
            } case 3 -> {
                y = "Please enter a type, or 2 types separated by a space: ";
            } case 4 -> {
                y = "Please enter the Pokemon National ID: ";
            }case 5 -> {
                y = "Please enter the Pokemon's name: ";
            } default -> {
                y= "Something broke in handleInput3.";
            }
        }//switch
        x = InputHandler.getStringInput(y);
        x = x.strip();

        return x;
    }

    public void handleCatchInput(int i){
        boolean running = true;
        while(running){
            printPokemonStateMenu();
            int choice = InputHandler.getIntInput("Please select from the following: ");
            switch(choice){
                case 1 -> {seePokemon(i);running=false;}
                case 2 -> {catchPokemon(i);running=false;}
                case 3 -> {releasePokemon(i);running=false;}
                case 0 -> {
                    System.out.println("Leaving Pokedex Services");
                    running = false;
                }
                default -> System.out.println("Invalid choice");
            }//switch
        }//while running
    }//handle search all

    private void printPokemonStateMenu(){
        System.out.println("=== UPDATE POKEMON STATE ===");
        System.out.println("1. Seen new Pokemon");
        System.out.println("2. Catch new Pokemon");
        System.out.println("3. Release a Pokemon");
        System.out.println("0. Exit");
    }


    // The following are the functions that go down to the service layer

    private void releasePokemon(int i) {
        //the int i parameter tell me if we are looking for id input or name input
        Optional<Trainer> trainer = trainerService.getModelById(trainerId);

        Optional<Pokemon> pokemon;
        if (trainer.isPresent()){
            if (i == 1) {
                // Pokemon Name
                String pokemonName = InputHandler.getStringInput("What is the Pokemon's full name? ");

                pokemon = pokemonService.getModelByName(pokemonName);
            } else{
                //pokemon id
                int pokemonId = InputHandler.getIntInput("What is the Pokemon's National ID? ");

                pokemon = pokemonService.getModelById(pokemonId);
            }//if else

            if(pokemon.isPresent()){
                TrainersPokemonEntity trainersPokemonEntity = new TrainersPokemonEntity();

                trainersPokemonEntity.setTrainerId(trainer.get().getId());
                trainersPokemonEntity.setPokemonId(pokemon.get().getId());

                int result = trainersPokemonService.releasePokemon(trainersPokemonEntity);

                if(result == 0){
                    System.out.println("Unable to release pokemon.");
                }else{
                    System.out.println("Goodbye " + pokemon.get().getName() +"!");
                }
            }else{
                System.out.println("Review your pokemon's info and try again!");
            }//ifelse pokemonpresent

        }else{
            System.out.println("Trainer ID is invalid.");
        }//ifelse trainer
    }//release

    private void seePokemon(int i) {
        //the int i parameter tell me if we are looking for id input or name input
        Optional<Trainer> trainer = trainerService.getModelById(trainerId);

        Optional<Pokemon> pokemon;
        if (trainer.isPresent()){
            if (i == 1) {
                // Pokemon Name
                String pokemonName = InputHandler.getStringInput("What is the Pokemon's full name? ");

                pokemon = pokemonService.getModelByName(pokemonName);
            } else{
                //pokemon id
                int pokemonId = InputHandler.getIntInput("What is the Pokemon's National ID? ");

                pokemon = pokemonService.getModelById(pokemonId);
            }//if else

            if(pokemon.isPresent()){
                TrainersPokemonEntity trainersPokemonEntity = new TrainersPokemonEntity();

                trainersPokemonEntity.setTrainerId(trainer.get().getId());
                trainersPokemonEntity.setPokemonId(pokemon.get().getId());
                trainersPokemonEntity.setSeen(true);

                int result = trainersPokemonService.seePokemon(trainersPokemonEntity);

                if(result == 0){
                    System.out.println("Pokemon already seen.");
                }else{
                    System.out.println("You spotted a wild " + pokemon.get().getName() +"!");
                }
            }else{
                System.out.println("Pokemon name is invalid");
            }//ifelse pokemon

        }else{
            System.out.println("Trainer ID is invalid.");
        }//ifelse trainer
    }

    private void searchPokemonBySeen(Integer i, String x){
        List<Pokemon> pokemons = pokemonService.getAllModelsBySeen(i,x,trainerId);
        for(Pokemon pokemon: pokemons){
            System.out.println(pokemon);
        }
    }

    private void searchPokemonByUnseen(Integer i, String x){
        List<Pokemon> pokemons = pokemonService.getAllModelsByUnseen(i,x,trainerId);
        for(Pokemon pokemon: pokemons){
            System.out.println(pokemon);
        }
    }

    private void searchPokemonByCaught(Integer i, String x){
        List<Pokemon> pokemons = pokemonService.getAllModelsByCaught(i,x,trainerId);
        for(Pokemon pokemon: pokemons){
            System.out.println(pokemon);
        }
    }

    private void listAllPokemon(Integer i, String x){
        List<Pokemon> pokemons = pokemonService.getAllModels(i,x);
        for(Pokemon pokemon: pokemons){
            System.out.println(pokemon);
        }
    }

    private void catchPokemon(int i) {
        //the int i parameter tell me if we are looking for id input or name input
        Optional<Trainer> trainer = trainerService.getModelById(trainerId);

        Optional<Pokemon> pokemon;
        if (trainer.isPresent()){
            if (i == 1) {
                // Pokemon Name
                String pokemonName = InputHandler.getStringInput("What is the Pokemon's full name? ");

                pokemon = pokemonService.getModelByName(pokemonName);
            } else{
                //pokemon id
                int pokemonId = InputHandler.getIntInput("What is the Pokemon's National ID? ");

                pokemon = pokemonService.getModelById(pokemonId);
            }//if else

            if(pokemon.isPresent()){
                TrainersPokemonEntity trainersPokemonEntity = new TrainersPokemonEntity();

                trainersPokemonEntity.setTrainerId(trainerId);
                trainersPokemonEntity.setPokemonId(pokemon.get().getId());
                trainersPokemonEntity.setDateObtained(LocalDate.now());
                trainersPokemonEntity.setSeen(true);

                giveNickname(trainersPokemonEntity);

                int result = trainersPokemonService.catchPokemon(trainersPokemonEntity);

                if(result == 0){
                    System.out.println("Pokemon already caught.");
                }else{
                    System.out.println("You caught a " + pokemon.get().getName() +"!");
                }
            }else{
                System.out.println("Pokemon name is invalid");
            }//ifelse pokemon

        }else{
            System.out.println("Trainer ID is invalid.");
        }//ifelse trainer
    }//add trainspokemon - catch pokemon

    private void giveNickname(TrainersPokemonEntity trainersPokemonEntity) {
        boolean running = true;
        String nickName;
        Optional<Pokemon> pokemon = pokemonService.getModelById(trainersPokemonEntity.getPokemonId());

        String pokemonName = pokemon.get().getName();

        while(running) {
            System.out.println("=== NICKNAME ===");
            System.out.println("Would you like to give a nickname to your newly caught " + pokemonName + "?");
            System.out.println("1. Yes");
            System.out.println("0. No");
            int choice = InputHandler.getIntInput("Make a choice: ");
            switch (choice) {
                case 1 -> {
                    nickName = InputHandler.getStringInput("Enter a nickname: ");
                    try {
                        trainerService.updateNickname(trainersPokemonEntity.getTrainerId(), trainersPokemonEntity.getPokemonId(), nickName);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    running = false;
                }
                case 0 -> {running = false;}
                default -> {}
            }//switch
        }//while running
    }

// methods that I decided I didnt need

    private void getAllTrainersPokemonsByTrainer() {
        String trainerName = InputHandler.getStringInput("What is the trainer name?");
        Optional<Trainer> trainer = trainerService.getModelByTrainerName(trainerName);
        if(trainer.isPresent()){

            List<TrainersPokemon> trainersPokemons = trainersPokemonService.getAllModelsByTrainer(trainer.get());
            for(TrainersPokemon trainersPokemon: trainersPokemons){
                System.out.println(trainersPokemon);
            }
        }else{
            System.out.println("Invalid Trainer Name");
        }
    }

    private void getAllTrainersPokemons() {
        List<TrainersPokemon> trainersPokemons = trainersPokemonService.getAllModels();
        for(TrainersPokemon trainersPokemon: trainersPokemons){
            System.out.println(trainersPokemon);
        }
    }
}//class
