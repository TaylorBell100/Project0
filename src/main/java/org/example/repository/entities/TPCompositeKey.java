package org.example.repository.entities;

import org.example.service.model.Pokemon;
import org.example.service.model.Trainer;

import java.util.ArrayList;

public class TPCompositeKey {
    private Integer nid;
    private Integer tid;

    public Integer[] getIds(){
        Integer[] arr = new Integer[2];
        arr[0] = nid;
        arr[1] = tid;
        return arr;

    }//getids

    public void setIds(Integer nid, Integer tid){
        this.nid = nid;
        this.tid = tid;
    }//set

    public TPCompositeKey(){}

    public TPCompositeKey(int nid, int tid){
        this.nid = nid;
        this.tid = tid;
    }//constructer advanced


}
