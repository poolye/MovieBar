package com.tec666.moviebar.database.dos;

import lombok.Data;

import java.io.Serializable;

/**
 * @author longge93
 */
@Data
public class FileDataDO implements Serializable {

    private static final long serialVersionUID = 2L;

    private String path;

    private String hashPath;

    private String name;

    private String thumb;

    private String thumbGif;

    /**
     * 用户重命名--别名
     */
    private String rename;

}
