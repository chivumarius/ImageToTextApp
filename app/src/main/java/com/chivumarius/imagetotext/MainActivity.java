package com.chivumarius.imagetotext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chivumarius.imagetotext.R;


/* "MainActivity" Screen
    → "Display" an "ImageView":
    → a "Text" and a "Button".
    → And when the "User Presses" the "Button",
    → he is "Redirected" to the "Scanner" Screen.
 */
public class MainActivity extends AppCompatActivity {

    // ▼ "DECLARATION" OF "WIDGETS IDS" OF "MAIN ACTIVITY" ▼
    private Button captureButton;



    // ▼ "ON CREATE()" METHOD ▼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // ▼ "INITIALIZATION" OF "WIDGETS IDS" OF "ACTIVITY_MAIN. XML" FILE ▼
        captureButton = findViewById(R.id.idBtnCapture);

        // ▼ "CLICK LISTENER" FOR "REDIRECTING" TO THE "SCANNER" SCREEN ▼
        captureButton.setOnClickListener(new View.OnClickListener() {
            // ▼ "ON CLICK" METHOD ▼
            @Override
            public void onClick(View view) {
                // ▼ "REDIRECTING" TO THE "SCANNER" SCREEN ▼
                startActivity(new Intent(MainActivity.this,ScannerActivity.class));
            }
        });
    }
}