package com.israel.livraisonexpresspos.ui.inventory_management;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.israel.livraisonexpresspos.databinding.ItemMoveBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.Move;

import java.util.ArrayList;
import java.util.List;

public class MoveListAdapter extends RecyclerView.Adapter<MoveViewHolder> {
    Activity mActivity;
    List<Move> movesList;
    /*todo : add the move list here */
    public MoveListAdapter(Activity activity){
        this.mActivity = activity;
        movesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MoveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMoveBinding binding = ItemMoveBinding.inflate(inflater, parent, false);
         return new MoveViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MoveViewHolder holder, int position) {
        holder.bindViewHolder(movesList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return this.movesList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMovesList(List<Move> moves) {
        this.movesList = new ArrayList<>(moves);
        this.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addMove(Move move){
        this.movesList.add(0, move);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void removeMove(int position){
        this.movesList.remove(position);
        notifyDataSetChanged();
    }
}
