package com.example.droidcafe;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class OrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView mDeliveryDateTextView;
    private Button mBackButton;
    private Button mSubmitButton;
    private TextInputEditText mNameText, mAddressText, mPhoneText, mNoteText;
    private RadioGroup mDeliveryRadioGroup;
    private RadioButton mSameDayRadio, mNextDayRadio, mPickUpRadio;
    private CheckBox mChocolateCheckbox, mSprinklesCheckbox, mNutsCheckbox;
    private DatabaseHelper dbHelper;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initializeViews();
        setupActionBar();
        setupOrderDetails();
        setupListeners();
        setupSpinner();
        initializeDate();
    }

    private void initializeViews() {
        mBackButton = findViewById(R.id.back_button);
        mSubmitButton = findViewById(R.id.submit_button);
        mNameText = findViewById(R.id.name_text);
        mAddressText = findViewById(R.id.address_text);
        mPhoneText = findViewById(R.id.phone_text);
        mNoteText = findViewById(R.id.note_text);
        mDeliveryRadioGroup = findViewById(R.id.delivery_radio_group);
        mChocolateCheckbox = findViewById(R.id.checkbox1_chocolate);
        mSprinklesCheckbox = findViewById(R.id.checkbox2_sprinkles);
        mNutsCheckbox = findViewById(R.id.checkbox3_nuts);
        mSameDayRadio = findViewById(R.id.sameday);
        mNextDayRadio = findViewById(R.id.nextday);
        mPickUpRadio = findViewById(R.id.pickup);
        mDeliveryDateTextView = findViewById(R.id.delivery_date_textview);
        dbHelper = new DatabaseHelper(this);
    }

    private void setupActionBar() {
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @SuppressLint("StringFormatInvalid")
    private void setupOrderDetails() {
        Intent intent = getIntent();
        String orderMessage = intent.getStringExtra(MainActivity.EXTRA_ORDER_KEY);

        TextView orderTextView = findViewById(R.id.order_textview);
        if (orderMessage != null && !orderMessage.isEmpty()) {
            orderTextView.setText(getString(R.string.order_details, orderMessage));
        }
    }

    private void setupListeners() {
        mBackButton.setOnClickListener(v -> finish());
        mSubmitButton.setOnClickListener(view -> submitOrder());
        mSubmitButton.setEnabled(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                validateForm();
            }
        };

        mNameText.addTextChangedListener(textWatcher);
        mAddressText.addTextChangedListener(textWatcher);
        mPhoneText.addTextChangedListener(textWatcher);

        mDeliveryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            updateDeliveryDateOptions(checkedId);
            validateForm();
        });

        mDeliveryDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void setupSpinner() {
        Spinner spinner = findViewById(R.id.label_spinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this, R.array.labels_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    private void initializeDate() {
        selectedDate = Calendar.getInstance();
        selectedDate.add(Calendar.DAY_OF_MONTH, 1); // Default to tomorrow
        updateDeliveryDateText();

    }

    private void updateDeliveryDateOptions(int checkedId) {
        if (checkedId == R.id.pickup) {
            mDeliveryDateTextView.setText(R.string.pick_up);
            mDeliveryDateTextView.setClickable(false);
        } else {
            mDeliveryDateTextView.setClickable(true);
            selectedDate = Calendar.getInstance();
            if (checkedId == R.id.nextday) {
                selectedDate.add(Calendar.DAY_OF_MONTH, 1);
            }
            updateDeliveryDateText();
        }
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                OrderActivity.this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
                    updateDeliveryDateText();
                    validateForm();
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );

        Calendar today = Calendar.getInstance();
        Calendar minDate = Calendar.getInstance();

        if (mSameDayRadio.isChecked()) {
            Calendar maxDate = Calendar.getInstance();
            maxDate.add(Calendar.DAY_OF_MONTH, 1); // Giới hạn là ngày mai
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        } else if (mNextDayRadio.isChecked()) {
            minDate.add(Calendar.DAY_OF_MONTH, 1); // Bắt đầu từ ngày mai
            datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        }
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.show();
    }


    private void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        selectedDate.set(year, month, dayOfMonth);
        updateDeliveryDateText();
        validateForm();
    }

    private void updateDeliveryDateText() {
        String dateMessage = String.format("%d/%d/%d",
                selectedDate.get(Calendar.MONTH) + 1,
                selectedDate.get(Calendar.DAY_OF_MONTH),
                selectedDate.get(Calendar.YEAR));
        mDeliveryDateTextView.setText(getString(R.string.delivery_date, dateMessage));
    }
    private void validateForm() {
        boolean isValid = !mNameText.getText().toString().trim().isEmpty() &&
                !mAddressText.getText().toString().trim().isEmpty() &&
                !mPhoneText.getText().toString().trim().isEmpty() &&
                mDeliveryRadioGroup.getCheckedRadioButtonId() != -1 &&
                (mChocolateCheckbox.isChecked() || mSprinklesCheckbox.isChecked() || mNutsCheckbox.isChecked());

        mSubmitButton.setEnabled(isValid);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        if (((RadioButton) view).isChecked()) {
            updateDeliveryDateOptions(view.getId());
            displayToast(((RadioButton) view).getText().toString());
        }
        if (view.getId() == R.id.sameday || view.getId() == R.id.nextday) {
            showDatePicker();
        }
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String spinnerLabel = adapterView.getItemAtPosition(position).toString();
        displayToast(spinnerLabel);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }

    private void submitOrder() {
        if (!mSubmitButton.isEnabled()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(OrderActivity.this, BillActivity.class);
        intent.putExtra("NAME", mNameText.getText().toString());
        intent.putExtra("ADDRESS", mAddressText.getText().toString());
        intent.putExtra("PHONE", mPhoneText.getText().toString());
        intent.putExtra("NOTE", mNoteText.getText().toString());
        intent.putExtra("DELIVERY_DATE", mDeliveryDateTextView.getText().toString());

        RadioButton selectedRadioButton = findViewById(mDeliveryRadioGroup.getCheckedRadioButtonId());
        intent.putExtra("DELIVERY_METHOD", selectedRadioButton.getText().toString());

        StringBuilder toppings = new StringBuilder();
        if (mChocolateCheckbox.isChecked()) toppings.append("Chocolate, ");
        if (mSprinklesCheckbox.isChecked()) toppings.append("Sprinkles, ");
        if (mNutsCheckbox.isChecked()) toppings.append("Crushed nuts, ");
        if (toppings.length() > 0) toppings.setLength(toppings.length() - 2);
        intent.putExtra("TOPPINGS", toppings.toString());

        intent.putExtra("ORDER_ITEM", getIntent().getStringExtra(MainActivity.EXTRA_ORDER_KEY));

        startActivity(intent);
    }
}