package com.example.droidcafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        TextView customerInfoTextView = findViewById(R.id.customer_info);
        TextView orderDetailsTextView = findViewById(R.id.order_details);
        Button confirmButton = findViewById(R.id.confirm_button);

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String address = intent.getStringExtra("ADDRESS");
        String phone = intent.getStringExtra("PHONE");
        String note = intent.getStringExtra("NOTE");
        String deliveryMethod = intent.getStringExtra("DELIVERY_METHOD");
        String toppings = intent.getStringExtra("TOPPINGS");
        String orderItem = intent.getStringExtra("ORDER_ITEM");
        String deliveryDate = intent.getStringExtra("DELIVERY_DATE");

        String customerInfo = "Name: " + name + "\n"
                + "Address: " + address + "\n"
                + "Phone: " + phone + "\n"
                + "Note: " + note;
        customerInfoTextView.setText(customerInfo);

        String orderDetails = "Order: " + orderItem + "\n"
                + "Delivery Method: " + deliveryMethod + "\n"
                + "Toppings: " + toppings + "\n"
                + "Delivery Date: " + deliveryDate;
        orderDetailsTextView.setText(orderDetails);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BillActivity.this, "Order confirmed!", Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(BillActivity.this, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            }
        });
    }
}