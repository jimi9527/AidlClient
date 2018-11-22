// BookManager.aidl
package com.example.aidldemo;
import com.example.aidldemo.Book;

// Declare any non-default types here with import statements

interface BookManager {

    List<Book> getBooks();

    void addBook(in Book book);
}
