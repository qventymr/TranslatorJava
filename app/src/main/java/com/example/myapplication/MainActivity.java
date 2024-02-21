package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private TextView result_text;
    private EditText editText;
    private Button button;
    private String text;
    private String finish_text;
    private Spinner spinner;
    private Spinner spinnertwo;
    private String langToSelected;
    private String langFromSelected;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        result_text = findViewById(R.id.text);
        editText = findViewById(R.id.EditTextPole);
        button = findViewById(R.id.button);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Spinner spinnertwo = (Spinner) findViewById(R.id.spinnertwo);

        List<String> elements = new ArrayList<String>();

        elements.add("ru Русский");
        elements.add("en Английский");
        elements.add("zh Китайский");
        elements.add("es Испанский");
        elements.add("fr Французский");
        elements.add("pt Португальский");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, elements);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinnertwo.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                langFromSelected = elements.get(position).substring(0, 2);
                result_text.setText(langFromSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.textNoUserInput, Toast.LENGTH_LONG).show();
                }
                else {
                    text = editText.getText().toString();
                    try {
                        finish_text = translate("ru", "en", text);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    result_text.setText(finish_text);
                }
            }
        });

    }
    @NonNull
    private static String translate(String langFrom, String langTo, String text) throws IOException{
        // INSERT YOU URL HERE
        String urlStr = "https://script.google.com/macros/s/AKfycbyGT-0mhCXm1zRHtxcUeFMBPY2bZOSwrgTv34sIe_HyUQLyHmV4siZCBv8wGLgOWumJ/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}