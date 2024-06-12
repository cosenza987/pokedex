package com.example.pokebuilder.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.content.IntentCompat;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pokebuilder.MainActivity;
import com.example.pokebuilder.R;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogoutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SharedPreferences sharedPreferences;

    public LogoutFragment() {
        // Required empty public constructor
    }
    private OkHttpClient client;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogoutFragment newInstance(String param1, String param2) {
        LogoutFragment fragment = new LogoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build .VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy .Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String session = sharedPreferences.getString("session", "");
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("session", session)
                .build();
        System.out.println(username);
        System.out.println(session);
        Response response;
        try {
            response = makePostRequest("http://10.0.2.2:8080/account/logoff", formBody);
            if(response.code() == 200) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(intent);
                System.exit(0);
            } else {
                Toast.makeText(getContext(), "Unable to logoff! Call Calheira!", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {
            System.out.println(e);
            Toast.makeText(getContext(), "Unable to logoff due to no connection!", Toast.LENGTH_LONG).show();
            return;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }
}