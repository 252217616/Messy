package com.example.demo01.jsoup.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ToString
public class HistoricalStock {
    //日期
    private String date;
    //开盘价格
    private BigDecimal open;
    //收盘价格
    private BigDecimal close;
    //最高
    private BigDecimal high;
    //最低
    private BigDecimal min;
    //成交量(股)
    private BigDecimal volume;
    //成交额(元)
    private BigDecimal turnover;

    public HistoricalStock(String [] params) {
        this.date = params[0].replaceAll("-","");
        this.open = new BigDecimal(params[1]).setScale(2,BigDecimal.ROUND_HALF_UP);
        this.high = new BigDecimal(params[2]).setScale(2,BigDecimal.ROUND_HALF_UP);;
        this.close = new BigDecimal(params[3]).setScale(2,BigDecimal.ROUND_HALF_UP);;
        this.min = new BigDecimal(params[4]).setScale(2,BigDecimal.ROUND_HALF_UP);;
        this.volume = new BigDecimal(params[5]);
        this.turnover = new BigDecimal(params[6]);
    }
    public HistoricalStock(Stock stock,String time) {
        this.date = time;
        this.open = stock.getOpen();
        this.high = stock.getHigh();
        this.close = stock.getTclose();
        this.min = stock.getMin();
        if("万手".equals(stock.getVolumeUnit())){
            this.volume = stock.getVolume().multiply(new BigDecimal(1000000));
        }else if("手".equals(stock.getVolumeUnit())){
            this.volume = stock.getVolume().multiply(new BigDecimal(10000));
        }
        if("亿".equals(stock.getTurnoverUnit())){
            this.turnover = stock.getTurnover().multiply(new BigDecimal(100000000));
        }else if("万".equals(stock.getTurnoverUnit())){
            this.turnover = stock.getTurnover().multiply(new BigDecimal(10000));
        }
    }
}
