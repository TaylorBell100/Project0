package org.example.repository.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrainerEntity {
    private Integer tid;
    private String name;
    private String region;
    private List<TrainersPokemonEntity> party = new ArrayList<>();

    public TrainerEntity() {
    }

    public TrainerEntity(Integer id, String name, String region) {
        this.tid = id;
        this.name = name;
        this.region = region;
    }

    public void addParty(TrainersPokemonEntity e){
        party.add(e);
    }

    public Integer getId() {
        return tid;
    }

    public void setId(Integer id) {
        this.tid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        String temp = "";
        for (TrainersPokemonEntity many: party){
            temp += many.formattedString()+"\n";
        }
        return "Name=" + name + ", Region=" + region+", Party: "+"\n"+temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainerEntity that = (TrainerEntity) o;
        return Objects.equals(tid, that.tid) && Objects.equals(name, that.name) && Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tid, name,region);
    }
}
