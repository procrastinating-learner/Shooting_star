package com.example.stars;

import static android.content.ContentValues.TAG;
import androidx.appcompat.widget.SearchView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stars.adapter.StarAdapter;
import com.example.stars.beans.Star;
import com.example.stars.service.StarService;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private List<Star> stars;
    private RecyclerView recyclerView;
    private StarAdapter starAdapter = null;
    private StarService  service = StarService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        stars = new ArrayList<>();

        if(service.findAll().isEmpty()){
            init();
        }

        recyclerView = findViewById(R.id.recycle_view);
        //insérer le code
        starAdapter = new StarAdapter(this, service.findAll());
        recyclerView.setAdapter(starAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void init() {
        service.create(new Star("Lebron James", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTZ4B2rsnIiNv6rdLVCq0bH0lzs1VbZ3kUlHQ&s", 3.5f));
        service.create(new Star("Hassan El Fad", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTK2DHbsqA0l2EEJ0Fv_aM90b_ySjoSVVF8GA&s", 3.0f));
        service.create(new Star("Marilyn Monroe", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzedlgctJpGeU1HVRPKpOYJ05eXa1uBzq6HQ&s", 3.0f));
        service.create(new Star("Princess Diana", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgsxg_Bboq15_-UXerrY1KXQ5Gzwl7PdtXpTIOipzXAjTs3-8kASYNW4Fr66GoyNK8XuA&usqp=CAU", 4.5f));
        service.create(new Star("Barack Obama", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRLZ91ERVxHa0w_82mDyyl0YMEHlsJASTa1zw&s", 4.5f));
        service.create(new Star("Mohamed Ali", "https://www.un.org/sites/un2.un.org/files/styles/large-article-image-style-16-9/public/field/image/2024/08/image1170x530cropped_2_0.jpg", 5.0f));
        service.create(new Star("Noussair Mazraoui", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0cLDb8YvKuvfb03Oyn40igP5462r0IPkShavQj5tJXGKkstm_I895WwdcmtA7-kp4adE&usqp=CAU", 4.0f));





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("ListActivity", "onCreateOptionsMenu started");

        getMenuInflater().inflate(R.menu.menu, menu);

        Log.d("ListActivity", "Menu inflated successfully");

        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        if (menuItem == null) {
            Log.e("ListActivity", "menuItem is null! Check menu.xml");
            return true; // Prevent crash
        }
        Log.d("ListActivity", "MenuItem found");



        SearchView searchView = (SearchView) menuItem.getActionView();
        if (searchView == null) {
            Log.e("ListActivity", "SearchView is null! Cannot set listener.");
            return true; // Prevent crash
        }
        Log.d("ListActivity", "SearchView initialized");



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SearchView", "Search submitted: " + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("SearchView", "Search text changed: " + newText);
                if (starAdapter != null) {
                    starAdapter.getFilter().filter(newText);
                } else {
                    Log.e("SearchView", "starAdapter is null, filtering skipped");
                }
                return true;
            }
        });

        Log.d("ListActivity", "SearchView listener set successfully");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.partager) {
            // Share text
            String shareText = "Partager Via";
            String mimeType = "text/plain";

            ShareCompat.IntentBuilder
                    .from(this)
                    .setType(mimeType)
                    .setChooserTitle("Partager")
                    .setText(shareText)
                    .startChooser();

            return true; // Indicate that the event was handled
        }

        return super.onOptionsItemSelected(item);
    }





}