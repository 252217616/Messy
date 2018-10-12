package com.example.demo01.otherText;

import java.util.ArrayList;
import java.util.List;

public class meiju {
    public static void main(String[] args) {
        List<String> l = new ArrayList<>();
        l.add("1212121212");
        l.add("1212121212");
        l.add("1212121212");
        l.add("1212121212");
        l.add("1212121212");
        l.add("1212121212");
        l.add("1");
//        for (String s : l){
//            if (s.equals("l")){
//                l.remove(s);
//            }
//        }
        for (int i = 0;i<l.size();i++){
            String s = l.get(i);
            if (s.equals("1")){
                l.remove(i);
            }
        }
        l.stream().forEach(p-> System.out.println(p));

    }
}
