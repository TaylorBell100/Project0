package org.example.service;

import org.example.repository.entities.TrainerEntity;
import org.example.service.model.Trainer;
import org.example.repository.DAO.TrainerDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainerService {

    private TrainerDAO trainerDAO = new TrainerDAO();

    public Integer createEntity(TrainerEntity entity) {
        try{
            Integer newId = trainerDAO.create(entity);
            return newId;
        }catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    public Optional<TrainerEntity> getEntityById(Integer id) {
        try{
            Optional<TrainerEntity> trainerEntity = trainerDAO.findById(id);
            if(trainerEntity.isEmpty()){
                throw new RuntimeException("Trainer not found");
            }
            return trainerEntity;
        }catch(SQLException | RuntimeException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<TrainerEntity> getAllEntities() {
        try{
            List<TrainerEntity> trainerEntities = trainerDAO.findAll();
            return trainerEntities;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public Integer updateEntity(TrainerEntity newEntity) {
        try {
            return trainerDAO.updateById(newEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            e.printStackTrace();
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
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<TrainerEntity> getEntityByTrainerName(String trainerName) {
        try{
            Optional<TrainerEntity> trainerEntity = trainerDAO.findByTrainerName(trainerName);
            return trainerEntity;
        }catch (SQLException e){
            e.printStackTrace();
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

    public List<TrainerEntity> getEntitiesByName(String name) {
        try{
            List<TrainerEntity> entities = trainerDAO.findAllByName(name);
            return entities;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<TrainerEntity> getEntitiesByRegion(String name) {
        try{
            List<TrainerEntity> entities = trainerDAO.findAllByRegion(name);
            return entities;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public int updateNickname(int tid, int nid, String nickname) throws SQLException {
        return trainerDAO.updateNickname(tid,nid,nickname);
    }

    public int addToParty(int tid, int nid, int slot) throws SQLException {
        return trainerDAO.addToParty(tid,nid,slot);
    }
    public int removeFromParty(int tid,int nid) throws SQLException {
        return trainerDAO.removeFromParty(tid,nid);
    }
}
