package com.example.demo01.pdf;

import com.baidu.aip.ocr.AipOcr;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 图片类型的表格识别
 * 例如财务报表
 */
public class TableOcrUtils {
    //设置Baidu APPID/AK/SK
    public static final String APP_ID = "18292924";
    public static final String API_KEY = "UIfqNomNnXvfvgneOkmiZHsL";
    public static final String SECRET_KEY = "Hv8QwUTGxTl0IUzBaXvAmGYaqsoqz8Dw";
    public static final String IMAGE_PATH = "D:\\Juno\\dkqt\\备忘录\\";
    public static final boolean DEBUG = true;

    static {
        System.load("F:\\software\\安装包\\opencv\\build\\java\\x64\\opencv_java420.dll");
    }

    public static void main(String[] args) throws InterruptedException {
//        Mat src = Imgcodecs.imread("F:\\temp\\04result.png");
////        Mat path = cutImage(src, false);
//        Map<String, String> data = splitTable(src, "6");
//        baiduTextOcr(data);
        StringBuilder sb = new StringBuilder();
        for (int i = 1 ;i<16;i++){
            sb.append(baiduTextOcSingr("图片"+i+".png"));
            sb.append("\n").append("\n").append("\n").append("\n").append("\n");
            Thread.sleep(1000);
        }
        System.out.println(sb.toString());

    }

    private static void imshow(Mat dst, String name) {
        if (name.substring( name.length() - 3).equals("jpg")) {
            MatOfInt compressParams = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100);
            Imgcodecs.imwrite(IMAGE_PATH + name, dst,compressParams);
        }else {
            Imgcodecs.imwrite(IMAGE_PATH + name, dst);
        }

    }


    private static Map<String, String> splitTable(Mat src, String num) {
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
        List<Rect> haveReacts = Lists.newArrayList();
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
            if (DEBUG) imshow(tables.get(i), "table_" + i + ".png");
            Mat table = tables.get(i);
//            Mat table = tables.get(i);
            Rect rect = haveReacts.get(i);
            int width = rect.width, height = rect.height;
            Map<String, Map<String, Double>> mapdata = mappoint.get("key" + i);
            int[] x_z = maptoint(mapdata.get("x"));
            int[] y_z = maptoint(mapdata.get("y"));

            //纵切
            String px_biao = StringUtils.isEmpty(num) ? "6" : num;
            int x_len = 0, x_biao = Integer.parseInt(px_biao);
            List<Mat> mats = new ArrayList<Mat>();
            for (int j = 0; j < x_z.length; j++) {
                if (j == 0) {
                    Mat img = new Mat(table, new Rect(0, 0, x_z[j], height));
                    if (img.cols() > x_biao) {
                        mats.add(img);
                        if (DEBUG) imshow(img, j + ".png");
                        x_len++;
                    }
                } else {
                    Mat img = new Mat(table, new Rect(x_z[j - 1], 0, x_z[j] - x_z[j - 1], height));
                    if (img.cols() > x_biao) {
                        mats.add(img);
                        if (DEBUG) imshow(img, j + ".png");
                        x_len++;
                    }
                    if (j == x_z.length - 1) {//最后一个处理
                        Mat img1 = new Mat(table, new Rect(x_z[x_z.length - 1], 0, width - x_z[x_z.length - 1], height));
                        if (img.cols() > x_biao) {
                            mats.add(img1);
                            if (DEBUG) imshow(img1, j + ".png");
                        }
                    }
                }

            }

            //横切保存
            String py_biao = "5";
            int y_len = 0, y_biao = Integer.parseInt(py_biao);
            for (int j = 0; j < mats.size(); j++) {
                Mat mat = mats.get(j);
//                Mat mat = Imgcodecs.imread(IMAGE_PATH+"table_0.png");
//                if(j==1) break;
                int tuwidth = mat.cols(), tugao = mat.rows();
                int cy_len = 0;
                for (int k = 0; k < y_z.length; k++) {
                    if (k == 0) {
                        Mat img = new Mat(mat, new Rect(0, 0, tuwidth, y_z[k]));
                        if (img.rows() > y_biao) {
//                            baiduTextOcSingr(img);
                            imshow(img, "table_" + i + "_" + j + "_" + cy_len + ".jpg");
                            cy_len++;
                        }
                    } else {
                        Mat img = new Mat(mat, new Rect(0, y_z[k - 1], tuwidth, y_z[k] - y_z[k - 1]));
                        if (img.rows() > y_biao) {
                            imshow(img, "table_" + i + "_" + j + "_" + cy_len + ".jpg");
                            cy_len++;
                        }
                        if (k == y_z.length - 1) {//最后一个处理
                            Mat img1 = new Mat(mat, new Rect(0, y_z[k], tuwidth, tugao - y_z[k]));
                            if (img.rows() > y_biao) {
                                imshow(img1, "table_" + i + "_" + j + "_" + cy_len + ".jpg");
                            }
                        }
                    }
                }
                y_len = cy_len;
            }
            //保存数据信息
            jspdata.put("table_" + i, x_len + "_" + y_len);

        }
        return jspdata;
    }

    private static void baiduTextOcr(Map<String, String> jspdata) {
        //百度识别处理
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        int num = 0;
        Map<String, String> map = Maps.newHashMap();
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
                        JSONObject res = client.basicGeneral(IMAGE_PATH + name, new HashMap<String, String>());
                        System.out.println(res.toString(2));
                        String s = "0";
                        try {
                            if (res.has("words_result")) {
                                JSONArray words_result = res.getJSONArray("words_result");
                                for (Object resj : words_result) {
                                    JSONObject jsonObject = (JSONObject) resj;
                                    if (jsonObject.has("words")) {
                                        Object words = jsonObject.get("words");
                                        s = words.toString();
                                        if (isNumber(s.substring(0, 1)) && s.length() > 3) {
                                            s = s.replaceAll("\\.", "");
                                            s = s.replaceAll(",", "");
                                            String s1 = s.substring(0, s.length() - 2);
                                            String s2 = s.substring(s.length() - 2, s.length());
                                            s = s1 + "." + s2;
                                        }
                                    }
                                }
                            }
                            if (map.containsKey(i + "")) {
                                map.put(i + "", map.get(i + "") + "\t" + s);
                            } else {
                                map.put(i + "", s);
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
        for (int i = 0; i < 50; i++) {
            if (map.containsKey(i + "")) {
                System.out.println(map.get(i + ""));
            }
        }
    }

    private static String  baiduTextOcSingr(String name) {
//        byte [] grayData = new byte[mat.cols()*mat.rows()];
//        mat.get(0,0,grayData);
        //百度识别处理
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        JSONObject res = client.basicGeneral(IMAGE_PATH+name, new HashMap<String, String>());
        System.out.println(res.toString(2));
        StringBuilder sb = new StringBuilder();
        String s = "";
        try {
            if (res.has("words_result")) {
                JSONArray words_result = res.getJSONArray("words_result");
                for (Object resj : words_result) {
                    JSONObject jsonObject = (JSONObject) resj;
                    if (jsonObject.has("words")) {
                        Object words = jsonObject.get("words");
                        s = words.toString();
//                        if (isNumber(s.substring(0, 1))) {
//                            s = s.replaceAll("\\.", "");
//                            s = s.replaceAll(",", "");
//                            String s1 = s.substring(0, s.length() - 2);
//                            String s2 = s.substring(s.length() - 2, s.length());
//                            s = s1 + "." + s2;
//                        }
                    }
                    sb.append(s).append("\n");

                }

            }
            return sb.toString();
        } catch (Exception e) {
            System.out.println("cuowu");
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static boolean isNumber(String string) {
        if (string == null)
            return false;
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        return pattern.matcher(string).matches();
    }

    private static int[] maptoint(Map<String, Double> x) {
        int[] zhi = new int[x.size()];
        int num = 0;
        for (Map.Entry<String, Double> m : x.entrySet()) {
            zhi[num] = m.getValue().intValue();
            num++;
        }
        Arrays.sort(zhi);
        return zhi;
    }


    /**
     * 识别图片中的表格，并且旋转至正常
     * param src :图片  isRemoveRed 是否去除红章
     */
    public static Mat cutImage(Mat src, boolean isRemoveRed) {
//		 Canny
        Mat cannyMat = canny(src);
        if (DEBUG) imshow(cannyMat, "01cannyMat.png");

        // 获取最大矩形
        RotatedRect rect = findMaxRect(cannyMat);

        // 旋转矩形
        Mat CorrectImg = rotation(cannyMat, rect);
        if (DEBUG) imshow(CorrectImg, "02CorrectImg.png");
        Mat NativeCorrectImg = rotation(src, rect);
        if (DEBUG) imshow(CorrectImg, "03NativeCorrectImg.png");
        //裁剪矩形
        Mat mat = cutRect(CorrectImg, NativeCorrectImg);
        if (DEBUG) imshow(mat, "04result.png");
        if (isRemoveRed) {
            mat = removeRed(mat);
            if (DEBUG) imshow(mat, "04result.png");
        }

        return mat;
    }

    /**
     * canny算法，边缘检测
     *
     * @return
     */
    private static Mat canny(Mat dst) {
        Mat mat = dst.clone();
        Imgproc.Canny(dst, mat, 60, 200);

        return mat;
    }

    /**
     * 返回边缘检测之后的最大矩形,并返回
     *
     * @param cannyMat Canny之后的mat矩阵
     * @return
     */
    private static RotatedRect findMaxRect(Mat cannyMat) {

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

        return Imgproc.minAreaRect(matOfPoint2f);
    }

    /**
     * 旋转矩形
     * <p>
     * mat矩阵
     *
     * @param rect 矩形
     * @return
     */
    private static Mat rotation(Mat cannyMat, RotatedRect rect) {
        // 获取矩形的四个顶点
        Point[] rectPoint = new Point[4];
        rect.points(rectPoint);

//        double angle = rect.angle + 90; 如果想变成横着的 就+90
        double angle = rect.angle;

        Point center = rect.center;

        Mat CorrectImg = new Mat(cannyMat.size(), cannyMat.type());

        cannyMat.copyTo(CorrectImg);

        // 得到旋转矩阵算子
        Mat matrix = Imgproc.getRotationMatrix2D(center, angle, 0.8);

        Imgproc.warpAffine(CorrectImg, CorrectImg, matrix, CorrectImg.size(), 1, 0, new Scalar(0, 0, 0));
        return CorrectImg;
    }

    /**
     * 把矫正后的图像切割出来
     *
     * @param correctMat 图像矫正后的Mat矩阵
     */
    private static Mat cutRect(Mat correctMat, Mat nativeCorrectMat) {
        // 获取最大矩形
        RotatedRect rect = findMaxRect(correctMat);

        Point[] rectPoint = new Point[4];
        rect.points(rectPoint);

        int startLeft = (int) Math.abs(rectPoint[0].x);
        int startUp = (int) Math.abs(rectPoint[0].y < rectPoint[1].y ? rectPoint[0].y : rectPoint[1].y);
        int width = (int) Math.abs(rectPoint[2].x - rectPoint[0].x);
        int height = (int) Math.abs(rectPoint[1].y - rectPoint[0].y);

        System.out.println("startLeft = " + startLeft);
        System.out.println("startUp = " + startUp);
        System.out.println("width = " + width);
        System.out.println("height = " + height);

        for (Point p : rectPoint) {
            System.out.println(p.x + " , " + p.y);
        }

        Mat temp = new Mat(nativeCorrectMat, new Rect(startLeft, startUp, width, height));
        Mat t = new Mat();
        temp.copyTo(t);
        return t;
    }

    /**
     * 去除红色公章 讲图片只显示红色通道
     */
    private static Mat removeRed(Mat table) {
        Mat hideChannel = new Mat(table.size(), CvType.CV_8UC1, new Scalar(255));
        List<Mat> list = Lists.newArrayList();
        Core.split(table, list);
        Mat[] mbgr = new Mat[3];
        Mat imageR = new Mat(table.size(), CvType.CV_8UC3);    //创建尺寸与srcImage相同，三通道图像imageB
        mbgr[0] = list.get(2);
        mbgr[1] = list.get(2);
        mbgr[2] = list.get(2);
        Core.merge(Arrays.asList(mbgr), imageR);
        Mat tart = new Mat();
        Imgproc.cvtColor(imageR, tart, Imgproc.COLOR_BGR2GRAY);
        if (DEBUG) imshow(tart, "tart.png");
        return tart;
    }

    /**
     * 增强对比度
     *
     * @param src
     * @return
     */
    public static Mat histEqualize(Mat src) {
        Mat dst = src.clone();
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_BGR2YCrCb);
        List<Mat> list1 = new ArrayList<>();
        Core.split(dst, list1);
        Imgproc.equalizeHist(list1.get(0), list1.get(0));
        Core.normalize(list1.get(0), list1.get(0), 0, 255, Core.NORM_MINMAX);
        Core.merge(list1, dst);
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_YCrCb2BGR);
        return dst;
    }

    /**
     * 用于整体偏暗图像的增强,变亮
     *
     * @param src
     * @return
     */
    public static Mat laplaceEnhance(Mat src) {
        Mat srcClone = src.clone();
        float[] kernel = {0, 0, 0, -1, 5f, -1, 0, 0, 0};
        Mat kernelMat = new Mat(3, 3, CvType.CV_32FC1);
        kernelMat.put(0, 0, kernel);
        Imgproc.filter2D(srcClone, srcClone, CvType.CV_8UC3, kernelMat);
        return srcClone;
    }
}
