package br.iesb.contatospos.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Helton on 03/09/16.
 */
public class InputUtils {

    private InputUtils(){

    }

    public static boolean validaSenha(final String senha){
        return true;
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
