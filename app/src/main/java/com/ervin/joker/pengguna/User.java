package com.ervin.joker.pengguna;

/**
 * Created by ervin on 5/30/2017.
 */

public class User {
    String nama, photo_profil, jenis_pengguna, deskripsi, video_id;

    public User() {
    }

    public User(String nama, String photo_profil, String jenis_pengguna) {
        this.nama = nama;
        this.photo_profil = photo_profil;
        this.jenis_pengguna = jenis_pengguna;
    }

    public User(String nama, String photo_profil, String jenis_pengguna, String deskripsi, String video_id) {
        this.nama = nama;
        this.photo_profil = photo_profil;
        this.jenis_pengguna = jenis_pengguna;
        this.deskripsi = deskripsi;
        this.video_id = video_id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPhoto_profil() {
        return photo_profil;
    }

    public void setPhoto_profil(String photo_profil) {
        this.photo_profil = photo_profil;
    }

    public String getJenis_pengguna() {
        return jenis_pengguna;
    }

    public void setJenis_pengguna(String jenis_pengguna) {
        this.jenis_pengguna = jenis_pengguna;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
