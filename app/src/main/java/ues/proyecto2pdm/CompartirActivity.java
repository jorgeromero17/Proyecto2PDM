package ues.proyecto2pdm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ues.proyecto2pdm.databinding.FragmentHomeBinding;

public class CompartirActivity extends AppCompatActivity {
    //constantes
    private static final int PERMISSION_CAMERA_REQUEST = 3;
    private static final int IMAGE_CAMERA_REQUEST = 2;
    private static final int PICK_MAGE_REQUEST = 1 ;
    private static final String SAMPLE_CROPPED_IMAGE = "SampleCropImg";
    //variables base de datos
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference miref;
    Uri FileUri;
    //variables xml
    ImageView buttontomarfoto,buttonsubirfoto;
    Button buttonmandar;
    EditText descripcion;
    ProgressBar progressBar;
    ImageView imageView;

    String currentPhotoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compartir);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        buttontomarfoto = findViewById(R.id.buttontomarfoto);
        buttonsubirfoto = findViewById(R.id.buttonsubirfoto);
        descripcion = findViewById(R.id.texviewpublicacion);
        buttonmandar = findViewById(R.id.buttonmandar);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.fotoasubir);

        database = FirebaseDatabase.getInstance();

        buttontomarfoto.setOnClickListener(view -> irATomarFoto());
        buttonsubirfoto.setOnClickListener(view -> abrirArchivos());
        buttonmandar.setOnClickListener(view ->mandarDatos());
    }

    private void mandarDatos() {
        miref = database.getReference("Publicaciones");
        StorageReference Folder = FirebaseStorage.getInstance().getReference().child("imagenespublicaciones");

        String fecha = getFechaHora();
        Publicacion publicacion = new Publicacion();
        publicacion.setId(fecha+""+currentUser.getDisplayName().replace(" ",""));
        publicacion.setDescripcion(descripcion.getText().toString());
        publicacion.setLikes(0);
        publicacion.setNombreusuario(currentUser.getDisplayName());

        if(FileUri!=null){

            buttonsubirfoto.setEnabled(false);
            buttonmandar.setEnabled(false);
            buttontomarfoto.setEnabled(false);

            StorageReference file_name = Folder.child(System.currentTimeMillis()+""+FileUri.getLastPathSegment());
            file_name.putFile(FileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },1000);

                    Toast.makeText(CompartirActivity.this,"Subido correctamente",Toast.LENGTH_LONG).show();
                    publicacion.setUrlimagen(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    miref.child(publicacion.getId()).setValue(publicacion);
                    finish();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CompartirActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    progressBar.setProgress((int)progress);
                }
            });
        }
        if(FileUri==null){
            Toast.makeText(CompartirActivity.this,"Agregue una imagen",Toast.LENGTH_LONG).show();
        }

    }

    private void irATomarFoto(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(CompartirActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                irACamara();
            }
            else{
                ActivityCompat.requestPermissions(CompartirActivity.this,new String[]{Manifest.permission.CAMERA},PERMISSION_CAMERA_REQUEST);
            }
        }else{
            irACamara();
        }
    }

    private void irACamara(){
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = null; // 1
        try {
            file = getImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri;
        uri = FileProvider.getUriForFile(CompartirActivity.this, BuildConfig.APPLICATION_ID.concat(".provider"), file);
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
        startActivityForResult(pictureIntent, PERMISSION_CAMERA_REQUEST);

    }

    private void abrirArchivos() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_MAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CAMERA_REQUEST){
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                irACamara();
            }
            else{
                Toast.makeText(CompartirActivity.this,"Necesitas el permiso habilitado",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_MAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uritemporal  = data.getData();
            IniciarCrop(uritemporal);

        }
        if(requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK  && data != null){
            FileUri = UCrop.getOutput(data);
            Picasso.get().load(FileUri).into(imageView);
        }
        if(requestCode == PERMISSION_CAMERA_REQUEST && resultCode == RESULT_OK){
            Uri uritemporal = Uri.parse(currentPhotoPath);
            IniciarCropCamara(uritemporal, uritemporal);
        }

    }

    private void IniciarCropCamara(Uri sourceUri, Uri destinationUri) {
        UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(500, 500)
                .withAspectRatio(1, 1)
                .withOptions(CropOpciones())
                .start(CompartirActivity.this);
    }

    public void IniciarCrop(Uri uri){
        String nombreFile = SAMPLE_CROPPED_IMAGE;
        nombreFile +=".jpg";
        UCrop ucrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(),nombreFile)));
        ucrop.withAspectRatio(1, 1);

        ucrop.withMaxResultSize(500, 500);
        ucrop.withOptions(CropOpciones());
        ucrop.start(CompartirActivity.this);

    }

    private UCrop.Options CropOpciones() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        options.setStatusBarColor(getResources().getColor(R.color.primaryLightColor));
        options.setToolbarColor(getResources().getColor(R.color.primaryColor));
        options.setToolbarWidgetColor(getResources().getColor(R.color.white));
        options.setToolbarTitle("Editar imagen");
        return options;

    }

    private String getFechaHora() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private File getImageFile() throws IOException {

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File file = File.createTempFile(SAMPLE_CROPPED_IMAGE, ".jpg", storageDir);
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }

}