package ues.proyecto2pdm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ues.proyecto2pdm.databinding.FragmentHomeBinding;

public class CompartirActivity extends AppCompatActivity {
    //variables base de datos
    private static final int PICK_MAGE_REQUEST = 1 ;
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

        database = FirebaseDatabase.getInstance();


        buttonsubirfoto.setOnClickListener(view -> abrirArchivos());
        buttonmandar.setOnClickListener(view -> mandarDatos());
    }

    private void mandarDatos() {
        miref = database.getReference("Publicaciones");
        String fecha = getFechaHora();

        if(FileUri!=null){

            buttonsubirfoto.setEnabled(false);
            buttonmandar.setEnabled(false);
            buttontomarfoto.setEnabled(false);

            StorageReference Folder = FirebaseStorage.getInstance().getReference().child("imagenespublicaciones");
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

                    Toast.makeText(CompartirActivity.this,"Subido si",Toast.LENGTH_LONG).show();

                    Publicacion publicacion = new Publicacion();
                    publicacion.setId(fecha+""+currentUser.getDisplayName().replace(" ",""));
                    publicacion.setDescripcion(descripcion.getText().toString());
                    publicacion.setLikes(0);
                    publicacion.setNombreusuario(currentUser.getDisplayName());
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
            })
            .addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Toast.makeText(CompartirActivity.this,"Publicacion cancelada",Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            Toast.makeText(CompartirActivity.this,"Agregue una imagen",Toast.LENGTH_LONG).show();
        }
    }

    private void abrirArchivos() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_MAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_MAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!=null){
            FileUri = data.getData();
        }
    }

    private String getFechaHora() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}