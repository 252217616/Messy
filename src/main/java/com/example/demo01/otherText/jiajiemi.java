package com.example.demo01.otherText;


import com.zrj.pay.util.SecureUtil;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class jiajiemi {

    private static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIL3+1vzd6ZsTAbV2f4wsIT2GnFpx/y785rVAy5gdV8xGrRZPDtKMZ1MGaFKbc98BqFQyQR7qsBv4R0bBNMUAPK1ggjCT1vQUM5/8Wm2EMMK6TBoQW6nngE5um6zOcWa8zDAT+d7btr1ZJ+hnAMZi3wuHn1rLVqJ/Kkqqp9e10x/AgMBAAECgYB/zqYR9ocV9ms+5GRmhXdNWak2cidCgNxahXMkR+dibNYFl2uK5H2EZiZA2xRMPVlvBRoV8OUSBFwnfh14KvY8fvT1Suv9bLRZD/8ePBYrkHPYxHBaNgbwWLYN0XierqU32ShT3O5AujxaG2gz2JTsYgIregxABssDqpc4faxpuQJBALj79CKqRtHxogvDcsSusDWV3ZMsgC2SpIdTbbxMVCQFky9mh//91X1UpxmY6FiKezN0g7yEL2gfCDbCS+BX0yMCQQC1P3EgCLPh8NEl86qtLGvWX2EQsfirnl85XBHpDlZ15FCibFhQ7VidDls3t+srWP1lm+Q34uqy5UKfv7F4/pT1AkEAh5vNAYyqlkX5cX46qI0XiHDxGm9JGB+klcBHdf7OwAmGOP2FPdkrpNFmHpHF4wLCBpqn6I9O4Zm+P8Z4MWKw2QJBAI1banYqc2ju8Z+g1+dx8rTh9IkZ1LC8ttW8mfdjCEmwBLd4urR7OBZdwQTGu3I2WILrv2Va4+McbQa8ccTFARUCQAsAqpWTSDUS5uNxh9CpdAIrk1+zR9yqZkrj+8609Q2CFnpdV3k1gzmibRG/iu6zBT0oOeDuzilGRH1WbVjBRvk=";
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCC9/tb83embEwG1dn+MLCE9hpxacf8u/Oa1QMuYHVfMRq0WTw7SjGdTBmhSm3PfAahUMkEe6rAb+EdGwTTFADytYIIwk9b0FDOf/FpthDDCukwaEFup54BObpusznFmvMwwE/ne27a9WSfoZwDGYt8Lh59ay1aifypKqqfXtdMfwIDAQAB";

    public static void main(String[] args) throws Exception {

        List<aaaa> list = new ArrayList<>();
        aaaa a = new aaaa();
        list.add(a);
        System.out.println(list.isEmpty()+"``````"+list.size());
        aaaa next = list.iterator().next();
        next.age="111";
        System.out.println(list.iterator().next().age);


//        String data = "加密数据";
//
//        Map<String, String> responesMap = SecureUtil.encryptTradeInfo("123", data, privateKey, publicKey);
//        for (Map.Entry<String, String> entity : responesMap.entrySet()){
//            System.out.println(entity.getKey());
//            System.out.println(entity.getValue());
//        }
//
//        String result = SecureUtil.decryptTradeInfo("123", responesMap.get("CER"), responesMap.get("DATA"), responesMap.get("SIGN"), privateKey, publicKey);
//        System.out.println(result);
    }

    static class aaaa{
        String name;
        String age;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }
}


