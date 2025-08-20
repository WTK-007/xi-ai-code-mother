package com.wtk.xiaicodemother.model.dto.app;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AppDeleteRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * app Id
     */
    private Long id;
}
