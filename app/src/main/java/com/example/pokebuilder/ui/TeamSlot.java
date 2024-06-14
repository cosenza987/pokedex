package com.example.pokebuilder.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pokebuilder.R;
import com.example.pokebuilder.databinding.ActivityTeamSlotBinding;
import com.example.pokebuilder.move;
import com.example.pokebuilder.pokemon;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TeamSlot extends AppCompatActivity {

    private ActivityTeamSlotBinding binding;
    Dialog dialogPoke, dialogMove;
    private OkHttpClient client;

    private SharedPreferences sharedPreferences;

    int pokeId = -1;
    ArrayList<Integer> moveIds = new ArrayList<>(Collections.nCopies(4, -1)) ;

    ArrayList<pokemon> pokemonList = new ArrayList<pokemon>();
    ArrayList<move> moveList = new ArrayList<move>();
    String imgUrl = new String();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build .VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy .Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
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

        binding = ActivityTeamSlotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final TextView teamSlotTitleTextView = binding.teamSlotTitle;
        final ImageView teamSlotImageImageView = binding.teamSlotImage;
        final ArrayList<TextView> teamSlotMoveTextViews = new ArrayList<>(Arrays.asList(
                binding.teamSlotMove1,
                binding.teamSlotMove2,
                binding.teamSlotMove3,
                binding.teamSlotMove4
        ));
        final Button teamSlotChooseButton = binding.teamSlotChoose;

        String responsePokedex = makeGetRequest("http://lolcraft.servebeer.com:8080/ImemonAPI_war_exploded/pokedex/simple");
        parseJSONPokemon(responsePokedex);

        teamSlotChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pokeId == -1) {
                    Toast.makeText(TeamSlot.this, "Choose a Pokémon", Toast.LENGTH_LONG).show();
                } else if (moveIds.get(0)==-1 || moveIds.get(1)==-1 || moveIds.get(2)==-1 || moveIds.get(3)==-1) {
                    Toast.makeText(TeamSlot.this, "Choose 4 moves", Toast.LENGTH_LONG).show();
                } else if (new HashSet<Integer>(moveIds).size() < 4) {
                    Toast.makeText(TeamSlot.this, "Choose 4 different moves", Toast.LENGTH_LONG).show();
                } else{
                    Intent intent = new Intent(getApplicationContext(), TeamBuilder.class);
                    intent.putExtra("selectedpoke", pokeId);
                    for (int i=1;i<=4;i++){
                        intent.putExtra("selectedmove"+i, moveIds.get(i-1));
                    }
                    intent.putExtra("imgUrl", imgUrl);
                    intent.putExtra("slot", getIntent().getIntExtra("slot", -1));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

        teamSlotTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPoke = new Dialog(TeamSlot.this);
                dialogPoke.setContentView(R.layout.dialog_searchable_spinner);
                dialogPoke.getWindow().setLayout(650,800);
                dialogPoke.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView textViewTitle =dialogPoke.findViewById(R.id.select_text);
                textViewTitle.setText("Select Pokémon");
                dialogPoke.show();
                EditText editText=dialogPoke.findViewById(R.id.edit_text);
                ListView listView=dialogPoke.findViewById(R.id.list_view);
                ArrayAdapter<pokemon> adapter = new ArrayAdapter(TeamSlot.this, android.R.layout.activity_list_item, android.R.id.text1, pokemonList) {

                    ArrayList<pokemon> filteredPokemonList = new ArrayList<pokemon>(pokemonList);
                    ArrayList<pokemon> originalPokemonList = new ArrayList<pokemon>(pokemonList);
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                        ImageView icon = (ImageView) view.findViewById(android.R.id.icon);

                        text1.setText(filteredPokemonList.get(position).getName());
                        Picasso.get().load(filteredPokemonList.get(position).getImage()).into(icon);
                        icon.setScaleType(ImageButton.ScaleType.FIT_CENTER);
                        icon.setPadding(10,10,10,10);
                        icon.setAdjustViewBounds(true);
                        icon.setEnabled(false);
                        return view;
                    }
                    public int getCount()
                    {
                        return filteredPokemonList.size();
                    }

                    //This should return a data object, not an int
                    public Object getItem(int position)
                    {
                        return filteredPokemonList.get(position);
                    }

                    @Override
                    public Filter getFilter()
                    {
                        return new Filter()
                        {
                            @Override
                            protected FilterResults performFiltering(CharSequence charSequence)
                            {
                                FilterResults results = new FilterResults();

                                if(charSequence == null || charSequence.length() == 0)
                                {
                                    results.values = originalPokemonList;
                                    results.count = originalPokemonList.size();
                                }
                                else
                                {
                                    ArrayList<pokemon> filterResultsData = new ArrayList<pokemon>();

                                    for(pokemon poke : originalPokemonList)
                                    {
                                        if(poke.getName().toUpperCase().startsWith(charSequence.toString().toUpperCase()))
                                        {
                                            filterResultsData.add(poke);
                                        }
                                    }

                                    results.values = filterResultsData;
                                    results.count = filterResultsData.size();
                                }

                                return results;
                            }

                            @Override
                            protected void publishResults(CharSequence charSequence, FilterResults results) {
                                filteredPokemonList = (ArrayList<pokemon>) results.values;
                                //originalPokemonList = pokemonList;
                                notifyDataSetChanged();
                            }

                        };
                    }
                };
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        teamSlotTitleTextView.setText(adapter.getItem(position).getName());
                        pokeId = adapter.getItem(position).getId();
                        imgUrl = adapter.getItem(position).getImage();
                        Picasso.get().load(imgUrl).into(teamSlotImageImageView);
                        String responseMoves = makeGetRequest("http://lolcraft.servebeer.com:8080/ImemonAPI_war_exploded/pokedex/movesByPokemon/" + pokeId);
                        parseJSONMoves(responseMoves);
                        dialogPoke.dismiss();
                    }
                });
            }
        });

        for(int i = 0; i < 4; i++) {
            int finalI = i;
            teamSlotMoveTextViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogMove = new Dialog(TeamSlot.this);
                    dialogMove.setContentView(R.layout.dialog_searchable_spinner);
                    dialogMove.getWindow().setLayout(650, 800);
                    dialogMove.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView textViewTitle =dialogMove.findViewById(R.id.select_text);
                    textViewTitle.setText("Select Move " + (finalI+1));
                    dialogMove.show();
                    EditText editText = dialogMove.findViewById(R.id.edit_text);
                    ListView listView = dialogMove.findViewById(R.id.list_view);
                    ArrayAdapter<move> adapter = new ArrayAdapter(TeamSlot.this, android.R.layout.simple_list_item_1, moveList) {
                        ArrayList<move> filteredMoveList = new ArrayList<move>(moveList);
                        ArrayList<move> originalMoveList = new ArrayList<move>(moveList);
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text1 = (TextView) view.findViewById(android.R.id.text1);

                            text1.setText(filteredMoveList.get(position).getName());
                            return view;
                        }
                        public int getCount()
                        {
                            return filteredMoveList.size();
                        }

                        //This should return a data object, not an int
                        public Object getItem(int position)
                        {
                            return filteredMoveList.get(position);
                        }
                        @Override
                        public Filter getFilter()
                        {
                            return new Filter()
                            {
                                @Override
                                protected FilterResults performFiltering(CharSequence charSequence)
                                {
                                    FilterResults results = new FilterResults();

                                    if(charSequence == null || charSequence.length() == 0)
                                    {
                                        results.values = originalMoveList;
                                        results.count = originalMoveList.size();
                                    }
                                    else
                                    {
                                        ArrayList<move> filterResultsData = new ArrayList<move>();

                                        for(move m : originalMoveList)
                                        {
                                            if(m.getName().toUpperCase().startsWith(charSequence.toString().toUpperCase()))
                                            {
                                                filterResultsData.add(m);
                                            }
                                        }

                                        results.values = filterResultsData;
                                        results.count = filterResultsData.size();
                                    }

                                    return results;
                                }

                                @Override
                                protected void publishResults(CharSequence charSequence, FilterResults results) {
                                    filteredMoveList = (ArrayList<move>) results.values;
                                    //originalPokemonList = pokemonList;
                                    notifyDataSetChanged();
                                }

                            };
                        }
                    };
                    listView.setAdapter(adapter);

                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            teamSlotMoveTextViews.get(finalI).setText(adapter.getItem(position).getName());
                            moveIds.set(finalI, adapter.getItem(position).getId());
                            dialogMove.dismiss();
                        }
                    });
                }
            });

        }
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

    private void parseJSONPokemon(String jsonText) {
        try {
            JSONObject json = new JSONObject(jsonText);
            JSONArray data = json.getJSONArray("pokedex");
            for(int i = 0; i < data.length(); i++) {
                JSONObject row = data.getJSONObject(i);
                String name = row.getString("name");
                String imgUrl = row.getString("image");
                int id = row.getInt("id");
                int index = pokemonList.size();
                pokemonList.add(index, new pokemon(id, name, imgUrl));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void parseJSONMoves(String jsonText) {
        try {
            JSONArray data = new JSONArray(jsonText);
            for(int i = 0; i < data.length(); i++) {
                if(data.isNull(i)) continue;
                JSONObject row = data.getJSONObject(i);
                String name = row.getString("name");
                int id = row.getInt("id");
                int index = moveList.size();
                moveList.add(index, new move(id, name));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}