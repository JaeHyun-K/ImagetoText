package com.example.imagetotext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.lang.annotation.Inherited;
import java.util.Locale;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;

public class MainActivity extends AppCompatActivity {
    public Integer calresult;
    EditText mResultEt;
    ImageView mPreviewIv;
    Button cal_btn;
    Button search_btn;
    Button speak_btn;
    Button drum_btn;
    TextToSpeech textToSpeech;

    private static final int CAMERA_REQUEST_CODE=200;
    private static final int STORAGE_REQUEST_CODE=400;
    private static final int IMAGE_PICK_GALLERY_CODE=1000;
    private static final int IMAGE_PICK_CAMERA_CODE=1001;

    String cameraPermission[];
    String storagePermission[];
    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setSubtitle("Click Image button to insert Image");
        mResultEt=findViewById(R.id.resultEt);
        mPreviewIv=findViewById(R.id.imageIv);
        cal_btn=findViewById(R.id.calbutton);
        search_btn=findViewById(R.id.button3);
        speak_btn=findViewById( R.id.button4 );
        drum_btn=findViewById( R.id.btn_drum_pad );

        cameraPermission=new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        textToSpeech = new TextToSpeech( MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage( Locale.ENGLISH );
                }
            }
        } );

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                search();
            }
        });

        speak_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mResultEt.getText().toString();

                textToSpeech.speak( text, QUEUE_FLUSH, null );
            }
        } );

        drum_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrumPadActivity();
            }
        } );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addImage) {
            showImageImportDialog();
        }
        if (id == R.id.settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImageImportDialog() {
        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickCamera();
                    }
                }
                if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickGallery();
                    }

                }
            }
        });
        dialog.create().show();
    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPid");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to Text");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);

    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickCamera();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickCamera();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);

            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).start(this);

            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mPreviewIv.setImageURI(resultUri);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else {
//
                    String itemGet = "";
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());

                    }

                    System.out.println(sb);
                    itemGet = sb.toString();
                    System.out.println(itemGet);
                    itemGet = itemGet.replaceAll(" ", "");
                    final String[] array = itemGet.split("");
                    mResultEt.setText(sb.toString());

                    //문자 사이 공백 없앤 후 array로 넣어줌.


                    cal_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (array.length > 2) {
                                String[] fin=new String[3];
                                String signal = array[2];
                                fin[1]=array[2];
                                fin[0]=array[1];
                                fin[2]=array[3];

                                int place=4;
                                if (!signal.equals("+") && !signal.equals("-") && !signal.equals("*") && !signal.equals("/")) {
                                    for (int i = 1; i < array.length; i++) {
                                        if (!array[i + 1].equals("+") && !array[i + 1].equals("-") && !array[i + 1].equals("*") && !array[i + 1].equals("/")) {
                                            String big = array[1];
                                            String small = array[i + 1];
                                            int b = Integer.parseInt(big);
                                            int s = Integer.parseInt(small);
                                            int sim = 10 * b + s;
                                            String put = Integer.toString(sim);
                                            array[i] = put;
                                            }
                                        else {
                                            place=i+1;
                                            fin[1]=array[i+1];
                                            signal=fin[1];
                                            break;
                                        }
                                    }
                                    fin[0]=array[1];
                                }//합치기 끝.}

                                if (array.length >place) {//뒤에 오는 수가 여러자리
                                    int leng = array.length;
                                    for (int i = place+1; i < leng - 1; i++) {
                                        String big = array[i];
                                        String small = array[i + 1];

                                        int b = Integer.parseInt(big);
                                        int s = Integer.parseInt(small);
                                        int sim = 10 * b + s;
                                        String put = Integer.toString(sim);
                                        array[i + 1] = put;
                                    }
                                    int size = leng - 1;
                                    fin[2] = array[size];
                                }


                                String first = fin[0];
                                String second = fin[2];
                                int a = Integer.parseInt(first);
                                int b = Integer.parseInt(second);
                                System.out.println(signal);
                                Calculate(a, b, signal);
                                System.out.println(calresult);
                                System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
                                String theresult = Integer.toString(calresult);
                                System.out.println(theresult);

                                String str = "";
                                str = str + first;
                                str = str + signal;
                                str = str + second;
                                str = str + "=";
                                str = str + theresult;

                                mResultEt.setText(str);
                            }
                }
            });

                    speak_btn.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String text = mResultEt.getText().toString();
                            CharSequence charSequence = text;

                            textToSpeech.speak( charSequence, 0, null, null);
                        }
                    } );

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();

            }
    }
    }

    private void Calculate(Integer a, Integer b, String sign) {
        String add = "+";
        String sub = "-";
        String mul = "*";
        String div = "/";
        if (sign.equals(add)) {
            calresult = a + b;
        } else if (sign.equals(sub)) {
            calresult = a - b;
        } else if (sign.equals(mul)) {
            calresult = a * b;
        } else if (sign.equals(div)) {
            calresult = a / b;
        } else {
            Toast.makeText(this, "Cannot calculate", Toast.LENGTH_SHORT).show();
        }
        //결과값을 어떻게 return 할것인가?
        return;
    }

    public void search() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, mResultEt.getText().toString() );
        startActivity(intent);
    }

    public void openDrumPadActivity(){
        Intent intent = new Intent(this, DrumPadActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

}
