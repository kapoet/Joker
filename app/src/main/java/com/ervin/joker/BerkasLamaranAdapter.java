package com.ervin.joker;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by ervin on 6/6/2017.
 */

public class BerkasLamaranAdapter extends RecyclerView.ViewHolder{
private final TextView email, nama;
private final ImageView gambar;

public BerkasLamaranAdapter(View itemView) {
        super(itemView);
        nama = (TextView) itemView.findViewById(R.id.tv_nama);
        gambar = (ImageView) itemView.findViewById(R.id.iv_gambar_pelamar);
        email = (TextView) itemView.findViewById(R.id.tv_email);
        }

public void setNama(String nama) {
        this.nama.setText(nama);
        }

public void setEmail(String email) {
        this.email.setText(email);
        }

public void setLink_gambar(String linkgambar) {
        Log.d("aaaa", "setLink_gambar: "+linkgambar);
        Glide.with(itemView.getContext()).load(linkgambar).placeholder(R.drawable.ic_menu_gallery).dontAnimate().centerCrop().into(gambar);
        }
}
