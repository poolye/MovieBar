package com.tec666.moviebar.service;

import com.tec666.moviebar.MovieBarApplication;
import com.tec666.moviebar.config.BaseCommonConfig;
import com.tec666.moviebar.config.properties.BasePropertiesConfig;
import com.tec666.moviebar.config.response.AjaxResponse;
import com.tec666.moviebar.database.cache.FolderDataCache;
import com.tec666.moviebar.database.dos.FileDataDO;
import com.tec666.moviebar.database.dos.FolderDataDO;
import com.tec666.moviebar.util.GifUtil;
import com.tec666.moviebar.util.VideoUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FolderManageService {

    @Autowired
    private FolderDataCache folderDataCache;
    @Autowired
    private BasePropertiesConfig basePropertiesConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AjaxResponse<String> addFolder(String path) {

        //更新文件信息到缓存数据库
        FolderDataDO folderDataDO = updateFolder(path);

        //生成缩略图
//        int i = 0;
        for (FileDataDO item : folderDataDO.getData()) {
            MovieBarApplication.threadPoolExecutor.execute(() -> {

                String hashPath = DigestUtils.md5Hex(item.getPath());
                int num = 50;

                boolean needRefresh = false;
                for (int a = 0; a < num; a++) {
                    File thumbFile = new File("./tmp/thumb/" + hashPath + "/" + a + ".jpg");
                    if (!thumbFile.exists()) {
                        needRefresh = true;
                        break;
                    }
                }

                //生成缩略图
                if (needRefresh) {
                    VideoUtil.getScreenshot(item.getPath(), "./tmp/thumb/" + hashPath, num, 0, 200);
                }


                //生成GIF
                String gifPath = "./tmp/gif/" + hashPath + "/thumb.gif";
                File gifFile = new File(gifPath);
                if (!gifFile.exists()) {
                    try {
                        GifUtil.generateGif("./tmp/thumb/" + hashPath + "/", gifPath, num);
                    } catch (Exception e) {
                        logger.error("generate gif err", e);
                    }
                }

            });
//            if (i > 10) {
//                break;
//            }
//            i++;
        }


        return new AjaxResponse<>();
    }

    public FolderDataDO updateFolder(String path) {

        String hashPath = DigestUtils.md5Hex(path);

        //当前文件夹详情写入缓存
        FolderDataDO folderDataDO = folderDataCache.save(hashPath, path);

        //当前文件夹存入目录
        HashMap<String, FolderDataDO> folderCatalog = folderDataCache.getFolderCatalog("k");
        folderCatalog.put(hashPath, folderDataDO);
        folderDataCache.saveFolderCatalog(folderCatalog, "k");

        return folderDataDO;
    }

    public AjaxResponse<List<FolderDataDO>> getFolderCatalog() {
        HashMap<String, FolderDataDO> data = folderDataCache.getFolderCatalog("k");
        List<FolderDataDO> result = new ArrayList<>();
        for (Map.Entry<String, FolderDataDO> entry : data.entrySet()) {

            FolderDataDO folderDataDO = entry.getValue();

            folderDataDO.setHashPath(DigestUtils.md5Hex(folderDataDO.getPath()));

            int num = Math.min(9, folderDataDO.getMovieNum());
            //填入缩略图请求URL
            List<String> thumbs = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                FileDataDO fileDataDO = folderDataDO.getData().get(i);
                thumbs.add(BaseCommonConfig.SERVER_ADDRESS + "/api/baseResource/thumb?hashPath=" + DigestUtils.md5Hex(fileDataDO.getPath()) + "&n=" + i);
            }
            folderDataDO.setThumbs(thumbs);

            result.add(folderDataDO);
        }
        return new AjaxResponse<>(true, 200, "success", result);
    }

    public AjaxResponse<List<FileDataDO>> openFolder(String hashPath) {
        FolderDataDO folderDataDO = folderDataCache.get(hashPath);
        if (folderDataDO != null) {
            List<FileDataDO> result = folderDataDO.getData();
            for (FileDataDO item : result) {
                item.setThumb(BaseCommonConfig.SERVER_ADDRESS + "/api/baseResource/thumb?hashPath=" + DigestUtils.md5Hex(item.getPath()) + "&n=0");
                item.setHashPath(DigestUtils.md5Hex(item.getPath()));
            }
            return new AjaxResponse<>(true, 200, "success", result);
        }
        return new AjaxResponse<>(false, -1, "文件夹不存在");
    }
}
