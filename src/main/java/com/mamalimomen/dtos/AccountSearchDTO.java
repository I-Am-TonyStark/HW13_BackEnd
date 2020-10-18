package com.mamalimomen.dtos;

import com.mamalimomen.base.dtos.BaseDTO;

public class AccountSearchDTO extends BaseDTO {

    private static final long serialVersionUID = -6558108268012383341L;

    private String username;
    private String ownerFirstName;
    private String ownerLastName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }
}
