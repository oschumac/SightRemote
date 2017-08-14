package zbp.rupbe.sightremote.adapters;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import zbp.rupbe.sightremote.R;

/**
 * Created by Tebbe Ubben on 07.07.2017.
 */

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder> {

    private ArrayList<BluetoothDevice> list;
    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public BluetoothDeviceAdapter(ArrayList<BluetoothDevice> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView deviceName;

        public ViewHolder(View itemView) {
            super(itemView);
            deviceName = (TextView) itemView.findViewById(R.id.device_name);
        }
    }


    @Override
    public BluetoothDeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bluetooth_devices, parent, false);
        if (onClickListener != null) view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BluetoothDeviceAdapter.ViewHolder holder, int position) {
        holder.deviceName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
