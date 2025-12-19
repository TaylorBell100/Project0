package org.example.repository.DAO;

import org.example.controller.PokedexController;
import org.example.repository.entities.PokemonEntity;
import org.example.util.ConnectionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PokemonDAO {
    private Connection connection = ConnectionHandler.getConnection();
    private static final Logger logger = LoggerFactory.getLogger(PokedexController.class);

    public Optional<PokemonEntity> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM pokemon WHERE nid = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    logger.info("Executing query statement.");
                    PokemonEntity pokemon = new PokemonEntity();
                    pokemon.setId(rs.getInt("nid"));
                    pokemon.setName(rs.getString("name"));

                    return Optional.of(pokemon);
                }
            }
        }
        return Optional.empty();
    }//find by id

    public Optional<PokemonEntity> findByName(String pokemonName) throws SQLException{
        String sql = "SELECT * FROM pokemon WHERE name = ?";

        pokemonName = wordFormat(pokemonName);

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, pokemonName);

            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    logger.info("Executing query statement.");
                    PokemonEntity pokemon = new PokemonEntity();
                    pokemon.setId(rs.getInt("nid"));
                    pokemon.setName(rs.getString("name"));

                    return Optional.of(pokemon);
                }
            }
        }
        return Optional.empty();
    }//find by name

    public List<PokemonEntity> findAll(Integer i, String x) throws SQLException {
        List<PokemonEntity> pokemons = new ArrayList<>();
        x += " ";
        String temp = "";
        String[] y = new String[2];

        String sql = "SELECT * FROM pokemon";

        sql = prepareInput(i,sql);

        try(PreparedStatement stmt = connection.prepareStatement(sql)){

            prepareOutput(stmt,i,x,y,0);

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    logger.info("Executing query statement.");
                    PokemonEntity pokemon = new PokemonEntity();
                    pokemon.setId(rs.getInt("nid"));
                    pokemon.setName(rs.getString("name"));
                    pokemons.add(pokemon);
                }
            }
        }
        return pokemons;
    }// find all


    public List<PokemonEntity> findAllBySeen(Integer i, String x, Integer id) throws SQLException {
        List<PokemonEntity> pokemons = new ArrayList<>();
        String[] y = new String[2];

        String sql = "SELECT p.nid,p.name,p.region,p.type1,p.type2,tp.seen,tp.date_obtained,tp.nickname,tp.party_slot FROM Pokemon p LEFT JOIN Trainer_Pokemon tp ON p.nid = tp.nid AND tp.tid = ? ";

        sql = prepareInput(i,sql);

        sql += " AND tp.seen = TRUE;";

        logger.debug("Executing large query statement <{}>.", sql);

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);

            prepareOutput(stmt,i,x,y,1);

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PokemonEntity pokemon = new PokemonEntity();
                    pokemon.setId(rs.getInt("nid"));
                    pokemon.setName(rs.getString("name"));
                    pokemons.add(pokemon);
                }
            }
        }
        return pokemons;
    }//find all by seen

    public List<PokemonEntity> findAllByUnseen(Integer i, String x, Integer id) throws SQLException {
        List<PokemonEntity> pokemons = new ArrayList<>();
        String[] y = new String[2];

        String sql = "SELECT p.nid,p.name,p.region,p.type1,p.type2,tp.seen,tp.date_obtained,tp.nickname,tp.party_slot FROM Pokemon p LEFT JOIN Trainer_Pokemon tp ON p.nid = tp.nid AND tp.tid = ?";

        sql = prepareInput(i,sql);

        sql += " AND (tp.seen = FALSE OR tp.seen IS NULL);";

        logger.debug("Executing large query statement <{}>.", sql);

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);

            prepareOutput(stmt,i,x,y,1);

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PokemonEntity pokemon = new PokemonEntity();
                    pokemon.setId(rs.getInt("nid"));
                    pokemon.setName(rs.getString("name"));
                    pokemons.add(pokemon);
                }
            }
        }
        return pokemons;
    }//find all by unseen

    //this method sets up our starting query and calls other methods to add more to the query until it satisfies what we need for our users request
    public List<PokemonEntity> findAllByCaught(Integer i, String x, Integer id) throws SQLException {
        //setup output
        List<PokemonEntity> pokemons = new ArrayList<>();

        //this array is needed for splitting up the users input in the case they entered multiple secifiers
        String[] y = new String[2];

        //out starting string
        String sql = "SELECT p.nid,p.name,p.region,p.type1,p.type2,tp.seen,tp.date_obtained,tp.nickname," +
                "tp.party_slot FROM Pokemon p LEFT JOIN Trainer_Pokemon tp ON p.nid = tp.nid AND tp.tid = ?";

        //prepare input will add the next part of our query depending on the users menu choice
        sql = prepareInput(i,sql);

        //after the menu choice is accounted for, we add more here since we know they are filtering by caught pokemon
        sql += " AND tp.date_obtained IS NOT NULL;";

        logger.debug("Executing large query statement <{}>.", sql);

        //preparing to connect to the database
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);

            //prepare output will sanitize user input and prepare it in our string depending on the users menu choice
            prepareOutput(stmt,i,x,y,1);

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PokemonEntity pokemon = new PokemonEntity();
                    pokemon.setId(rs.getInt("nid"));
                    pokemon.setName(rs.getString("name"));
                    pokemons.add(pokemon);
                }
            }// try rs

        }//try prepared statment
        return pokemons;
    }//find all by caught
/*
    public List<PokemonEntity> findAllByCaught(Integer i, String x, Integer id) throws SQLException {
        List<PokemonEntity> pokemons = new ArrayList<>();
        x += " ";
        String temp = "";
        String[] y = new String[2];

        String sql = "SELECT p.nid,p.name,p.region,p.type1,p.type2,tp.seen,tp.date_obtained,tp.nickname,tp.party_slot FROM Pokemon p LEFT JOIN Trainer_Pokemon tp ON p.nid = tp.nid AND tp.tid = ?";

        switch (i){
            case 1 -> {
                sql += " WHERE 1=1";
            } case 2 -> {
                sql += " WHERE region = ?";
                x = x.toLowerCase();
                temp = x.substring(0,1).toUpperCase();
                x = temp + x.substring(1);
                x = x.replaceAll("\\s", "");
            } case 3 -> {
                sql += " WHERE ((type1 = ? OR type2 = ?)) AND (? IS NULL OR (type1 = ? OR type2 = ?))";
                y = x.split(" ",2);
                if (y[1] != ""){
                    y[1] = y[1].toLowerCase();
                    temp = y[1].substring(0,1).toUpperCase();
                    y[1] = temp + y[1].substring(1);
                    y[1] = y[1].replaceAll("\\s", "");
                } else {
                    y[1] = "NULL";
                }
                y[0] = y[0].toLowerCase();
                temp = y[0].substring(0,1).toUpperCase();
                y[0] = temp + y[0].substring(1);
                y[0] = y[0].replaceAll("\\s", "");
                System.out.println(y[0]);
                System.out.println(y[1]);

            } case 4 -> {
                sql += " WHERE nid = ?";
                x = x.replaceAll("\\s", "");
            } case 5 -> {
                sql += " WHERE LOWER(name) LIKE LOWER('%' || ? || '%')";
            } default ->{
                System.out.println("Something broke within PokemonDAO.");
            }
        }//switch

        sql += " AND tp.date_obtained IS NOT NULL;";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);
            switch (i){
                case 1 -> {

                } case 2 -> {
                    stmt.setString(2, x);
                } case 3 -> {
                    //do other stuff
                    stmt.setString(2, y[0]);
                    stmt.setString(3, y[0]);
                    if (y[1] == "NULL"){
                        stmt.setNull(4,java.sql.Types.VARCHAR);
                        stmt.setNull(5,java.sql.Types.VARCHAR);
                        stmt.setNull(6,java.sql.Types.VARCHAR);
                    } else {
                        stmt.setString(4, y[1]);
                        stmt.setString(5, y[1]);
                        stmt.setString(6, y[1]);
                    }
                } case 4 -> {
                    //set nid
                    stmt.setInt(2, Integer.parseInt(x));
                } case 5 -> {
                    //set name
                    stmt.setString(1, x);
                } default ->{
                    System.out.println("Something broke within PokemonDAO 2.");
                }
            }//switch


            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PokemonEntity pokemon = new PokemonEntity();
                    pokemon.setId(rs.getInt("nid"));
                    pokemon.setName(rs.getString("name"));
                    pokemons.add(pokemon);
                }
            }
        }
        return pokemons;
    }//find all by caught

 */

    //prepare input will add the next part of our query depending on the users menu choice
    private String prepareInput(int i, String sql){
        switch (i){
            case 1 -> {
                sql += " WHERE 1=1";
            } case 2 -> {
                sql += " WHERE LOWER(region) LIKE LOWER(?)";
            } case 3 -> {
                sql += " WHERE ((type1 = ? OR type2 = ?)) AND (? IS NULL OR (type1 = ? OR type2 = ?))";
            } case 4 -> {
                sql += " WHERE p.nid = ?";
            } case 5 -> {
                sql += " WHERE LOWER(name) LIKE LOWER(?)";
            } default ->{
                System.out.println("Something broke within PokemonDAO.");
            }
        }//switch
        logger.info("Prepared input with case: {}.", i);
        return sql;
    }//prepare input


    private void prepareOutput(PreparedStatement stmt, int i, String x, String[] y, int offset) throws SQLException {

        //preparing for input sanitization
        x += " ";
        int tempInt = 0;
        logger.info("Preparing output with case: {}.", i);
        //switch for each type of user search
        switch (i){
            case 1 -> {
                //in the case of listing all, no additional input is needed
            } case 2 -> {
                //Search by region, we sanitize our string input
                x = wordFormat(x);
                x = "%"+x+"%";

                //Then we set string to our query
                tempInt = 1+offset;
                System.out.println(tempInt);
                stmt.setString(tempInt, x);
            } case 3 -> {
                //sanitize inputs
                y = x.split(" ",2);
                if (y[1] != ""){
                    y[1] = wordFormat(y[1]);
                } else {
                    y[1] = "NULL";
                }
                y[0] = wordFormat(y[0]);

                //Set strings
                stmt.setString(1+offset, y[0]);
                stmt.setString(2+offset, y[0]);
                if (y[1] == "NULL"){
                    stmt.setNull(3+offset,java.sql.Types.VARCHAR);
                    stmt.setNull(4+offset,java.sql.Types.VARCHAR);
                    stmt.setNull(5+offset,java.sql.Types.VARCHAR);
                } else {
                    stmt.setString(3+offset, y[1]);
                    stmt.setString(4+offset, y[1]);
                    stmt.setString(5+offset, y[1]);
                }
            } case 4 -> {
                x = x.replaceAll("\\s", "");
                int temp = Integer.parseInt(x);
                //set nid
                stmt.setInt(1+offset, temp);
            } case 5 -> {
                //set name
                x = wordFormat(x);
                x = "%"+x+"%";
                stmt.setString(1+offset, x);
            } default ->{
                System.out.println("Something broke within PokemonDAO prepareOutput.");
            }
        }//switch
    }// prepare output

    //a method I made for consistently preparing strings for my database
    private String wordFormat(String x){
        //this method turns any string (sTRinG) into -> (String)
        String temp = "";
        x = x.toLowerCase();
        x = x.replaceAll("\\s", "");
        temp = x.substring(0,1).toUpperCase();
        return temp + x.substring(1);

    }//word format


}//pokedao
