package com.wtk.xiaicodemother.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDeleteRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
}
