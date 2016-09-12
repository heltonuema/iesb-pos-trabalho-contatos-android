package br.iesb.contatospos.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import br.iesb.contatospos.R;

/**
 * Created by Helton on 12/09/16.
 */
public class MainFragmentActivity extends FragmentActivity {

    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.activity_lista_contatos);

        mViewPager = (ViewPager) findViewById(R.id.pager);

    }
}
