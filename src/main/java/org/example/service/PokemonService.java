package org.example.service;

import org.example.controller.PokedexController;
import org.example.repository.DAO.PokemonDAO;
import org.example.repository.DAO.TrainerDAO;
import org.example.repository.entities.PokemonEntity;
import org.example.service.model.Pokemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PokemonService {
    private PokemonDAO pokemonDAO;
    private static final Logger logger = LoggerFactory.getLogger(PokedexController.class);

    // Constructor injection
    public PokemonService(PokemonDAO pokemonDAO) {
        this.pokemonDAO = pokemonDAO;
    }

    public PokemonService(){
        this(new PokemonDAO());
    }

    public Optional<PokemonEntity> getEntityById(Integer id) {
        try{
            logger.debug("Using PokemonDAO to find ID: {}",id);
            Optional<PokemonEntity> pokemonEntity = pokemonDAO.findById(id);
            if(pokemonEntity.isEmpty()){
                throw new RuntimeException("Pokemon not found");
            }
            return pokemonEntity;
        }catch(SQLException | RuntimeException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }//getbyid

    public List<PokemonEntity> getAllEntities(Integer i, String x) {

        try{
            logger.debug("Using PokemonDAO to find all");
            List<PokemonEntity> pokeEntities = pokemonDAO.findAll(i, x);
            return pokeEntities;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }//getallentities

    public List<PokemonEntity> getAllEntitiesBySeen(Integer i, String x, Integer tid) {
        try{
            logger.debug("Using PokemonDAO to find by seen with tid {}",tid);
            List<PokemonEntity> pokeEntities = pokemonDAO.findAllBySeen(i,x,tid);
            return pokeEntities;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }//getallentitiesbyseen

    public List<PokemonEntity> getAllEntitiesByUnseen(Integer i, String x, Integer tid) {
        try{
            logger.debug("Using PokemonDAO to find by unseen with tid {}",tid);
            List<PokemonEntity> pokeEntities = pokemonDAO.findAllByUnseen(i,x,tid);
            return pokeEntities;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }//getallentitiesbyseen

    public List<PokemonEntity> getAllEntitiesByCaught(Integer i, String x, Integer tid) {
        try{
            logger.debug("Using PokemonDAO to find by caught with tid {}",tid);
            List<PokemonEntity> pokeEntities = pokemonDAO.findAllByCaught(i,x,tid);
            return pokeEntities;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }//getallentitiesbycaught

    private Optional<PokemonEntity> getEntityByName(String name) {
        try{
            logger.debug("Using PokemonDAO to find by name: {}",name);
            Optional<PokemonEntity> pokeEntity = pokemonDAO.findByName(name);
            if(pokeEntity.isEmpty()){
                throw new RuntimeException("Pokemon not found");
            }
            return pokeEntity;
        }catch(SQLException | RuntimeException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Pokemon> convertEntityToModel(PokemonEntity entity) {
        Pokemon poke = new Pokemon();
        poke.setId(entity.getId());
        poke.setRegion(entity.getRegion());
        poke.setName(entity.getName());
        poke.setType1(entity.getType1());
        poke.setType2(entity.getType2());

        return Optional.of(poke);
    }//convert to model

    public Optional<Pokemon> getModelById(Integer id) {
        Optional<PokemonEntity> pokeEntity = getEntityById(id);
        try{
            if(pokeEntity.isPresent()){
                Optional<Pokemon> poke = convertEntityToModel(pokeEntity.get());
                if(poke.isPresent()){
                    return poke;
                }else{
                    throw new RuntimeException("PokemonEntity conversion failed");
                }
            }else{
                throw new RuntimeException("PokemonEntity not found");
            }
        }catch(RuntimeException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Pokemon> getModelByName(String name) {
        Optional<PokemonEntity> pokeEntity = getEntityByName(name);
        try{
            if(pokeEntity.isPresent()){
                Optional<Pokemon> poke = convertEntityToModel(pokeEntity.get());
                if(poke.isPresent()){
                    return poke;
                }else{
                    throw new RuntimeException("PokemonEntity conversion failed");
                }
            }else{
                throw new RuntimeException("PokemonEntity not found");
            }
        }catch(RuntimeException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Pokemon> getAllModels(Integer i, String x) {
        List<PokemonEntity> pokeEntities = getAllEntities(i,x);
        List<Pokemon> pokes = new ArrayList<>();
        for(PokemonEntity pokeEntity : pokeEntities){
            Optional<Pokemon> poke = convertEntityToModel(pokeEntity);
            if(poke.isPresent()){
                pokes.add(poke.get());
            }
        }
        return pokes;
    }

    public List<Pokemon> getAllModelsBySeen(Integer i, String x, Integer trainerId) {
        List<PokemonEntity> pokeEntities = getAllEntitiesBySeen(i,x,trainerId);
        List<Pokemon> pokes = new ArrayList<>();
        for(PokemonEntity pokeEntity : pokeEntities){
            Optional<Pokemon> poke = convertEntityToModel(pokeEntity);
            if(poke.isPresent()){
                pokes.add(poke.get());
            }
        }
        return pokes;
    }//get models by seen

    public List<Pokemon> getAllModelsByUnseen(Integer i, String x, Integer trainerId) {
        List<PokemonEntity> pokeEntities = getAllEntitiesByUnseen(i,x,trainerId);
        List<Pokemon> pokes = new ArrayList<>();
        for(PokemonEntity pokeEntity : pokeEntities){
            Optional<Pokemon> poke = convertEntityToModel(pokeEntity);
            if(poke.isPresent()){
                pokes.add(poke.get());
            }
        }
        return pokes;
    }//get models by unseen

    public List<Pokemon> getAllModelsByCaught(Integer i, String x, Integer trainerId) {
        List<PokemonEntity> pokeEntities = getAllEntitiesByCaught(i,x,trainerId);
        List<Pokemon> pokes = new ArrayList<>();
        for(PokemonEntity pokeEntity : pokeEntities){
            Optional<Pokemon> poke = convertEntityToModel(pokeEntity);
            if(poke.isPresent()){
                pokes.add(poke.get());
            }
        }
        return pokes;
    }//get models by unseen

}//pokemon service class
