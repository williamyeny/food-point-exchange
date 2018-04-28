package edu.duke.compsci290.fpx;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

/**
 * Created by Serena on 4/24/18.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    private Context mContext;
    private Transaction[] mTransactions;
    //private String[] senderIDs;


    public ProfileAdapter(final Context context, Transaction[] transactions) {
        mContext = context;
        mTransactions = transactions.clone();
//
//        for (int k = 0; k<mTransactions.length; k++) {
//            senderIDs[k] = mTransactions[k].getmSenderID();
//        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = mInflater.inflate(R.layout.profile_holder, parent, false);
        final ViewHolder profileHolder = new ViewHolder(row);

        return profileHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //The process of preparing a child view to display data corresponding to a position within the adapter.
        Transaction tx = mTransactions[position];
        String senderID= tx.getmSenderID();
        String receiverID= tx.getmReceiverID();
        String amtID= Integer.toString(tx.getmAmount());
        String dateID = tx.getmDate();

        StringBuilder sb = new StringBuilder();
        sb.append(senderID);
        sb.append(" gave ");
        sb.append(amtID);
        sb.append(" food points to ");
        sb.append(receiverID);
        sb.append(" on ");
        sb.append(dateID);
        sb.append(".");

        holder.mTransact.setText(sb.toString());

    }

    @Override
    public int getItemCount() {
        return mTransactions.length; //length of the recycler view
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLinearLayout;
        private TextView mTransact;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mLinearLayout = itemView.findViewById(R.id.profile_holder_linear_layout);
            this.mTransact = itemView.findViewById(R.id.transaction_textview);

        }

    }
}
