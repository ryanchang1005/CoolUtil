package idv.haojun.objectboxdemo;

import java.util.Comparator;
import java.util.List;

import io.objectbox.Box;

public class AuthorDao {

    public AuthorDao() {

    }

    public Box<Author> getBox() {
        return App.getInstance().getBoxStore().boxFor(Author.class);
    }

    public List<Author> getAuthors() {
        return getBox().query().sort(new Comparator<Author>() {
            @Override
            public int compare(Author o1, Author o2) {
                return o1.id < o2.id ? 1 : -1;
            }
        }).build().find();
    }

    public Author getAuthor(long id) {
        return getBox().get(id);
    }

    public long putAuthor(Author author) {
        return getBox().put(author);
    }

    public void removeAuthor(long id) {
        getBox().remove(id);
    }
}
