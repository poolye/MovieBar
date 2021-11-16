package com.tec666.moviebar.database.dos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author longge93
 */
@Data
public class FolderDataDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String path;

    private String hashPath;

    private Integer movieNum;

    private List<String> thumbs;

    @JsonIgnore
    private List<FileDataDO> data;

}
