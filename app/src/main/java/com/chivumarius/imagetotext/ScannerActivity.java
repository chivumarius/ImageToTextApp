package com.chivumarius.imagetotext;

import static android.Manifest.permission_group.CAMERA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chivumarius.imagetotext.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;



/* ▼ "ScannerActivity"
        → Contains "All" the "Functionality" and "Logic"
        → of the Application. ▼ */
public class ScannerActivity extends AppCompatActivity {

    // ▼ "DECLARATION" OF "WIDGETS IDS" OF "SCANNER ACTIVITY .XML" FILE ▼
    private ImageView captureIV;
    private TextView resultTV;
    private Button snapbtn,detectBtn;
    private Bitmap imageBitmap;

    // ▼ "INITIALIZED CONSTANT" ▼
    static  final int REQUEST_IMAGE_CAPTURE=1;



    // ▼ "ON CREATE()" METHOD ▼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);


        // ▼ "INITIALIZATION" OF "WIDGETS IDS" OF "SCANNER ACTIVITY .XML" FILE ▼
        captureIV=findViewById(R.id.idIVCaptureImage);
        resultTV=findViewById(R.id.idTVDetectedText);
        snapbtn=findViewById(R.id.idButtonSnap);
        detectBtn=findViewById(R.id.idButtonDetect);


        // ▼ "CLICK LISTENER" ON "DETECT BUTTON"
        //      → FOR "TEXT DETECTION" ▼
        detectBtn.setOnClickListener(new View.OnClickListener() {

            // ▼ "ON CLICK" METHOD ▼
            @Override
            public void onClick(View view) {
                // ▼ CALLING "DETECT TEXT()" METHOD ▼
                detectText();
            }
        });



        // ▼ "CLICK LISTENER" ON "SNAP BUTTON"
        //      → FOR "IMAGE CAPTURE" ▼
        snapbtn.setOnClickListener(new View.OnClickListener() {

            // ▼ "ON CLICK" METHOD ▼
            @Override
            public void onClick(View view) {

                // ▼ "CHECKING PERMISSION" ▼
                if(checkPermission()){
                    // ▼ CALLING "CAPTURE IMAGE()" METHOD ▼
                    captureImage();
                } else {
                    // CALLING "REQUEST PERMISSION" METHOD ▼
                    requestPermission();
                }
            }
        });
    }



    // ▼ "CHECK PERMISSION()" METHOD ▼
    private boolean checkPermission(){
        // ▼ "CAMERA PERMISSION"▼
        int camerapermission = ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA);
        return camerapermission == PackageManager.PERMISSION_GRANTED;
    }



    // ▼ "REQUEST PERMISSION()" METHOD ▼
    private void requestPermission(){
        int PERMISSION_CODE = 200;
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.CAMERA
                },
                PERMISSION_CODE
        );
    }




    // ▼ "CAPTURE IMAGE()" METHOD ▼
    private void captureImage(){

        // ▼ "TAKING PICTURE" ▼
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // ▼ "CHECK": IF THE "TAKING PICTURE" EXISTS ▼
        if(takePicture.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE);
        }
    }




    // ▼ "ON REQUEST PERMISSIONS RESULT()" METHOD ▼
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        // ▼ "CHECKING": IF THE "GRANT RESULT" ▼
        if(grantResults.length>0){

            // ▼ SETTING "PERMISSION GRANTED" ▼
            boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;


            // ▼ CHECKING: "CAMERA PERMISSION" ▼
            if(cameraPermission){
                // ▼ DISPLAYING A "TOAST MESSAGE" ▼
                Toast.makeText(this, "Permission Granted ", Toast.LENGTH_SHORT).show();

                // ▼ CALLING "METHOD" ▼
                captureImage();
            } else {
                // ▼ DISPLAYING A "TOAST MESSAGE" ▼
                Toast.makeText(getApplicationContext(), "Permission denied !", Toast.LENGTH_SHORT).show();
            }
        }
    }




    // ▼ "ON ACTIVITY RESULT()" METHOD ▼
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // ▼ "CHECKING": IF THE "RESULT CODE" IS "RESULT OK" ▼
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            // ▼ GETTING "EXTRAS" ▼
            Bundle extras = data.getExtras();

            // ▼ CASTING "EXTRAS" TO "BITMAP" ▼
            imageBitmap = (Bitmap) extras.get("data");

            // ▼ SETTING "IMAGE BITMAP" FOR "CAPTURE IMAGE VIEW" ▼
            captureIV.setImageBitmap((imageBitmap));
        }
    }

    private void detectText() {
        InputImage image = InputImage.fromBitmap(imageBitmap,0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {

                StringBuilder result = new StringBuilder();
                for(Text.TextBlock block: text.getTextBlocks()){
                    String blockText = block.getText();
                    Point[] blockCornerpoint = block.getCornerPoints();
                    Rect blockFrame = block.getBoundingBox();
                    for(Text.Line line : block.getLines()){
                        String lineText = line.getText();
                        Point[] lineCornerPoint = line.getCornerPoints();
                        Rect lineRect = line.getBoundingBox();
                        for(Text.Element element: line.getElements()){
                            String elementText = element.getText();
                            result.append(elementText);
                        }
                        resultTV.setText(blockText);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to detect text From image ....!"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}