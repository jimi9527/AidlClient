package com.example.daxiong;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.aidldemo.Book;
import com.example.aidldemo.BookManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private BookManager bookManager = null;
    private List<Book> mBooks;
    private boolean isTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void target(View view) {
        Log.d(TAG, "isTarget:" +isTarget);
        if(!isTarget){
            attemptoService();
        }

    }

    public void disconnect(View view){
        attemptDisConnect();
    }

    public void add(View view){
        if(isTarget && bookManager != null){
            Book book = new Book();
            book.setName("射雕英雄传");
            book.setPrice(13);
            try {
                bookManager.addBook(book);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.d(TAG, "add fail");
            }
        }else {
            Log.d(TAG, "isTarget is false or bookmanager is null");
        }
    }

    private void attemptoService() {
        Intent intent = new Intent();
        intent.setAction("com.example.aidldemo");
        intent.setPackage("com.example.aidldemo");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void attemptDisConnect(){
        Log.d(TAG, "isTarget:" +isTarget);
        if(isTarget){
            unbindService(mServiceConnection);
            isTarget = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected");
            isTarget = true;
            bookManager = BookManager.Stub.asInterface(iBinder);
            if (bookManager != null) {
                try {
                    mBooks = bookManager.getBooks();
                    Log.d(TAG, "mBooks:" + mBooks.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected");
            isTarget = false;
        }
    };
}
