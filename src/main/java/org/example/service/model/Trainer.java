package org.example.service.model;

import java.util.Objects;

public class Trainer {

    private Integer tid;
    private String name;
    private String region;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return Objects.equals(tid, trainer.tid) && Objects.equals(name, trainer.name) && Objects.equals(region, trainer.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tid, name, region);
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "tid=" + tid +
                ", name='" + name + '\'' +
                ", region=" + region +
                '}';
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

    public void setName(String fullName) {
        this.name = fullName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Trainer(Integer id, String fullName, String region) {
        this.tid = id;
        this.name = fullName;
        this.region = region;
    }

    public Trainer() {
    }
}
