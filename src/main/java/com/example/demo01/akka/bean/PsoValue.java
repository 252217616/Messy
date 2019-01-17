package com.example.demo01.akka.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PsoValue {
    private final double value;
    //1 2 3 4表示同年收益
    private final List<Double> result;

    public PsoValue(double value, List<Double> result) {
        this.value = value;
        List<Double> b = new ArrayList<Double>(5);
        b.addAll(result);
        this.result = Collections.unmodifiableList(b);
    }

    public double getValue() {
        return value;
    }

    public List<Double> getResult() {
        return result;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        String str =  sb.append("value:").append(value).append("\n").append(result.toString()).toString();
        return str;
    }
}
