package org.example.service;

import org.example.repository.DAO.TrainersPokemonDAO;
import org.example.repository.entities.TPCompositeKey;
import org.example.repository.entities.TrainersPokemonEntity;
import org.example.service.model.Pokemon;
import org.example.service.model.Trainer;
import org.example.service.model.TrainersPokemon;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainersPokemonService {
    private TrainersPokemonDAO trainersPokemonDAO = new TrainersPokemonDAO();

    private TrainerService trainerService = new TrainerService();
    private PokemonService pokemonService = new PokemonService();

    public TPCompositeKey createEntity(TrainersPokemonEntity entity) {
        try{
            TPCompositeKey newId = trainersPokemonDAO.create(entity);
            return newId;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public Optional<TrainersPokemonEntity> getEntityById(TPCompositeKey id) {
        try{
            Optional<TrainersPokemonEntity> trainersPokemon = trainersPokemonDAO.findById(id);
            if(trainersPokemon.isEmpty()){
                throw new RuntimeException("TrainersPokemon not found");
            }
            return trainersPokemon;
        }catch(SQLException | RuntimeException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<TrainersPokemonEntity> getAllEntities() {

        try{
            List<TrainersPokemonEntity> trainersPokemonEntities = trainersPokemonDAO.findAll();
            return trainersPokemonEntities;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public TrainersPokemonEntity updateEntity(Integer id, TrainersPokemonEntity newEntity) {
        return null;
    }

    public boolean deleteEntity(Integer id) {
        return false;
    }

    public int catchPokemon(TrainersPokemonEntity entity){
        try{
            return trainersPokemonDAO.catchPokemon(entity);
        }catch (SQLException e){
            e.printStackTrace();
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
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<TrainersPokemon> getModelById(TPCompositeKey id) {

        try{
            Optional<TrainersPokemon> trainersPokemon = convertEntityToModel(getEntityById(id).get());
            return trainersPokemon;
        }catch(RuntimeException e){
            e.printStackTrace();
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

    public List<TrainersPokemon> getAllModelsByTrainer(Trainer trainer) {
        List<TrainersPokemonEntity> trainersPokemonEntities = getAllEntitiesByTrainerId(trainer.getId());
        List<TrainersPokemon> trainersPokemons = new ArrayList<>();
        for(TrainersPokemonEntity entity : trainersPokemonEntities){
            Optional<TrainersPokemon> trainersPokemon = convertEntityToModel(entity);
            if(trainersPokemon.isPresent()){
                trainersPokemons.add(trainersPokemon.get());
            }
        }
        return trainersPokemons;


    }

    private List<TrainersPokemonEntity> getAllEntitiesByTrainerId(Integer trainerId) {
        try{
            List<TrainersPokemonEntity> trainersPokemonEntities = trainersPokemonDAO.findAllByTrainerId(trainerId);
            return trainersPokemonEntities;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public int seePokemon(TrainersPokemonEntity entity) {
        try{
            return trainersPokemonDAO.seePokemon(entity);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public int releasePokemon(TrainersPokemonEntity entity) {
        try{
            return trainersPokemonDAO.releasePokemon(entity);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
}//class
