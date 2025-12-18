package org.example.service.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class TrainersPokemon {
    private Integer id;
    private Boolean seen;
    private LocalDate dateObtained;
    private String nickname;
    private Integer partySlot;
    private Pokemon pokemon;
    private Trainer trainer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainersPokemon tPoke = (TrainersPokemon) o;
        return Objects.equals(id, tPoke.id) && Objects.equals(seen, tPoke.seen) && Objects.equals(dateObtained, tPoke.dateObtained) && Objects.equals(nickname, tPoke.nickname) && Objects.equals(pokemon, tPoke.pokemon) && Objects.equals(trainer, tPoke.trainer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seen, dateObtained, nickname, partySlot, pokemon, trainer);
    }

    @Override
    public String toString() {
        return "TrainersPokemon{"  +
                "seen='" + seen +
                ", dateObtained='" + dateObtained +
                ", nickname='" + nickname +
                ", partySlot='" + partySlot +'\'' +
                ", Pokemon=" + pokemon +
                ", Trainer=" + trainer +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPartySlot() {
        return partySlot;
    }

    public void setPartySlot(Integer id) {
        this.partySlot = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String name) {
        this.nickname = name;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer){this.trainer = trainer;}

    public LocalDate getDateObtained(){return dateObtained;}

    public void setDateObtained(LocalDate s){dateObtained = s;}

    public TrainersPokemon(Integer id, Boolean seen, LocalDate dateObtained, String nickname, Integer partySlot, Trainer trainer, Pokemon pokemon) {
        this.id = id;
        this.seen = seen;
        this.dateObtained = dateObtained;
        this.nickname = nickname;
        this.partySlot = partySlot;
        this.trainer = trainer;
        this.pokemon = pokemon;
    }

    public TrainersPokemon() {
    }
}//class Trainerspokemon
