package com.ervin.joker.dokumen;

/**
 * Created by ervin on 6/10/2017.
 */

public class Dokumen {
    String link_dokumen, nama_dokumen, pembuat_dokumen;

    public Dokumen() {
    }

    public Dokumen(String link_dokumen, String nama_dokumen, String pembuat_dokumen) {
        this.link_dokumen = link_dokumen;
        this.nama_dokumen = nama_dokumen;
        this.pembuat_dokumen = pembuat_dokumen;
    }

    public String getPembuat_dokumen() {
        return pembuat_dokumen;
    }

    public void setPembuat_dokumen(String pembuat_dokumen) {
        this.pembuat_dokumen = pembuat_dokumen;
    }

    public String getLink_dokumen() {
        return link_dokumen;
    }

    public void setLink_dokumen(String link_dokumen) {
        this.link_dokumen = link_dokumen;
    }

    public String getNama_dokumen() {
        return nama_dokumen;
    }

    public void setNama_dokumen(String nama_dokumen) {
        this.nama_dokumen = nama_dokumen;
    }
}
