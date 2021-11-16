package com.tec666.moviebar.util;

import com.madgag.gif.fmsware.AnimatedGifEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author longge93
 */
public class GifUtil {


    /**
     * @param basePath   必须以斜杠结尾
     * @param targetFile 目标GIF完路径
     * @param num        资源文件下一共有几张图片
     * @throws Exception
     */
    public static void generateGif(String basePath, String targetFile, Integer num) throws Exception {

        //目标gif路径预生成
        String fartherPath = targetFile.substring(0, targetFile.lastIndexOf("/"));
        File fartherFile = new File(fartherPath);
        if (!fartherFile.exists()) {
            fartherFile.mkdirs();
        }

        AnimatedGifEncoder e = new AnimatedGifEncoder();
        //生成的图片路径
        e.start(new FileOutputStream(targetFile));
        e.setQuality(1);
//        e.setSize(300, 300);
        //图片之间间隔时间
        e.setDelay(1);
        //重复次数 0表示无限重复 默认不重复
        e.setRepeat(0);

        for (int i = 0; i < num; i++) {
            BufferedImage image = ImageIO.read(new File(basePath + i + ".jpg"));
            int imgHeight = image.getHeight();
            int imgWidth = image.getWidth();

            e.addFrame(image);
        }

        e.finish();
    }


    public static void main(String[] args) {

    }
}
