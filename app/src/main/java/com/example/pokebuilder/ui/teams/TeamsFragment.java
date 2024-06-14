package com.example.pokebuilder.ui.teams;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokebuilder.PokemonFourMoves;
import com.example.pokebuilder.databinding.FragmentTeamsBinding;
import com.example.pokebuilder.move;
import com.example.pokebuilder.pokemon;
import com.example.pokebuilder.ui.TeamBuilder;
import com.example.pokebuilder.ui.TeamSlot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.Normalizer;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TeamsFragment extends Fragment implements TeamsFragmentRecyclerAdapter.ItemClickListener{

    TeamsFragmentRecyclerAdapter adapter;
    private FragmentTeamsBinding binding;
    private OkHttpClient client;
    private SharedPreferences sharedPreferences;
    ArrayList<Team> teams = new ArrayList<Team>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TeamsViewModel teamsViewModel =
                new ViewModelProvider(this).get(TeamsViewModel.class);

        if (android.os.Build .VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy .Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        binding = FragmentTeamsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        teams.clear();
        sharedPreferences = getContext().getSharedPreferences("UserInfo", MODE_PRIVATE);

        FormBody formBody = new FormBody.Builder()
                .add("username",  sharedPreferences.getString("username", ""))
                .add("session", sharedPreferences.getString("session", ""))
                .build();

        String responseTeams = makePostRequest("http://lolcraft.servebeer.com:8080/ImemonAPI_war_exploded/teams/retrieve", formBody);
        parseJSONTeams(responseTeams);
        RecyclerView recyclerView = binding.teamDisplay;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TeamsFragmentRecyclerAdapter(getContext(), teams);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                binding.teamFragmentLayout.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter.setClickListener(this);
    }
    @Override
    public void onItemClick(View view, int position) {
        Intent toTeamView = new Intent(getContext(), TeamView.class);
        toTeamView.putExtra("team", adapter.getItem(position));
        startActivity(toTeamView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String makeGetRequest(String url) {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();

            //if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private String makePostRequest(String url, RequestBody formBody) {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();

            //if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void parseJSONTeams(String jsonText) {
        try {
            JSONObject json = new JSONObject(jsonText);
            JSONArray data = json.getJSONArray("teams");
            for(int i = 0; i < data.length(); i++) {
                JSONObject row = data.getJSONObject(i);
                int id = row.getInt("id");
                String teamName = row.getString("teamName");
                ArrayList<PokemonFourMoves> pokemons = new ArrayList<PokemonFourMoves>();
                for(int j=1;j<=6;j++){
                    String pokeId = row.getString("poke"+j);
                    String responsePoke = makeGetRequest("http://lolcraft.servebeer.com:8080/ImemonAPI_war_exploded/pokedex/"+pokeId);
                    pokemon poke = parseJSONPokemon(responsePoke);
                    ArrayList<move> moves = new ArrayList<move>();
                    for(int k=1;k<=4;k++){
                        String moveId = row.getString("move"+j+k);
                        String responsemove = makeGetRequest("http://lolcraft.servebeer.com:8080/ImemonAPI_war_exploded/pokedex/moves/"+moveId);
                        move m = parseJSONMove(responsemove);
                        moves.add(m);
                    }
                    PokemonFourMoves pokeFourMoves = new PokemonFourMoves(poke, moves);
                    pokemons.add(pokeFourMoves);
                }
                Team team = new Team(id, teamName, pokemons);
                teams.add(team);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private pokemon parseJSONPokemon(String jsonText) {
        try {
            JSONObject json = new JSONObject(jsonText);
            JSONObject data = json.getJSONObject("pokemon");
            String name = data.getString("name");
            String imgUrl = data.getString("image");
            int id = data.getInt("id");
            return new pokemon(id, name, imgUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private move parseJSONMove(String jsonText) {
        try {
            JSONObject json = new JSONObject(jsonText);
            String name = json.getString("name");
            return new move(-1, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}