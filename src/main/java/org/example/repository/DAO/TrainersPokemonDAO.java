package org.example.repository.DAO;

import org.example.repository.entities.TPCompositeKey;
import org.example.repository.entities.TrainersPokemonEntity;
import org.example.util.ConnectionHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainersPokemonDAO {
    private Connection connection = ConnectionHandler.getConnection();


    public TPCompositeKey create(TrainersPokemonEntity entity) throws SQLException {

        String sql = """
        INSERT INTO trainer_pokemon (tid, nid)
        VALUES (?, ?)
        RETURNING tid, nid
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, entity.getTrainerId());
            stmt.setInt(2, entity.getPokemonId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new TPCompositeKey(rs.getInt("tid"), rs.getInt("nid"));
                }
            }
        }//try stmt
        return null;
    }

    /*
    public Integer create(TrainersPokemonEntity entity) throws SQLException {
        String sql = "INSERT INTO trainer_pokemon (full_name, dept_id, loc_id) VALUES (?, ?, ?) RETURNING id";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, entity.getNickname());
            stmt.setInt(2, entity.getTrainerId());
            stmt.setInt(3, entity.getPokemonId());

            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("id");
                }
            }
        }
        return null;
    }//create
    */

    public Optional<TrainersPokemonEntity> findById(TPCompositeKey id) throws SQLException {
        String sql = "SELECT * FROM trainer_pokemon WHERE tid = ? AND nid = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            Integer[] ids = id.getIds();
            stmt.setInt(1, ids[1]);
            stmt.setInt(2, ids[0]);

            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    TrainersPokemonEntity trainersPokemon = new TrainersPokemonEntity();
                    trainersPokemon.setTrainerId(rs.getInt("tid"));
                    trainersPokemon.setPokemonId(rs.getInt("nid"));
                    trainersPokemon.setSeen(rs.getBoolean("seen"));
                    Date sqlDate = rs.getDate("date_obtained");
                    if (sqlDate != null) {
                        trainersPokemon.setDateObtained(sqlDate.toLocalDate());
                    }
                    trainersPokemon.setNickname(rs.getString("nickname"));
                    trainersPokemon.setPartySlot(rs.getInt("party_slot"));


                    return Optional.of(trainersPokemon);
                }
            }
        }
        return Optional.empty();
    }//findbyid

    public List<TrainersPokemonEntity> findAll() throws SQLException {
        List<TrainersPokemonEntity> trainersPokemons = new ArrayList<>();

        String sql = "SELECT * FROM trainersPokemon";
        try(Statement stmt = connection.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){

                TrainersPokemonEntity trainersPokemon = new TrainersPokemonEntity();
                //trainersPokemon.setId(rs.getInt("id"));
                trainersPokemon.setNickname(rs.getString("full_name"));
                trainersPokemon.setTrainerId(rs.getInt("dept_id"));
                trainersPokemon.setPokemonId(rs.getInt("loc_id"));

                trainersPokemons.add(trainersPokemon);
            }
        }
        return trainersPokemons;
    }

    public TrainersPokemonEntity updateById(TrainersPokemonEntity entity) throws SQLException {
        return null;
    }

    public boolean deleteById(Integer id) throws SQLException {
        return false;
    }

    public List<TrainersPokemonEntity> findAllByTrainerId(Integer trainerId) throws SQLException {
        List<TrainersPokemonEntity> trainersPokemons = new ArrayList<>();

        String sql = "SELECT * FROM trainersPokemon WHERE dept_id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, trainerId);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){

                TrainersPokemonEntity trainersPokemon = new TrainersPokemonEntity();
                //trainersPokemon.setId(rs.getInt("id"));
                trainersPokemon.setNickname(rs.getString("full_name"));
                trainersPokemon.setTrainerId(rs.getInt("dept_id"));
                trainersPokemon.setPokemonId(rs.getInt("loc_id"));

                trainersPokemons.add(trainersPokemon);
            }
        }
        return trainersPokemons;
    }

    public int seePokemon(TrainersPokemonEntity entity) throws SQLException{

        String sql = "INSERT INTO Trainer_Pokemon (tid, nid, seen) VALUES (?, ?, TRUE) ON CONFLICT (tid, nid) DO UPDATE SET seen = TRUE;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, entity.getTrainerId());
            stmt.setInt(2, entity.getPokemonId());

            return stmt.executeUpdate();
        }//try stmt
    }//see pokemon

    public int catchPokemon(TrainersPokemonEntity entity) throws SQLException{

        String sql = "INSERT INTO Trainer_Pokemon (tid, nid, seen, date_obtained) VALUES (?, ?, TRUE, CURRENT_DATE) ON CONFLICT (tid, nid) DO UPDATE SET seen = TRUE, date_obtained = CURRENT_DATE;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, entity.getTrainerId());
            stmt.setInt(2, entity.getPokemonId());

            return stmt.executeUpdate();
        }//try stmt
    }//see pokemon

    public int releasePokemon(TrainersPokemonEntity entity) throws SQLException{
        String sql = "UPDATE Trainer_Pokemon SET date_obtained = NULL, nickname = NULL, party_slot = NULL WHERE tid = ? AND nid = ?;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, entity.getTrainerId());
            stmt.setInt(2, entity.getPokemonId());

            return stmt.executeUpdate();
        }//try stmt
    }//release
}//class
