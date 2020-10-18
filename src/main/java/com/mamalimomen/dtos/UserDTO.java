package com.mamalimomen.dtos;

import com.mamalimomen.base.controllers.utilities.InValidDataException;
import com.mamalimomen.base.controllers.utilities.SecurityManager;

import java.io.Serializable;

public final class UserDTO implements Serializable {

    private static final long serialVersionUID = 7514504754105592800L;

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String aboutMe;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws InValidDataException {
        if (!firstName.matches("(\\w\\s?){3,}")) {
            throw new InValidDataException("FirstName");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws InValidDataException {
        if (!lastName.matches("(\\w\\s?){3,}")) {
            throw new InValidDataException("LastName");
        }
        this.lastName = lastName;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws InValidDataException {
        if (!username.matches("^(?!.*\\.\\.)(?!.*\\.$)[^\\W][\\w.]{0,29}$")) {
            throw new InValidDataException("Password");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setAndHashPassword(String password) throws InValidDataException {
        if (!password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$")) {
            throw new InValidDataException("Password");
        }
        this.setPassword(SecurityManager.getPasswordHash(password));
    }

    public void setPassword(String hashedPassword) {
        this.password = hashedPassword;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    @Override
    public String toString() {
        return String.format("%s%n%s%n%s%n", getUsername(), getFullName(), getAboutMe());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserDTO user = (UserDTO) obj;
        return this.hashCode() == user.hashCode();
    }

    public UserDTO copy(){
        UserDTO copyUser = new UserDTO();
        try {
            copyUser.setFirstName(this.getFirstName());
            copyUser.setLastName(this.getLastName());
            copyUser.setAboutMe(this.getAboutMe());
            copyUser.setUsername(this.getUsername());
            copyUser.setPassword(this.getPassword());
        } catch (InValidDataException e) {
            e.printStackTrace();
        }
        return copyUser;
    }
}
