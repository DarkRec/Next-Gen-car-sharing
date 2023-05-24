package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CarActivity extends AppCompatActivity {

    Button addUser;
    int carID;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    @SuppressLint("UseCompatLoadingForDrawables")
    public TextView createUserTextView(String text){
        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        txtParams.setMargins(0,25,0, 25);
        TextView textView = new TextView(CarActivity.this);
        textView.setText(text);
        textView.setTextColor(getColor(R.color.mainBlue));
        textView.setTextSize(24);
        textView.setPadding(20, 0, 20, 0);
        textView.setLayoutParams(txtParams);
        textView.setBackground(getDrawable(R.drawable.rounded_corner));
        return textView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now write to external storage
            } else {
                // Permission denied, you cannot write to external storage
            }
        }
    }

    public void addingUser(View popupView, PopupWindow popupWindow, TextView name, TextView phone) throws IOException {
        RadioGroup radioGroup = popupView.findViewById(R.id.radioGroup);
        RadioButton radioButton = popupView.findViewById(radioGroup.getCheckedRadioButtonId());

        boolean successfullyAdded = false;
            Gson gson = new Gson();

            String carsJsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "cars.json");
            Type listCarType = new TypeToken<List<Car>>() {
            }.getType();
            List<Car> cars = gson.fromJson(carsJsonFileString, listCarType);


            String usersJsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "users.json");
            Type listUserType = new TypeToken<List<User>>() {
            }.getType();
            List<User> users = gson.fromJson(usersJsonFileString, listUserType);

            for (Car c : cars) {
                if (c.getId().equals(carID)) {
                    ArrayList<ArrayList<Car.BasicUser>> auta = c.getUsers();
                    for (User u : users) {
                        if (u.getPhone().equals(phone.getText().toString())) {

                            if (radioButton.getTag().toString().equals("admin"))
                                auta.get(0).add(new Car.BasicUser(name.getText().toString(), Integer.parseInt(Integer.toString(u.getId()))));
                            else if (radioButton.getTag().toString().equals("user")) {
                                auta.get(1).add(new Car.BasicUser(name.getText().toString(), Integer.parseInt(Integer.toString(u.getId()))));
                            }
                            c.setUsers(auta);
                            successfullyAdded = true;
                        }
                    }
                }
            }
            for (Car c : cars){
                Log.i("car ", c.toString());
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL_STORAGE);
            }
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

        if (successfullyAdded){
            popupWindow.dismiss();
        }
        else{
            Toast.makeText(getApplicationContext(),"Unable to add user",Toast.LENGTH_LONG).show();
        }
    }


    public void popUP(View v){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pop_up, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        Button addButton = popupView.findViewById(R.id.addUserSubmit);
        addButton.setOnClickListener(new View.OnClickListener() {
            final TextView name =  popupView.findViewById(R.id.newUserNameTxt);
            final TextView phone =  popupView.findViewById(R.id.TextPhone);
            @Override
            public void onClick(View v) {

                if (!name.getText().toString().equals("") && !phone.getText().toString().equals("")) {
                    Log.i("check ", "not empty");
                    try {
                        addingUser(popupView, popupWindow, name, phone);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please fill in all fields",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        addUser = findViewById(R.id.addUserButton);

        Intent intent = getIntent();
        carID = Integer.parseInt(intent.getStringExtra(MainActivity.EXTRA_TEXT));

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


        Gson gson = new Gson();
        Type listCarType = new TypeToken<List<Car>>() { }.getType();
        List<Car> cars = gson.fromJson(jsonString, listCarType);

        TextView carTextView = findViewById(R.id.carNameText);
        int[] arr = {R.id.admins, R.id.users};

        for (Car c : cars){
            if (c.getId().equals(carID)){
                carTextView.setText(c.getCustom_name());
                final LinearLayout ownerLinear = findViewById(R.id.owner);
                TextView ownerTextView = createUserTextView(c.getOwner().getName());
                ownerLinear.addView(ownerTextView);

                for (int i = 0; i < 2;i++){
                    final LinearLayout userLinear = findViewById(arr[i]);
                    for (Car.BasicUser usr : c.getUsers().get(i)){
                        TextView userTextView = createUserTextView(usr.getName());
                        userLinear.addView(userTextView);
                    }
                }

                TextView colorText = findViewById(R.id.color);
                colorText.setText(c.getInfo().getColor());

                TextView brandText = findViewById(R.id.brand);
                brandText.setText(c.getInfo().getBrand());

                TextView modelText = findViewById(R.id.model);
                modelText.setText(c.getInfo().getModel());

                TextView plateText = findViewById(R.id.plate);
                plateText.setText(c.getInfo().getLicense_plate());
            }
        }

        Button button = findViewById(R.id.addUserButton);
        button.setOnClickListener(v -> popUP(v));
    }
}