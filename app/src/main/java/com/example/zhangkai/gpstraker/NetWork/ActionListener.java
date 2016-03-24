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

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import android.content.Context;

/**
 * This Class handles receiving information from the
 // * {@link } and updating the {@link } associated with
 * the action
 */
class ActionListener implements IMqttActionListener {

    /**
     * Actions that can be performed Asynchronously <strong>and</strong> associated with a
     * {@link ActionListener} object
     *
     */
    enum Action {
        /** Connect Action **/
        CONNECT,
        /** Disconnect Action **/
        DISCONNECT,
        /** Subscribe Action **/
        SUBSCRIBE,
        /** Publish Action **/
        PUBLISH
    }

    /**
     * The {@link Action} that is associated with this instance of
     * <code>ActionListener</code>
     **/
    private Action action;
    /** The arguments passed to be used for formatting strings**/
    private String[] additionalArgs;
    /** Handle of the { Connection} this action was being executed on **/
    private String clientHandle;
    /** {@link Context} for performing various operations **/
    private Context context;

    /**
     * Creates a generic action listener for actions performed form any activity
     *
     * @param context
     *            The application context
     * @param action
     *            The action that is being performed
     * @param clientHandle
     *            The handle for the client which the action is being performed
     *            on
     * @param additionalArgs
     *            Used for as arguments for string formating
     */
    public ActionListener(Context context, Action action,
                          String clientHandle, String... additionalArgs) {
        this.context = context;
        this.action = action;
        this.clientHandle = clientHandle;
        this.additionalArgs = additionalArgs;
    }

    /**
     * The action associated with this listener has been successful.
     *
     * @param asyncActionToken
     *            This argument is not used
     */
    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        switch (action) {
            case CONNECT:
                try {
                    connect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
            case DISCONNECT:
                disconnect();
                break;
            case SUBSCRIBE:
                subscribe();
                break;
            case PUBLISH:
                publish();
                break;
        }

    }

    /**
     * A publish action has been successfully completed, update connection
     * object associated with the client this action belongs to, then notify the
     * user of success
     */
    private void publish() {

    }

    /**
     * A subscribe action has been successfully completed, update the connection
     * object associated with the client this action belongs to and then notify
     * the user of success
     */
    private void subscribe() {

    }

    /**
     * A disconnection action has been successfully completed, update the
     * connection object associated with the client this action belongs to and
     * then notify the user of success.
     */
    private void disconnect() {
    }

    /**
     * A connection action has been successfully completed, update the
     * connection object associated with the client this action belongs to and
     * then notify the user of success.
     */
    private void connect() throws MqttException {
        MqttConn.getInstance(context, "zhangkai").subscribe("zhangkai", 0);
    }

    /**
     * The action associated with the object was a failure
     *
     * @param token
     *            This argument is not used
     * @param exception
     *            The exception which indicates why the action failed
     */
    @Override
    public void onFailure(IMqttToken token, Throwable exception) {
        switch (action) {
            case CONNECT:
                //       MqttConnections.getInstance().getConnection(token.getClient().getClientId()).setConnectionStatus(MqttConnection.MqttConnectionStatus.ERROR);
                //MqttConnections.getInstance().getConnection(token.getClient().getClientId()).connect(10);
                MqttConn.getInstance(context, "zhangkai").connect();
                connect(exception);
                break;
            case DISCONNECT:
                disconnect(exception);
                break;
            case SUBSCRIBE:
                subscribe(exception);
                break;
            case PUBLISH:
                publish(exception);
                break;
        }

    }

    /**
     * A publish action was unsuccessful, notify user and update client history
     *
     * @param exception
     *            This argument is not used
     */
    private void publish(Throwable exception) {
    }

    /**
     * A subscribe action was unsuccessful, notify user and update client history
     * @param exception This argument is not used
     */
    private void subscribe(Throwable exception) {
    }

    /**
     * A disconnect action was unsuccessful, notify user and update client history
     * @param exception This argument is not used
     */
    private void disconnect(Throwable exception) {
    }

    /**
     * A connect action was unsuccessful, notify the user and update client history
     * @param exception This argument is not used
     */
    private void connect(Throwable exception) {
    }

}