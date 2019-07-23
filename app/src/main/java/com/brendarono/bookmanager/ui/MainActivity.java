package com.brendarono.bookmanager.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brendarono.bookmanager.R;
import com.brendarono.bookmanager.data.FirebaseManager;
import com.brendarono.bookmanager.data.IFirebaseManager;
import com.brendarono.bookmanager.data.model.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private RecyclerView list_rv;
    private FloatingActionButton add_fab;

    private SwipeRefreshLayout swipeContainer;

    private FirebaseManager firebaseManager;
    private ArrayList<Book> bookArrayList;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();

        firebaseManager = new FirebaseManager();

    }



    @Override
    protected void onStart() {
        super.onStart();

        firebaseManager.loadList(new IFirebaseManager.loadListCallBack() {
            @Override
            public void onLoad(ArrayList<Book> bookArrayList) {
                loadList(bookArrayList);
                Log.i("BookManager", "onLoad");

                showMessage("List updated!");

                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(String msg) {
                showMessage("Firebase error!");
                Log.i("BookManager", "onFailure");

                bookArrayList = new ArrayList<>();
            }
        });
    }
    private void loadList(ArrayList<Book> bookArrayList){
        BookAdapter bookAdapter = new BookAdapter(getApplicationContext(), bookArrayList);
        list_rv.setAdapter(bookAdapter);

        bookAdapter.notifyDataSetChanged();
    }


    private void initView() {
        list_rv = findViewById(R.id.list_rv);
        list_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        add_fab = findViewById(R.id.add_fab);


        swipeContainer = findViewById(R.id.swiperefresh);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onStart();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initListener() {
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Book book = (Book) data.getExtras().getSerializable("Book");

                firebaseManager.addBook(book, new IFirebaseManager.addBookCallBack() {
                    @Override
                    public void onAdded() {
                        showMessage("Added!");
                    }

                    @Override
                    public void onFailure(String msg) {
                        showMessage("Firebase error!");
                    }
                });
            }
        }
    }

    private void showMessage(final String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_action) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Sort");
            String[] types = {"by date", "by price", "by name"};
            b.setItems(types, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    switch (which) {
                        case 0:
                            sortByDate();
                            break;
                        case 1:
                            sortByPrice();
                            break;
                        case 2:
                            sortByName();
                            break;
                    }
                }});
            b.show();
        }

        if(item.getItemId()==R.id.search_action){
            final EditText editText = new EditText(this);

            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Enter name:");
            b.setView(editText);
            b.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String name = String.valueOf(editText.getText());
                    search(name);
                }
            });
            b.setNegativeButton("Cancel", null)
                    .create();
            b.show();
        }

        if (item.getItemId() == R.id.btn_logout) {
            final EditText editText = new EditText(this);
            //final Button logoutBtn = findViewById(R.id.btn_logout);

            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(
                            new Intent(MainActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    );
                    finish();
                }
            });

        }

        return true;
    }

    private void search(String name) {
        firebaseManager.searchBook(name, new IFirebaseManager.searchBookCallBack() {

            @Override
            public void onFind(ArrayList<Book> bookArrayList) {
                loadList(bookArrayList);
                showMessage("Find!");
            }

            @Override
            public void onFailure(String msg) {
                showMessage("Firebase error");
            }
        });
    }

    private void sortByName() {
        firebaseManager.sortListByName(new IFirebaseManager.sortListByNameCallBack() {
            @Override
            public void onSorted(ArrayList<Book> bookArrayList) {
                loadList(bookArrayList);
                showMessage("Sorted!");
            }

            @Override
            public void onFailure(String msg) {
                showMessage("Firebase error!");
            }
        });
    }

    private void sortByPrice() {
        firebaseManager.sortListByPrice(new IFirebaseManager.sortListByPriceCallBack() {
            @Override
            public void onSorted(ArrayList<Book> bookArrayList) {
                loadList(bookArrayList);
                showMessage("Sorted!");
            }

            @Override
            public void onFailure(String msg) {
                showMessage("Firebase error!");
            }
        });
    }

    private void sortByDate() {
        firebaseManager.loadList(new IFirebaseManager.loadListCallBack() {
            @Override
            public void onLoad(ArrayList<Book> bookArrayList) {
                loadList(bookArrayList);
                showMessage("Sorted!");
            }

            @Override
            public void onFailure(String msg) {
                showMessage("Firebase error!");
            }
        });
    }
}
