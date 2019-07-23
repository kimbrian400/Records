package com.brendarono.bookmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.brendarono.bookmanager.R;
import com.brendarono.bookmanager.data.model.Book;
import com.google.firebase.database.annotations.Nullable;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    private EditText name_et;
    private EditText author_et;
    private EditText price_et;
    private EditText year_et;
    private EditText code_et;
    //private EditText count_et;

    private Button add_btn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initView();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText [] fields = {name_et, author_et, price_et, year_et, code_et, };

                if(validate(fields)){
                    Date date = new Date();
                    Timestamp timestamp  = new Timestamp(date.getTime());

                    Book book = new Book(
                            encodeString(timestamp.toString()),
                            name_et.getText().toString(),
                            author_et.getText().toString(),
                            price_et.getText().toString(),
                            year_et.getText().toString(),
                            code_et.getText().toString()
                    );


                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    intent.putExtra("Book", book);
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(AddActivity.this, "Empty fields!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void initView() {
        name_et = findViewById(R.id.name_et);
        author_et = findViewById(R.id.author_et);
        price_et = findViewById(R.id.price_et);
        year_et = findViewById(R.id.year_et);
        code_et = findViewById(R.id.code_et);
        //count_et = findViewById(R.id.count_et);

        add_btn = findViewById(R.id.add_btn);
    }

    private boolean validate(EditText[] fields) {
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            if (currentField.getText().toString().length() <= 0) {
                return false;
            }
        }
        return true;
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }
}
