package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button loginButton,b2;
    EditText loginText,passwdText;
    TextView tx1;

    public static final String EXTRA_TEXT="com.example.myapp.example.EXTRA_TEXT";
    public static final String EXTRA_NUMBER = "com.example.myapp.example.EXTRA_NUMBER";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("loaded", "YAY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button)findViewById(R.id.loginButton);
        loginText = (EditText)findViewById(R.id.loginText);
        passwdText = (EditText)findViewById(R.id.passwordText);
        final String[] userName = new String[1];

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", loginText.getText().toString());
                String login = loginText.getText().toString();


                Gson gson = new Gson();
                String usersJsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "users.json");
                Type listUserType = new TypeToken<List<User>>() {}.getType();
                List<User> users = gson.fromJson(usersJsonFileString, listUserType);

                boolean logged = false;

                for (User u : users){
                    if (loginText.getText().toString().equals(u.getUsername()) && passwdText.getText().toString().equals("admin") ){
                        Toast.makeText(getApplicationContext(),"Successfully logged as "+u.getUsername(),Toast.LENGTH_LONG).show();
                        changeActivity(u.getPhone());
                        logged = true;
                    }
                }
                if (logged != true){
                    Toast.makeText(getApplicationContext(),"Wrong Login or Password" , Toast.LENGTH_LONG).show();
                }
            };
        });

    }
    public void changeActivity(String userName){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_TEXT, userName);
        startActivity(intent);
    }
}