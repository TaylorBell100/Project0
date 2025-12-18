package org.example.repository.DAO;

import org.example.repository.entities.PokemonEntity;
import org.example.repository.entities.TrainerEntity;
import org.example.repository.entities.TrainersPokemonEntity;
import org.example.util.ConnectionHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainerDAO {
    private Connection connection = ConnectionHandler.getConnection();

    public Integer create(TrainerEntity trainerEntity) throws SQLException {

        String sql = "INSERT INTO trainer (name, region) VALUES (?,?) RETURNING tid";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, trainerEntity.getName());
            stmt.setString(2, trainerEntity.getRegion());

            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("tid");
                }
            }//try
        }//try
        return null;
    }//create trainerentity


    public Optional<TrainerEntity> findById(Integer id) throws SQLException{
        String sql = "SELECT t.tid, t.name AS trainer_name, t.region, p.nid AS pokemon_id, p.name AS pokemon_name, tp.nickname, tp.party_slot, tp.date_obtained FROM Trainer t  LEFT JOIN Trainer_Pokemon tp ON t.tid = tp.tid AND tp.date_obtained IS NOT NULL AND tp.party_slot IS NOT NULL LEFT JOIN Pokemon p ON tp.nid = p.nid WHERE t.tid = ? ORDER BY tp.party_slot;";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);

            List<TrainerEntity> results = executeDetailedQuery(stmt);
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        }//try
        //return Optional.empty();
    }//find byid

    public List<TrainerEntity> executeDetailedQuery(PreparedStatement stmt) throws SQLException {
        List<TrainerEntity> trainers = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            TrainerEntity currentTrainer = null;
            Integer lastTid = null;

            while (rs.next()) {
                Integer tid = rs.getInt("tid");

                if (lastTid == null || !tid.equals(lastTid)) {
                    // New trainer row
                    currentTrainer = new TrainerEntity();
                    currentTrainer.setId(tid);
                    currentTrainer.setName(rs.getString("trainer_name"));
                    currentTrainer.setRegion(rs.getString("region"));
                    trainers.add(currentTrainer);

                    lastTid = tid;
                }

                Integer pokemonId = rs.getInt("pokemon_id");
                if (!rs.wasNull() && currentTrainer != null) {
                    TrainersPokemonEntity tp = new TrainersPokemonEntity();
                    tp.setPokemonId(pokemonId);
                    tp.setTrainerId(currentTrainer.getId());
                    tp.setNickname(rs.getString("nickname"));
                    tp.setPartySlot(rs.getInt("party_slot"));
                    Date sqlDate = rs.getDate("date_obtained");
                    if (sqlDate != null) {
                        tp.setDateObtained(sqlDate.toLocalDate());
                    }
                    currentTrainer.addParty(tp);
                }
            }//while next
        }//try
        return trainers;
    }

    // READ ALL
    public List<TrainerEntity> findAll() throws SQLException {
        String sql = "SELECT t.tid, t.name AS trainer_name, t.region, p.nid AS pokemon_id, p.name AS pokemon_name, tp.nickname, tp.party_slot, tp.date_obtained FROM Trainer t LEFT JOIN Trainer_Pokemon tp ON t.tid = tp.tid AND tp.date_obtained IS NOT NULL AND tp.party_slot IS NOT NULL LEFT JOIN Pokemon p ON tp.nid = p.nid ORDER BY t.tid, tp.party_slot;";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            return executeDetailedQuery(stmt);
        }//try
    }//find all


    public Integer updateById(TrainerEntity trainerEntity) throws SQLException {
        String sql = "UPDATE trainer SET Name=?, Region=? WHERE tid=?;";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, trainerEntity.getName());
            stmt.setString(2, trainerEntity.getRegion());
            stmt.setInt(3, trainerEntity.getId());

            //returns 0 if you dont have that poke, and 1 if successful
            return stmt.executeUpdate();
        }//try
    }//update by id


    public boolean deleteById(Integer id) throws SQLException{
        return false;
    }


    public Optional<TrainerEntity> findByTrainerName(String trainerName) throws SQLException{
        String sql = "SELECT * FROM trainer WHERE name = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, trainerName);

            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    TrainerEntity trainerEntity = new TrainerEntity();
                    trainerEntity.setId(rs.getInt("tid"));
                    trainerEntity.setName(rs.getString("name"));
                    trainerEntity.setRegion(rs.getString("region"));

                    return Optional.of(trainerEntity);
                }
            }
        }
        return Optional.empty();
    }

    public List<TrainerEntity> findAllByName(String name) throws SQLException {
        List<TrainerEntity> trainers = new ArrayList<>();

        String sql = "SELECT t.tid, t.name AS trainer_name, t.region, p.nid AS pokemon_id, p.name AS pokemon_name, tp.nickname, tp.party_slot, tp.date_obtained FROM Trainer t LEFT JOIN Trainer_Pokemon tp ON t.tid = tp.tid AND tp.date_obtained IS NOT NULL AND tp.party_slot IS NOT NULL LEFT JOIN Pokemon p ON tp.nid = p.nid WHERE t.name ILIKE ? ORDER BY t.tid, tp.party_slot;";

        name = name.strip();
        name = "%"+name+"%";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, name);

            return executeDetailedQuery(stmt);
        }//try stmt
    }//find all by name

    public List<TrainerEntity> findAllByRegion(String name) throws SQLException {
        List<TrainerEntity> trainers = new ArrayList<>();

        String sql = "SELECT t.tid, t.name AS trainer_name, t.region, p.nid AS pokemon_id, p.name AS pokemon_name, tp.nickname, tp.party_slot, tp.date_obtained FROM Trainer t LEFT JOIN Trainer_Pokemon tp ON t.tid = tp.tid AND tp.date_obtained IS NOT NULL AND tp.party_slot IS NOT NULL LEFT JOIN Pokemon p ON tp.nid = p.nid WHERE t.region ILIKE ? ORDER BY t.tid, tp.party_slot;";

        name = name.strip();
        name = "%"+name+"%";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, name);

            return executeDetailedQuery(stmt);
        }//try stmt
    }//find all by region

    public int updateNickname(int trainerId, int pokemonId, String nickname) throws SQLException {

        String sql = """
        UPDATE Trainer_Pokemon
        SET nickname = ?
        WHERE tid = ? AND nid = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nickname);
            stmt.setInt(2, trainerId);
            stmt.setInt(3, pokemonId);

            //returns 0 if you dont have that poke, and 1 if successful
            return stmt.executeUpdate();
        }
    }//update nickname

    public int addToParty(int trainerId, int pokemonId, int slot) throws SQLException {

        String sql = """
        UPDATE Trainer_Pokemon
        SET party_slot = ?
        WHERE tid = ?
          AND nid = ?
          AND date_obtained IS NOT NULL
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, slot);
            stmt.setInt(2, trainerId);
            stmt.setInt(3, pokemonId);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            // PostgreSQL unique violation
            if ("23505".equals(e.getSQLState())) {
                return 0; // slot already occupied
            }
            throw e; // real error
        }
    }//add to party

    public int removeFromParty(int trainerId, int pokemonId) throws SQLException {

        String sql = """
        UPDATE Trainer_Pokemon
        SET party_slot = NULL
        WHERE tid = ? AND nid = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trainerId);
            stmt.setInt(2, pokemonId);

            return stmt.executeUpdate();
        }
    }//remove from Party



}//class
