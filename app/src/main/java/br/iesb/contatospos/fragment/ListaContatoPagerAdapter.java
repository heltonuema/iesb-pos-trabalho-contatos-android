package br.iesb.contatospos.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Helton on 12/09/16.
 */
public class ListaContatoPagerAdapter extends FragmentStatePagerAdapter {


    private static String[] OPCOES_TABS = {"CONTATOS","MENSAGENS"};

    public ListaContatoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new IContatoFragment();
                break;
            case 1:
                fragment = new IMensagemFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return OPCOES_TABS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return OPCOES_TABS[position];
    }
}
