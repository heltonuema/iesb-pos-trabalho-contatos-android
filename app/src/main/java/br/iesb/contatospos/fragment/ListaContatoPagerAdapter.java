package br.iesb.contatospos.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helton on 12/09/16.
 */
public class ListaContatoPagerAdapter extends FragmentPagerAdapter {


    private static String[] OPCOES_TABS = {"CONTATOS","MENSAGENS"};
    private final List<Fragment> fragments = new ArrayList<>();

    public ListaContatoPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(new IContatoFragment());
        fragments.add(new IMensagemFragment());
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments.get(position);
        Log.i("Tab", String.valueOf(position));
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
