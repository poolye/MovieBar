package com.tec666.moviebar.controller.api.computer;

import com.tec666.moviebar.config.response.AjaxResponse;
import com.tec666.moviebar.database.dos.FileDataDO;
import com.tec666.moviebar.database.dos.FolderDataDO;
import com.tec666.moviebar.service.FolderManageService;
import com.tec666.moviebar.util.FolderFileScanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/computer/fileManage")
public class FileManageComApiController {

    @Autowired
    private FolderManageService folderManageService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/checkFilePath", method = RequestMethod.POST)
    public AjaxResponse<String> checkFilePath(
            @RequestParam(value = "path") String path
    ) {
        try {
            FolderFileScanUtil.pathIsFolderAndExist(path);
            return folderManageService.addFolder(path);
        } catch (Exception e) {
            logger.error("checkFile err:", e);
            return new AjaxResponse<>(false, -1, e.getMessage());
        }
    }

    @RequestMapping(value = "/getCatalog", method = RequestMethod.GET)
    public AjaxResponse<List<FolderDataDO>> getCatalog() {
        return folderManageService.getFolderCatalog();
    }

    @RequestMapping(value = "/openFolder", method = RequestMethod.GET)
    public AjaxResponse<List<FileDataDO>> openFolder(@RequestParam(value = "hashPath")String hashPath){
        return folderManageService.openFolder(hashPath);
    }
}
