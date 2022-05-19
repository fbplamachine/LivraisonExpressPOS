package com.israel.livraisonexpresspos.ui.order_detail.comment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Room.repository.UnSyncedRepository;
import com.israel.livraisonexpresspos.databinding.BottomSheetAutoMessagesBinding;
import com.israel.livraisonexpresspos.databinding.FragmentCommentBinding;
import com.israel.livraisonexpresspos.models.Comment;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.ui.order_detail.OrderDetailActivity;
import com.israel.livraisonexpresspos.uiComponents.Utilities;

import java.util.List;

public class CommentFragment extends Fragment implements View.OnClickListener, View.OnLayoutChangeListener {
    private FragmentCommentBinding mBinding;
    private CommentViewModel mViewModel;
    private final CommentAdapter mCommentAdapter = new CommentAdapter();;
    private OrderDetailActivity mActivity;
    private OrderSteed mOrderSteed;
    private BottomSheetDialog mBottomSheetDialog;
    private boolean mAlreadyFetchedUnSynced = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(CommentViewModel.class);
        mActivity = (OrderDetailActivity) requireActivity();
        mOrderSteed = mActivity.getOrderSteed();
        initUI();
        stream();
        return mBinding.getRoot();
    }

    private void initUI() {
        mBinding.rvComments.setAdapter(mCommentAdapter);
        mBinding.rvComments.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.ivSend.setOnClickListener(this);
        mBinding.ivShowAutoMessage.setOnClickListener(this);
        mBinding.rvComments.addOnLayoutChangeListener(this);
        initBottomSheet();
    }

    private void initBottomSheet() {
        mBottomSheetDialog = new BottomSheetDialog(requireActivity()
                , R.style.BottomSheetDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        BottomSheetAutoMessagesBinding binding = BottomSheetAutoMessagesBinding.inflate(inflater);
        binding.tvText1.setOnClickListener(this);
        binding.tvText2.setOnClickListener(this);
        binding.tvText3.setOnClickListener(this);
        binding.tvText4.setOnClickListener(this);
        binding.tvText5.setOnClickListener(this);
        binding.tvText6.setOnClickListener(this);
        binding.tvText7.setOnClickListener(this);
        binding.tvText8.setOnClickListener(this);
        binding.tvText9.setOnClickListener(this);
        binding.tvText10.setOnClickListener(this);
        binding.tvText11.setOnClickListener(this);
        binding.tvText12.setOnClickListener(this);
        mBottomSheetDialog.setContentView(binding.getRoot());
    }

    private void stream(){
        mViewModel.getLoading().observe(requireActivity(), loading -> {
            if (loading ==null)return;
            if (loading){
                mBinding.progressBar.setVisibility(View.VISIBLE);
                mBinding.rvComments.setVisibility(View.GONE);
                mBinding.tvNoComment.setVisibility(View.GONE);
            }else {
                mBinding.rvComments.setVisibility(View.VISIBLE);
                mBinding.progressBar.setVisibility(View.GONE);
            }
        });
        mViewModel.getComments(mOrderSteed.getInfos().getId()).observe(requireActivity(), new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> comments) {
                if (comments == null)return;
                mBinding.progressBar.setVisibility(View.GONE);
                if (comments.isEmpty()){
                    mBinding.tvNoComment.setVisibility(View.VISIBLE);
                }else {
                    mCommentAdapter.setComments(comments);
                    mBinding.tvNoComment.setVisibility(View.GONE);
                    mBinding.rvComments.smoothScrollToPosition(mCommentAdapter.getItemCount() - 1);
                }

//                fetchUnSyncedComments();
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.ivSend.getId()){
            String comment = mBinding.etComment.getText().toString();
            if (!TextUtils.isEmpty(comment)){
                mViewModel.postComment(mOrderSteed.getInfos().getId(), comment);
                mBinding.etComment.setText("");
                mBinding.etComment.clearFocus();
                Utilities.hideKeyBoard(requireActivity());
            }
        }else if (id == mBinding.ivShowAutoMessage.getId()){
            mBottomSheetDialog.show();
        }else if (v instanceof TextView){
            mBottomSheetDialog.dismiss();
            mBinding.etComment.setText(((TextView) v).getText());
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (mCommentAdapter.getItemCount() > 0){
            mBinding.rvComments.smoothScrollToPosition(mCommentAdapter.getItemCount() - 1);
        }
    }
}
