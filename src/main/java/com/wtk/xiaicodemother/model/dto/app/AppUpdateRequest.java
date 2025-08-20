package com.wtk.xiaicodemother.model.dto.app;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AppUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 应用名称
     */
    private String appName;

}
