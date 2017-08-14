package zbp.rupbe.sightremote.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import zbp.rupbe.sightparser.Message;
import zbp.rupbe.sightparser.applayer.descriptors.ActiveBolus;
import zbp.rupbe.sightparser.applayer.statusreader.AppActiveBoluses;
import zbp.rupbe.sightparser.applayer.statusreader.AppBatteryAmount;
import zbp.rupbe.sightparser.applayer.statusreader.AppCartridgeAmount;
import zbp.rupbe.sightparser.applayer.statusreader.AppCurrentBasal;
import zbp.rupbe.sightparser.applayer.statusreader.AppCurrentTBR;
import zbp.rupbe.sightparser.applayer.AppLayerMessage;
import zbp.rupbe.sightparser.applayer.statusreader.AppPumpStatus;
import zbp.rupbe.sightparser.applayer.descriptors.BolusType;
import zbp.rupbe.sightparser.applayer.descriptors.PumpStatus;
import zbp.rupbe.sightremote.R;
import zbp.rupbe.sightremote.aidl.IMessageCallback;

/**
 * Created by Tebbe Ubben on 09.07.2017.
 */

public class StatusFragment extends BaseFragment {

    private ImageView pumpStatusIcon;
    private TextView pumpStatusText;
    private TextView currentBasalName;
    private TextView currentBasalAmount;
    private TextView cartridgeAmount;
    private TextView batteryAmount;
    private TextView tbrTime;
    private TextView tbrAmount;
    private ProgressBar tbrTimeVisual;
    private LinearLayout tbrContainer;
    private LinearLayout bolus1;
    private ImageView bolus1Icon;
    private TextView bolus1Amount;
    private TextView bolus1Delay;
    private ImageView bolus1DelayIcon;
    private ProgressBar bolus1AmountVisual;
    private LinearLayout bolus2;
    private ImageView bolus2Icon;
    private TextView bolus2Amount;
    private TextView bolus2Delay;
    private ImageView bolus2DelayIcon;
    private ProgressBar bolus2AmountVisual;
    private LinearLayout bolus3;
    private ImageView bolus3Icon;
    private TextView bolus3Amount;
    private TextView bolus3Delay;
    private ImageView bolus3DelayIcon;
    private ProgressBar bolus3AmountVisual;
    private TextView bolusTitle;

    private PumpStatus sPumpStatus;
    private String sCurrentBasalName;
    private float sCurrentBasalAmount;
    private float sCartridgeAmount;
    private int sBatteryAmount;
    private int sTBRTime;
    private int sTBRTimeInitial;
    private int sTBRAmount;
    private ActiveBolus sBolus1;
    private ActiveBolus sBolus2;
    private ActiveBolus sBolus3;
    private boolean sPending = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("pumpStatus", sPumpStatus);
        outState.putString("currentBasalName", sCurrentBasalName);
        outState.putFloat("currentBasalAmount", sCurrentBasalAmount);
        outState.putFloat("cartridgeAmount", sCartridgeAmount);
        outState.putInt("batteryAmount", sBatteryAmount);
        outState.putInt("tbrTime", sTBRTime);
        outState.putInt("tbrTimeInitial", sTBRTimeInitial);
        outState.putInt("tbrAmount", sTBRAmount);
        outState.putParcelable("bolus1", sBolus1);
        outState.putParcelable("bolus2", sBolus2);
        outState.putParcelable("bolus3", sBolus3);
        outState.putBoolean("pending", sPending);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.status);

        View view = getView();
        pumpStatusText = (TextView) view.findViewById(R.id.status_text);
        pumpStatusIcon = (ImageView) view.findViewById(R.id.status_icon);
        currentBasalName = (TextView) view.findViewById(R.id.current_basal_name);
        currentBasalAmount = (TextView) view.findViewById(R.id.current__basal_amount);
        cartridgeAmount = (TextView) view.findViewById(R.id.cartridge_amount);
        batteryAmount = (TextView) view.findViewById(R.id.battery_amount);
        tbrTime = (TextView) view.findViewById(R.id.tbr_time);
        tbrAmount = (TextView) view.findViewById(R.id.tbr_amount);
        tbrTimeVisual = (ProgressBar) view.findViewById(R.id.tbr_time_visual);
        tbrContainer = (LinearLayout) view.findViewById(R.id.tbr_container);
        bolus1 = (LinearLayout) view.findViewById(R.id.bolus1);
        bolus1Icon = (ImageView) view.findViewById(R.id.bolus1_icon);
        bolus1Amount = (TextView) view.findViewById(R.id.bolus1_amount);
        bolus1Delay = (TextView) view.findViewById(R.id.bolus1_delay);
        bolus1DelayIcon = (ImageView) view.findViewById(R.id.bolus1_delay_icon);
        bolus1AmountVisual = (ProgressBar) view.findViewById(R.id.bolus1_amount_visual);
        bolus2 = (LinearLayout) view.findViewById(R.id.bolus2);
        bolus2Icon = (ImageView) view.findViewById(R.id.bolus2_icon);
        bolus2Amount = (TextView) view.findViewById(R.id.bolus2_amount);
        bolus2Delay = (TextView) view.findViewById(R.id.bolus2_delay);
        bolus2DelayIcon = (ImageView) view.findViewById(R.id.bolus2_delay_icon);
        bolus2AmountVisual = (ProgressBar) view.findViewById(R.id.bolus2_amount_visual);
        bolus3 = (LinearLayout) view.findViewById(R.id.bolus3);
        bolus3Icon = (ImageView) view.findViewById(R.id.bolus3_icon);
        bolus3Amount = (TextView) view.findViewById(R.id.bolus3_amount);
        bolus3Delay = (TextView) view.findViewById(R.id.bolus3_delay);
        bolus3DelayIcon = (ImageView) view.findViewById(R.id.bolus3_delay_icon);
        bolus3AmountVisual = (ProgressBar) view.findViewById(R.id.bolus3_amount_visual);
        bolusTitle = (TextView)view.findViewById(R.id.bolus_title);

        if (savedInstanceState != null) {
            sPumpStatus = savedInstanceState.getParcelable("pumpStatus");
            sCurrentBasalName = savedInstanceState.getString("currentBasalName");
            sCurrentBasalAmount = savedInstanceState.getFloat("currentBasalAmount");
            sCartridgeAmount = savedInstanceState.getFloat("cartridgeAmount");
            sBatteryAmount = savedInstanceState.getInt("batteryAmount");
            sTBRTime = savedInstanceState.getInt("tbrTime");
            sTBRTimeInitial= savedInstanceState.getInt("tbrTimeInitial");
            sTBRAmount = savedInstanceState.getInt("tbrAmount");
            sBolus1 = savedInstanceState.getParcelable("bolus1");
            sBolus2 = savedInstanceState.getParcelable("bolus2");
            sBolus3 = savedInstanceState.getParcelable("bolus3");
            sPending = savedInstanceState.getBoolean("pending");
            if (!sPending) {
                setPumpStatusView();
                setCurrentBasalView();
                setCartridgeAmountView();
                setTBRView();
                setBatteryAmountView();
                setBolusView();
            }
        }
    }

    private IMessageCallback messageCallback = new IMessageCallback() {
        @Override
        public void onMessage(final Message message) throws RemoteException {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (message instanceof AppPumpStatus) {
                        sPumpStatus = ((AppPumpStatus) message).getPumpStatus();
                    } else if (message instanceof AppCurrentBasal) {
                        AppCurrentBasal currentBasal = (AppCurrentBasal) message;
                        sCurrentBasalName = currentBasal.getCurrentBasalName();
                        sCurrentBasalAmount = currentBasal.getCurrentBasalAmount();
                    } else if (message instanceof AppBatteryAmount) {
                        AppBatteryAmount batteryAmount = (AppBatteryAmount) message;
                        sBatteryAmount = batteryAmount.getBatteryAmount();
                    } else if (message instanceof AppCartridgeAmount) {
                        AppCartridgeAmount cartridgeAmount = (AppCartridgeAmount) message;
                        sCartridgeAmount = cartridgeAmount.getCartridgeAmount();
                    } else if (message instanceof AppCurrentTBR) {
                        AppCurrentTBR currentTBR = (AppCurrentTBR) message;
                        sTBRAmount = currentTBR.getPercentage();
                        sTBRTime = currentTBR.getLeftoverTime();
                        sTBRTimeInitial = currentTBR.getInitialTime();
                    } else if (message instanceof AppActiveBoluses) {
                        AppActiveBoluses activeBoluses = (AppActiveBoluses) message;
                        sBolus1 = activeBoluses.getBolus1();
                        sBolus2 = activeBoluses.getBolus2();
                        sBolus3 = activeBoluses.getBolus3();
                        sPending = false;
                        setPumpStatusView();
                        setBolusView();
                        setCurrentBasalView();
                        setTBRView();
                        setCartridgeAmountView();
                        setBatteryAmountView();
                    }
                }
            });
        }

        @Override
        public boolean isInbound() throws RemoteException {
            return true;
        }

        @Override
        public boolean isOutbound() throws RemoteException {
            return false;
        }

        @Override
        public String getMessageClass() throws RemoteException {
            return AppLayerMessage.class.getName();
        }

        @Override
        public IBinder asBinder() {
            return getMainActivity().getConnectionService().asBinder();
        }
    };

    private void setPumpStatusView() {
        if (sPumpStatus == PumpStatus.PAUSED) {
            pumpStatusIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_paused));
            pumpStatusText.setText(R.string.paused);
        } else if (sPumpStatus == PumpStatus.STARTED) {
            pumpStatusIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_started));
            pumpStatusText.setText(R.string.started);
        } else if (sPumpStatus == PumpStatus.STOPPED) {
            pumpStatusIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_stopped));
            pumpStatusText.setText(R.string.stopped);
        } else return;
        pumpStatusIcon.setVisibility(View.VISIBLE);
    }

    private void setCurrentBasalView() {
        currentBasalName.setText(sCurrentBasalName);
        if (sTBRAmount == 100 || currentBasalName == null) {
            currentBasalAmount.setTypeface(null, Typeface.BOLD);
            currentBasalAmount.setAllCaps(true);
        } else {
            currentBasalAmount.setTypeface(null, Typeface.ITALIC);
            currentBasalAmount.setAllCaps(false);
        }
        currentBasalAmount.setText(getString(R.string.basal_unit, sCurrentBasalAmount / 100 * sTBRAmount));
    }

    private void setBatteryAmountView() {
        StatusFragment.this.batteryAmount.setText(getString(R.string.battery_unit, sBatteryAmount));
    }

    private void setCartridgeAmountView() {
        StatusFragment.this.cartridgeAmount.setText(getString(R.string.insulin_unit, sCartridgeAmount));
    }

    private void setTBRView() {
        if (sTBRAmount != 100) {
            tbrAmount.setText(getString(R.string.tbr_amount, sTBRAmount));
            tbrTime.setText(formatTBRTime(sTBRTime));
            tbrTimeVisual.setMax(sTBRTimeInitial);
            tbrTimeVisual.setProgress(sTBRTime);
            tbrContainer.setVisibility(View.VISIBLE);
        } else tbrContainer.setVisibility(View.GONE);
    }

    private void setBolusView() {
        boolean displayTitle = false;
        if (sBolus1 == null || sBolus1.getLeftoverAmount() == 0F) bolus1.setVisibility(View.GONE);
        else {
            displayTitle = true;
            bolus1.setVisibility(View.VISIBLE);
            bolus1Amount.setText(getString(R.string.insulin_unit, sBolus1.getLeftoverAmount()));
            bolus1AmountVisual.setMax((int) (sBolus1.getInitialAmount() * 100));
            bolus1AmountVisual.setProgress((int) (sBolus1.getLeftoverAmount() * 100));
            setIcon(sBolus1.getBolusType(), bolus1Icon);
            setBolusDelay(bolus1Delay, bolus1DelayIcon, sBolus1.getBolusType(), sBolus1.getDelay());
            setBolusProgressColor(sBolus1.getBolusType(), bolus1AmountVisual);
        }
        if (sBolus2 == null || sBolus2.getLeftoverAmount() == 0F) bolus2.setVisibility(View.GONE);
        else {
            displayTitle = true;
            bolus2.setVisibility(View.VISIBLE);
            bolus2Amount.setText(getString(R.string.insulin_unit, sBolus2.getLeftoverAmount()));
            bolus2AmountVisual.setMax((int) (sBolus2.getInitialAmount() * 100));
            bolus2AmountVisual.setProgress((int) (sBolus2.getLeftoverAmount() * 100));
            setIcon(sBolus2.getBolusType(), bolus2Icon);
            setBolusDelay(bolus2Delay, bolus2DelayIcon, sBolus2.getBolusType(), sBolus2.getDelay());
            setBolusProgressColor(sBolus2.getBolusType(), bolus2AmountVisual);
        }
        if (sBolus3 == null || sBolus3.getLeftoverAmount() == 0F) bolus3.setVisibility(View.GONE);
        else {
            displayTitle = true;
            bolus3.setVisibility(View.VISIBLE);
            bolus3Amount.setText(getString(R.string.insulin_unit, sBolus3.getLeftoverAmount()));
            bolus3AmountVisual.setMax((int) (sBolus3.getInitialAmount() * 100));
            bolus3AmountVisual.setProgress((int) (sBolus3.getLeftoverAmount() * 100));
            setIcon(sBolus3.getBolusType(), bolus3Icon);
            setBolusDelay(bolus3Delay, bolus3DelayIcon, sBolus3.getBolusType(), sBolus3.getDelay());
            setBolusProgressColor(sBolus3.getBolusType(), bolus3AmountVisual);
        }
        bolusTitle.setVisibility(displayTitle ? View.VISIBLE : View.GONE);
    }

    private void setBolusProgressColor(BolusType bolusType, ProgressBar progressBar) {
        if (bolusType == BolusType.INSTANT) {
            progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#0127fe"), PorterDuff.Mode.SRC_IN);
        } else if (bolusType == BolusType.EXTENDED) {
            progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#e69900"), PorterDuff.Mode.SRC_IN);
        } else if (bolusType == BolusType.MULTIWAVE) {
            progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#29702d"), PorterDuff.Mode.SRC_IN);
        }
    }


    private void setIcon(BolusType bolusType, ImageView imageView) {
        if (bolusType == BolusType.INSTANT) {
            imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_instant_bolus));
        } else if (bolusType == BolusType.EXTENDED) {
            imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_extended_bolus));
        } else if (bolusType == BolusType.MULTIWAVE) {
            imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_multiwave_bolus));
        }
    }

    private void setBolusDelay(TextView textView, ImageView imageView, BolusType bolusType, int delay) {
        if (bolusType == BolusType.INSTANT) {
            textView.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }
        else {
            textView.setText(formatTBRTime(delay));
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    private String formatTBRTime(int minutes) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.time_formatter));
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(new Date(minutes * 60 * 1000));
    }

    @Override
    public void onStart() {
        super.onStart();
        getMainActivity().registerMessageCallback("zbp.rupbe.sightremote.fragments.StatusFragment.MESSAGECALLBACK", messageCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        getMainActivity().unregisterMessageCallback("zbp.rupbe.sightremote.fragments.StatusFragment.MESSAGECALLBACK");
    }

    @Override
    public boolean autoUpdate() {
        return true;
    }

    @Override
    public void update() {
        if (getMainActivity() == null) return;
        try {
            getMainActivity().getConnectionService().send(new AppPumpStatus());
            getMainActivity().getConnectionService().send(new AppBatteryAmount());
            getMainActivity().getConnectionService().send(new AppCartridgeAmount());
            getMainActivity().getConnectionService().send(new AppCurrentTBR());
            getMainActivity().getConnectionService().send(new AppCurrentBasal());
            getMainActivity().getConnectionService().send(new AppActiveBoluses());
        } catch (RemoteException e) {
        }
    }
}
