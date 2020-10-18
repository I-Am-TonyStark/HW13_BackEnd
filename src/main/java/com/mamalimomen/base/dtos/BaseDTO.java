package com.mamalimomen.base.dtos;

import java.io.Serializable;

public abstract class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1131263775501712167L;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
