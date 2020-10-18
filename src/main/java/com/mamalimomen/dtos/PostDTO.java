package com.mamalimomen.dtos;

import com.mamalimomen.base.controllers.utilities.InValidDataException;
import com.mamalimomen.base.dtos.BaseDTO;

import java.util.Date;

public final class PostDTO extends BaseDTO implements Comparable<PostDTO> {

    private static final long serialVersionUID = 3156033230431579881L;

    private String text;
    private Date insertDate;
    private AccountDTO account;

    public String getText() {
        return text;
    }

    public void setText(String text) throws InValidDataException {
        if (text.length() < 20) {
            throw new InValidDataException("Text");
        }
        this.text = text;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PostDTO dto = (PostDTO) obj;
        return this.hashCode() == dto.hashCode();
    }

    @Override
    public int hashCode() {
        return this.getId().intValue();
    }

    @Override
    public String toString() {
        return String.format("%s%n%s%n", getInsertDate(), getText());
    }

    public void printCompleteInformation() {
        System.out.printf("%s%n%s%n%s%n", getAccount(), getInsertDate(), getText());
    }

    @Override
    public int compareTo(PostDTO p) {
        return this.getInsertDate().compareTo(p.getInsertDate());
    }
}
