package com.ervin.joker.lowongan;

import java.io.Serializable;

/**
 * Created by ervin on 6/3/2017.
 */
@SuppressWarnings("serial")
public class LowonganPekerjaan implements Serializable{
    String posisi_lowong, pembuat_lowongan, deskripsi;
    long batas_pengiriman, tanggal_terbit;

    public LowonganPekerjaan() {
    }

    public LowonganPekerjaan(String posisi_lowong, String pembuat_lowongan, String deskripsi, long batas_pengiriman, long tanggal_terbit) {
        this.posisi_lowong = posisi_lowong;
        this.pembuat_lowongan = pembuat_lowongan;
        this.deskripsi = deskripsi;
        this.batas_pengiriman = batas_pengiriman;
        this.tanggal_terbit = tanggal_terbit;
    }

    public String getPosisi_lowong() {
        return posisi_lowong;
    }

    public void setPosisi_lowong(String posisi_lowong) {
        this.posisi_lowong = posisi_lowong;
    }

    public String getPembuat_lowongan() {
        return pembuat_lowongan;
    }

    public void setPembuat_lowongan(String pembuat_lowongan) {
        this.pembuat_lowongan = pembuat_lowongan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public long getBatas_pengiriman() {
        return batas_pengiriman;
    }

    public void setBatas_pengiriman(long batas_pengiriman) {
        this.batas_pengiriman = batas_pengiriman;
    }

    public long getTanggal_terbit() {
        return tanggal_terbit;
    }

    public void setTanggal_terbit(long tanggal_terbit) {
        this.tanggal_terbit = tanggal_terbit;
    }
}
