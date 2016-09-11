package br.iesb.contatospos.modelo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Helton on 11/09/16.
 */
public class Foto extends RealmObject {

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @PrimaryKey
    private String path;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    private String base64;

}
