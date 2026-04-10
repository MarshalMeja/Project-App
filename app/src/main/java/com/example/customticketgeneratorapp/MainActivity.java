package com.example.customticketgeneratorapp;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText eventNameInput, dateInput, venueInput, holderInput, seatInput, priceInput, ticketNumberInput;

    private TextView previewEventName, previewDateTime, previewVenue, previewHolder, previewSeat, previewPrice, previewTicketNumber;
    private ImageView qrCodeImage;
    private CardView previewCard;

    private Button downloadBtn, randomizeBtn, batchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Design"));
        tabLayout.addTab(tabLayout.newTab().setText("Colors"));
        tabLayout.addTab(tabLayout.newTab().setText("Advanced"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText() == null) return;

                switch (tab.getText().toString()) {
                    case "Design":
                        Toast.makeText(MainActivity.this, "Template switching coming soon", Toast.LENGTH_SHORT).show();
                        break;

                    case "Colors":
                        previewCard.setCardBackgroundColor(Color.parseColor("#FFF8E7"));
                        break;

                    case "Advanced":
                        Toast.makeText(MainActivity.this, "Batch export ready", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        setupTextWatchers();

        updatePreview();
        updateQRCode(getSafeText(ticketNumberInput));

        downloadBtn.setOnClickListener(v ->
                Toast.makeText(this, "Ticket saved (simulated)", Toast.LENGTH_SHORT).show()
        );

        randomizeBtn.setOnClickListener(v -> randomizeAllFields());

        batchBtn.setOnClickListener(v ->
                Toast.makeText(this, "Batch generation coming soon", Toast.LENGTH_SHORT).show()
        );
    }

    private void initViews() {
        eventNameInput = findViewById(R.id.eventNameInput);
        dateInput = findViewById(R.id.dateInput);
        venueInput = findViewById(R.id.venueInput);
        holderInput = findViewById(R.id.holderInput);
        seatInput = findViewById(R.id.seatInput);
        priceInput = findViewById(R.id.priceInput);
        ticketNumberInput = findViewById(R.id.ticketNumberInput);

        previewEventName = findViewById(R.id.previewEventName);
        previewDateTime = findViewById(R.id.previewDateTime);
        previewVenue = findViewById(R.id.previewVenue);
        previewHolder = findViewById(R.id.previewHolder);
        previewSeat = findViewById(R.id.previewSeat);
        previewPrice = findViewById(R.id.previewPrice);
        previewTicketNumber = findViewById(R.id.previewTicketNumber);

        qrCodeImage = findViewById(R.id.qrCodeImage);
        previewCard = findViewById(R.id.previewCard);

        downloadBtn = findViewById(R.id.downloadBtn);
        randomizeBtn = findViewById(R.id.randomizeBtn);
        batchBtn = findViewById(R.id.batchBtn);
    }

    private void setupTextWatchers() {
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updatePreview();
            }
        };

        eventNameInput.addTextChangedListener(watcher);
        dateInput.addTextChangedListener(watcher);
        venueInput.addTextChangedListener(watcher);
        holderInput.addTextChangedListener(watcher);
        seatInput.addTextChangedListener(watcher);
        priceInput.addTextChangedListener(watcher);

        ticketNumberInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updatePreview();
                updateQRCode(s.toString());
            }
        });
    }

    private void updatePreview() {
        previewEventName.setText(getSafeText(eventNameInput));
        previewDateTime.setText(getSafeText(dateInput));
        previewVenue.setText(getSafeText(venueInput));
        previewHolder.setText(getSafeText(holderInput));
        previewSeat.setText("Seat: " + getSafeText(seatInput));
        previewPrice.setText(getSafeText(priceInput));
        previewTicketNumber.setText("Ticket #: " + getSafeText(ticketNumberInput));
    }

    private void updateQRCode(String text) {
        if (text == null || text.isEmpty()) return;

        try {
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 300, 300);
            qrCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private String getSafeText(TextInputEditText input) {
        return input.getText() != null ? input.getText().toString() : "";
    }

    private void randomizeAllFields() {
        Random rand = new Random();

        String[] events = {"Jazz Night", "Rock Fest", "EDM Party", "Comedy Show"};
        String[] venues = {"Stadium Hall", "Beach Club", "Convention Center"};
        String[] holders = {"Sam Smith", "Jamie Lee", "Taylor W."};

        eventNameInput.setText(events[rand.nextInt(events.length)]);
        venueInput.setText(venues[rand.nextInt(venues.length)]);
        holderInput.setText(holders[rand.nextInt(holders.length)]);
        seatInput.setText("A-" + (rand.nextInt(30) + 1));
        priceInput.setText("$" + (50 + rand.nextInt(100)));
        ticketNumberInput.setText(String.valueOf(rand.nextInt(999999)));

        dateInput.setText("August " + (rand.nextInt(28) + 1) + ", 2026 8:00 PM");
    }
}