package ues.proyecto2pdm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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


import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ues.proyecto2pdm.databinding.FragmentHomeBinding;

public class CompartirActivity extends AppCompatActivity {
    //constantes
    private static final int PERMISSION_CAMERA_REQUEST = 3;
    private static final int IMAGE_CAMERA_REQUEST = 2;
    private static final int PICK_MAGE_REQUEST = 1 ;
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

    Boolean cam = false;
    byte bb[];

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

        if(FileUri!=null && !cam){

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
        if (FileUri==null && cam){
            StorageReference file_name = Folder.child(System.currentTimeMillis()+".jpg");
            file_name.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
        if(FileUri==null && !cam){
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

    private void irACamara() {
        Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if(camara.resolveActivity(getPackageManager()) != null){
            startActivityForResult(camara,PERMISSION_CAMERA_REQUEST);
        //}
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

        if(requestCode == PICK_MAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!=null){
            FileUri = data.getData();
            Picasso.get().load(FileUri).into(imageView);
        }

        if(requestCode == PERMISSION_CAMERA_REQUEST && resultCode == RESULT_OK ){
            cam = true;
            FileUri = data.getData();

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,bytes);
            bb = bytes.toByteArray();
            //String file = Base64.encodeToString(bb,Base64.DEFAULT);
            imageView.setImageBitmap(bitmap);


        }

    }


    private String getFechaHora() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}