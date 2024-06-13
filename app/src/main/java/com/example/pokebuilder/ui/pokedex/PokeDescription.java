package com.example.pokebuilder.ui.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.pokebuilder.R;
import com.example.pokebuilder.UrlSingleton;
import com.example.pokebuilder.databinding.ActivityPokeDescriptionBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PokeDescription extends AppCompatActivity {
    private OkHttpClient client;
    private String name, flavor, imgUrl;
    private String[] type = new String[2];
    private Integer hp, attack, defense, spAtt, spDef, speed;
    private ArrayList<PokeInfo> ability = new ArrayList<>(), move = new ArrayList<>();
    private ActivityPokeDescriptionBinding binding;
    RecyclerView recyclerViewAbilities, recyclerViewMoves, recyclerViewBaseStats;
    BaseStatsAdapter baseStatsAdapter;
    AbilitiesAdapter abilitiesAdapter;
    MovesAdapter movesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build .VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy .Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_poke_description);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String url = "http://" + UrlSingleton.getInstance().url + ":8080/pokedex/" + String.valueOf(id);
        String response = makeGetRequest(url);
        parseJSON(response);
        setTitle(name);
        binding = ActivityPokeDescriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Picasso.get().load(imgUrl).into(binding.pokemonImage);
        binding.flavorText.setText(flavor);
        binding.pokemonName.setText(name);
        binding.type1Text.setText(type[0]);
        if(type[1].length() > 0) {
            binding.type2Text.setText(type[1]);
            binding.type2Text.setVisibility(View.VISIBLE);
        }
        ViewPager viewPager = findViewById(R.id.view_pager);
        recyclerViewAbilities = createRecyclerView();
        recyclerViewAbilities.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMoves = createRecyclerView();
        recyclerViewMoves.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBaseStats = createRecyclerView();
        recyclerViewBaseStats.setLayoutManager(new LinearLayoutManager(this));
        baseStatsAdapter = new BaseStatsAdapter(this, generateBaseStats());
        recyclerViewBaseStats.setAdapter(baseStatsAdapter);

        abilitiesAdapter = new AbilitiesAdapter(this, ability);
        recyclerViewAbilities.setAdapter(abilitiesAdapter);

        movesAdapter = new MovesAdapter(this, move);
        recyclerViewMoves.setAdapter(movesAdapter);
        ArrayList<RecyclerView> recyclerViews = new ArrayList<>();
        recyclerViews.add(recyclerViewBaseStats);
        recyclerViews.add(recyclerViewAbilities);
        recyclerViews.add(recyclerViewMoves);
        ViewPagerAdapter adapter = new ViewPagerAdapter(recyclerViews);
        viewPager.setAdapter(adapter);
    }
    private ArrayList generateBaseStats() {
        ArrayList baseStatsList = new ArrayList<BaseStat>();
        baseStatsList.add(new BaseStat("HP", hp));
        baseStatsList.add(new BaseStat("Attack", attack));
        baseStatsList.add(new BaseStat("Defense", defense));
        baseStatsList.add(new BaseStat("Special Attack", spAtt));
        baseStatsList.add(new BaseStat("Special Defense", spDef));
        baseStatsList.add(new BaseStat("Speed", speed));
        return baseStatsList;
    }

    private RecyclerView createRecyclerView() {
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        ));
        return recyclerView;
    }

    private String makeGetRequest(String url) {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("no response fuck");
            return "";
        }
    }
    private void parseJSON(String jsonText) {
        try {
            JSONObject json = new JSONObject(jsonText);
            JSONObject data = json.getJSONObject("pokemon");

            name = data.getString("name");
            flavor = data.getString("flavor");
            fixFlavor();
            imgUrl = data.getString("image");
            hp = data.getInt("hp");
            attack = data.getInt("attack");
            defense = data.getInt("defense");
            spAtt = data.getInt("spAtt");
            spDef = data.getInt("spDef");
            speed = data.getInt("speed");

            JSONArray types = data.getJSONArray("types");
            type[0] = type[1] = "";
            for(int j = 0; j < types.length(); j++) {
                type[j] = types.getJSONObject(j).getString("name");
            }

            JSONArray abilities = data.getJSONArray("abilities");
            for(int j = 0; j < abilities.length(); j++) {
                if(abilities.isNull(j)) continue;
                ability.add(new PokeInfo(abilities.getJSONObject(j).getString("name"), abilities.getJSONObject(j).getString("effect")));
            }
            JSONArray moves = data.getJSONArray("moves");
            for(int j = 0; j < moves.length(); j++) {
                if(moves.isNull(j)) continue;
                move.add(new PokeInfo(moves.getJSONObject(j).getString("name"), moves.getJSONObject(j).getString("effect")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void fixFlavor() {
        String newFlavor = new String();
        for(int i = 0; i < flavor.length(); i++) {
            if(flavor.charAt(i) == '\n' || flavor.charAt(i) == '\f' || flavor.charAt(i) == '\t' || flavor.charAt(i) == '\b') {
                newFlavor += " ";
            } else {
                newFlavor += flavor.charAt(i);
            }
        }
        newFlavor += '\n';
        flavor = newFlavor;
    }
}