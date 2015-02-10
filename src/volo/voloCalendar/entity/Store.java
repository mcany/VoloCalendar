package volo.voloCalendar.entity;

import javax.persistence.*;

/**
 * Created by Emin Guliyev on 25/01/2015.
 */
@Entity
@Table(name = "\"Store\"")
public class Store {
    private String id;
    private String data;

    public Store() {
    }

    public Store(String id, String data) {
        this.id = id;
        this.data = data;
    }

    @Id
    @Column(name = "\"id\"", nullable = true, insertable = true, updatable = true, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "\"data\"", nullable = true, insertable = true, updatable = true, length = 3000)
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Store store = (Store) o;

        if (id != null ? !id.equals(store.id) : store.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        return result;
    }
}
