package com.ervin.joker;

/**
 * Created by ervin on 6/5/2017.
 */

public class BerkasLamaran {
    String link_video, email_pelamar, lowongan_id, link_dokumen, id_pelamar, lowonganID_tanda;
    boolean tanda;

    public BerkasLamaran() {
    }

    public BerkasLamaran(String link_video, String email_pelamar, String lowongan_id, String link_dokumen,String id_pelamar, boolean tanda, String lowonoganID_tanda) {
        this.link_video = link_video;
        this.email_pelamar = email_pelamar;
        this.lowongan_id = lowongan_id;
        this.link_dokumen = link_dokumen;
        this.id_pelamar= id_pelamar;
        this.tanda = tanda;
        this.lowonganID_tanda = lowonoganID_tanda;
    }

    public String getlowonganID_tanda() {
        return lowonganID_tanda;
    }

    public void setlowonganID_tanda(String lowonganID_tanda) {
        this.lowonganID_tanda = lowonganID_tanda;
    }

    public String getEmail_pelamar() {
        return email_pelamar;
    }

    public void setEmail_pelamar(String email_pelamar) {
        this.email_pelamar = email_pelamar;
    }

    public String getLink_video() {
        return link_video;
    }

    public void setLink_video(String link_video) {
        this.link_video = link_video;
    }

    public String getLowongan_id() {
        return lowongan_id;
    }

    public void setLowongan_id(String lowongan_id) {
        this.lowongan_id = lowongan_id;
    }

    public String getLink_dokumen() {
        return link_dokumen;
    }

    public void setLink_dokumen(String link_dokumen) {
        this.link_dokumen = link_dokumen;
    }

    public boolean isTanda() {
        return tanda;
    }

    public void setTanda(boolean tanda) {
        this.tanda = tanda;
    }

    public String getId_pelamar() {
        return id_pelamar;
    }

    public void setId_pelamar(String id_pelamar) {
        this.id_pelamar = id_pelamar;
    }
}
