package com.tec666.moviebar.database.cache;

import com.tec666.moviebar.database.dos.FileDataDO;
import com.tec666.moviebar.database.dos.FolderDataDO;
import com.tec666.moviebar.util.FileUtil;
import com.tec666.moviebar.util.FolderFileScanUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author longge93
 */
@Component
public class FolderDataCache {

    @CachePut(value = "folderDataCatalogCache", key = "#k")
    public HashMap<String, FolderDataDO> saveFolderCatalog(HashMap<String, FolderDataDO> data,String k) {
        return data;
    }

    @Cacheable(value = "folderDataCatalogCache", key = "#k")
    public HashMap<String, FolderDataDO> getFolderCatalog(String k) {
        return new HashMap<>();
    }

    @CachePut(value = "folderDataCache", key = "#hashPath")
    public FolderDataDO save(String hashPath, String path) {
        List<String> data = FolderFileScanUtil.scanFile(path, null);

        List<FileDataDO> fileDataDOList = new ArrayList<>();
        for (String p : data) {
            FileDataDO fileDataDO = new FileDataDO();
            fileDataDO.setName(FileUtil.getPathFileName(p));
            fileDataDO.setPath(p);
            fileDataDO.setHashPath(DigestUtils.md5Hex(p));
            fileDataDOList.add(fileDataDO);
        }

        FolderDataDO folderDataDO = new FolderDataDO();
        folderDataDO.setData(fileDataDOList);
        folderDataDO.setPath(path);
        folderDataDO.setMovieNum(data.size());
        return folderDataDO;
    }

    @Cacheable(value = "folderDataCache", key = "#hashPath")
    public FolderDataDO get(String hashPath) {
        return null;
    }
}
