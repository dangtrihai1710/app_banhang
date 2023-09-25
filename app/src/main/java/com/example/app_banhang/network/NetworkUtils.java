package com.example.app_banhang.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

public class NetworkUtils {

    public interface NetworkListener {
        void onNetworkConnected();

        void onNetworkDisconnected();
    }

    private ConnectivityManager.NetworkCallback networkCallback;
    private Context context;

    public NetworkUtils(Context context) {
        this.context = context;
    }

    public void startNetworkListener(NetworkListener listener) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                if (listener != null) {
                    listener.onNetworkConnected();
                }
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                if (listener != null) {
                    listener.onNetworkDisconnected();
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }
    }

    public void stopNetworkListener() {
        if (networkCallback != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }
}
