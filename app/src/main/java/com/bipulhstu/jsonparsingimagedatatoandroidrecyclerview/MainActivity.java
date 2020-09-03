package com.bipulhstu.jsonparsingimagedatatoandroidrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<DemoData> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        arrayList = new ArrayList<DemoData>();

        //call jsontask
        JSONTask jsonTask = new JSONTask();
        jsonTask.execute();

    }

    public class JSONTask extends AsyncTask<String, String, String> {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String fullFile, name, image, description;

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://pastebin.com/raw/e52ZYwMq");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();


                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                fullFile = stringBuffer.toString();

                JSONObject jsonObject = new JSONObject(fullFile);
                JSONObject hero = jsonObject.getJSONObject("HERO");

                for (Iterator key = hero.keys(); key.hasNext(); ) {

                    JSONObject child = (JSONObject) hero.get((String) key.next());
                    name = child.getString("name");
                    image = child.getString("img");
                    description = child.getString("description");

                    arrayList.add(new DemoData(name, image, description));
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                try {
                    httpURLConnection.disconnect();
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            HeroAdapter heroAdapter = new HeroAdapter(arrayList, getApplicationContext(), new MyClick() {
                @Override
                public void onClickMe(View view, int position) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    //intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

                    //Send data to DetailsActivity using putExtra
                    intent.putExtra("img", arrayList.get(position).getImg());
                    intent.putExtra("name", arrayList.get(position).getName());
                    intent.putExtra("description", arrayList.get(position).getDescription());

                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(heroAdapter);
        }
    }
}