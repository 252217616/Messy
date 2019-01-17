package com.example.demo01.akka.bean;

import lombok.AllArgsConstructor;

/**
 * 个体最优
 */
@AllArgsConstructor
public final class PBestMsg {
    final PsoValue value;
    public PsoValue getValue(){
        return value;
    }

    @Override
    public String toString(){

        return value.toString();
    }
}
