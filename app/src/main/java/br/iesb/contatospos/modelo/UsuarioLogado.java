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

    private String nome;
    private String sobrenome;
    private String uriFoto;

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


    public UsuarioLogado(final IUsuario usuario){
        this.setNome(usuario.getNome());
        this.setEmailUsuario(usuario.getEmailUsuario());
        this.setSobrenome(usuario.getSobrenome());
        this.setUriFoto(usuario.getUriFoto());
        this.setFacebookId(usuario.getFacebookId());
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getNome() {
        return nome;
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
    public String getSobrenome() {
        return sobrenome;
    }

    @Override
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    @Override
    public String getUriFoto() {
        return uriFoto;
    }

    @Override
    public String getContatoUsuario() {
        return this.contatoUsuario;
    }

    @Override
    public void setUriFoto(String uriFoto) {
        this.uriFoto = uriFoto;
    }

}
