package com.example.pokebuilder.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pokebuilder.R;
import com.example.pokebuilder.databinding.ActivityTeamBuilderBinding;
import com.example.pokebuilder.ui.ui.login.LoginActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class TeamBuilder extends AppCompatActivity {

    private ActivityTeamBuilderBinding binding;

    private OkHttpClient client;

    private SharedPreferences sharedPreferences;

    private Bundle team = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_team_builder);
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

        binding = ActivityTeamBuilderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final EditText teamNameEditText = binding.teamBuilderTeamName;
        final ArrayList<ImageButton> teamBuilderSlotButtons = new ArrayList<>(Arrays.asList(
                binding.teamBuilderButtonSlot1,
                binding.teamBuilderButtonSlot2,
                binding.teamBuilderButtonSlot3,
                binding.teamBuilderButtonSlot4,
                binding.teamBuilderButtonSlot5,
                binding.teamBuilderButtonSlot6
        ));
        final Button teamBuilderSaveButton = binding.teamBuilderButtonSaveTeam;
        team.putString("name", "");
        for(int i=1;i<=6;i++){
            team.putInt("poke" + i, -1);
            for(int j=1;j<=4;j++){
                team.putInt("move"+i+j, -1);
            }
        }

        teamBuilderSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean fullTeam = true;
                for(int i=1;i<=6;i++) {
                    if (team.getInt("poke" + i) == -1) fullTeam = false;
                }
                if (fullTeam && teamNameEditText.getText().toString() != ""){
                    FormBody.Builder formBodyBuilder = new FormBody.Builder()
                            .add("username",  sharedPreferences.getString("username", ""))
                            .add("session", sharedPreferences.getString("session", ""))
                            .add("teamName", teamNameEditText.getText().toString());

                    for (int i=1;i<=6;i++){
                        formBodyBuilder.add("poke"+i, String.valueOf(team.getInt("poke"+i)));
                        for (int j=1;j<=4;j++){
                            formBodyBuilder.add("move"+i+j, String.valueOf(team.getInt("move"+i+j)));
                        }
                    }
                    FormBody formBody = formBodyBuilder.build();
                    Response response = makePostRequest("http://lolcraft.servebeer.com:8080/ImemonAPI_war_exploded/teams/add", formBody);
                    if (response.code() == 200) {
                        Toast.makeText(TeamBuilder.this, "Team successfully added!",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else Toast.makeText(TeamBuilder.this, "Server error",Toast.LENGTH_LONG).show();
                }
                else if (!fullTeam){
                    Toast.makeText(TeamBuilder.this, "Select 6 Pokémon",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(TeamBuilder.this, "Name your team",Toast.LENGTH_LONG).show();
                }
            }
        });

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int slot = data.getIntExtra("slot", -1);
                        team.putInt("poke"+slot, data.getIntExtra("selectedpoke", -1));
                        for (int i=1;i<=4;i++){
                            team.putInt("move"+slot+i, data.getIntExtra("selectedmove"+i, -1));
                        }
                        Picasso.get().load(data.getStringExtra("imgUrl")).into(teamBuilderSlotButtons.get(slot-1));
                        teamBuilderSlotButtons.get(slot-1).setScaleType(ImageButton.ScaleType.FIT_CENTER);
                        teamBuilderSlotButtons.get(slot-1).setPadding(10,10,10,10);
                        teamBuilderSlotButtons.get(slot-1).setAdjustViewBounds(true);
                        teamBuilderSlotButtons.get(slot-1).setEnabled(false);
                    }
                }
            }
        );
        for (int i = 0; i < 6; i++) {
            int finalI = i;
            teamBuilderSlotButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toSlot = new Intent(getApplicationContext(), TeamSlot.class);
                    toSlot.putExtra("slot", finalI+1);
                    someActivityResultLauncher.launch(toSlot);
                }
            });
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