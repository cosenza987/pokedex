package com.example.pokebuilder.ui.teams;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokebuilder.R;
import com.example.pokebuilder.databinding.ActivityTeamBuilderBinding;
import com.example.pokebuilder.databinding.ActivityTeamviewBinding;

import okhttp3.OkHttpClient;

public class TeamView extends AppCompatActivity {
    private ActivityTeamviewBinding binding;
    private OkHttpClient client;
    private SharedPreferences sharedPreferences;
    TeamViewAdapter adapter;
    private Team team;

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
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        binding = ActivityTeamviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        team = (Team) getIntent().getSerializableExtra("team");

        TextView teamViewTitle = binding.teamViewTeamName;
        teamViewTitle.setText(team.getName());

        RecyclerView recyclerView = binding.teamDescription;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TeamViewAdapter(this, team);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                binding.teamViewActivityLayout.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}
