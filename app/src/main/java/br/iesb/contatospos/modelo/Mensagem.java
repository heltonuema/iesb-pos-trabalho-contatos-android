package br.iesb.contatospos.modelo;

/**
 * Created by Helton on 12/10/16.
 */

public class Mensagem {

    private String text;
    private String name;
    private String photoUrl;

    public Mensagem() {
    }

    public Mensagem(String texto, String nome, String photoUrl) {
        this.text = texto;
        this.name = nome;
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
