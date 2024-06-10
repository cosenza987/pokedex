package com.example.pokebuilder.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonToken;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pokebuilder.R;
import com.example.pokebuilder.databinding.ActivityLoginBinding;
import com.example.pokebuilder.databinding.ActivitySignupMenuBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupMenu extends AppCompatActivity {

    private ActivitySignupMenuBinding binding;

    private OkHttpClient client;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        binding = ActivitySignupMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final EditText usernameEditText = binding.registerUsername;
        final EditText emailEditText = binding.registerEmail;
        final EditText passwordEditText = binding.registerPassword;
        final Button registerButton = binding.registerRegister;

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                RequestBody formBody = new FormBody.Builder()
                        .add("username", username)
                        .add("email", email)
                        .add("password", password)
                        .build();

                Response response;
                try {
                    response = makePostRequest("http://localhost:8080/account/register", formBody);
                    if(response.message().equals("400")) {
                        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        getSession(response.body().string(), editor);
                        editor.putString("email", emailEditText.getText().toString());
                        editor.putString("password", passwordEditText.getText().toString());
                        editor.commit();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid credentials!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    Toast.makeText(getApplicationContext(), "Server Offline!", Toast.LENGTH_LONG).show();
                    sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", usernameEditText.getText().toString());
                    editor.putString("email", emailEditText.getText().toString());
                    editor.putString("password", passwordEditText.getText().toString());
                    editor.commit();
                    finish();
                }
            }

        });

    }

    private void getSession(String jsonText, SharedPreferences.Editor editor) {
        try {
            JSONObject json = new JSONObject(jsonText);
            String session = json.getString("session");
            String username = json.getString("username");
            editor.putString("username", username);
            editor.putString("session", session);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    private Response makePostRequest(String url, RequestBody formBody) {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            return client.newCall(request).execute();

            //if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}