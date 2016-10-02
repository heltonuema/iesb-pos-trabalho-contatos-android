package br.iesb.contatospos.modelo;

/**
 * Created by andre on 09/09/2016.
 */
public class ContatoImpl implements IContato {

    private String id;

    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private String uriFoto;

    public ContatoImpl (final IContato contato){

        this.nome = contato.getNome();
        this.email = contato.getEmail();
        this.telefone = contato.getTelefone();
        this.uriFoto = contato.getUriFoto();
    }

    @Override
    public String getSobrenome() {
        return sobrenome;
    }

    @Override
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setId(final String id){
        this.id = new String(id);
    }

    public String getId(){
        return this.id;
    }

    public String getUriFoto(){
        return new String(uriFoto);
    }

    public void setUriFoto(final String uri){
        this.uriFoto = uri;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = new String(nome);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = new String(email);
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = new String(telefone);
    }








}
