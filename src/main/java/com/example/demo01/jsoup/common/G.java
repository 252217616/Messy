package com.example.demo01.jsoup.common;

import java.util.Random;

public interface G {
    String HS_URL = "http://app.finance.ifeng.com/list/stock.php?t=ha&f=chg_pct&o=desc&p=";
    String SZ_URL = "http://app.finance.ifeng.com/list/stock.php?t=sa&f=chg_pct&o=desc&p=";
    String TODAY_URL = "https://xueqiu.com/";
    String HIS_FILE_PATH = "D:\\tmp\\_";
    String NEW_FILE_PATH = "D:\\newtmp\\_";
    String TODAY_NAME = ".html";
    String H_URL = "http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/";
    String H_CODE = ".phtml?year=";
    String H_JD = "&jidu=";
    Random RANDOM = new Random();
}
