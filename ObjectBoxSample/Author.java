package idv.haojun.objectboxdemo;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Author {

    @Id
    public long id;

    public String name;

}
