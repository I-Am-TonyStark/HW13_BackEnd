package com.mamalimomen.dtos;

import com.mamalimomen.base.dtos.BaseDTO;

import java.util.Date;

public class PostSearchDTO extends BaseDTO {

    private static final long serialVersionUID = 1033764190288685994L;

    private String text;
    private Date fromDate;
    private Date tillDate;
    private String accountUsername;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getTillDate() {
        return tillDate;
    }

    public void setTillDate(Date tillDate) {
        this.tillDate = tillDate;
    }

    public String getAccountUsername() {
        return accountUsername;
    }

    public void setAccountUsername(String accountUsername) {
        this.accountUsername = accountUsername;
    }
}
