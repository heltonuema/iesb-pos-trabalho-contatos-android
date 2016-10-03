package br.iesb.contatospos.modelo;

/**
 * Created by Helton on 09/09/16.
 */
public class UsuarioLogado  implements IUsuario{

    private String emailUsuario;
    private String facebookId;
    private String contatoUsuario;
    private String senha;
    private double ultimaLatitude;
    private double ultimaLongitude;


    public UsuarioLogado(final IUsuario usuario){
        this.setEmailUsuario(usuario.getEmailUsuario());
        this.setFacebookId(usuario.getFacebookId());
    }

    @Override
    public String getFacebookId() {
        return facebookId;
    }

    @Override
    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    @Override
    public void setContatoUsuario(String uuidContato) {
        this.contatoUsuario = uuidContato;
    }

    @Override
    public String getSenha() {
        return senha;
    }

    @Override
    public double getUltimaLatitude() {
        return ultimaLatitude;
    }

    @Override
    public double getUltimaLongitude() {
        return ultimaLongitude;
    }

    @Override
    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public void setUltimaLatitude(double latitude) {
        this.ultimaLatitude = latitude;
    }

    @Override
    public void setUltimaLongitude(double longitude) {
        this.ultimaLongitude = longitude;
    }

    @Override
    public String getEmailUsuario() {
        return emailUsuario;
    }

    @Override
    public void setEmailUsuario(String email) {
        this.emailUsuario = email;
    }

    @Override
    public String getContatoUsuario() {
        return this.contatoUsuario;
    }
}
