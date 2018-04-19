package idv.haojun.objectboxdemo;

import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

public class Book {
    @Id
    public long id;
    public String title;
    public ToOne<Author> author;
}
