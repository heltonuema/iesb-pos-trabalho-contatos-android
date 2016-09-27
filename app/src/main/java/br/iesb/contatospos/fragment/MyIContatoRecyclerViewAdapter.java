package br.iesb.contatospos.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.iesb.contatospos.R;
import br.iesb.contatospos.activity.CadastroContatoActivity;
import br.iesb.contatospos.fragment.IContatoFragment.OnListFragmentInteractionListener;
import br.iesb.contatospos.fragment.dummy.DummyContentContato.DummyItem;
import br.iesb.contatospos.modelo.Contato;
import io.realm.RealmQuery;

import java.util.Collection;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyIContatoRecyclerViewAdapter extends RecyclerView.Adapter<MyIContatoRecyclerViewAdapter.ViewHolder> {

    private final List<Contato> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyIContatoRecyclerViewAdapter(RealmQuery<Contato> items, OnListFragmentInteractionListener listener) {
        mValues = items.findAll();

        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_icontato, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getEmail());
        holder.mContentView.setText(mValues.get(position).getNome());
        CadastroContatoActivity.setPic(holder.mImageView, mValues.get(position).getUriFoto());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public Contato mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);

            mImageView = (ImageView) view.findViewById(R.id.fotoNaLista);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
