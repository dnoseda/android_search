package com.example.productlist.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mercadolibre.sdk.Meli;
import com.mercadolibre.sdk.MeliException;
import com.ning.http.client.FluentStringsMap;
import com.ning.http.client.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class DisplaySearchResults extends MainActivity {

    Iterable<JSONObject> getIterable(final JSONArray jsonArray){
        return new Iterable<JSONObject>() {
            @Override
            public Iterator<JSONObject> iterator() {
                return new Iterator<JSONObject>() {
                    JSONArray myarray = jsonArray;
                    int index = 0;
                    @Override
                    public boolean hasNext() {
                        return index < myarray.length();
                    }

                    @Override
                    public JSONObject next() {
                        try {
                            return myarray.getJSONObject(index++);
                        }catch(Exception e){
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search_results);
        Intent intent = getIntent();
        String message = intent.getStringExtra(INPUT_SEARCH);

        ListView listView = (ListView )findViewById(R.id.result_list);
        ArrayList<String> list = new ArrayList<String>();
        Meli meli = new Meli(3836, "jjImATZ2AarRNTWoqWQFB6lxZNRds9TA");
        FluentStringsMap params = new FluentStringsMap();
        params.add("access_token", meli.getAccessToken());
        try {
            Response response  = meli.get(String.format("/sites/MLA/search?q=%s&limit=%d",message,5), params);
            JSONObject json= new JSONObject(response.getResponseBody());
            for(JSONObject item: getIterable(json.getJSONArray("results"))){
                list.add(item.getString("title"));
            }
        } catch (MeliException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

    ArrayAdapter<String> adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_search_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
