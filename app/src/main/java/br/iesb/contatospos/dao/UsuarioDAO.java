package br.iesb.contatospos.dao;

import java.util.UUID;

import br.iesb.contatospos.application.ContatosPos;
import br.iesb.contatospos.modelo.Contato;
import br.iesb.contatospos.modelo.IContato;
import br.iesb.contatospos.modelo.IUsuario;
import br.iesb.contatospos.modelo.Usuario;
import br.iesb.contatospos.util.InputUtils;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Helton on 02/10/16.
 */

public class UsuarioDAO {

    private String uidFirebase;

    public UsuarioDAO setUidFirebase(final String uidFirebase){
        this.uidFirebase = uidFirebase;
        return this;
    }

    public String incluiOuAltera(final IUsuario iUsuario, final IContato iContato) {

        String retorno = iUsuario.getEmailUsuario();

        final String uidFirebase = null;

        if (!InputUtils.notNullOrEmpty(retorno)) {
            throw new IllegalArgumentException("E-mail é obrigatório para usuário");
        }

        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    final String uuid = (uidFirebase != null) ? uidFirebase : getUid(iUsuario);
                    if (iContato != null) {
                        Contato contato = null;
                        if (InputUtils.notNullOrEmpty(iContato.getId())) {
                            RealmQuery<Contato> query = realm.where(Contato.class);
                            query.equalTo("id", iContato.getId());
                            contato = query.findAll().first();
                        }
                        if (contato == null) {
                            contato = new Contato();
                        }
                        contato.setId(uuid);
                        contato.setNome(iContato.getNome());
                        contato.setSobrenome(iContato.getSobrenome());
                        contato.setUriFoto(iContato.getUriFoto());
                        contato.setEmail(iContato.getEmail());
                        contato.setTelefone(iContato.getTelefone());
                        realm.copyToRealmOrUpdate(contato);
                    }

                    Usuario usuario = new Usuario();
                    usuario.setEmailUsuario(iUsuario.getEmailUsuario());
                    usuario.setSenha(iUsuario.getSenha());
                    usuario.setContatoUsuario(uuid.toString());
                    usuario.setFacebookId(iUsuario.getFacebookId());
                    realm.copyToRealmOrUpdate(usuario);
                }
            });
        }
        return retorno;
    }

    private String getUid(final IUsuario iUsuario){
        return ((iUsuario.getContatoUsuario() != null) ? UUID.fromString(iUsuario.getContatoUsuario()) : UUID.randomUUID()).toString();
    }

    public Usuario findUsuarioOnRealm(final String field, final String value) {

        Usuario retorno = null;
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmQuery<Usuario> where = realm.where(Usuario.class);
            where.equalTo(field, value);
            RealmResults<Usuario> usuarios = where.findAll();
            if (usuarios.size() == 1) {
                retorno = ContatosPos.copyFromUsuario(usuarios.first());
            }
        }
        return retorno;
    }
}
