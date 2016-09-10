package br.iesb.contatospos.modelo;

/**
 * Created by Helton on 09/09/16.
 */
public class UsuarioLogado  implements IUsuario{

    private String email;
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

    private String facebookId;

    public UsuarioLogado(final IUsuario usuario){
        this.setNome(usuario.getNome());
        this.setEmail(usuario.getEmail());
        this.setSobrenome(usuario.getSobrenome());
        this.setUriFoto(usuario.getUriFoto());
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getUriFoto() {
        return uriFoto;
    }

    public void setUriFoto(String uriFoto) {
        this.uriFoto = uriFoto;
    }

}
