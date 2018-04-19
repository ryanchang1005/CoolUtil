package idv.haojun.objectboxdemo;

import java.util.List;

import io.objectbox.Box;

public class BookDao {

    public BookDao() {

    }

    public Box<Book> getBox() {
        return App.getInstance().getBoxStore().boxFor(Book.class);
    }

    public List<Book> getBooks() {
        return getBox().query().build().find();
    }

    public Book getBook(long id){
        return getBox().get(id);
    }

    public long putBook(Book book){
        return getBox().put(book);
    }

    public void removeBook(long id){
        getBox().remove(id);
    }
}
