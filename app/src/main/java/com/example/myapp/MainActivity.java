package com.example.myapp;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int counter = 0;
    public static final String EXTRA_TEXT="com.example.myapp.example.EXTRA_TEXT";
    public static final String EXTRA_NUMBER = "com.example.myapp.example.EXTRA_NUMBER";

    public void changeToCarActivity(String clickedCarID){
        Intent carIntent = new Intent(this, CarActivity.class);
        carIntent.putExtra(EXTRA_TEXT, clickedCarID);
        startActivity(carIntent);
    }
    public LinearLayout  createCarTextView(Car car){

        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        LinearLayout LLH = new LinearLayout(this);
        LLH.setOrientation(LinearLayout.HORIZONTAL);
        LLH.setLayoutParams(linearParams);

        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        //Log.d("textView", car.getCustom_name());
        txtParams.setMargins(0,25,0, 25);
        TextView textView = new TextView(MainActivity.this);
        textView.setText(car.getCustom_name());
        textView.setTextColor(getColor(R.color.mainBlue));
        textView.setTextSize(24);
        textView.setPadding(20, 0, 20, 0);
        textView.setLayoutParams(txtParams);
        LLH.addView(textView);

        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        ImageView img = new ImageView(this);
        if (car.isAvailable())
            img.setImageResource(R.drawable.car_available_true);
        else
            img.setImageResource(R.drawable.car_available_false);
        img.setLayoutParams(imgParams);
        LLH.addView(img);


        return LLH;
    }


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String text = intent.getStringExtra(LoginActivity.EXTRA_TEXT);
        //Toast.makeText(getApplicationContext(),text , Toast.LENGTH_LONG).show();

        String userPhoneNumber = intent.getStringExtra(MainActivity.EXTRA_TEXT);

        Gson gson = new Gson();

        /*
        File file = new File(getExternalFilesDir(null), "cars.json");
        String jsonString = null;

        try {
            FileInputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (jsonString != null) {
            try {
                JSONObject json = new JSONObject(jsonString);
                // do something with the JSON object
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Type listCarType = new TypeToken<List<Car>>() {}.getType();
        List<Car> cars = gson.fromJson(jsonString, listCarType);*/

        String carsJsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "cars.json");
        Type listCarType = new TypeToken<List<Car>>() {
        }.getType();
        List<Car> cars = gson.fromJson(carsJsonFileString, listCarType);

        String usersJsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "users.json");
        Type listUserType = new TypeToken<List<User>>() {}.getType();
        List<User> users = gson.fromJson(usersJsonFileString, listUserType);
        User currentUser = null;
        for (User u : users) {
            if (u.getPhone().equals(userPhoneNumber)){
                currentUser = u;
            }
        }
        final TextView userNameText =  findViewById(R.id.userNameText);
        userNameText.setText(currentUser.getUsername());

        File file = new File(getExternalFilesDir(null), "cars.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
                Log.d("C ", "Created");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            Log.d("E ", "Existed");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(gson.toJson(cars));
            Log.d(":D ", "saved");
            Log.d("json",gson.toJson(cars));
        } catch (IOException e) {
            e.printStackTrace();
        }


        int[] arr = {R.id.ownedCars, R.id.adminedCars, R.id.usedCars};
        Log.d("file", getExternalFilesDir(null).getAbsolutePath());
        for (int i = 0; i<3; i++){
            for(Integer car : currentUser.getCars().get(i)){
                for(Car c : cars){
                    if (c.getId().equals(car)){
                        final LinearLayout ownerLinear = findViewById(arr[i]);
                        LinearLayout LLH = createCarTextView(c);
                        LLH.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeToCarActivity(c.getId().toString());
                            }
                        });
                        ownerLinear.addView(LLH);
                    }
                }
            }
        }
    }

}