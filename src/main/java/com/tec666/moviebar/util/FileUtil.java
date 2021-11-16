package com.tec666.moviebar.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tec666.moviebar.database.dos.FileDataDO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {


    public static void pathToDataPojo(List<String> paths){

    }


    public static void pathToPojo(String basePath, List<String> paths) throws Exception{

        List<FileDataDO> data = new ArrayList<>();

        for (String p : paths) {
            FileDataDO fileDataDO = new FileDataDO();
            fileDataDO.setPath(p);
            fileDataDO.setName(getPathFileName(p));
            data.add(fileDataDO);
        }

        data = data.stream().distinct().collect(Collectors.toList());

        HashMap<String, List<FileDataDO>> mapData = new HashMap<>();
        for (FileDataDO fileDataDO : data) {
            String fatherPath = getFatherPath(fileDataDO.getPath());
            if (!mapData.containsKey(fatherPath)) {
                List<FileDataDO> tempData = new ArrayList<>();
                tempData.add(fileDataDO);
                mapData.put(fatherPath, tempData);
            }else{
                mapData.get(fatherPath).add(fileDataDO);
            }
        }

        ObjectMapper mapper = ObjectMapperUtil.get();
        System.out.println(mapper.writeValueAsString(mapData));
    }

    public static String getPathFileName(String path) {
        path = path.replace("//", "/").replace("\\\\", "\\");
        int l = path.lastIndexOf("/");
        int r = path.lastIndexOf("\\");
        return path.substring(Math.max(l, r) + 1);
    }

    private static String getFatherPath(String path) {
        path = path.replace("//", "/").replace("\\\\", "\\");
        int l = path.lastIndexOf("/");
        int r = path.lastIndexOf("\\");
        return path.substring(0, Math.max(l, r));
    }


    public static void main(String[] args) {
        System.out.println(getFatherPath("D:/文档/公司电脑资料备份/test-video-data/aiheihei/xvideos.com_4616fc54dc50fced69654cb8802628ab.mp4"));
    }
}
