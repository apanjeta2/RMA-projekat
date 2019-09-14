package ba.unsa.etf.rma.ajla.projekatrma;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

public class MojResultReceiver extends ResultReceiver {
    private Receiver mReceiver;

    @SuppressLint("RestrictedApi")
    public MojResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver=receiver;
    }

    public interface Receiver {
        public void onReceiverResult(int resultCode, Bundle resultData);
    }
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(mReceiver!=null) {
            mReceiver.onReceiverResult(resultCode, resultData);
        }
    }
}
