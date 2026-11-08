
package com.example.excessbaggagecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etWeight, etRate;
    private Spinner spTicketClass;
    private Button btnCalculate, btnReset;
    private TextView tvExcessWeight, tvExcessCharge, tvTotal;
    private LinearLayout resultLayout;

    private static final String APP_URL =
            "https://github.com/YOUR_USERNAME/ExcessBaggageCalculator";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        ViewGroup.LayoutParams params = toolbar.getLayoutParams();

        int originalHeight = params.height;

        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {

            int topInset = insets.getInsets(
                    WindowInsetsCompat.Type.statusBars()
            ).top;

            v.setPadding(
                    v.getPaddingLeft(),
                    topInset,
                    v.getPaddingRight(),
                    v.getPaddingBottom()
            );

            ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
            layoutParams.height = originalHeight + topInset;
            v.setLayoutParams(layoutParams);

            return insets;
        });

        ViewCompat.requestApplyInsets(toolbar);

        setSupportActionBar(toolbar);


        etWeight = findViewById(R.id.etWeight);
        etRate = findViewById(R.id.etRate);
        spTicketClass = findViewById(R.id.spTicketClass);

        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);

        tvExcessWeight = findViewById(R.id.tvExcessWeight);
        tvExcessCharge = findViewById(R.id.tvExcessCharge);
        tvTotal = findViewById(R.id.tvTotal);

        resultLayout = findViewById(R.id.resultLayout);

        btnCalculate.setOnClickListener(v -> calculateFee());
        btnReset.setOnClickListener(v -> resetForm());
    }

    private void calculateFee() {

        String weightInput = etWeight.getText().toString().trim();
        String rateInput = etRate.getText().toString().trim();

        resultLayout.setVisibility(View.GONE);

        if (weightInput.isEmpty()) {
            etWeight.setError("Please enter baggage weight");
            etWeight.requestFocus();
            return;
        }

        if (rateInput.isEmpty()) {
            etRate.setError("Please enter baggage rate");
            etRate.requestFocus();
            return;
        }

        double weight;
        double rate;

        try {
            weight = Double.parseDouble(weightInput);
            rate = Double.parseDouble(rateInput);
        } catch (NumberFormatException e) {
            Toast.makeText(this,
                    "Please enter valid numbers",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Double.isFinite(weight) || weight < 0) {
            etWeight.setError("Weight must be zero or greater");
            etWeight.requestFocus();
            return;
        }

        if (!Double.isFinite(rate) || rate < 0) {
            etRate.setError("Rate must be zero or greater");
            etRate.requestFocus();
            return;
        }

        int allowance;

        if (spTicketClass.getSelectedItemPosition() == 0) {
            allowance = 20;
        } else {
            allowance = 40;
        }

        double excessWeight = Math.max(0, weight - allowance);
        double excessCharge = excessWeight * rate;
        double totalPayable = excessCharge * 1.08;

        if (!Double.isFinite(totalPayable)) {
            Toast.makeText(this,
                    "Input values are too large",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        tvExcessWeight.setText(String.format(
                Locale.US,
                "Excess Weight: %.2f kg",
                excessWeight
        ));

        tvExcessCharge.setText(String.format(
                Locale.US,
                "Excess Charge: RM %.2f",
                excessCharge
        ));

        tvTotal.setText(String.format(
                Locale.US,
                "Total Payable: RM %.2f",
                totalPayable
        ));

        resultLayout.setVisibility(View.VISIBLE);
    }

    private void resetForm() {

        etWeight.setText("");
        etRate.setText("");
        spTicketClass.setSelection(0);

        etWeight.setError(null);
        etRate.setError(null);

        resultLayout.setVisibility(View.GONE);

        Toast.makeText(this,
                "Form has been reset",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_about) {
            Intent intent = new Intent(
                    MainActivity.this,
                    AboutActivity.class
            );
            startActivity(intent);
            return true;
        }

        if (id == R.id.menu_share) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            shareIntent.putExtra(
                    Intent.EXTRA_SUBJECT,
                    "ExcessBaggageCalculator"
            );

            shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Try ExcessBaggageCalculator!\n" + APP_URL
            );

            startActivity(Intent.createChooser(
                    shareIntent,
                    "Share Application"
            ));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
