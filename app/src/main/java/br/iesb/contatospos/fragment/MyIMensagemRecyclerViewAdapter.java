package br.iesb.contatospos.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.iesb.contatospos.R;
import br.iesb.contatospos.fragment.IMensagemFragment.OnListFragmentInteractionListener;
import br.iesb.contatospos.fragment.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */

public class MyIMensagemRecyclerViewAdapter extends RecyclerView.Adapter<MyIMensagemRecyclerViewAdapter.ViewHolderMsg> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyIMensagemRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolderMsg onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_mensagem_firebase, parent, false);
        return new ViewHolderMsg(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderMsg holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

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

    public static class ViewHolderMsg extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView usuarioImagew;
        public DummyItem mItem;

        public ViewHolderMsg(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            usuarioImagew = (ImageView) view.findViewById(R.id.usuarioImageView);
        }



        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}
