package com.example.cities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button buttonSave;
    private Button buttonModify;
    private Button buttonBack;
    private Button buttonNewData;
    private ProgressBar progressBar;
    private EditText editTextId;
    private EditText editTextName;
    private EditText editTextCountry;
    private EditText editTextPopulation;
    private LinearLayout linearLayoutForm;
    private ListView listViewData;
    private List <Cities> city = new ArrayList<>();
    private String url = "https://retoolapi.dev/nc68vM/cities";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //GET kérés elküldése
        RequestTask task = new RequestTask(url, "GET");
        //kérés elküldése
        task.execute();

        buttonNewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //urlap megjelenítése
                linearLayoutForm.setVisibility(View.VISIBLE);
                buttonSave.setVisibility(View.VISIBLE);
                buttonNewData.setVisibility(View.GONE);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //urlap alaphelyzetbe állítása
                formReset();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //varos hozzáadása
                cityAdd();
            }
        });

        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //város módosítása
                cityModify();
            }
        });

    }

    public void cityAdd() {
        //urlap adatainak lekérése
        String name = editTextName.getText().toString();
        String country = editTextCountry.getText().toString();
        int population = Integer.parseInt(editTextPopulation.getText().toString());
        //új varos létrehozása
        Cities cities = new Cities(0, name, country, population);
        //Gson létrehozása a jsonConverter-hez
        Gson jsonConverter = new Gson();
        //POST kérés elküldése
        RequestTask task = new RequestTask(url, "POST", jsonConverter.toJson(cities));
        //kérés elküldése
        task.execute();
    }

    public void cityModify() {
        //urlap adatainak lekérése
        int id = Integer.parseInt(editTextId.getText().toString());
        String name = editTextName.getText().toString();
        String country = editTextCountry.getText().toString();
        int population = Integer.parseInt(editTextPopulation.getText().toString());
        //új varos létrehozása
        Cities cities = new Cities(0, name, country, population);
        //Gson létrehozása a jsonConverter-hez
        Gson jsonConverter = new Gson();
        //PUT kérés elküldése
        RequestTask task = new RequestTask(url + "/" + id, "PUT", jsonConverter.toJson(cities));
        //kérés elküldése
        task.execute();
    }

    public void init() {
        buttonSave = findViewById(R.id.buttonSave);
        buttonModify = findViewById(R.id.buttonModify);
        buttonBack = findViewById(R.id.buttonBack);
        buttonNewData = findViewById(R.id.buttonNewData);
        progressBar = findViewById(R.id.progressBar);
        editTextId = findViewById(R.id.editTextId);
        editTextName = findViewById(R.id.editTextName);
        editTextCountry = findViewById(R.id.editTextCountry);
        editTextPopulation = findViewById(R.id.editTextPopulation);
        linearLayoutForm = findViewById(R.id.linearLayoutForm);
        listViewData = findViewById(R.id.listViewData);
        //listViewData adapterének beállítása
        listViewData.setAdapter(new CitiesAdapter());
        linearLayoutForm.setVisibility(View.GONE);
        buttonModify.setVisibility(View.GONE);
    }

    private class CitiesAdapter extends ArrayAdapter<Cities> {

        public CitiesAdapter() {
            super(MainActivity.this, R.layout.cities_list_items, city);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //inflater létrehozása
            LayoutInflater inflater = getLayoutInflater();
            //view létrehozása a person_list_items.xml-ből
            View view = inflater.inflate(R.layout.cities_list_items, null, false);
            //cities_list_items lévő elemek inicializálása
            TextView textViewDelete = view.findViewById(R.id.textViewDelete);
            TextView textViewModify = view.findViewById(R.id.textViewModify);
            TextView textViewName = view.findViewById(R.id.textViewName);
            TextView textViewCountry = view.findViewById(R.id.textViewCountry);
            TextView textViewPopulation = view.findViewById(R.id.textViewPopulation);
            //actuaCities létrehozása a people listából
            Cities actualCities = city.get(position);

            textViewName.setText(actualCities.getName());
            textViewCountry.setText(actualCities.getCountry());
            textViewPopulation.setText(String.valueOf(actualCities.getPopulation()));

            textViewModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //urlap kitöltése a kiválasztott elem adataival
                    editTextId.setText(String.valueOf(actualCities.getId()));
                    editTextName.setText(actualCities.getName());
                    editTextCountry.setText(actualCities.getCountry());
                    editTextPopulation.setText(String.valueOf(actualCities.getPopulation()));
                    //urlap megjelenítése
                    linearLayoutForm.setVisibility(View.VISIBLE);
                    buttonModify.setVisibility(View.VISIBLE);
                    buttonSave.setVisibility(View.GONE);
                    buttonNewData.setVisibility(View.GONE);
                }
            });

            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //DELETE kérés elküldése
                    RequestTask task = new RequestTask(url, "DELETE", String.valueOf(actualCities.getId()));
                    //kérés elküldése
                    task.execute();
                }
            });

            return view;
        }
    }

    //urlap alaphelyzetbe állítása
    public void formReset() {
        editTextId.setText("");
        editTextName.setText("");
        editTextCountry.setText("");
        editTextPopulation.setText("");
        linearLayoutForm.setVisibility(View.GONE);
        buttonModify.setVisibility(View.GONE);
        buttonSave.setVisibility(View.VISIBLE);
        buttonNewData.setVisibility(View.VISIBLE);
    }

    private class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;
        String requestParams;

        public RequestTask(String requestUrl, String requestType, String requestParams) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }


        public RequestTask(String requestUrl, String requestType) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }

        //doInBackground metódus létrehozása a kérés elküldéséhez
        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                switch (requestType) {
                    case "GET":
                        response = RequestHandler.get(requestUrl);
                        break;
                    case "POST":
                        response = RequestHandler.post(requestUrl, requestParams);
                        break;
                    case "PUT":
                        response = RequestHandler.put(requestUrl, requestParams);
                        break;
                    case "DELETE":
                        response = RequestHandler.delete(requestUrl + "/" + requestParams);
                        break;
                }
            } catch (IOException e) {
                Toast.makeText(MainActivity.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        //onPreExecute metódus létrehozása a ProgressBar megjelenítéséhez
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        //onPostExecute metódus létrehozása a válasz feldolgozásához
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            progressBar.setVisibility(View.GONE);
            Gson converter = new Gson();
            if (response.getResponseCode() >= 400) {
                Toast.makeText(MainActivity.this,
                        "Error during processing request.", Toast.LENGTH_SHORT).show();
                Log.d("onPostExecuteError:", response.getContent());
            }
            switch (requestType) {
                case "GET":
                    Cities[] cityArray = converter.fromJson(
                            response.getContent(), Cities[].class);
                    //city lista frissítése a GET válaszban kapott elemekkel
                    city.clear();
                    city.addAll(Arrays.asList(cityArray));
                    Toast.makeText(MainActivity.this, "Successful data query", Toast.LENGTH_SHORT).show();
                    break;
                case "POST":
                    Cities cities = converter.fromJson(
                            response.getContent(), Cities.class);
                    //city lista frissítése az új elemmel
                    city.add(0, cities);
                    formReset();
                    Toast.makeText(MainActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                    break;
                case "PUT":
                    Cities updateCities = converter.fromJson(
                            response.getContent(), Cities.class);
                    //city lista frissítése a módosított elemmel
                    city.replaceAll(cities1 ->
                            cities1.getId() == updateCities.getId() ? updateCities : cities1);
                    formReset();
                    Toast.makeText(MainActivity.this, "Successful modify", Toast.LENGTH_SHORT).show();
                    break;
                case "DELETE":
                    int id = Integer.parseInt(requestParams);
                    //city lista frissítése a törölt elem nélkül
                    city.removeIf(cities1 -> cities1.getId() == id);
                    Toast.makeText(MainActivity.this, "Successful deleted", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}