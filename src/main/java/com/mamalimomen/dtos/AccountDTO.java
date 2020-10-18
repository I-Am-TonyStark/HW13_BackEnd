package com.mamalimomen.dtos;

import com.mamalimomen.base.dtos.BaseDTO;

public final class AccountDTO extends BaseDTO implements Comparable<AccountDTO> {

    private static final long serialVersionUID = -1541995039780498705L;

    private UserDTO user;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccountDTO account = (AccountDTO) obj;
        return this.hashCode() == account.hashCode();
    }

    @Override
    public int hashCode() {
        return this.getId().intValue();
    }

    @Override
    public String toString() {
        return String.format("%s%n", getUser());
    }

    @Override
    public int compareTo(AccountDTO a) {
        return this.getId().compareTo(a.getId());
    }
}
