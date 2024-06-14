package com.example.pokebuilder.ui.pokedex;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokebuilder.MainActivity;
import com.example.pokebuilder.R;
import com.example.pokebuilder.UrlSingleton;
import com.example.pokebuilder.databinding.FragmentPokedexBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PokedexFragment extends Fragment {
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<PokeDescriptor> pokemonList = new ArrayList<PokeDescriptor>();
    private OkHttpClient client;
    private FragmentPokedexBinding binding;
    SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (android.os.Build .VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy .Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        binding = FragmentPokedexBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        getActivity().setTitle("Pokédex");
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //pokemonList.add(new PokeDescriptor("buba", "grass", "poison", "https://reqres.in/img/faces/6-image.jpg"));
        //pokemonList.add(new PokeDescriptor("hamilton", "gata", "poison", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMMjDtV8HQp1QkzGkQhjzOFgbyr7dU8vBIog&s"));
        String url = "http://" + UrlSingleton.getInstance().url + "/pokedex";
        System.out.println(url);
        String response = makeGetRequest(url);
        parseJSON(response);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        System.out.println(pokemonList);
        adapter = new Adapter(getActivity(), pokemonList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        searchView = getActivity().findViewById(R.id.pokeSearch);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String id = adapter.getItem(position).getPokeid();
                        id = id.substring(1);
                        int pokeid = Integer.parseInt(id);
                        Intent intent = new Intent(getContext(), PokeDescription.class);
                        intent.putExtra("id", pokeid);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        return;
                    }
                })
        );
    }

    private void filterList(String newText) {
        ArrayList<PokeDescriptor> filteredList = new ArrayList<>();
        for(PokeDescriptor poke : pokemonList) {
            if(poke.getName().toLowerCase().contains(newText)) {
                filteredList.add(poke);
            }
        }
        if(filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No Pokémon Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setFilteredList(filteredList);
        }
    }

    private String makeGetRequest(String url) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES);
        client = builder.build();
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
            JSONArray data = json.getJSONArray("pokedex");
            for(int i = 0; i < data.length(); i++) {
                JSONObject row = data.getJSONObject(i);
                String name = row.getString("name");
                String imgUrl = row.getString("image");
                String pokeid = String.valueOf(row.getInt("id"));
                while(pokeid.length() < 5) pokeid = "0" + pokeid;
                pokeid = "#" + pokeid;
                JSONArray types = row.getJSONArray("types");
                String type[] = new String[2];
                type[0] = type[1] = "";
                for(int j = 0; j < types.length(); j++) {
                    type[j] = types.getString(j);
                }
                int index = pokemonList.size();
                pokemonList.add(index, new PokeDescriptor(name, type[0], type[1], imgUrl, pokeid));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}