/*******************************************************************************
 * Copyright (c) 1999, 2014 IBM Corp.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * <p>
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */
package com.example.zhangkai.gpstraker.NetWork;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.zhangkai.gpstraker.R;
import com.example.zhangkai.gpstraker.ui.NotificationDialogFragment;
import com.example.zhangkai.gpstraker.ui.NotifyActivity;

/**
 * Handles call backs from the MQTT Client
 *
 */
public class MqttCallbackHandler implements MqttCallback {

    /** {@link Context} for the application used to format and import external strings**/
    private Context context;
    /** Client handle to reference the connection that this handler is attached to**/
    private String clientHandle;

    /**
     * Creates an <code>MqttCallbackHandler</code> object
     * @param context The application's context
     * @param clientHandle The handle to a {@link } object
     */
    public MqttCallbackHandler(Context context, String clientHandle) {
        this.context = context;
        this.clientHandle = clientHandle;
    }

    /**
     * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.Throwable)
     */
    @Override
    public void connectionLost(Throwable cause) {
//	  cause.printStackTrace();
        if (cause != null) {
            //MqttConnections.getInstance().getConnection(clientHandle).connect(10);
            MqttConn.getInstance(context, "zhangkai").connect();
            Intent intent = new Intent(context, NotifyActivity.class);
            context.startActivity(intent);

            NotificationManager mNotifyManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setContentTitle("Picture Download")
                    .setContentText("Download in progress")
                    .setSmallIcon(R.drawable.loction);
            mNotifyManager.notify(1222,mBuilder.build());

            NotificationDialogFragment dialog = new NotificationDialogFragment();
            dialog.getDialog().show();
        }
    }

    /**
     * @see org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String, org.eclipse.paho.client.mqttv3.MqttMessage)
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        //Get connection object associated with this object
        //create arguments to format message arrived notifcation string
        //get the string from strings.xml and format
        //create intent to start activity
        Toast.makeText(context,topic, Toast.LENGTH_SHORT).show();

    }

    /**
     * @see org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken)
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Do nothing
    }

}
