package com.apaza.proyecto.Clases;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apaza.proyecto.Interface.ApiService;
import com.apaza.proyecto.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Julio Cesar on 2/12/2017.
 */

public class MenusAdapter extends RecyclerView.Adapter<MenusAdapter.ViewHolder> {
    private static final String TAG = MenusAdapter.class.getSimpleName();
    private List<Menu> menus;

    public MenusAdapter(){
        this.menus = new ArrayList<>();
    }

    public void setMenus(List<Menu> menus){
        this.menus = menus;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //   public ImageView fotoImage;
        public TextView nombreText;
        public TextView precioText;
        public TextView comidaText;
        public ImageButton menuButton;

        public ViewHolder(View itemView) {
            super(itemView);
            //    fotoImage = (ImageView) itemView.findViewById(R.id.foto_image);
            nombreText = (TextView) itemView.findViewById(R.id.nombre_text);
            precioText = (TextView) itemView.findViewById(R.id.precio_text);
            comidaText = (TextView) itemView.findViewById(R.id.comida_text);
            menuButton = (ImageButton) itemView.findViewById(R.id.menu_button);
        }
    }

    @Override
    public MenusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MenusAdapter.ViewHolder viewHolder,final int position) {

        final Menu menu = this.menus.get(position);

        viewHolder.nombreText.setText("Menu "+menu.getTipo());
        viewHolder.precioText.setText("Costo: S./ " + menu.getCosto());
        viewHolder.comidaText.setText("Comida: " + menu.getComida());
        //String url = ApiService.API_BASE_URL + "/images/" + producto.getImagen();
        //Picasso.with(viewHolder.itemView.getContext()).load(url).into(viewHolder.fotoImage);

        viewHolder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.remove_button:

                                ApiService service = ApiServiceGenerator.createService(ApiService.class);

                                Call<ResponseMessage> call = service.destroyMenu(menu.getId());

                                call.enqueue(new Callback<ResponseMessage>() {
                                    @Override
                                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                        try {

                                            int statusCode = response.code();
                                            Log.d(TAG, "HTTP status code: " + statusCode);

                                            if (response.isSuccessful()) {

                                                ResponseMessage responseMessage = response.body();
                                                Log.d(TAG, "responseMessage: " + responseMessage);

                                                // Eliminar item del recyclerView y notificar cambios
                                                menus.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position, menus.size());

                                                Toast.makeText(v.getContext(), responseMessage.getMessage(), Toast.LENGTH_LONG).show();

                                            } else {
                                                Log.e(TAG, "onError: " + response.errorBody().string());
                                                throw new Exception("Error en el servicio");
                                            }

                                        } catch (Throwable t) {
                                            try {
                                                Log.e(TAG, "onThrowable: " + t.toString(), t);
                                                Toast.makeText(v.getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                            }catch (Throwable x){}
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseMessage> call, Throwable t) {
                                        Log.e(TAG, "onFailure: " + t.toString());
                                        Toast.makeText(v.getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                });

                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.menus.size();
    }

}
