package com.example.demo01.jsoup.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@ToString
public class Stock {
    //今日最高
    private BigDecimal high;
    //今日最低
    private BigDecimal min;
    //今日开盘
    private BigDecimal open;
    //昨日收盘
    private BigDecimal Yclose;
    //今日收盘
    private BigDecimal Tclose;
    //涨停
    private BigDecimal limitUp;
    //跌停
    private BigDecimal limitDown;
    //成交量
    private BigDecimal volume;
    //成交量单位
    private String volumeUnit;
    //成交额
    private BigDecimal turnover;
    //成交额单位
    private String turnoverUnit;
    //量比
    private BigDecimal VR;
    //换手率
    private BigDecimal TR;
    //市盈率(动)
    private BigDecimal PeD;
    //市盈率(静)
    private BigDecimal PeJ;
    //市盈率(TTM)
    private BigDecimal PeT;
    //委比
    private BigDecimal ER;
    //振幅
    private BigDecimal amplitude;
    //市净率
    private BigDecimal MR;
    //每股收益
    private BigDecimal PSE;
    //股息
    private String dividend;
    //总股本
    private BigDecimal totalCapitalStock;
    //总股本单位
    private String totalCapitalStockUnit;
    //总市值
    private BigDecimal totalMarketValue;
    //总市值单位
    private String totalMarketValueUnit;
    //每股净资产
    private BigDecimal EPS;
    //股息率
    private BigDecimal dividendYield;
    //流通股
    private BigDecimal circulatingShares;
    //流通股单位
    private String circulatingSharesUnit;
    //流通值
    private BigDecimal circulatingValue;
    //流通值单位
    private String circulatingValueUnit;
    // 52周最高
    private BigDecimal weeksMax;
    //52周最低
    private BigDecimal weeksMin;
    //历史最高
    private BigDecimal HMax;
    //历史最低
    private BigDecimal HMin;


    public Stock(Map<String,String> map) {
        this.high = new BigDecimal(map.get("最高"));
        this.min = new BigDecimal(map.get("最低"));
        this.open = new BigDecimal(map.get("今开"));
        this.Yclose = new BigDecimal(map.get("昨收"));
        this.Tclose = new BigDecimal(map.get("价格"));
        this.limitUp = new BigDecimal(map.get("涨停"));
        this.limitDown = new BigDecimal(map.get("跌停"));
        this.volume = new BigDecimal(map.get("成交量").substring(0,map.get("成交量").indexOf(".")+3));
        this.volumeUnit = map.get("成交量").substring(map.get("成交量").indexOf(".")+3);
        this.turnover = new BigDecimal(map.get("成交额").substring(0,map.get("成交额").indexOf(".")+3));
        this.turnoverUnit = map.get("成交额").substring(map.get("成交额").indexOf(".")+3);
        this.VR = new BigDecimal(map.get("量比"));
        this.TR = new BigDecimal(map.get("换手").substring(0,map.get("换手").length()-1));
        this.PeD = new BigDecimal(map.get("市盈率(动)"));
        this.PeJ = new BigDecimal(map.get("市盈率(静)"));
        this.PeT = new BigDecimal(map.get("市盈率(TTM)"));
        this.ER = new BigDecimal(map.get("委比").substring(0,map.get("委比").length()-1));
        this.amplitude = new BigDecimal(map.get("振幅").substring(0,map.get("振幅").length()-1));
        this.MR = new BigDecimal(map.get("市净率"));
        this.PSE = new BigDecimal(map.get("每股收益"));
        this.dividend = map.get("股息");
        this.totalCapitalStock = new BigDecimal(map.get("总股本").substring(0,map.get("总股本").indexOf(".")+3));
        this.totalCapitalStockUnit = map.get("总股本").substring(map.get("总股本").indexOf(".")+3);
        this.totalMarketValue = new BigDecimal(map.get("总市值").substring(0,map.get("总市值").indexOf(".")+3));
        this.totalMarketValueUnit = map.get("总市值").substring(map.get("总市值").indexOf(".")+3);
        this.EPS = new BigDecimal(map.get("每股净资产"));
        this.dividendYield = new BigDecimal(map.get("股息率").substring(0,map.get("股息率").length()-1));
        this.circulatingShares = new BigDecimal(map.get("流通股").substring(0,map.get("流通股").indexOf(".")+3));
        this.circulatingSharesUnit = map.get("流通股").substring(map.get("流通股").indexOf(".")+3);
        this.circulatingValue = new BigDecimal(map.get("流通值").substring(0,map.get("流通值").indexOf(".")+3));
        this.circulatingValueUnit = map.get("流通值").substring(map.get("流通值").indexOf(".")+3);
        this.weeksMax = new BigDecimal(map.get("52周最高"));
        this.weeksMin = new BigDecimal(map.get("52周最低"));
    }

    public static void main(String[] args) {
        String a = "1.75%";
        System.out.println(a.substring(0,a.length()-1));
    }

}
