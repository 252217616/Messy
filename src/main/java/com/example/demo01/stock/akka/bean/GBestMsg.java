package com.example.demo01.stock.akka.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 全局最优
 */
@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
public final class GBestMsg {
    private int seed;
    private int zhunqueCount;

    private double rate;
    private BigDecimal wucha;
    private BigDecimal cgl;
}
