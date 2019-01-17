package com.example.demo01.akka.bean;

import lombok.AllArgsConstructor;

/**
 * 全局最优
 */
@AllArgsConstructor
public final class GBestMsg {
    final PsoValue value;
    public PsoValue getValue(){
        return value;
    }
}
