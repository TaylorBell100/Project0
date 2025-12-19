package org.example.controller;

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
import org.example.util.InputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainerController {
    private static final Logger logger = LoggerFactory.getLogger(PokedexController.class);
    private final TrainersPokemonService trainersPokemonService = new TrainersPokemonService();
    private final PokemonService pokemonService = new PokemonService();
    private final TrainerService trainerService = new TrainerService();



    public void handleInput() throws SQLException {
        boolean running = true;
        while(running){
            printMenu();
            int choice = InputHandler.getIntInput("Make a choice!");
            switch(choice){
                case 1 -> addTrainer();
                case 2 -> getTrainerById();
                case 3 -> getTrainerByName();
                case 4 -> getTrainersByRegion();
                case 5 -> getAllTrainers();
                case 6 -> changeNickname();
                case 7 -> addToParty();
                case 8 -> removeFromParty();
                case 9 -> updateTrainer();
                case 0 -> {
                    System.out.println("Leaving Trainer Services");
                    running = false; }
                default -> {
                    System.out.println("Invalid choice"); logger.warn("Invalid menu choice entered by user: {}", choice);
                }

            }
        }
    }

    private void getAllTrainers() {
        List<TrainerEntity> trainers = trainerService.getAllEntities();

        for(TrainerEntity trainer: trainers){
            System.out.println(trainer);
        }
    }

    private void getTrainersByRegion() {
        // tid
        String name = InputHandler.getStringInput("Please enter a region: ");

        List<TrainerEntity> trainers = trainerService.getEntitiesByRegion(name);

        for(TrainerEntity trainer: trainers){
            System.out.println(trainer);
        }
    }

    private void getTrainerByName() {
        // tid
        String name = InputHandler.getStringInput("Please enter the Trainers name: ");

        List<TrainerEntity> trainers = trainerService.getEntitiesByName(name);

        for(TrainerEntity trainer: trainers){
            System.out.println(trainer);
        }
    }


    private void getTrainerById() {
        // tid
        Integer trainerId = InputHandler.getIntInput("Please enter the Trainer ID: ");

        Optional<TrainerEntity> trainer = trainerService.getEntityById(trainerId);
        ArrayList<TrainerEntity> trainerList = new ArrayList<>();

        logger.debug("Fetching trainer with ID: {}", trainerId);
        if (trainer.isPresent()){
            trainerList.add(trainer.get());
        }

        for(TrainerEntity trainers: trainerList){
            System.out.println(trainers);
        }
    }//getby id


    private void addTrainer() {
        // What do we expect from the user?
        String trainerName = InputHandler.getStringInput("What is your Trainer name? ");
        // Trainer Name
        String region = InputHandler.getStringInput("What region are you from? ");

        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setName(trainerName);
        trainerEntity.setRegion(region);

        logger.debug("Adding new trainer with name '{}' and region '{}'", trainerName, region);
        Integer trainer = trainerService.createEntity(trainerEntity);

        if (trainer != 0){
            logger.debug("Trainer created successfully with ID: {}", trainer);
            System.out.println("Success! Your Trainder ID is: " + trainer);
        }
    }//add trainer

    private void changeNickname() throws SQLException {
        int tid = InputHandler.getIntInput("Please enter your Trainer ID: ");
        String pid = InputHandler.getStringInput("Please enter the Pokemon's full name or National ID: ");
        pid = pid.strip();
        String newNickname = InputHandler.getStringInput("Please enter the Pokemon's new nickname: ");

        Optional<Pokemon> pokemonOpt;

        // Determine whether pid is numeric (National ID) or name
        if (pid.matches("\\d+")) {
            int nid = Integer.parseInt(pid);
            pokemonOpt = pokemonService.getModelById(nid);
        } else {
            pokemonOpt = pokemonService.getModelByName(pid);
        }

        // Pokémon not found
        if (pokemonOpt.isEmpty()) {
            logger.warn("Pokémon not found for input '{}'", pid);
            System.out.println("Pokémon not found.");
            return;
        }

        Pokemon pokemon = pokemonOpt.get();
        int nid = pokemon.getId();
        String pokemonName = pokemon.getName();

        logger.debug("Changing nickname of Pokémon ID {} for trainer ID {} to '{}'", nid, tid, newNickname);
        int updated = trainerService.updateNickname(tid, nid, newNickname);

        if (updated == 1) {
            System.out.println(pokemonName + " is now nicknamed " + newNickname + "!");
        } else {
            System.out.println("Could not update nickname. Make sure the trainer owns this Pokémon.");
        }
    }//change nickname

    private void addToParty() throws SQLException {
        int tid = InputHandler.getIntInput("Please enter your Trainer ID: ");
        String pid = InputHandler.getStringInput("Please enter the Pokemon's full name or National ID: ");
        pid = pid.strip();

        int slot = collectSlot();

        Optional<Pokemon> pokemonOpt;

        //  Determine whether pid is numeric (National ID) or name
        if (pid.matches("\\d+")) {
            int nid = Integer.parseInt(pid);
            pokemonOpt = pokemonService.getModelById(nid);
        } else {
            pokemonOpt = pokemonService.getModelByName(pid);
        }

        //  Pokémon not found
        if (pokemonOpt.isEmpty()) {
            System.out.println("Pokémon not found.");
            return;
        }

        Pokemon pokemon = pokemonOpt.get();
        int nid = pokemon.getId();
        String pokemonName = pokemon.getName();

        logger.debug("Adding Pokémon '{}' (ID {}) to trainer {} slot {}", pokemonName, nid, tid, slot);
        int updated = trainerService.addToParty(tid, nid, slot);

        if (updated == 1) {
            System.out.println(pokemonName + " was added to your party.");
        } else {
            System.out.println("Could not add to party. Make sure the trainer owns this Pokémon, and your chosen party slot is not full.");
        }
    }    //add to party

    private void updateTrainer() {
        // What do we expect from the user?
        String trainerName = "";
        String region = "";
        TrainerEntity trainerEntity = new TrainerEntity();
        Optional<Trainer> tr;

        Integer tid = trainerValidator();
        if (tid == 0){
            return;
        }
        tr = trainerService.getModelById(tid);
        switch(collectUpdate()){
            case 0 ->{break;}
            case 1 -> {
                trainerName = InputHandler.getStringInput("What is your Trainer name? ");
                trainerEntity.setName(trainerName);
                trainerEntity.setRegion(tr.get().getRegion());
            }
            case 2 -> {
                region = InputHandler.getStringInput("What region are you from? ");
                trainerEntity.setRegion(region);
                trainerEntity.setName(tr.get().getName());
            }
        }
        trainerEntity.setId(tid);

        logger.debug("Updating trainer ID {}: name='{}', region='{}'", tid, trainerName, region);
        Integer trainer = trainerService.updateEntity(trainerEntity);

        if (trainer != 0){
            System.out.println("Success! Your info was updated!");
        }
    }//update trainer

    private int collectSlot(){
        boolean running = true;
        int slot = 0;
        while(running){
            slot = InputHandler.getIntInput("Please enter a party slot between 1 and 6: ");
            if (slot >=1 && slot<=6){
                running = false;
            }
        }//while running
        return slot;
    }

    private int collectUpdate(){
        boolean running = true;
        int slot = 0;
        while(running){
            printUpdateOptions();
            slot = InputHandler.getIntInput("Please choose from the following: ");
            if (slot >=0 && slot<=2){
                running = false;
            }
        }//while running
        return slot;
    }

    private void removeFromParty() throws SQLException {
        int tid = InputHandler.getIntInput("Please enter your Trainer ID: ");
        String pid = InputHandler.getStringInput("Please enter the Pokemon's full name or National ID: ");
        pid = pid.strip();

        Optional<Pokemon> pokemonOpt;

        // Determine whether pid is numeric (National ID) or name
        if (pid.matches("\\d+")) {
            int nid = Integer.parseInt(pid);
            pokemonOpt = pokemonService.getModelById(nid);
        } else {
            pokemonOpt = pokemonService.getModelByName(pid);
        }

        //  Pokémon not found
        if (pokemonOpt.isEmpty()) {
            System.out.println("Pokémon not found.");
            return;
        }

        Pokemon pokemon = pokemonOpt.get();
        int nid = pokemon.getId();
        String pokemonName = pokemon.getName();

        logger.debug("Removing Pokémon '{}' (ID {}) from trainer {}", pokemonName, nid, tid);
        int updated = trainerService.removeFromParty(tid, nid);

        if (updated == 1) {
            System.out.println(pokemonName + " was removed from your party.");
        } else {
            System.out.println("Could not add to party. Make sure the trainer owns this Pokémon, and your party is not full.");
        }
    }

    private void printMenu(){
        System.out.println("========================");
        System.out.println("=== Trainer SERVICES ===");
        System.out.println("1. Register New Trainer");
        System.out.println("2. Get Trainer by ID");
        System.out.println("3. Get Trainers by Name");
        System.out.println("4. Get Trainers by Region");
        System.out.println("5. List all Trainers");
        System.out.println("6. Change Pokemon's Nickname");
        System.out.println("7. Add Pokemon to party");
        System.out.println("8. Remove Pokemon from party");
        System.out.println("9. Update a Trainer");
        System.out.println("0. Exit");

    }

    private void printUpdateOptions(){
        System.out.println("======================");
        System.out.println("=== Trainer Fields ===");
        System.out.println("1. Update Name");
        System.out.println("2. Update Region");
        System.out.println("0. Nevermind");

    }

    public int trainerValidator(){
        boolean running = true;
        Integer trainerId = 0;
        while(running){
            System.out.println                            ("===============================================");
            trainerId = InputHandler.getIntInput(   "Please enter a valid TrainerID or 0 to cancel: ");
            Optional<Trainer> trainer = trainerService.getModelById(trainerId);
            if (trainerId == 0) {
                running = false;
            } else if(trainer.isPresent()){
                    running = false;
            } else{
                logger.warn("Invalid Trainer ID entered: {}", trainerId);
                System.out.println("Invalid Trainer ID");
            }
        }//while running
        return trainerId;
    }//handleinput0


}
