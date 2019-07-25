package com.example.demo01.stock.akka.bean;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class GResource {
    private String[] data;
    private File[] arffFiles;
}
