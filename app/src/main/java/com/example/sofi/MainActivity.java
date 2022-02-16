package com.example.sofi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Uri imagePhotoCameraUri;
    private static final int REQUEST_EXTERNAL_STORAGE_RESULT = 2;
    private static final int START_CAMERA_APP = 1;
    private Toolbar toolbar;
    private ImageView imageView;
    private TextView textView;
    private TextView textView2;
    private String filePath = "";
    private int cornerTopX, cornerTopY, cornerBottomX, cornerBottomY, countTop, countBottom,
            legTopX, legTopY, countLegTop, cornerTopX1, cornerTopY1, countTop1, cornerBottomX1, cornerBottomY1, countBottom1;
    private float size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
    }

    public void onClickClearAll(View view) {
        BitmapDrawable mydrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = mydrawable.getBitmap();
        int width = bitmap.getWidth() / (bitmap.getHeight() / 160);
        int height = bitmap.getHeight() / (bitmap.getHeight() / 160);
        Bitmap bitmapSmale = Bitmap.createScaledBitmap(bitmap, width, height, false);
        bitmapSmale = doWhiteBlackBitmap(bitmapSmale);
        doInvertNew(bitmapSmale);
        float factorSideLeft = (float)(cornerBottomY - cornerTopY) / (float)(cornerBottomX - cornerTopX);
        float factorBottom = (float)(cornerBottomY1 - cornerBottomY) / (float)(cornerBottomX1- cornerBottomX);
        float factorSideRight = (float)(cornerBottomY1 - cornerTopY1) / (float)(cornerBottomX1 - cornerTopX1);
        float leftX =(float)(legTopX * factorSideLeft - cornerBottomX * factorBottom + cornerBottomY - legTopY)
                / (float) (factorSideLeft - factorBottom);
        float leftY = (float)(factorBottom * leftX - factorBottom * cornerBottomX + cornerBottomY);
        double lenghtFoot = Math.sqrt((double) (Math.pow((double)(leftX - legTopX), 2) + Math.pow((double)(leftY - legTopY), 2)));
        double lenghtLeftSide = Math.sqrt((double)(Math.pow((double)(cornerBottomX - cornerTopX), 2) + Math.pow((double)(cornerBottomY - cornerTopY), 2)));
        size = (float)lenghtFoot / (float)lenghtLeftSide * 297;
        //size = 297.0f - (float) ((legTopY  - legTopY1) * 297) / (float)(cornerBottomY - cornerTopY);
        textView2.setText("( " + cornerTopX + ", " + cornerTopY + ")  " + "\n" + "(" + cornerBottomX +
                ", " + cornerBottomY + ") " + "\n" + "("
                + legTopX + ", " + legTopY + ")" + "\n" +"("
                + cornerTopX1 + ", " + cornerTopY1 + ")" + "\n" + "("
                + cornerBottomX1 + ", " + cornerBottomY1 + ")"+ "\n" + size);
    }
    public static Bitmap doWhiteBlackBitmap(Bitmap src) {
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        int A, R, G, B;
        int pixel;
        int width = src.getWidth();
        int height = src.getHeight();
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                if (R > 135 && G > 135 && B > 135) {
                    R = B = G = 255;
                }
                else{
                    R = G = B = 0;
                }
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }
    private void doInvertNew(Bitmap src) {
        int R, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, B, B1, B2, B3, B4, B5, B6, B7, B8, B9, B10,
                G, G1, G2, G3, G4, G5, G6, G7, G8, G9, G10;
        int pixelColor, pixelColor1, pixelColor2, pixelColor3, pixelColor4, pixelColor5, pixelColor6,
                pixelColor7, pixelColor8, pixelColor9, pixelColor10;
        int height = src.getHeight();
        int width = src.getWidth();
        for (int x = 0; x < width - 3; x++) {
            for (int y = 2; y < height - 3; y++) {
                pixelColor = src.getPixel(x, y);
                pixelColor1 = src.getPixel(x + 1, y + 1);
                pixelColor2 = src.getPixel(x + 2, y + 2);
                pixelColor3 = src.getPixel(x + 3, y + 3);
                pixelColor4 = src.getPixel(x, y - 2);
                pixelColor5 = src.getPixel(x + 1, y - 2);
                pixelColor6 = src.getPixel(x + 2, y - 2);

                R = Color.red(pixelColor);
                R1 = Color.red(pixelColor1);
                R2 = Color.red(pixelColor2);
                R3 = Color.red(pixelColor3);
                R4 = Color.red(pixelColor4);
                R5 = Color.red(pixelColor5);
                R6 = Color.red(pixelColor6);

                if ((R == 255) && (R1 == 255) && (R2 == 255) && (R3 == 255) && (R4 == 0) && (R5 == 0)
                        && (R6 == 0)){
                    if (countTop == 1) {
                        break;
                    }
                    cornerTopX = x;
                    cornerTopY = y;
                    countTop++;
                }
            }
        }
        for (int x = width - 5; x >= 6; x--) {
            for (int y = 2; y < height - 5; y++) {
                pixelColor = src.getPixel(x, y);
                pixelColor1 = src.getPixel(x - 1, y + 1);
                pixelColor2 = src.getPixel(x - 2, y + 2);
                pixelColor3 = src.getPixel(x - 3, y + 3);
                pixelColor4 = src.getPixel(x, y - 2);
                pixelColor5 = src.getPixel(x - 1, y - 2);
                pixelColor6 = src.getPixel(x - 2, y - 2);

                R = Color.red(pixelColor);
                R1 = Color.red(pixelColor1);
                R2 = Color.red(pixelColor2);
                R3 = Color.red(pixelColor3);
                R4 = Color.red(pixelColor4);
                R5 = Color.red(pixelColor5);
                R6 = Color.red(pixelColor6);
                if ((R == 255) && (R1 == 255) && (R2 == 255) && (R3 == 255) && (R4 == 0) &&
                        (R5 == 0) && (R6 == 0)){
                    if (countTop1 == 1) {
                        break;
                    }
                    cornerTopX1 = x;
                    cornerTopY1 = y;
                    countTop1++;
                }
            }
        }
        for (int x = 0; x < width - 3; x++) {
            for (int y = height - 3; y >= 3; y--) {
                pixelColor = src.getPixel(x, y);
                pixelColor1 = src.getPixel(x + 1, y - 1);
                pixelColor2 = src.getPixel(x + 2, y - 2);
                pixelColor3 = src.getPixel(x + 3, y - 3);
                pixelColor4 = src.getPixel(x, y + 2);
                pixelColor5 = src.getPixel(x + 1, y + 2);
                pixelColor6 = src.getPixel(x + 2, y + 2);

                R = Color.red(pixelColor);
                R1 = Color.red(pixelColor1);
                R2 = Color.red(pixelColor2);
                R3 = Color.red(pixelColor3);
                R4 = Color.red(pixelColor4);
                R5 = Color.red(pixelColor5);
                R6 = Color.red(pixelColor6);

                if ((R == 255) && (R1 == 255) && (R2 == 255) && (R3 == 255) && (R4 == 0)
                        && (R5 == 0) && (R6 == 0)){
                    if (countBottom == 1) {
                        break;
                    }
                    cornerBottomX = x;
                    cornerBottomY = y;
                    countBottom++;
                }
            }
        }
        for (int x = width - 1; x >= 4; x--) {
            for (int y = height - 3; y >= 3; y--) {
                pixelColor = src.getPixel(x, y);
                pixelColor1 = src.getPixel(x - 1, y - 1);
                pixelColor2 = src.getPixel(x - 2, y - 2);
                pixelColor3 = src.getPixel(x - 3, y - 3);
                pixelColor4 = src.getPixel(x, y + 2);
                pixelColor5 = src.getPixel(x - 1, y + 2);
                pixelColor6 = src.getPixel(x - 2, y + 2);

                R = Color.red(pixelColor);
                R1 = Color.red(pixelColor1);
                R2 = Color.red(pixelColor2);
                R3 = Color.red(pixelColor3);
                R4 = Color.red(pixelColor4);
                R5 = Color.red(pixelColor5);
                R6 = Color.red(pixelColor6);

                if ((R == 255) && (R1 == 255) && (R2 == 255) && (R3 == 255) && (R4 == 0)
                        && (R5 == 0) && (R6 == 0)){
                    if (countBottom1 == 1) {
                        break;
                    }
                    cornerBottomX1 = x;
                    cornerBottomY1 = y;
                    countBottom1++;
                }
            }
        }

        for (int y = 2; y < height - 3; y++) {
            for (int x = 3; x < width - 3; x++) {
                pixelColor = src.getPixel(x, y);
                pixelColor1 = src.getPixel(x, y + 1);
                pixelColor2 = src.getPixel(x, y + 2);
                pixelColor3 = src.getPixel(x, y + 3);
                pixelColor4 = src.getPixel(x - 3, y - 2);
                pixelColor5 = src.getPixel(x - 2, y - 2);
                pixelColor6 = src.getPixel(x - 1, y - 2);
                pixelColor7 = src.getPixel(x , y - 2);
                pixelColor8 = src.getPixel(x + 1, y - 2);
                pixelColor9 = src.getPixel(x + 2, y - 2);
                pixelColor10 = src.getPixel(x + 3, y - 2);

                R = Color.red(pixelColor);
                R1 = Color.red(pixelColor1);
                R2 = Color.red(pixelColor2);
                R3 = Color.red(pixelColor3);
                R4 = Color.red(pixelColor4);
                R5 = Color.red(pixelColor5);
                R6 = Color.red(pixelColor6);
                R7 = Color.red(pixelColor7);
                R8 = Color.red(pixelColor8);
                R9 = Color.red(pixelColor9);
                R10 = Color.red(pixelColor10);

                G = Color.green(pixelColor);
                G1 = Color.green(pixelColor1);
                G2 = Color.green(pixelColor2);
                G3 = Color.green(pixelColor3);
                G4 = Color.green(pixelColor4);
                G5 = Color.green(pixelColor5);
                G6 = Color.green(pixelColor6);
                G7 = Color.green(pixelColor7);
                G8 = Color.green(pixelColor8);
                G9 = Color.green(pixelColor9);
                G10 = Color.green(pixelColor10);

                B = Color.blue(pixelColor);
                B1 = Color.blue(pixelColor1);
                B2 = Color.blue(pixelColor2);
                B3 = Color.blue(pixelColor3);
                B4 = Color.blue(pixelColor4);
                B5 = Color.blue(pixelColor5);
                B6 = Color.blue(pixelColor6);
                B7 = Color.blue(pixelColor7);
                B8 = Color.blue(pixelColor8);
                B9 = Color.blue(pixelColor9);
                B10 = Color.blue(pixelColor10);

                if((R == 0) &&
                        (R1 == 0) &&
                        (R2 ==  0) &&
                        (R3 == 0) &&
                        (R4 == 255) &&
                        (R5 == 255) &&
                        (R6 == 255) &&
                        (R7 == 255) &&
                        (R8 == 255) &&
                        (R9 == 255) &&
                        (R10 > 145)) {
                    if(countLegTop == 1){
                        break;
                    }
                    legTopX = x;
                    legTopY = y;
                    countLegTop++;
                }
            }
        }
    }

    public void onClickDeletePhoto(View view) {
        startActivity(new Intent(MainActivity.this, MainActivity.class)); //reload MainActivity
        finish();
        //File file = new File(filePath);
        //file.delete();
    }

    public void onClickSize(View view) {
        BitmapDrawable mydrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = mydrawable.getBitmap();
        int width = bitmap.getWidth() / (bitmap.getHeight() / 160);
        int height = bitmap.getHeight() / (bitmap.getHeight() / 160);
        Bitmap bitmapSmale = Bitmap.createScaledBitmap(bitmap, width, height,false );

        doInvert(bitmapSmale);
        size = (float)((cornerBottomY - legTopY ) * 297) / (float)(cornerBottomY - cornerTopY);
        textView.setText("( " + cornerTopX + ", " + cornerTopY + ")  " + "\n" +"("+ cornerBottomX +
                ", "+ cornerBottomY + ") " +"\n" + "("
                + legTopX + ", " + legTopY +")" + "\n" + size);
    }

    public void onClickCamera(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED) {
                callCameraApp();
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission
                        .WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getApplicationContext(),
                            "Camera Permission Required", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE_RESULT);
            }
        } else {
            callCameraApp();
        }
    }

    private void callCameraApp() {
        Intent cameraAppIntent = new Intent();
        cameraAppIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String authorities = getApplicationContext().getPackageName() + ".fileprovider";
        imagePhotoCameraUri = FileProvider.getUriForFile(this, authorities, photoFile);
        cameraAppIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagePhotoCameraUri);
        startActivityForResult(cameraAppIntent, START_CAMERA_APP);
    }
    private File createImageFile() throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String imageFileName = "IMAGE_" + simpleDateFormat.format(new Date()) + ".jpg";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDirectory, imageFileName);
        filePath = imageFile.getAbsolutePath();
        return imageFile;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_CAMERA_APP && resultCode == RESULT_OK) {
            imageView.setImageURI(imagePhotoCameraUri);
            //startActivity(new Intent(MainActivity.this, MainActivity.class)); //reload MainActivity
            //finish();
        }
    }
    public void doInvert(Bitmap src) {
        //Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        int R, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, B, B1, B2, B3, B4, B5, B6, B7, B8, B9, B10,
                G, G1, G2, G3, G4, G5, G6, G7, G8, G9, G10;
        int pixelColor, pixelColor1, pixelColor2, pixelColor3, pixelColor4, pixelColor5, pixelColor6,
                pixelColor7, pixelColor8, pixelColor9, pixelColor10;
        int height = src.getHeight();
        int width = src.getWidth();
        for (int x = 0; x < width - 3; x++) {
            for (int y = 2; y < height - 3; y++) {
                pixelColor = src.getPixel(x, y);
                pixelColor1 = src.getPixel(x + 1, y + 1);
                pixelColor2 = src.getPixel(x + 2, y + 2);
                pixelColor3 = src.getPixel(x + 3, y + 3);
                pixelColor4 = src.getPixel(x, y - 2);
                pixelColor5 = src.getPixel(x + 1, y - 2);
                pixelColor6 = src.getPixel(x + 2, y - 2);

                R = Color.red(pixelColor);
                R1 = Color.red(pixelColor1);
                R2 = Color.red(pixelColor2);
                R3 = Color.red(pixelColor3);
                R4 = Color.red(pixelColor4);
                R5 = Color.red(pixelColor5);
                R6 = Color.red(pixelColor6);

                G = Color.green(pixelColor);
                G1 = Color.green(pixelColor1);
                G2 = Color.green(pixelColor2);
                G3 = Color.green(pixelColor3);
                G4 = Color.green(pixelColor4);
                G5 = Color.green(pixelColor5);
                G6 = Color.green(pixelColor6);

                B = Color.blue(pixelColor);
                B1 = Color.blue(pixelColor1);
                B2 = Color.blue(pixelColor2);
                B3 = Color.blue(pixelColor3);
                B4 = Color.blue(pixelColor4);
                B5 = Color.blue(pixelColor5);
                B6 = Color.blue(pixelColor6);

                if ((R > 145) && (G > 145) && (B > 145) &&
                        (R1 > 145) && (G1 > 145) && (B1 > 145) &&
                        (R2 > 145) && (G2 > 145) && (B2 > 145) &&
                        (R3 > 145) && (G3 > 145) && (B3 > 145) &&
                        (R4 < 130) && (G4 < 110) && (B4 < 90) &&
                        (R5 < 130) && (G5 < 110) && (B5 < 90) &&
                        (R6 < 130) && (G6 < 110) && (B6 < 90)){
                    if (countTop == 1) {
                        break;
                    }
                    cornerTopX = x;
                    cornerTopY = y;
                    countTop++;
                }
            }
        }

        for (int x = 0; x < width - 3; x++) {
            for (int y = height - 3; y >= 3; y--) {
                pixelColor = src.getPixel(x, y);
                pixelColor1 = src.getPixel(x + 1, y - 1);
                pixelColor2 = src.getPixel(x + 2, y - 2);
                pixelColor3 = src.getPixel(x + 3, y - 3);
                pixelColor4 = src.getPixel(x, y + 2);
                pixelColor5 = src.getPixel(x + 1, y + 2);
                pixelColor6 = src.getPixel(x + 2, y + 2);

                R = Color.red(pixelColor);
                R1 = Color.red(pixelColor1);
                R2 = Color.red(pixelColor2);
                R3 = Color.red(pixelColor3);
                R4 = Color.red(pixelColor4);
                R5 = Color.red(pixelColor5);
                R6 = Color.red(pixelColor6);

                G = Color.green(pixelColor);
                G1 = Color.green(pixelColor1);
                G2 = Color.green(pixelColor2);
                G3 = Color.green(pixelColor3);
                G4 = Color.green(pixelColor4);
                G5 = Color.green(pixelColor5);
                G6 = Color.green(pixelColor6);

                B = Color.blue(pixelColor);
                B1 = Color.blue(pixelColor1);
                B2 = Color.blue(pixelColor2);
                B3 = Color.blue(pixelColor3);
                B4 = Color.blue(pixelColor4);
                B5 = Color.blue(pixelColor5);
                B6 = Color.blue(pixelColor6);

                if ((R > 145) && (G > 145) && (B > 145) &&
                        (R1 > 145) && (G1 > 145) && (B1 > 145) &&
                        (R2 > 145) && (G2 > 145) && (B2 > 145) &&
                        (R3 > 145) && (G3 > 145) && (B3 > 145) &&
                        (R4 < 130) && (G4 < 110) && (B4 < 90) &&
                        (R5 < 130) && (G5 < 110) && (B5 < 90) &&
                        (R6 < 130) && (G6 < 110) && (B6 < 90)){
                    if (countBottom == 1) {
                        break;
                    }
                    cornerBottomX = x;
                    cornerBottomY = y;
                    countBottom++;
                }
            }
        }

        for (int y = 2; y < height - 3; y++) {
            for (int x = 3; x < width - 3; x++) {
                pixelColor = src.getPixel(x, y);
                pixelColor1 = src.getPixel(x, y + 1);
                pixelColor2 = src.getPixel(x, y + 2);
                pixelColor3 = src.getPixel(x, y + 3);
                pixelColor4 = src.getPixel(x - 3, y - 2);
                pixelColor5 = src.getPixel(x - 2, y - 2);
                pixelColor6 = src.getPixel(x - 1, y - 2);
                pixelColor7 = src.getPixel(x , y - 2);
                pixelColor8 = src.getPixel(x + 1, y - 2);
                pixelColor9 = src.getPixel(x + 2, y - 2);
                pixelColor10 = src.getPixel(x + 3, y - 2);

                R = Color.red(pixelColor);
                R1 = Color.red(pixelColor1);
                R2 = Color.red(pixelColor2);
                R3 = Color.red(pixelColor3);
                R4 = Color.red(pixelColor4);
                R5 = Color.red(pixelColor5);
                R6 = Color.red(pixelColor6);
                R7 = Color.red(pixelColor7);
                R8 = Color.red(pixelColor8);
                R9 = Color.red(pixelColor9);
                R10 = Color.red(pixelColor10);

                G = Color.green(pixelColor);
                G1 = Color.green(pixelColor1);
                G2 = Color.green(pixelColor2);
                G3 = Color.green(pixelColor3);
                G4 = Color.green(pixelColor4);
                G5 = Color.green(pixelColor5);
                G6 = Color.green(pixelColor6);
                G7 = Color.green(pixelColor7);
                G8 = Color.green(pixelColor8);
                G9 = Color.green(pixelColor9);
                G10 = Color.green(pixelColor10);

                B = Color.blue(pixelColor);
                B1 = Color.blue(pixelColor1);
                B2 = Color.blue(pixelColor2);
                B3 = Color.blue(pixelColor3);
                B4 = Color.blue(pixelColor4);
                B5 = Color.blue(pixelColor5);
                B6 = Color.blue(pixelColor6);
                B7 = Color.blue(pixelColor7);
                B8 = Color.blue(pixelColor8);
                B9 = Color.blue(pixelColor9);
                B10 = Color.blue(pixelColor10);

                if((R < 75) && (G < 85) && (B < 125) &&
                        (R1 < 75) && (G1 < 85) && (B1 < 125) &&
                        (R2 < 75) && (G2 < 85) && (B2 < 125) &&
                        (R3 < 75) && (G3 < 85) && (B3 < 125) &&
                        (R4 > 145) && (G4 > 145) && (B4 > 145) &&
                        (R5 > 145) && (G5 > 145) && (B5 > 145) &&
                        (R6 > 145) && (G6 > 145) && (B6 > 145) &&
                        (R7 > 145) && (G7 > 145) && (B7 > 145) &&
                        (R8 > 145) && (G8 > 145) && (B8 > 145) &&
                        (R9 > 145) && (G9 > 145) && (B9 > 145) &&
                        (R10 > 145) && (G10 > 145) && (B10 > 145)) {
                    if(countLegTop == 1){
                        break;
                    }
                    legTopX = x;
                    legTopY = y;
                    countLegTop++;
                }
            }
        }
    }
}