package com.mamalimomen.base.domains;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Transient
    private static final long serialVersionUID = -4281565028591599756L;

    @Transient
    private static Long count = 20L;

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private Long id;

    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted = false;

    public BaseEntity(){
        this.id = count;
        count++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
