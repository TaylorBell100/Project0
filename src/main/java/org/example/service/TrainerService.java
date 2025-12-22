package org.example.service;

import org.example.controller.PokedexController;
import org.example.repository.DAO.TrainersPokemonDAO;
import org.example.repository.entities.TrainerEntity;
import org.example.service.model.Trainer;
import org.example.repository.DAO.TrainerDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainerService {

    private TrainerDAO trainerDAO;
    private static final Logger logger = LoggerFactory.getLogger(PokedexController.class);

    // Constructor injection
    public TrainerService(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    public TrainerService(){
        this(new TrainerDAO());
    }

    public Integer createEntity(TrainerEntity entity) {
        try{
            logger.debug("Using TrainerDAO to make new entry.");
            Integer newId = trainerDAO.create(entity);
            return newId;
        }catch(SQLException e){
            logger.info("Caught SQL exception trying to make Trainer entity");
            return -1;
        }
    }

    public Optional<TrainerEntity> getEntityById(Integer id) {
        try{
            logger.debug("Using TrainerDAO to find ID: {}",id);
            Optional<TrainerEntity> trainerEntity = trainerDAO.findById(id);
            if(trainerEntity.isEmpty()){
                throw new RuntimeException("Trainer not found");
            }
            return trainerEntity;
        }catch(SQLException | RuntimeException e){
            logger.info("Caught SQL exception trying to find trainer by ID: {}",id);
            return Optional.empty();
        }
    }

    public List<TrainerEntity> getAllEntities() {
        try{
            logger.debug("Using TrainerDAO to find all");
            List<TrainerEntity> trainerEntities = trainerDAO.findAll();
            return trainerEntities;
        }catch (SQLException e){
            logger.info("Caught SQL exception trying get all trainer entities");
            return null;
        }
    }

    public Integer updateEntity(TrainerEntity newEntity) {
        try {
            logger.debug("Using TrainerDAO to update");
            return trainerDAO.updateById(newEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<TrainerEntity> getEntityByTrainerName(String trainerName) {
        try{
            logger.debug("Using TrainerDAO to find by name: {}",trainerName);
            Optional<TrainerEntity> trainerEntity = trainerDAO.findByTrainerName(trainerName);
            return trainerEntity;
        }catch (SQLException e){
            logger.info("Caught SQL exception trying to get entity by trainers name: {}",name);
            return Optional.empty();
        }
    }

    public List<TrainerEntity> getEntitiesByName(String name) {
        try{
            logger.debug("Using TrainerDAO to find by name: {}",name);
            List<TrainerEntity> entities = trainerDAO.findAllByName(name);
            return entities;
        }catch(SQLException e){
            logger.info("Caught SQL exception trying to get all trainers by name: {}",a);
            return null;
        }
    }

    public List<TrainerEntity> getEntitiesByRegion(String name) {
        try{
            logger.debug("Using TrainerDAO to find by region: {}",name);
            List<TrainerEntity> entities = trainerDAO.findAllByRegion(name);
            return entities;
        }catch(SQLException e){
            logger.info("Caught SQL exception trying to get all trainers by region: {}",name);
            return null;
        }
    }

    public int updateNickname(int tid, int nid, String nickname) throws SQLException {
        logger.debug("Using TrainerDAO to update nickname to {}",nickname);
        return trainerDAO.updateNickname(tid,nid,nickname);
    }

    public int addToParty(int tid, int nid, int slot) throws SQLException {
        logger.debug("Using TrainerDAO to add to party slot: {}",slot);
        return trainerDAO.addToParty(tid,nid,slot);
    }
    public int removeFromParty(int tid,int nid) throws SQLException {
        logger.debug("Using TrainerDAO to remove from party");
        return trainerDAO.removeFromParty(tid,nid);
    }

    public Optional<Trainer> convertEntityToModel(TrainerEntity entity) {

        Trainer trainer = new Trainer();
        trainer.setId(entity.getId());
        trainer.setName(entity.getName());
        trainer.setRegion(entity.getRegion());

        return Optional.of(trainer);
    }

    public Optional<Trainer> getModelById(Integer id) {
        Optional<TrainerEntity> trainerEntity = getEntityById(id);
        try{
            if(trainerEntity.isPresent()){
                Optional<Trainer> trainer = convertEntityToModel(trainerEntity.get());
                if(trainer.isPresent()){
                    return trainer;
                }else{
                    throw new RuntimeException("TrainerEntity conversion failed");
                }
            }else{
                throw new RuntimeException("TrainerEntity not found");
            }

        }catch(RuntimeException e){
            logger.info("Caught Runtime exception trying to convert Trainer entity to a model.");
            return Optional.empty();
        }
    }

    public Optional<Trainer> getModelByTrainerName(String trainerName) {
        Optional<TrainerEntity> trainerEntity = getEntityByTrainerName(trainerName);
        try{
            if(trainerEntity.isPresent()){
                Optional<Trainer> trainer = convertEntityToModel(trainerEntity.get());
                if(trainer.isPresent()){
                    return trainer;
                }else{
                    throw new RuntimeException("TrainerEntity conversion failed");
                }
            }else{
                throw new RuntimeException("TrainerEntity not found");
            }
        }catch(RuntimeException e){
            logger.info("Caught Runtime exception trying to get model by trainer name: {}",trainerName);
            return Optional.empty();
        }
    }

    public List<Trainer> getAllModels() {
        List<TrainerEntity> trainerEntities = getAllEntities();
        List<Trainer> trainers = new ArrayList<>();
        for(TrainerEntity trainerEntity : trainerEntities){
            Optional<Trainer> trainer = convertEntityToModel(trainerEntity);
            if(trainer.isPresent()){
                trainers.add(trainer.get());
            }
        }
        return trainers;
    }

}//class
