package nl.jovmit.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SimpleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BaseAdapter adapter = new BaseAdapter();
        adapter.setItems(generateItems(20));
        recyclerView.setAdapter(adapter);
    }

    private List<RecyclerItem> generateItems(int count) {
        List<RecyclerItem> items = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            items.add(new Person("First Name: " + i, "Last Name: " + i));
        }
        return items;
    }
}
