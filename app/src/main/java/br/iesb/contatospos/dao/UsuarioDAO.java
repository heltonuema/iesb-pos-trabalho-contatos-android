package br.iesb.contatospos.dao;

import br.iesb.contatospos.application.ContatosPos;
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

    public String incluiOuAltera(final IUsuario iUsuario) {

        String retorno = iUsuario.getEmailUsuario();

        if(!InputUtils.notNullOrEmpty(retorno)){
            throw new IllegalArgumentException("E-mail é obrigatório para usuário");
        }


        try(Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    Usuario usuario = new Usuario();
                    usuario.setEmailUsuario(iUsuario.getEmailUsuario());
                    usuario.setSenha(iUsuario.getSenha());
                    usuario.setContatoUsuario(iUsuario.getContatoUsuario());
                    usuario.setFacebookId(iUsuario.getFacebookId());
                    usuario.setNome(iUsuario.getNome());

                    realm.copyToRealmOrUpdate(usuario);
                }
            });
        }
        return retorno;
    }

    public Usuario findUsuarioOnRealm(final String field, final String value) {

        Usuario retorno = null;
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmQuery<Usuario> where = realm.where(Usuario.class);
            where.equalTo(field, value);
            RealmResults<Usuario> usuarios = where.findAll();
            if (usuarios.size() == 1) {
                retorno = usuarios.first();
            }
        }
        return retorno;
    }
}
