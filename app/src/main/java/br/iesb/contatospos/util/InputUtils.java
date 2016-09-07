package br.iesb.contatospos.util;

import android.widget.AutoCompleteTextView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import br.iesb.contatospos.exception.EntradaInvalidaException;
import br.iesb.contatospos.modelo.Contato;
import br.iesb.contatospos.modelo.Usuario;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Helton on 03/09/16.
 */
public class InputUtils {

    public static final int TAMANHO_MINIMO_SENHA = 6;

    private InputUtils(){

    }

    public static boolean isSenhaValida(final String senha, final String user, final AutoCompleteTextView inputSenha) throws EntradaInvalidaException {

        boolean retorno = true;

        if(user != null && !user.isEmpty()){

            if(senha.contains(user.split("@")[0])){
                throw new EntradaInvalidaException("Senha não deve conter nome do usuário", inputSenha);
            }

        }

        if(senha.length() < TAMANHO_MINIMO_SENHA){
            throw new EntradaInvalidaException(String.format("Senha deve ter pelo menos %s carateres", TAMANHO_MINIMO_SENHA),inputSenha);
        }

        if(!senha.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])[A-Z0-9a-z]+$")){
            throw new EntradaInvalidaException("Senha deve ser alfanumérica, com letras maiúsculas e minúsculas", inputSenha);
        }

        return retorno;
    }

    public static List<Contato> getContatosDaBaseDeDados(){

        List<Contato> retorno = null;
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Contato> results = null;

        try{
            RealmQuery<Contato> query = realm.where(Contato.class);
            results = query.findAll();

            retorno = new ArrayList<Contato>(results);

        }finally{
            realm.close();
        }

        return retorno;
    }

    public static String geraMD5(final String string) {

        MessageDigest messageDigest= null;
        try {

            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(string.getBytes(),0,string.length());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        BigInteger valor = new BigInteger(BigInteger.ONE.intValue(), messageDigest.digest());

        return String.format("%1$032X", valor);
    }

}
