package com.example.pokebuilder.ui.pokedex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokebuilder.databinding.FragmentPokedexBinding;

import java.util.ArrayList;

public class PokedexFragment extends Fragment {
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList pokemonList = new ArrayList<>();
    private FragmentPokedexBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Pokédex");
        PokedexViewModel pokedexViewModel =
                new ViewModelProvider(this).get(PokedexViewModel.class);

        binding = FragmentPokedexBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        pokedexViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}