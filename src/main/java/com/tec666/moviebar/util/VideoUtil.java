package com.tec666.moviebar.util;


import org.apache.commons.codec.digest.DigestUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


public class VideoUtil {


    /**
     * 通过Javacv的方式获取视频截图
     *
     * @param filePath 视频文件路径
     */
    public static void getScreenshot(String filePath, String targetImgPath, int num, Integer step, Integer beginJump) {
        try {
            long beginTime = System.currentTimeMillis();
//            System.out.println("#截取视频截图开始#");

            FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(filePath);

            // 第一帧图片存储位置
            String targerFilePath = filePath.substring(0, filePath.lastIndexOf("\\"));
            // 视频文件名
            String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
            // 图片名称
            String targetFileName = fileName.substring(0, fileName.lastIndexOf("."));
//            System.out.println("视频路径是：" + targerFilePath);
//            System.out.println("视频文件名：" + fileName);
//            System.out.println("图片名称是：" + targetFileName);

            grabber.start();
            int length = grabber.getLengthInFrames() - beginJump;
//            System.out.println("Length is:" + length);
            if (num > length) {
                num = length;
            }
            if (step == null) {
                step = length / num - 2;
            }
//            System.out.println("Step is:" + step);

            Frame frame;
            int i = 0;
            int stepI = 0;
            int n = 0;
            int baseLength = grabber.getLengthInFrames();
            while (i < baseLength) {

                //设置视频截取帧
                frame = grabber.grabImage();

                i++;

                if (i < beginJump) {
                    continue;
                }

                if (stepI < step) {
                    stepI++;
                    continue;
                } else {
                    stepI = 0;
                }
                //grabber.setVideoFrameNumber();

//                System.out.println("cut img i is:" + i);

                if (frame != null && frame.image != null) {
                    //视频旋转度
                    String rotate = grabber.getVideoMetadata("rotate");
                    //开始截图
                    cutImg(targetImgPath, targetFileName, DigestUtils.md5Hex(filePath), n, frame, rotate);
                    n++;
                    if (n >= num) {
                        break;
                    }
                }

            }


//            System.out.println("截取视频截图结束，耗时：" + (System.currentTimeMillis() - beginTime));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cutImg(String targetImgPath, String targetFileName, String md5, int id, Frame frame, String rotate) throws Exception {

        //绘制图片
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bi = converter.getBufferedImage(frame);
        if (rotate != null) {
            // 旋转图片
            bi = rotate(bi, Integer.parseInt(rotate));
        }

        //图片的类型
        String imageMat = "jpg";
        //图片的完整路径
        String imageFolder = targetImgPath + File.separator;
        File imageFolderFile = new File(imageFolder);
        if (!imageFolderFile.exists()) {
            imageFolderFile.mkdirs();
        }
        String imagePath = imageFolder + id + "." + imageMat;
        //创建文件
        File output = new File(imagePath);
        ImageIO.write(bi, imageMat, output);
    }

    /**
     * 根据视频旋转度来调整图片
     *
     * @param src   BufferedImage
     * @param angel angel	视频旋转度
     * @return BufferedImage
     */
    public static BufferedImage rotate(BufferedImage src, int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        int type = src.getColorModel().getTransparency();
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
        BufferedImage bi = new BufferedImage(rect_des.width, rect_des.height, type);
        Graphics2D g2 = bi.createGraphics();
        g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return bi;
    }


    /**
     * 计算图片旋转大小
     *
     * @param src   Rectangle
     * @param angel int
     * @return Rectangle
     */
    public static Rectangle calcRotatedSize(Rectangle src, int angel) {
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
        return new Rectangle(new Dimension(des_width, des_height));
    }

}
