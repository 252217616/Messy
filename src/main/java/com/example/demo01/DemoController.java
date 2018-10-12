package com.example.demo01;

import com.example.demo01.bean.QRCode;
import com.example.demo01.section.ParameterDefaultValue;
import com.example.demo01.service.DemoService;
import com.example.demo01.twocore.TwoCore;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("demo")
@Validated
public class DemoController {

    @Autowired
    private DemoService demoService;

    @Autowired
    private TwoCore tc;//二维码生成工具类


    @RequestMapping(value = "hhhh")
    public String erroe (@NotNull String str, @Max(10) Integer i){

        return "ok";
    }

    @RequestMapping("/dxc")
    public String dxc(Integer in) throws InterruptedException {


        demoService.add(in);

        return "ok";
    }

    @RequestMapping("/hello")
    public String seyHello() {
        return "hello";
    }

    @RequestMapping("/qrcode")
    public String createQRCode( @ParameterDefaultValue  QRCode qrCode,  HttpServletResponse response,HttpServletRequest request) throws IOException {

        String json = IOUtils.toString(request.getInputStream());
        System.out.println(json);
//        BufferedImage image = null;
//        OutputStream os = null;
//        try {
//            image = tc.generateQRCodeImage(qrCode.getMsg(), qrCode.getWidth(), qrCode.getHigth(), "png");
//            response.setContentType("image/png");
//            os = response.getOutputStream();
//            ImageIO.write(image, "png", os);
//            return "生成成功";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "生成失败";
//        }
        return "ok";
    }

}
