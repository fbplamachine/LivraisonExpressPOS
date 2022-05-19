package com.israel.livraisonexpresspos.ui.modules;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemModuleBinding;
import com.israel.livraisonexpresspos.models.Module;

import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleViewHolder> {
    private List<Module> mModules;
    private final Activity mActivity;

    public ModuleAdapter(List<Module> modules, Activity activity) {
        mModules = modules;
        mActivity = activity;
    }

    public void setModules(List<Module> modules) {
        mModules = modules;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemModuleBinding binding = ItemModuleBinding.inflate(inflater, parent, false);
        return new ModuleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        Module module = mModules.get(position);
        holder.bind(module, mActivity);
    }

    @Override
    public int getItemCount() {
        return mModules.size();
    }
}
