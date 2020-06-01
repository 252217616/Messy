package com.example.demo01.pdf;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


import com.baidu.aip.ocr.AipOcr;



/**
 * Servlet implementation class TutableRead
 */
public class TutableReadBaidu {
    private static final long serialVersionUID = 1L;


    static {
        System.load("F:\\software\\安装包\\opencv\\build\\java\\x64\\opencv_java420.dll");
    }

    //注册百度有设置APPID/AK/SK
    public static final String APP_ID = "18292924";
    public static final String API_KEY = "UIfqNomNnXvfvgneOkmiZHsL";
    public static final String SECRET_KEY = "Hv8QwUTGxTl0IUzBaXvAmGYaqsoqz8Dw";

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    public static void doPost(boolean isRed)  {
        Mat src = Imgcodecs.imread("F:\\temp\\04mat.png");
        Mat gray = new Mat();
        Mat erod = new Mat();
        Mat blur = new Mat();
        int src_height = src.cols(), src_width = src.rows();
        //先转为灰度 cvtColor(src, gray, COLOR_BGR2GRAY);
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        /**
         * 腐蚀（黑色区域变大）
         Mat element = getStructuringElement(MORPH_RECT, Size(erodeSize, erodeSize));
         erode(gray, erod, element);
         */
        int erodeSize = src_height / 200;
        if (erodeSize % 2 == 0) {
            erodeSize++;
        }
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(erodeSize, erodeSize));
        Imgproc.erode(gray, erod, element);
        //高斯模糊化
        int blurSize = src_height / 200;
        if (blurSize % 2 == 0) {
            blurSize++;
        }
        Imgproc.GaussianBlur(erod, blur, new Size(blurSize, blurSize), 0, 0);


        //封装的二值化 adaptiveThreshold(~gray, thresh, 255, CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY, 15, -2);
        Mat thresh = gray.clone();
        Mat xx = new Mat();
        Core.bitwise_not(gray, xx);//反色
        Imgproc.adaptiveThreshold(xx, thresh, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);

		/*
		这部分的思想是将线条从横纵的方向处理后抽取出来，再进行交叉，矩形的点，进而找到矩形区域的过程

		*/
        // Create the images that will use to extract the horizonta and vertical lines
        //使用二值化后的图像来获取表格横纵的线
        Mat horizontal = thresh.clone();
        Mat vertical = thresh.clone();
        //这个值越大，检测到的直线越多
        String parameter = "20";
        int scale = Integer.parseInt(parameter); // play with this variable in order to increase/decrease the amount of lines to be detected 使用这个变量来增加/减少待检测的行数


        // Specify size on horizontal axis 指定水平轴上的大小
        int horizontalsize = horizontal.cols() / scale;
        // Create structure element for extracting horizontal lines through morphology operations 创建通过形态学运算提取水平线的结构元素
        // 为了获取横向的表格线，设置腐蚀和膨胀的操作区域为一个比较大的横向直条
        Mat horizontalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(horizontalsize, 1));
        // Apply morphology operations
        // 先腐蚀再膨胀
        // iterations 最后一个参数，迭代次数，越多，线越多。在页面清晰的情况下1次即可。
        Imgproc.erode(horizontal, horizontal, horizontalStructure, new Point(-1, -1), 1);
        Imgproc.dilate(horizontal, horizontal, horizontalStructure, new Point(-1, -1), 1);
        // dilate(horizontal, horizontal, horizontalStructure, Point(-1, -1)); // expand horizontal lines

        // Specify size on vertical axis 同上
        int verticalsize = vertical.rows() / scale;
        // Create structure element for extracting vertical lines through morphology operations
        Mat verticalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, verticalsize));
        Imgproc.erode(vertical, vertical, verticalStructure, new Point(-1, -1), 1);
        Imgproc.dilate(vertical, vertical, verticalStructure, new Point(-1, -1), 1);
        /*
         * 合并线条
         * 将垂直线，水平线合并为一张图
         */
        Mat mask = new Mat();
        Core.add(horizontal, vertical, mask);
        /*
         * 通过 bitwise_and 定位横线、垂直线交汇的点
         */
        Mat joints = new Mat();
        Core.bitwise_and(horizontal, vertical, joints);
        /*
         * 通过 findContours 找轮廓
         *
         * 第一个参数，是输入图像，图像的格式是8位单通道的图像，并且被解析为二值图像（即图中的所有非零像素之间都是相等的）。
         * 第二个参数，是一个 MatOfPoint 数组，在多数实际的操作中即是STL vectors的STL vector，这里将使用找到的轮廓的列表进行填充（即，这将是一个contours的vector,其中contours[i]表示一个特定的轮廓，这样，contours[i][j]将表示contour[i]的一个特定的端点）。
         * 第三个参数，hierarchy，这个参数可以指定，也可以不指定。如果指定的话，输出hierarchy，将会描述输出轮廓树的结构信息。0号元素表示下一个轮廓（同一层级）；1号元素表示前一个轮廓（同一层级）；2号元素表示第一个子轮廓（下一层级）；3号元素表示父轮廓（上一层级）
         * 第四个参数，轮廓的模式，将会告诉OpenCV你想用何种方式来对轮廓进行提取，有四个可选的值：
         *   CV_RETR_EXTERNAL （0）：表示只提取最外面的轮廓；
         *   CV_RETR_LIST （1）：表示提取所有轮廓并将其放入列表；
         *   CV_RETR_CCOMP （2）:表示提取所有轮廓并将组织成一个两层结构，其中顶层轮廓是外部轮廓，第二层轮廓是“洞”的轮廓；
         *   CV_RETR_TREE （3）：表示提取所有轮廓并组织成轮廓嵌套的完整层级结构。
         * 第五个参数，见识方法，即轮廓如何呈现的方法，有三种可选的方法：
         *   CV_CHAIN_APPROX_NONE （1）：将轮廓中的所有点的编码转换成点；
         *   CV_CHAIN_APPROX_SIMPLE （2）：压缩水平、垂直和对角直线段，仅保留它们的端点；
         *   CV_CHAIN_APPROX_TC89_L1 （3）or CV_CHAIN_APPROX_TC89_KCOS（4）：应用Teh-Chin链近似算法中的一种风格
         * 第六个参数，偏移，可选，如果是定，那么返回的轮廓中的所有点均作指定量的偏移
         */
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        List<MatOfPoint> contours_poly = contours;
        Rect[] boundRect = new Rect[contours.size()];
        List<Mat> tables = new ArrayList<Mat>();
        //my
        List<Rect> haveReacts = new ArrayList();
        Map<String, Map<String, Map<String, Double>>> mappoint = new HashMap<String, Map<String, Map<String, Double>>>();
        //循环所有找到的轮廓-点
        for (int i = 0; i < contours.size(); i++) {
            //每个表的点
            MatOfPoint point = contours.get(i);
            MatOfPoint contours_poly_point = contours_poly.get(i);
            /*
             * 获取区域的面积
             * 第一个参数，InputArray contour：输入的点，一般是图像的轮廓点
             * 第二个参数，bool oriented = false:表示某一个方向上轮廓的的面积值，顺时针或者逆时针，一般选择默认false
             */
            double area = Imgproc.contourArea(contours.get(i));
            //如果小于某个值就忽略，代表是杂线不是表格
            if (area < 100) {
                continue;
            }
            /*
             * approxPolyDP 函数用来逼近区域成为一个形状，true值表示产生的区域为闭合区域。比如一个带点幅度的曲线，变成折线
             *
             * MatOfPoint2f curve：像素点的数组数据。
             * MatOfPoint2f approxCurve：输出像素点转换后数组数据。
             * double epsilon：判断点到相对应的line segment 的距离的阈值。（距离大于此阈值则舍弃，小于此阈值则保留，epsilon越小，折线的形状越“接近”曲线。）
             * bool closed：曲线是否闭合的标志位。
             */
            Imgproc.approxPolyDP(new MatOfPoint2f(point.toArray()), new MatOfPoint2f(contours_poly_point.toArray()), 3, true);
            //为将这片区域转化为矩形，此矩形包含输入的形状
            boundRect[i] = Imgproc.boundingRect(contours_poly.get(i));
            // 找到交汇处的的表区域对象
            Mat table_image = joints.submat(boundRect[i]);

            List<MatOfPoint> table_contours = new ArrayList<MatOfPoint>();
            Mat joint_mat = new Mat();
            Imgproc.findContours(table_image, table_contours, joint_mat, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
            //从表格的特性看，如果这片区域的点数小于4，那就代表没有一个完整的表格，忽略掉
            if (table_contours.size() < 4) {
                continue;
            }


            //表格里面的每个点
            Map<String, Double> x_zhis = new HashMap<String, Double>();
            Map<String, Double> y_zhis = new HashMap<String, Double>();
            for (MatOfPoint matOfPoint : table_contours) {
                Point[] array = matOfPoint.toArray();
                for (Point point2 : array) {
                    x_zhis.put("x" + point2.x, point2.x);
                    y_zhis.put("y" + point2.y, point2.y);
                }
            }
            //System.out.println( boundRect[i].x+"|"+boundRect[i].y+"|"+boundRect[i].width+"|"+boundRect[i].height+"|"+table_contours.size()+">>>>>>>>>>>>>>>>>>>");
            //my add
            haveReacts.add(boundRect[i]);
            Map<String, Map<String, Double>> x = new HashMap<String, Map<String, Double>>();
            x.put("x", x_zhis);
            x.put("y", y_zhis);
            mappoint.put("key" + (haveReacts.size() - 1), x);

            //保存图片
            tables.add(src.submat(boundRect[i]).clone());
            //将矩形画在原图上
            Imgproc.rectangle(src, boundRect[i].tl(), boundRect[i].br(), new Scalar(255, 0, 255), 1, 8, 0);
        }
        //页面数据
        Map<String, String> jspdata = new HashMap<String, String>();



        for (int i = 0; i < tables.size(); i++) {
            Mat table = tables.get(i);
            if(isRed){
                Mat hideChannel = new Mat(table.size(), CvType.CV_8UC1, new Scalar(255));
                List<Mat> list = Lists.newArrayList();
                Core.split(table,list);
                Mat [] mbgr = new Mat[3];
                Mat imageR = new Mat(table.size(), CvType.CV_8UC3);	//创建尺寸与srcImage相同，三通道图像imageB
                mbgr[0] = hideChannel;
                mbgr[1] = hideChannel;
                mbgr[2] = list.get(2);
                Core.merge(Arrays.asList(mbgr), imageR);
                Mat tart = new Mat();
                Imgproc.cvtColor(imageR, tart, Imgproc.COLOR_BGR2GRAY);
                table = tart;
            }
            Rect rect = haveReacts.get(i);
            int width = rect.width, height = rect.height;
            Map<String, Map<String, Double>> mapdata = mappoint.get("key" + i);
            int[] x_z = maptoint(mapdata.get("x"));
            int[] y_z = maptoint(mapdata.get("y"));

            //纵切
            String px_biao = "6";
            int x_len = 0, x_biao = Integer.parseInt(px_biao);
            List<Mat> mats = new ArrayList<Mat>();
            for (int j = 0; j < x_z.length; j++) {
                if (j == 0) {
                    Mat img = new Mat(table, new Rect(0, 0, x_z[j], height));
                    if (img.cols() > x_biao) {
                        mats.add(img);
//						Imgcodecs.imwrite("F:\\Temp\\"+j+".png",img);
                        x_len++;
                    }
                } else {
                    Mat img = new Mat(table, new Rect(x_z[j - 1], 0, x_z[j] - x_z[j - 1], height));
                    if (img.cols() > x_biao) {
                        mats.add(img);
//						Imgcodecs.imwrite("F:\\Temp\\"+j+".png",img);
                        x_len++;
                    }
                    if (j == x_z.length - 1) {//最后一个处理
                        Mat img1 = new Mat(table, new Rect(x_z[x_z.length - 1], 0, width - x_z[x_z.length - 1], height));
                        if (img.cols() > x_biao) {
                            mats.add(img1);
//							Imgcodecs.imwrite("F:\\Temp\\"+j+".png",img1);
                        }
                    }
                }

            }
            imshow("F:\\Temp", table, "table_" + i + ".png");//当前table图

            //横切保存
            String py_biao = "5";
            int y_len = 0, y_biao = Integer.parseInt(py_biao);
            for (int j = 0; j < mats.size(); j++) {
                Mat mat = mats.get(j);
                int tuwidth = mat.cols(), tugao = mat.rows();
                int cy_len = 0;
                for (int k = 0; k < y_z.length; k++) {
                    if (k == 0) {
                        Mat img = new Mat(mat, new Rect(0, 0, tuwidth, y_z[k]));
                        if (img.rows() > y_biao) {
                            imshow("F:\\Temp", img, "table_" + i + "_" + j + "_" + cy_len + ".png");
                            cy_len++;
                        }
                    } else {
                        Mat img = new Mat(mat, new Rect(0, y_z[k - 1], tuwidth, y_z[k] - y_z[k - 1]));
                        if (img.rows() > y_biao) {
                            imshow("F:\\Temp", img, "table_" + i + "_" + j + "_" + cy_len + ".png");
                            cy_len++;
                        }
                        if (k == y_z.length - 1) {//最后一个处理
                            Mat img1 = new Mat(mat, new Rect(0, y_z[k], tuwidth, tugao - y_z[k]));
                            if (img.rows() > y_biao) {
                                imshow("F:\\Temp", img1, "table_" + i + "_" + j + "_" + (cy_len) + ".png");
                            }
                        }
                    }
                }
                y_len = cy_len;
            }
            //保存数据信息
            jspdata.put("table_" + i, x_len + "_" + y_len);
        }
        //百度识别处理
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        int num = 0;
        Map<String,String> map = Maps.newHashMap();
        for (Map.Entry<String, String> d : jspdata.entrySet()) {
            String value = d.getValue();
            if (value.indexOf("_") != -1) {
                //
                String len[] = value.split("_");
                int xlen = Integer.parseInt(len[0]);
                int ylen = Integer.parseInt(len[1]);
                for (int i = 0; i < ylen; i++) {
                    //行
                    for (int j = 0; j < xlen; j++) {
                        String name = "table_" + num + "_" + j + "_" + i + ".png";
                        JSONObject res = client.basicGeneral("F:\\Temp\\" + name, new HashMap<String, String>());
                        System.out.println(res.toString(2));
                        String s = "0";
                        try {
                            if(res.has("words_result")) {
                                JSONArray words_result = res.getJSONArray("words_result");
                                for (Object resj : words_result) {
                                    JSONObject jsonObject = (JSONObject) resj;
                                    if (jsonObject.has("words")) {
                                        Object words = jsonObject.get("words");
                                        s = words.toString();
                                        if(isNumber(s.substring(0,1))){
                                            s = s.replaceAll("\\.","");
                                            s = s.replaceAll(",","");
                                            String s1 = s.substring(0,s.length()-2);
                                            String s2 = s.substring(s.length()-2,s.length());
                                            s = s1+"."+s2;
                                        }
                                    }
                                }
                            }
                            if( map.containsKey(i+"")){
                                map.put(i+"",map.get(i+"")+"\t"+s);
                            }else {
                                map.put(i+"",s);
                            }
                            System.out.println(s);
                        } catch (Exception e) {
                            System.out.println("cuowu");
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(400);//百度qps限制
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
            num++;
        }
        for(int i = 0;i< 50 ;i++){
            if(map.containsKey(i+"")){
                System.out.println(map.get(i+""));
            }
        }
    }

    public static void imshow(String basePath, Mat dst, String name) {
        Imgcodecs.imwrite(basePath + "/" + name, dst);
    }

    public static String getjsontext(JSONArray array) {
        String text = "";
        for (int i = 0; i < array.length(); i++) {
            JSONObject textx = (JSONObject) array.get(i);
            text += textx.get("words");
        }
        return text;
    }

    public static int[] maptoint(Map<String, Double> x) {
        int[] zhi = new int[x.size()];
        int num = 0;
        for (Map.Entry<String, Double> m : x.entrySet()) {
            zhi[num] = m.getValue().intValue();
            num++;
        }
        Arrays.sort(zhi);
        return zhi;
    }

    public static boolean isNumber(String string) {
        if (string == null)
            return false;
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        return pattern.matcher(string).matches();
    }


    /**
     * canny算法，边缘检测
     * @return
     */
    public static Mat canny( Mat dst) {
        Mat mat = dst.clone();
        Imgproc.Canny(dst, mat, 60, 200);
        imshow("F:\\Temp",dst,"01canny.png");
        return mat;
    }

    /**
     * 返回边缘检测之后的最大矩形,并返回
     *
     * @param cannyMat
     *            Canny之后的mat矩阵
     * @return
     */
    public static RotatedRect findMaxRect(Mat cannyMat) {

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

        // 寻找轮廓
        Imgproc.findContours(cannyMat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE,
                new Point(0, 0));

        // 找出匹配到的最大轮廓
        double area = Imgproc.boundingRect(contours.get(0)).area();
        int index = 0;

        // 找出匹配到的最大轮廓
        for (int i = 0; i < contours.size(); i++) {
            double tempArea = Imgproc.boundingRect(contours.get(i)).area();
            if (tempArea > area) {
                area = tempArea;
                index = i;
            }
        }

        MatOfPoint2f matOfPoint2f = new MatOfPoint2f(contours.get(index).toArray());

        RotatedRect rect = Imgproc.minAreaRect(matOfPoint2f);

        return rect;
    }

    /**
     * 旋转矩形
     *
     *            mat矩阵
     * @param rect
     *            矩形
     * @return
     */
    public static Mat rotation(Mat cannyMat, RotatedRect rect) {
        // 获取矩形的四个顶点
        Point[] rectPoint = new Point[4];
        rect.points(rectPoint);

        double angle = rect.angle + 90;

        Point center = rect.center;

        Mat CorrectImg = new Mat(cannyMat.size(), cannyMat.type());

        cannyMat.copyTo(CorrectImg);

        // 得到旋转矩阵算子
        Mat matrix = Imgproc.getRotationMatrix2D(center, angle, 0.8);

        Imgproc.warpAffine(CorrectImg, CorrectImg, matrix, CorrectImg.size(), 1, 0, new Scalar(0, 0, 0));
        imshow("F:\\Temp",CorrectImg,"02CorrectImg.png");
        return CorrectImg;
    }

    /**
     * 把矫正后的图像切割出来
     *
     * @param correctMat
     *            图像矫正后的Mat矩阵
     */
    public static void cutRect(Mat correctMat , Mat nativeCorrectMat) {
        // 获取最大矩形
        RotatedRect rect = findMaxRect(correctMat);

        Point[] rectPoint = new Point[4];
        rect.points(rectPoint);

        int startLeft = (int)Math.abs(rectPoint[0].x);
        int startUp = (int)Math.abs(rectPoint[0].y < rectPoint[1].y ? rectPoint[0].y : rectPoint[1].y);
        int width = (int)Math.abs(rectPoint[2].x - rectPoint[0].x);
        int height = (int)Math.abs(rectPoint[1].y - rectPoint[0].y);

        System.out.println("startLeft = " + startLeft);
        System.out.println("startUp = " + startUp);
        System.out.println("width = " + width);
        System.out.println("height = " + height);

        for(Point p : rectPoint) {
            System.out.println(p.x + " , " + p.y);
        }

        Mat temp = new Mat(nativeCorrectMat , new Rect(startLeft , startUp , width , height ));
        Mat t = new Mat();
        temp.copyTo(t);
        imshow("F:\\Temp",t,"03t.png");
    }

    /**
     *
     * @param bufferedImage
     *            图片
     * @param angel
     *            旋转角度
     * @return
     */
    public static BufferedImage rotateImage(BufferedImage bufferedImage, int angel) throws IOException {
        if (bufferedImage == null) {
            return null;
        }
        if (angel < 0) {
            // 将负数角度，纠正为正数角度
            angel = angel + 360;
        }
        int imageWidth = bufferedImage.getWidth(null);
        int imageHeight = bufferedImage.getHeight(null);
        // 计算重新绘制图片的尺寸
        Rectangle rectangle = calculatorRotatedSize(new Rectangle(new Dimension(imageWidth, imageHeight)), angel);
        // 获取原始图片的透明度
        int type = bufferedImage.getColorModel().getTransparency();
        BufferedImage newImage = null;
        newImage = new BufferedImage(rectangle.width, rectangle.height, type);
        Graphics2D graphics = newImage.createGraphics();
        // 平移位置
        graphics.translate((rectangle.width - imageWidth) / 2, (rectangle.height - imageHeight) / 2);
        // 旋转角度
        graphics.rotate(Math.toRadians(angel), imageWidth / 2, imageHeight / 2);
        // 绘图
        graphics.drawImage(bufferedImage, null, null);
        return newImage;
    }

    /**
     * 计算旋转后的尺寸
     *
     * @param src
     * @param angel
     * @return
     */
    private static Rectangle calculatorRotatedSize(Rectangle src, int angel) {
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new java.awt.Rectangle(new Dimension(des_width, des_height));
    }

    public static void main(String[] args) throws ServletException, IOException {
		doPost(false);
//		Mat src = Imgcodecs.imread("F:\\temp\\tttt1.jpg");
//		// Canny
//		Mat cannyMat = canny(src);
//
//		// 获取最大矩形
//		RotatedRect rect = findMaxRect(cannyMat);
//
//		// 旋转矩形
//		Mat CorrectImg = rotation(cannyMat , rect);
//		Mat NativeCorrectImg = rotation(src , rect);
//
//
//		//裁剪矩形
//		cutRect(CorrectImg , NativeCorrectImg);
//        BufferedImage read = ImageIO.read(new File("F:\\Temp\\03t.png"));
//        BufferedImage bufferedImage = rotateImage(read, -90);
//        ImageIO.write(bufferedImage,"png",new File("F:\\Temp\\reslutr"));
    }
}