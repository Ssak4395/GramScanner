package com.example.bar_code_scanner.utilities;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bar_code_scanner.R;
import com.example.bar_code_scanner.StockItemFoundActivity;
import com.example.bar_code_scanner.model.CoilMaster;
import com.example.bar_code_scanner.model.Scan;
import com.example.bar_code_scanner.view_model.CoilMasterViewModel;
import com.example.bar_code_scanner.view_model.ScanViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Scan_Adapter extends  RecyclerView.Adapter<Scan_Adapter.scanHolder>{

    private ScanViewModel scanViewModel;
    private CoilMasterViewModel coilMasterViewModel;
    private Context mContext;

    public List<Scan> getScans() {
        return scans;
    }

    public void setScans(List<Scan> scans) {
        this.scans = scans;
        notifyDataSetChanged();
    }

    private List<Scan> scans = new ArrayList<>();


    public Scan_Adapter(Context context,ScanViewModel scanViewModel, CoilMasterViewModel coilMasterViewModel)
    {
        this.scanViewModel = scanViewModel;
        this.coilMasterViewModel = coilMasterViewModel;
        this.mContext = context;
    }

    @NonNull
    @Override
    public scanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item,parent,false);
        return new scanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull scanHolder holder, int position) {
        Scan current = scans.get(position);
        holder.coilID.setText(current.getCOILDID());
        holder.dateScanned.setText(Helper.dateToString(current.getTimeScanned()));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                scanViewModel.delete(scans.get(position));
                Toast.makeText(mContext,"Item deleted",Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CoilMaster coilFromScan = coilMasterViewModel.getSpecificCoilFromDB(scans.get(position).getCOILDID());
                    Intent intent = new Intent(mContext,StockItemFoundActivity.class);
                    intent.putExtra("map",parcelToActivity(coilFromScan));
                    mContext.startActivity(intent);

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            }
        });
//
    }

    private HashMap parcelToActivity(CoilMaster coilMaster)
    {
        HashMap<String, String> parcel = new HashMap<String, String>();
        parcel.put("CoilID",coilMaster.getCOILID());
        parcel.put("Type",coilMaster.getTYPE());
        parcel.put("Status",coilMaster.getSTATUS());
        parcel.put("Gauge",coilMaster.getGAUGE());
        parcel.put("Weight",Integer.toString(coilMaster.getWEIGHT()));
        parcel.put("Width",Integer.toString(coilMaster.getWIDTH()));
        parcel.put("Color",coilMaster.getCOLOR());
        return parcel;
    }



    @Override
    public int getItemCount() {
        return scans.size();
    }

    class scanHolder extends RecyclerView.ViewHolder{
        private TextView coilID;
        private TextView dateScanned;

        public scanHolder(@NonNull View itemView) {
            super(itemView);
             coilID = itemView.findViewById(R.id.coil_id_text);
             dateScanned = itemView.findViewById(R.id.date_scanned);
        }
    }
}
