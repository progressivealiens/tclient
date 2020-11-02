package com.techsalt.tclient.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.techsalt.tclient.R;
import com.techsalt.tclient.modelClass.attandanceDataModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class OperationalAttendanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    List<attandanceDataModel.Datum> operationalAttendanceModels;
    List<attandanceDataModel.Datum.QrCodeScanVisitList> qrCodeScanVisitListBeans = new ArrayList<>();


    String checkInTime = "", checkOutTime = "", checkinDate = "", checkoutDate = "", dutyHours;
    String siteId = "", clientId = "";
    int pos = 0;


    public OperationalAttendanceAdapter(Context context, List<attandanceDataModel.Datum> operationalAttendanceModels) {
        this.context = context;
        this.operationalAttendanceModels = operationalAttendanceModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_operational_attend_layout, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final ViewHolder holder = (ViewHolder) viewHolder;

        holder.tvSerialNumber.setText("# " + String.valueOf(i + 1));
        holder.tvName.setText(operationalAttendanceModels.get(i).getName());
        holder.tvEmpCode.setText(operationalAttendanceModels.get(i).getEmpcode());
        holder.tvLoginVia.setText("Self Mobile");
        holder.tvRating.setText("Score : " + operationalAttendanceModels.get(i).getLevel());

        dutyHours = operationalAttendanceModels.get(i).getDutyHours();
        String[] seperatedDuty = dutyHours.split(":");
        holder.tvDutyHours.setText(seperatedDuty[0] + "h : " + seperatedDuty[1] + "m : " + seperatedDuty[2] + "s");
        holder.tvCheckinBattery.setText(operationalAttendanceModels.get(i).getCheckInBatteryLevel() + " %");

        if (operationalAttendanceModels.get(i).getCheckOutBatteryLevel().equalsIgnoreCase("")) {
            holder.tvCheckoutBattery.setText("On Work");
        } else {
            holder.tvCheckoutBattery.setText(operationalAttendanceModels.get(i).getCheckOutBatteryLevel() + " %");
        }

        holder.tvCheckinTime.setText(operationalAttendanceModels.get(i).getCheckInTime());
        holder.tvCheckinAddress.setText(operationalAttendanceModels.get(i).getCheckInAddress());
        holder.tvCheckoutTime.setText(operationalAttendanceModels.get(i).getCheckOutTime());
        holder.tvCheckoutAddress.setText(operationalAttendanceModels.get(i).getCheckOutAddress());
        holder.tvEmpMobile.setText(operationalAttendanceModels.get(i).getMobile());
        Log.i("IMAGE LINKS ", "onBindViewHolder: -->" + operationalAttendanceModels.get(i).getStartImageName());
        Picasso.get().load("https://www.trackkers.com/storage/operationalCheckInImages/" + operationalAttendanceModels.get(i).getStartImageName()).resize(300, 300).into(holder.ivOpeartional);

        holder.ivOpeartional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogToViewImage(operationalAttendanceModels.get(i).getStartImageName(), v);
            }
        });

        holder.btnQrPetrolHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operationalAttendanceModels.get(i).getQrCodeScanVisitList().size() > 0) {
                    qrCodeScanVisitListBeans.clear();
                    qrCodeScanVisitListBeans.addAll(operationalAttendanceModels.get(i).getQrCodeScanVisitList());
                    openDialogToViewQrPetrolHistory(context, qrCodeScanVisitListBeans, String.valueOf(operationalAttendanceModels.get(i).getEmployeeId()));
                } else {
                    Toasty.error(context, "No qr code history found", Toasty.LENGTH_LONG, true).show();
                }
            }
        });
    }

    private void openDialogToViewImage(String image, View v) {

        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_image_layout);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) (displaymetrics.widthPixels);
        int height = (int) (displaymetrics.heightPixels);
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnDone = dialog.findViewById(R.id.btn_done);
        ImageView selfieImage = dialog.findViewById(R.id.iv_selfie);
        Picasso.get().load("https://www.trackkers.com/storage/operationalCheckInImages/" + image).placeholder(R.drawable.icons8_loader_52px).into(selfieImage);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void openDialogToViewQrPetrolHistory(Context con, List<attandanceDataModel.Datum.QrCodeScanVisitList> qrCodeScanVisitListBeans, String empId) {
        final Dialog dialog = new Dialog(con);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_qr_petrol_history);

        WindowManager wm = (WindowManager) con.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) (displaymetrics.widthPixels * 0.9);
        int height = (int) (displaymetrics.heightPixels * 0.9);
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button btnDone = dialog.findViewById(R.id.btn_done);
        RecyclerView qrPetrolHistoryRecycler = dialog.findViewById(R.id.recycler_qr_petrol_history);
        OperationalQrPatrolHistoryAdapter mAdapter;

        LinearLayoutManager manager = new LinearLayoutManager(context);
        qrPetrolHistoryRecycler.setLayoutManager(manager);
        mAdapter = new OperationalQrPatrolHistoryAdapter(qrPetrolHistoryRecycler, context, qrCodeScanVisitListBeans, false);
        qrPetrolHistoryRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return operationalAttendanceModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_serial_number)
        TextView tvSerialNumber;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_opeartional)
        ImageView ivOpeartional;
        @BindView(R.id.tv_emp_code)
        TextView tvEmpCode;
        @BindView(R.id.tv_login_via)
        TextView tvLoginVia;
        @BindView(R.id.tv_duty_hours)
        TextView tvDutyHours;
        @BindView(R.id.tv_checkin_battery)
        TextView tvCheckinBattery;
        @BindView(R.id.tv_checkout_battery)
        TextView tvCheckoutBattery;
        @BindView(R.id.tv_checkin_time)
        TextView tvCheckinTime;
        @BindView(R.id.tv_checkin_address)
        TextView tvCheckinAddress;
        @BindView(R.id.tv_checkout_time)
        TextView tvCheckoutTime;
        @BindView(R.id.tv_checkout_address)
        TextView tvCheckoutAddress;
        @BindView(R.id.btn_qr_petrol_history)
        Button btnQrPetrolHistory;
        @BindView(R.id.btn_view_tracking)
        Button btnViewTracking;
        @BindView(R.id.btn_feedback)
        Button btnFeedback;
        @BindView(R.id.btn_tracking_history)
        Button btnTrackingHistory;
        @BindView(R.id.tv_emp_mobile)
        TextView tvEmpMobile;
        @BindView(R.id.tv_rating)
        TextView tvRating;
      /*  @BindView(R.id.tv_vpmuid)
        TextView tvVpmuid;
        @BindView(R.id.tv_qrType)
        TextView tvQrType;
        @BindView(R.id.tv_qrName)
        TextView tvQrName;
        @BindView(R.id.tv_timeStamp)
        TextView tvTimeStamp;*/

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
