package com.israel.livraisonexpresspos.ui.order_detail.content.feed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentFeedBinding;
import com.israel.livraisonexpresspos.models.Feed;

import java.util.ArrayList;
import java.util.List;

public class FeedDialogFragment extends AppCompatDialogFragment {
    private FeedViewModel mViewModel;
    private FragmentFeedBinding mBinding;
    private ActivityFeedAdapter mAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        mViewModel = new ViewModelProvider(requireActivity()).get(FeedViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = FragmentFeedBinding.inflate(inflater);
        builder.setView(mBinding.getRoot());
        initUi();
        return builder.create();
    }


    private void initUi() {
        mAdapter = new ActivityFeedAdapter(new ArrayList<Feed>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        mBinding.rvFeedItem.setLayoutManager(layoutManager);
        mBinding.rvFeedItem.setAdapter(mAdapter);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mViewModel.loadOrderFeed(getArguments().getInt("order_id"), new FeedViewModel.OnFeedRequestResponse() {
            @Override
            public void onFeedUnAvailable() {
                mBinding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFeedAvailable(List<Feed> feedItems) {
                // hide the loader test array size
                mBinding.progressBar.setVisibility(View.GONE);
                mViewModel.setFeedItems(feedItems);
                mAdapter.setFeedItems(mViewModel.getFeedItems());
            }
        });
        mBinding.imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
