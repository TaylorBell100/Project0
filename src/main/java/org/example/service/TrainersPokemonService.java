package org.example.service;

import org.example.controller.PokedexController;
import org.example.repository.DAO.TrainersPokemonDAO;
import org.example.repository.entities.TPCompositeKey;
import org.example.repository.entities.TrainersPokemonEntity;
import org.example.service.model.Pokemon;
import org.example.service.model.Trainer;
import org.example.service.model.TrainersPokemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainersPokemonService {
    private TrainersPokemonDAO trainersPokemonDAO;

    private TrainerService trainerService;
    private PokemonService pokemonService;

    private static final Logger logger = LoggerFactory.getLogger(PokedexController.class);

    // Constructor injection
    public TrainersPokemonService(TrainersPokemonDAO trainersPokemonDAO, TrainerService trainerService, PokemonService pokemonService) {

        this.trainersPokemonDAO = trainersPokemonDAO;
        this.trainerService = trainerService;
        this.pokemonService = pokemonService;
    }

    public TrainersPokemonService(){
        this(new TrainersPokemonDAO(),new TrainerService(), new PokemonService());
    }

    public TPCompositeKey createEntity(TrainersPokemonEntity entity) {
        try{
            logger.debug("Using trainersPokemonDAO to create new entity");
            TPCompositeKey newId = trainersPokemonDAO.create(entity);
            return newId;
        }catch(SQLException e){
            logger.info("Caught SQL exception trying to make TrainersPokemon entity");
            return null;
        }
    }

    public Optional<TrainersPokemonEntity> getEntityById(TPCompositeKey id) {
        try{
            logger.debug("Using trainersPokemonDAO to get party with id: {}", id);
            Optional<TrainersPokemonEntity> trainersPokemon = trainersPokemonDAO.findById(id);
            if(trainersPokemon.isEmpty()){
                throw new RuntimeException("TrainersPokemon not found");
            }
            return trainersPokemon;
        }catch(SQLException | RuntimeException e){
            logger.info("Caught SQL exception trying to get TrainersPokemon entity by id: {}",id);
            return Optional.empty();
        }
    }

    public List<TrainersPokemonEntity> getAllEntities() {
        try{
            logger.debug("Using trainersPokemonDAO to get all entities");
            List<TrainersPokemonEntity> trainersPokemonEntities = trainersPokemonDAO.findAll();
            return trainersPokemonEntities;
        }catch (SQLException e){
            logger.info("Caught SQL exception trying to get all TrainersPokemon entities");
            return null;
        }
    }

    public int catchPokemon(TrainersPokemonEntity entity){
        try{
            logger.debug("Using trainersPokemonDAO to catch a pokemon");
            return trainersPokemonDAO.catchPokemon(entity);
        }catch (SQLException e){
            logger.info("Caught SQL exception trying to catch a pokemon");
        }
        return 0;
    }


    public int seePokemon(TrainersPokemonEntity entity) {
        try{
            logger.debug("Using trainersPokemonDAO to see a pokemon");
            return trainersPokemonDAO.seePokemon(entity);
        }catch (SQLException e){
            logger.info("Caught SQL exception trying to see a pokemon");
        }
        return 0;
    }

    public int releasePokemon(TrainersPokemonEntity entity) {
        try{
            logger.debug("Using trainersPokemonDAO to release a pokemon");
            return trainersPokemonDAO.releasePokemon(entity);
        }catch (SQLException e){
            logger.info("Caught SQL exception trying to release a TrainersPokemon");
        }
        return 0;
    }

    public Optional<TrainersPokemon> convertEntityToModel(TrainersPokemonEntity entity) {
        try{
            Optional<Pokemon> pokemon = pokemonService.getModelById(entity.getPokemonId());

            if(pokemon.isEmpty()){
                throw new RuntimeException("Invalid pokemon id");
            }

            Optional<Trainer> trainer = trainerService.getModelById(entity.getTrainerId());

            if(trainer.isEmpty()){
                throw new RuntimeException("Invalid trainer id");
            }

            TrainersPokemon trainersPokemon = new TrainersPokemon();
            //trainersPokemon.setId(entity.getId());
            trainersPokemon.setNickname(entity.getNickname());
            trainersPokemon.setPokemon(pokemon.get());
            trainersPokemon.setTrainer(trainer.get());

            return Optional.of(trainersPokemon);

        }catch(RuntimeException e){
            logger.info("Caught Runtime exception trying to conver TrainersPokemon entity to Model");
            return Optional.empty();
        }
    }

    public Optional<TrainersPokemon> getModelById(TPCompositeKey id) {

        try{
            Optional<TrainersPokemon> trainersPokemon = convertEntityToModel(getEntityById(id).get());
            return trainersPokemon;
        }catch(RuntimeException e){
            logger.info("Caught Runtime exception trying to get Pokemon model by ID: {}",id);
            return Optional.empty();
        }

    }

    public List<TrainersPokemon> getAllModels() {
        List<TrainersPokemonEntity> trainersPokemonEntities = getAllEntities();
        List<TrainersPokemon> trainersPokemons = new ArrayList<>();
        for(TrainersPokemonEntity trainersPokemonEntity: trainersPokemonEntities){
            Optional<TrainersPokemon> trainersPokemon = convertEntityToModel(trainersPokemonEntity);
            if(trainersPokemon.isPresent()){
                trainersPokemons.add(trainersPokemon.get());
            }
        }
        return trainersPokemons;
    }

}//class
