package com.example.demo01.stock.akka.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 个体最优
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public final class PBestMsg {
    private int seed;
    private int zhunqueCount;

    private double rate;
    private BigDecimal wucha;
    private BigDecimal cgl;
}
