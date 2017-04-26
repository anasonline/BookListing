package com.example.android.booklisting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anas on 26.04.17.
 */

public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static List<Book> fetchBookData() {
        List books = new ArrayList();
        books.add(new Book("Android for Beginners", "Anas Marrawi"));
        books.add(new Book("Damns up: the secrets to live", "Batman"));

        return books;
    }

}
