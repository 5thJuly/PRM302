package com.example.droidcafe;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private ListView orderHistoryListView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        orderHistoryListView = findViewById(R.id.order_history_list_view);
        dbHelper = new DatabaseHelper(this);

        displayOrderHistory();
    }

    private void displayOrderHistory() {
        List<String> orderHistory = dbHelper.getAllOrders();

        if (orderHistory.isEmpty()) {
            orderHistory.add("No orders yet");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderHistory);
        orderHistoryListView.setAdapter(adapter);
    }
}