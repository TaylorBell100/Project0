package org.example.repository.entities;

import org.example.service.PokemonService;
import org.example.service.model.Pokemon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class TrainersPokemonEntity {
    // new class replaces id for the representation
    private TPCompositeKey id = new TPCompositeKey();
    private Boolean seen;
    private LocalDate dateObtained;
    private String nickname;
    private Integer partySlot;
    private Integer pokemonId;
    private Integer trainerId;

    public TrainersPokemonEntity() {
    }

    public TrainersPokemonEntity(Boolean seen, LocalDate dateObtained, String nickname, Integer partySlot, Integer pokemonId, Integer trainerId) {
        id.setIds(pokemonId,trainerId);
        this.seen = seen;
        this.dateObtained = dateObtained;
        this.nickname = nickname;
        this.partySlot = partySlot;
        this.pokemonId = pokemonId;
        this.trainerId = trainerId;
    }

    public Integer[] getId() {
        return id.getIds();
    }

    /*
    public void setId(Integer nid, Integer tid) {
        id.setIds(nid,tid);
    }*/

    public Integer getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(Integer pokemonId) {
        this.pokemonId = pokemonId;
        id.setIds(pokemonId, this.trainerId);
    }

    public Integer getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
        id.setIds(this.pokemonId, trainerId);
    }

    public Boolean getSeen(){return seen;}

    public void setSeen(Boolean s){seen = s;}

    public String getNickname(){return nickname;}

    public void setNickname(String s){nickname = s;}

    public LocalDate getDateObtained(){return dateObtained;}

    public void setDateObtained(LocalDate s){dateObtained = s;}

    public Integer getPartySlot(){return partySlot;}

    public void setPartySlot(Integer s){partySlot = s;}

    @Override
    public String toString() {
        return "TrainersPokemonEntity{" +
                "id=" + id +
                ", seen='" + seen +
                ", dateObtained='" + dateObtained +
                ", nickname='" + nickname +
                ", partySlot='" + partySlot +'\'' +
                ", Pokemon=" + pokemonId +
                ", Trainer=" + trainerId +
                '}';
    }

    String formattedString(){
        PokemonService pokemonService = new PokemonService();
        Optional<Pokemon> pokemon = pokemonService.getModelById(pokemonId);
        String name = pokemon.get().getName();

        return name + ", dateObtained=" + dateObtained +
                ", nickname=" + nickname +
                ", partySlot=" + partySlot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainersPokemonEntity that = (TrainersPokemonEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(nickname, that.nickname) && Objects.equals(pokemonId, that.pokemonId) && Objects.equals(trainerId, that.trainerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seen,dateObtained,nickname,partySlot, pokemonId, trainerId);
    }
}
