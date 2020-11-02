package com.techsalt.tclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.techsalt.tclient.R;
import com.techsalt.tclient.modelClass.attandanceDataModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OperationalQrPatrolHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    RecyclerView qrCodeScanRecycler;

    Context context;
    List<attandanceDataModel.Datum.QrCodeScanVisitList> qrCodeScanVisitListBeans = new ArrayList<>();
    List<attandanceDataModel.Datum.QrCodeScanVisitList> qrCodeScanVisitToSendMail = new ArrayList<>();
    boolean isFieldEditable;


    View view;

    public OperationalQrPatrolHistoryAdapter(RecyclerView qrCodeScanRecycler, Context context, List<attandanceDataModel.Datum.QrCodeScanVisitList> qrCodeScanVisitListBeans, boolean isFieldEditable) {
        this.qrCodeScanRecycler = qrCodeScanRecycler;
        this.context = context;
        this.qrCodeScanVisitListBeans = qrCodeScanVisitListBeans;
        this.isFieldEditable = isFieldEditable;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_qr_petrol, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder holder1 = (MyViewHolder) holder;

        holder1.tvSerialNumber.setText((position + 1) + ". ");
        holder1.tvSiteName.setText(qrCodeScanVisitListBeans.get(position).getQrType());
        holder1.tvQrName.setText(qrCodeScanVisitListBeans.get(position).getQrName());
        holder1.tvQrScanTimestamp.setText(qrCodeScanVisitListBeans.get(position).getTimeStamp());
        Picasso.get().load("https://www.trackkers.com/storage/siteVisitPresence/" + qrCodeScanVisitListBeans.get(position).getScanSelfie()).resize(300, 300).into(holder1.ivPostImage);


        if (isFieldEditable) {
            holder1.tvSiteName.setFocusable(true);
            holder1.tvQrName.setFocusable(true);
            holder1.tvQrScanTimestamp.setFocusable(true);
        } else {
            holder1.tvSiteName.setFocusable(false);
            holder1.tvQrName.setFocusable(false);
            holder1.tvQrScanTimestamp.setFocusable(false);
        }
    }

    @Override
    public int getItemCount() {
        return qrCodeScanVisitListBeans.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.et_qr_type)
        TextInputEditText tvSiteName;
        @BindView(R.id.et_qr_name)
        TextInputEditText tvQrName;
        @BindView(R.id.et_qr_scan_timestamp)
        TextInputEditText tvQrScanTimestamp;
        @BindView(R.id.iv_post_image)
        ImageView ivPostImage;
        @BindView(R.id.tv_serial_number)
        TextView tvSerialNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
