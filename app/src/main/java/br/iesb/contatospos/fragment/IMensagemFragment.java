package br.iesb.contatospos.fragment;

import android.content.Context;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.iesb.contatospos.R;
import br.iesb.contatospos.activity.CadastroContatoActivity;
import br.iesb.contatospos.dao.ContatoDAO;
import br.iesb.contatospos.fragment.dummy.DummyContent;
import br.iesb.contatospos.fragment.dummy.DummyContent.DummyItem;
import br.iesb.contatospos.modelo.IContato;
import br.iesb.contatospos.modelo.Mensagem;
import br.iesb.contatospos.util.InputUtils;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class IMensagemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private FirebaseRecyclerAdapter<Mensagem, MyIMensagemRecyclerViewAdapter.ViewHolderMsg> firebaseRecyclerAdapter;
    private DatabaseReference firebaseDatabaseReference;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IMensagemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static IMensagemFragment newInstance(int columnCount) {
        IMensagemFragment fragment = new IMensagemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View outerView = inflater.inflate(R.layout.fragment_imensagem_list, container, false);

        View view = outerView.findViewById(R.id.list);
        // Set the adapter

        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            Log.i("mColumnCount", "IMensgemFragment".concat(String.valueOf(mColumnCount)));
            if (mColumnCount <= 1) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(getFirebaseAdapter(linearLayoutManager, recyclerView));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }


        }
        return view;
    }

    private FirebaseRecyclerAdapter<Mensagem, MyIMensagemRecyclerViewAdapter.ViewHolderMsg> getFirebaseAdapter(final LinearLayoutManager layoutManager, final RecyclerView recyclerView){

        final FirebaseRecyclerAdapter<Mensagem, MyIMensagemRecyclerViewAdapter.ViewHolderMsg>retorno =
                new FirebaseRecyclerAdapter<Mensagem, MyIMensagemRecyclerViewAdapter.ViewHolderMsg>(Mensagem.class, R.layout.fragment_mensagem_firebase,
                MyIMensagemRecyclerViewAdapter.ViewHolderMsg.class, firebaseDatabaseReference.child("messages")) {
            @Override
            protected void populateViewHolder(MyIMensagemRecyclerViewAdapter.ViewHolderMsg viewHolder, Mensagem mensagem, int position) {

                IContato contato = new ContatoDAO().findContatoOnRealm("email", mensagem.getName());
                final String nomeContato = (contato != null) ? contato.getNome() : mensagem.getName();
                final String fotoUri = (contato != null) ? contato.getUriFoto() : null;
                viewHolder.mIdView.setText(nomeContato);
                viewHolder.mContentView.setText(mensagem.getText());

                if(InputUtils.notNullOrEmpty(fotoUri)){
                    CadastroContatoActivity.setPic(viewHolder.usuarioImagew, contato.getUriFoto());
                }
            }
        };

        retorno.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int mensagemCount = retorno.getItemCount();
                int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();

                if(lastVisible == -1 || (positionStart >= (mensagemCount - 1) &&
                        lastVisible == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });

        return retorno;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
