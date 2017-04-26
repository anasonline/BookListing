package com.example.android.booklisting;

/**
 * Created by anas on 24.04.17.
 */

/**
 * A {@link Book} object contains information related to a single book.
 */

public class Book {
    /**
     * Book title
     **/
    private String mTitle;

    /**
     * Book author
     **/
    private String mAuthor;

    /**
     * Construct a new {@link Book} object.
     *
     * @param title is the book's title
     */

    public Book(String title, String author) {
        mTitle = title;
        mAuthor = author;
    }

    /**
     * Returns the title of a book
     */

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
