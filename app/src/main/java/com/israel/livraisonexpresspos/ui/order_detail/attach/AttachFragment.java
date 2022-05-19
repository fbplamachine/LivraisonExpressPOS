package com.israel.livraisonexpresspos.ui.order_detail.attach;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.DialogAddAttachmentBinding;
import com.israel.livraisonexpresspos.databinding.FragmentAttachBinding;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.ui.order_detail.OrderDetailActivity;
import com.israel.livraisonexpresspos.utils.ImageResizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class AttachFragment extends Fragment implements View.OnClickListener, OnDeleteAttachment {
    public static final int REQUEST_CODE = 111;
    private FragmentAttachBinding mBinding;
    private AttachmentAdapter mAdapter;
    private AttachViewModel mViewModel;
    private OrderSteed mOrderSteed;
    private AlertDialog mAddAttachmentDialog;
    private String mAttachmentName;
    private String mFileExt;
    private DialogAddAttachmentBinding mAttachmentBinding;
    private File mFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_attach, container, false);
        mViewModel = new ViewModelProvider(this).get(AttachViewModel.class);
        mOrderSteed = ((OrderDetailActivity) requireActivity()).getOrderSteed();
        initUI();
        stream();
        return mBinding.getRoot();
    }

    private void initUI() {
        mAdapter = new AttachmentAdapter();
        mAdapter.setOnDeleteAttachment(this);
        mBinding.fabAttach.setOnClickListener(this);
        mViewModel.getAttachments(mOrderSteed.getInfos().getId());
        mBinding.rvAttach.setAdapter(mAdapter);
        mBinding.rvAttach.setLayoutManager(new LinearLayoutManager(requireContext()));
        initAddAttachmentDialog();
    }

    private void initAddAttachmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mAttachmentBinding = DialogAddAttachmentBinding.inflate(inflater);
        mAttachmentBinding.ivAttachment.setClipToOutline(true);
        builder.setView(mAttachmentBinding.getRoot());
        mAddAttachmentDialog = builder.create();
        mAttachmentBinding.buttonSave.setOnClickListener(v -> {
            mAttachmentBinding.etAttachmentName.setError(null);
            if (!TextUtils.isEmpty(mAttachmentBinding.etAttachmentName.getText())){
                mAttachmentName = mAttachmentBinding.etAttachmentName.getText().toString();
                postImage();
            }else {
                mAttachmentBinding.etAttachmentName.setError(getString(R.string.required_field));
            }
        });
    }

    private void stream(){
        mViewModel.getLoad().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null)return;
                if (aBoolean){
                    mBinding.progress.setVisibility(View.VISIBLE);
                    mBinding.rvAttach.setVisibility(View.GONE);
                }else {
                    mBinding.rvAttach.setVisibility(View.VISIBLE);
                    mBinding.progress.setVisibility(View.GONE);
                }
            }
        });

        mViewModel.getSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success == null)return;
            if (success){
                mAddAttachmentDialog.dismiss();
            }else {
                if (!((OrderDetailActivity)requireContext()).isFinishing())Toast.makeText(requireContext(), "Erreur", Toast.LENGTH_SHORT).show();
            }
        });

        mViewModel.getPostLoad().observe(getViewLifecycleOwner(), load -> {
            if (load == null)return;
            if (load){
                mAddAttachmentDialog.setCancelable(false);
                mAttachmentBinding.buttonSave.setVisibility(View.GONE);
                mAttachmentBinding.progress.setVisibility(View.VISIBLE);
            }else {
                mAddAttachmentDialog.setCancelable(true);
                mAttachmentBinding.buttonSave.setVisibility(View.VISIBLE);
                mAttachmentBinding.progress.setVisibility(View.GONE);
            }
        });

        mViewModel.getAttachments().observe(getViewLifecycleOwner(), attachments -> {
            if (attachments == null)return;
            Log.e("Attachments", String.valueOf(attachments.size()));
            mAdapter.updateList(attachments);
        });
    }

    @Override
    public void onClick(View v) {
        Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
        imagePickerIntent.setType("image/*");
        startActivityForResult(imagePickerIntent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e("AR", "TRY");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK && data != null){
                try {
                    Uri selectedImage = data.getData();
                    String filePath = getPath(selectedImage);
                    mFileExt = filePath.substring(filePath.lastIndexOf(".") + 1);
                    mAttachmentBinding.ivAttachment.setImageURI(selectedImage);
                    mFile = new File(filePath);
                    Log.e("ORIGINAL_SIZE", String.valueOf(mFile.length()/1024));
                    mFile = reduceImageSize(filePath);
                    if (mFile == null){
                        Log.e("FILE", "IS_NULL");
                        mFile = new File(filePath);
                    }else {
                        mAttachmentBinding.ivAttachment.setImageURI(Uri.fromFile(mFile));
                        Log.e("REDUCED_SIZE", String.valueOf(mFile.length()/1024));
                        Log.e("FILE", "IS_NOT_NULL");
                    }
                    mAttachmentBinding.etAttachmentName.setText(mFile.getName());
                    mAddAttachmentDialog.show();
                }catch (Exception e){
                    e.printStackTrace();
                    App.handleError(e);
                }
            } else {
                Log.e("DATA", "NULL");
            }
        }else {
            Log.e("AR", "NULL");
        }
    }

    private File reduceImageSize(String filePath){
        try {
            Bitmap originalImage;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                ParcelFileDescriptor parcelFileDescriptor = requireActivity().getContentResolver()
                        .openFileDescriptor(Uri.fromFile(new File(filePath)), "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                originalImage = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            } else {
                originalImage = BitmapFactory.decodeFile(filePath);
            }
            Bitmap reducedImage = ImageResizer.reduceBitmapSize(originalImage, 307200);
            String destinationPath = requireActivity().getExternalFilesDir(null).getAbsolutePath();
            File file = new File(destinationPath + File.separator + System.currentTimeMillis() + ".jpeg");
            boolean fileCreated = file.createNewFile();
            if (fileCreated){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                reducedImage.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] data = stream.toByteArray();
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(data);
                outputStream.flush();
                outputStream.close();
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
            App.handleError(e);
        }
        return null;
    }

    private void postImage(){
        if (mFileExt.equals("img") || mFileExt.equals("jpg")
                || mFileExt.equals("jpeg") || mFileExt.equals("gif") || mFileExt.equals("png")){
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), mFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("attachment", mAttachmentName, reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
            mViewModel.postImage(mOrderSteed.getInfos().getId(), body, name);
        }else {
            if (!((OrderDetailActivity)requireContext()).isFinishing())Toast.makeText(requireContext(), "Erreur", Toast.LENGTH_SHORT).show();
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = requireActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    @Override
    public void delete(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setIcon(R.drawable.ic_round_warning_24).setTitle(getString(R.string.warning))
                .setMessage("Vous êtes sur le point de supprimer cette piece jointe.")
                .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModel.deleteAttachment(mOrderSteed.getInfos().getId(), id);
                    }
                })
                .setNegativeButton("Annuler", null);
        builder.create().show();
    }

}
