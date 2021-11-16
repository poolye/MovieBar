package com.tec666.moviebar.controller.api;

import com.tec666.moviebar.config.NonStaticResourceHttpRequestHandler;
import com.tec666.moviebar.database.cache.FolderDataCache;
import com.tec666.moviebar.database.dos.FileDataDO;
import com.tec666.moviebar.database.dos.FolderDataDO;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * @author longge93
 */
@Controller
@RequestMapping(value = "/api/baseResource")
public class BaseSourceApiController {

    @Autowired
    private FolderDataCache folderDataCache;
    @Autowired
    private NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    //produces = MediaType.IMAGE_JPEG_VALUE
    @RequestMapping(value = "/thumb", method = RequestMethod.GET)
    public void thumb(
            HttpServletResponse response,
            @RequestParam(value = "hashPath") String hashPath,
            @RequestParam(value = "n") Integer n,
            @RequestParam(value = "type", required = false, defaultValue = "img") String type
    ) {

        String imgPath = "./tmp/thumb/" + hashPath + "/" + n + ".jpg";
        String contentType = "image/jpg";
        if ("gif".equals(type.toLowerCase(Locale.ROOT))) {
            imgPath = "./tmp/gif/" + hashPath + "/thumb.gif";
            contentType = "image/gif";
        }
        File file = new File(imgPath);
        if (file.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                response.setContentType(contentType);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(fileInputStream.readAllBytes());
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
//                logger.error("read img err");
            }
        }
    }

    @RequestMapping(value = "/video", method = RequestMethod.GET)
    public void video(
            @RequestParam(value = "folderHash") String folderHash,
            @RequestParam(value = "fileHash") String fileHash,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            FolderDataDO folderDataDO = folderDataCache.get(folderHash);

            FileDataDO fileDataDO = null;
            List<FileDataDO> data = folderDataDO.getData();
            for (FileDataDO item : data) {
                if (DigestUtils.md5Hex(item.getPath()).equals(fileHash)) {
                    fileDataDO = item;
                    break;
                }
            }
            if (fileDataDO == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
                return;
            }
            File file = new File(fileDataDO.getPath());
            if (file.exists()) {
                request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, fileDataDO.getPath());
                nonStaticResourceHttpRequestHandler.handleRequest(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            }
        } catch (Exception e) {

        }
    }
}
