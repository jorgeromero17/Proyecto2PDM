package ues.proyecto2pdm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder> {

    private Context context;
    private List<Publicacion> publicaciones;

    public PublicacionAdapter(Context context,List<Publicacion> publicaciones){
        this.context = context;
        this.publicaciones = publicaciones;
    }

    @Override
    public PublicacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.publicacion_item,parent,false);
        return new PublicacionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PublicacionViewHolder holder, int position) {
        Publicacion publicacionActual = publicaciones.get(position);
        holder.pubdescripcion.setText(publicacionActual.getDescripcion());
        holder.pubnombreusuario.setText(publicacionActual.getNombreusuario());
        Picasso.get().load(publicacionActual.getUrlimagen()).into(holder.pubimagen);
    }

    @Override
    public int getItemCount() {
        return publicaciones.size();
    }

    public class PublicacionViewHolder extends RecyclerView.ViewHolder {

        public TextView pubdescripcion;
        public ImageView pubimagen;
        public TextView pubnombreusuario;


        public PublicacionViewHolder(View itemView) {
            super(itemView);
            pubdescripcion = itemView.findViewById(R.id.pubdescripcion);
            pubimagen = itemView.findViewById(R.id.pubimagen);
            pubnombreusuario = itemView.findViewById(R.id.pubnombreusuario);
        }
    }
}
