package com.mamalimomen.domains;

import com.mamalimomen.base.controllers.utilities.InValidDataException;
import com.mamalimomen.base.domains.BaseEntity;
import com.mamalimomen.dtos.AccountDTO;
import com.mamalimomen.dtos.PostDTO;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_post", catalog = "HW13_One", schema = "HW13_One")
@NamedQueries({
        @NamedQuery(
                name = "Post.findAll",
                query = "SELECT p FROM Post p"),
        @NamedQuery(
                name = "Post.findManyByAccountUsername",
                query = "SELECT p FROM Post p JOIN p.account a WHERE a.user.username = ?1 AND a.deleted = FALSE ORDER BY p.insertDate ASC"),
        @NamedQuery(
                name = "Post.findFetchManyByAccountUsernameLike",
                query = "SELECT p FROM Post p JOIN FETCH p.account a WHERE a.user.username like ?1")
})
public class Post extends BaseEntity implements Comparable<Post> {

    @Transient
    private static final long serialVersionUID = 6446817660773091639L;

    @Column(name = "text", nullable = false, columnDefinition = "text")
    private String text;

    @Temporal(TemporalType.DATE)
    @Column(name = "insert_date", updatable = false, nullable = false)
    private Date insertDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_account", nullable = false, updatable = false)
    private Account account;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public int compareTo(Post p) {
        return this.getInsertDate().compareTo(p.getInsertDate());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Post post = (Post) obj;
        return this.hashCode() == post.hashCode();
    }

    @Override
    public int hashCode() {
        return this.getId().intValue();
    }

    public Post copyMeFrom(PostDTO postDTO) {
        Account account = new Account();
        account.setId(postDTO.getAccount().getId());

        this.setText(postDTO.getText());
        this.setInsertDate(postDTO.getInsertDate());
        this.setAccount(account);
        return this;
    }

    public PostDTO copyMeTo() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(this.getAccount().getId());

        PostDTO dto = new PostDTO();
        dto.setId(this.getId());
        dto.setAccount(accountDTO);
        dto.setInsertDate(this.getInsertDate());
        try {
            dto.setText(this.getText());
        } catch (InValidDataException e) {
            e.printStackTrace();
        }
        return dto;
    }
}
