package com.ervin.joker;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by ervin on 6/3/2017.
 */

class LowonganPekerjaanAdapter extends RecyclerView.ViewHolder{
    private final TextView batas_pengiriman, lowongan, nama_perusahaan, deskripsi_lowongan;
    private final ImageView gambar_perusahaan;

    public LowonganPekerjaanAdapter(View itemView) {
        super(itemView);
        nama_perusahaan = (TextView) itemView.findViewById(R.id.tv_nama_perusahaan);
        gambar_perusahaan = (ImageView) itemView.findViewById(R.id.iv_gambar_perusahaan);
        batas_pengiriman = (TextView) itemView.findViewById(R.id.tv_batas_pengiriman);
        lowongan = (TextView) itemView.findViewById(R.id.tv_lowongan);
        deskripsi_lowongan = (TextView) itemView.findViewById(R.id.tv_deskripsi_perusahaan);
    }

    public void setNamaPerusahaan(String nama) {
        nama_perusahaan.setText(nama);
    }

    public void setDeskripsiPerusahaan(String deskripsiPerusahaan){
        deskripsi_lowongan.setText(deskripsiPerusahaan);
    }
    public void setBatasPengiriman(int batas) {
        batas_pengiriman.setText( "Diposting "+batas+" hari yang lalu");
    }

    public void setLowongan(String lowong) {
        lowongan.setText(lowong);
    }

    public void setLink_gambar(String linkgambar) {
        Log.d("aaaa", "setLink_gambar: "+linkgambar);
        Glide.with(itemView.getContext()).load(linkgambar).placeholder(R.drawable.ic_menu_gallery).dontAnimate().centerCrop().into(gambar_perusahaan);
    }
}
