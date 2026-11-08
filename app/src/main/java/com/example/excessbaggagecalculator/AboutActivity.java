
package com.example.excessbaggagecalculator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutActivity extends AppCompatActivity {


    private static final String APP_URL =
            "https://github.com/faeyscha/ExcessBaggageCalculator";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.aboutToolbar);

        ViewGroup.LayoutParams params = toolbar.getLayoutParams();
        int originalHeight = params.height;

        int left = toolbar.getPaddingLeft();
        int right = toolbar.getPaddingRight();
        int bottom = toolbar.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {

            int topInset = insets.getInsets(
                    WindowInsetsCompat.Type.statusBars()
            ).top;

            v.setPadding(left, topInset, right, bottom);

            ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
            layoutParams.height = originalHeight + topInset;
            v.setLayoutParams(layoutParams);

            return insets;
        });

        ViewCompat.requestApplyInsets(toolbar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        TextView tvGithub = findViewById(R.id.tvGithub);

        tvGithub.setOnClickListener(v -> {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(APP_URL)
            );

            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(
                        this,
                        "Unable to open GitHub URL",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}
