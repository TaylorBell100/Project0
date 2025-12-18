package org.example.repository.entities;

import java.util.Objects;

public class PokemonEntity {
    private Integer nid;
    private String name;
    private String region;
    private String type1;
    private String type2;

    public PokemonEntity() {
    }//emp constr

    public PokemonEntity(Integer id, String name, String region, String type1, String type2) {
        this.nid = id;
        this.name = name;
        this.region = region;
        this.type1 = type1;
        this.type2 = type2;
    }//construct

    public Integer getId() {
        return nid;
    }

    public void setId(Integer id) {
        this.nid = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String id) {
        this.region = id;
    }

    public String getName(){return name;}

    public void setName(String id) {
        name = id;
    }

    public String getType1(){return type1;}

    public void setType1(String id) {
        this.type1 = id;
    }

    public String getType2(){return type2;}

    public void setType2(String id) {
        this.type2 = id;
    }

    public String getTypes() {return (getType1() + getType2());}

    @Override
    public String toString() {
        return "NID=" + nid +
                ", name=" + name;
    }

    public String toString(boolean b) {
        if (b) {
            return "PokemonEntity{" +
                    "id=" + nid +
                    ", name=" + name +
                    ", region='" + region +
                    ", types=" + type1 +
                    " " + type2 + '\'' +
                    '}';
        } else {
            return "NID=" + nid +
                    ", name=" + name;
        }//if else
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PokemonEntity pokemon1 = (PokemonEntity) o;
        return Objects.equals(nid, pokemon1.nid) && Objects.equals(name, pokemon1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nid, name,region,type1,type2);
    }
}
