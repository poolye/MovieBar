package com.tec666.moviebar.util;

import com.tec666.moviebar.config.exception.BaseException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author longge93
 */
public class FolderFileScanUtil {


    public static void pathIsFolderAndExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new BaseException("路径不存在");
        }
        if (!file.isDirectory()) {
            throw new BaseException("该路径不是文件夹");
        }
    }

    public static List<String> scanFile(String path, String[] exts) {

        pathIsFolderAndExist(path);

        List<String> result = scanFileWorker(path);

        if (exts != null && exts.length > 0) {
            result = result.stream().filter(item -> {
                int index = item.lastIndexOf(".");
                String ext = item.substring(index + 1);
                return Arrays.asList(exts).contains(ext);
            }).collect(Collectors.toList());
        }


        return result;
    }

    private static List<String> scanFileWorker(String path) {

        List<String> result = new ArrayList<>();

        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return result;
        }
        for (File f : files) {
            if (f.isFile()) {
                result.add(f.getAbsolutePath());
            } else {
                result.addAll(scanFileWorker(f.getAbsolutePath()));
            }
        }

        return result;
    }


}
