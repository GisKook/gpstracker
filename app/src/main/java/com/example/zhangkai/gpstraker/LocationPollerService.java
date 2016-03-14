/**
 * Copyright (c) 2010 CommonsWare, LLC
 * Copyright (c) 2011 Birkett Enterprise Ltd
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.zhangkai.gpstraker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Service providing the guts of the location polling
 * engine. Uses a WakeLock to ensure the CPU stays on while
 * the location lookup is going on. Handles both successful
 * and timeout conditions.
 * <p/>
 * Those wishing to leverage this service should do so via
 * the LocationPoller class.
 */
public class LocationPollerService extends Service {
    private static final String LOCK_NAME_STATIC = "com.commonsware.cwac.locpoll.LocationPoller";
    private static volatile PowerManager.WakeLock lockStatic;
    private LocationManager locationManager;

    /**
     * Lazy-initializes the WakeLock when we first use it. We
     * use a partial WakeLock since we only need the CPU on,
     * not the screen.
     */
    synchronized private static PowerManager.WakeLock getLock(Context context) {
        if (lockStatic == null) {
            PowerManager mgr = (PowerManager) context.getApplicationContext()
                    .getSystemService(Context.POWER_SERVICE);

            lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    LOCK_NAME_STATIC);
            lockStatic.setReferenceCounted(true);
        }
        return (lockStatic);
    }

    /**
     * Called by LocationPoller to trigger a poll for the
     * location. Acquires the WakeLock, then starts the
     * service using the supplied Intent (setting the
     * component so routing always goes to the service).
     */
    public static void requestLocation(Context context, Intent intent)
            throws InvalidParameterException {

        assertValidParameters(intent);
        getLock(context.getApplicationContext()).acquire();

        intent.setClass(context, LocationPollerService.class);

        context.startService(intent);
    }

    public static void assertValidParameters(Intent intent)
            throws InvalidParameterException {
        LocationPollerParameter parameters = getParametersFromIntent(intent);
        if (parameters.getProviders() == null
                || parameters.getProviders().length == 0) {
            throw new InvalidParameterException(
                    "at least one provider must be set");
        }
    }

    public static LocationPollerParameter getParametersFromIntent(Intent intent)
            throws InvalidParameterException {

        LocationPollerParameter parameter = new LocationPollerParameter(intent.getExtras());

        return parameter;
    }

    /**
     * Obtain the LocationManager on startup
     */
    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //android.os.Debug.waitForDebugger();
    }

    /**
     * No-op implementation as required by superclass
     */
    @Override
    public IBinder onBind(Intent i) {
        return (null);
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        Log.i("giskook", "servie gone");
    }

    /**
     * Validates the required extras (EXTRA_PROVIDER and
     * EXTRA_INTENT). If valid, updates the Intent to be
     * broadcast with the application's own package (required
     * to keep the broadcast within this application, so we do
     * not leak security information). Then, forks a
     * PollerThread to do the actual location lookup.
     *
     * @return START_REDELIVER_INTENT to ensure we get the
     * last request again
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PowerManager.WakeLock lock = getLock(this.getApplicationContext());
        if (!lock.isHeld() || (flags & START_FLAG_REDELIVERY) != 0) {
            lock.acquire();
        }

        LocationPollerParameter parameters = getParametersFromIntent(intent);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.loction)
                        .setContentTitle("My notification")
                        .setContentText(android.os.Process.myPid() + " " + android.os.Process.myTid());

//		NotificationManager mNotifyMgr =
//				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //mNotifyMgr.notify(1,mBuilder.build());
        startForeground(1234, mBuilder.build());
        util.recordLog(Constants.LOGFILE, "b join");

        PollerThread pollerThread = new PollerThread(lock, locationManager, parameters);
        pollerThread.start();
        try {
            pollerThread.join(parameters.getTimeout() + 1000 );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        util.recordLog(Constants.LOGFILE, "a join");
        return (START_NOT_STICKY);
//		return START_STICKY;
    }

    /**
     * A WakefulThread subclass that knows how to look up the current location,
     * plus handle the timeout scenario.
     */
    private class PollerThread extends WakefulThread {
        private LocationManager locationManager;

        private Handler handler = new Handler();
        private LocationPollerParameter locationPollerParameter;
        private int currentLocationProviderIndex;

        private Runnable onTimeout = new Runnable() {

            public void run() {
                locationManager.removeUpdates(listener);
                if (isTriedAllProviders()) {
                    broadCastFailureMessage();
                    util.recordLog(Constants.LOGFILE, "timeout run");
                    quit();
                } else {
                    currentLocationProviderIndex++;
                    tryNextProvider();
                }
            }
        };

        private LocationListener listener = new LocationListener() {
            /**
             * If we get a fix, get rid of the timeout condition,
             * then attach the location as an extra
             * (EXTRA_LOCATION) on the Intent, broadcast it, then
             * exit the polling loop so the thread terminates.
             */
            public synchronized  void onLocationChanged(Location location) {
                handler.removeCallbacks(onTimeout);
                Intent toBroadcast = createIntentToBroadcastOnCompletion();
                toBroadcast.putExtra(LocationPollerResult.ERROR_KEY, Constants.LOCATIONPOLLER_SUCCESS);
                toBroadcast.putExtra(LocationPollerResult.LOCATION_KEY, location);
                util.recordLog(Constants.LOGFILE, "change location");
                sendBroadcast(toBroadcast);
                quit();
            }

            public void onProviderDisabled(String provider) {
                // required for interface, not used
                Log.i("giskook", "provider disabled");
            }

            public void onProviderEnabled(String provider) {
                // required for interface, not used
            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // required for interface, not used
                Log.i("giskook", "status changed");
            }
        };

        /**
         * Constructor.
         *
         * @param lock            Already-locked WakeLock
         * @param locationManager LocationManager for doing the location
         *                        lookup
         * @param provider        name of the location provider to use
         * @param intentTemplate  Intent to be broadcast when location found
         *                        or timeout occurs
         */
        PollerThread(PowerManager.WakeLock lock,
                     LocationManager locationManager,
                     LocationPollerParameter locationPollerParameter) {
            super(lock, "LocationPoller-PollerThread");

            this.locationManager = locationManager;
            this.locationPollerParameter = locationPollerParameter;
        }

        /**
         * Called before the Handler loop begins. Registers a
         * timeout, so we do not wait forever for a location.
         * When a timeout occurs, broadcast an Intent containing
         * an error extra, then terminate the thread. Also,
         * requests a location update from the LocationManager.
         */
        @Override
        protected void onPreExecute() {
            tryNextProvider();
        }

        private void tryNextProvider() {
//			if(getCurrentProvider().equals(LocationManager.GPS_PROVIDER)){
//				handler.postDelayed(onTimeout, locationPollerParameter.getTimeout());
//			}else{
//				handler.postDelayed(onTimeout, 1000);
//			}
            handler.postDelayed(onTimeout, locationPollerParameter.getTimeout());
            requestLocationUdpate();
        }

        private void requestLocationUdpate() {
            try {
                locationManager.requestLocationUpdates(getCurrentProvider(), 0, 0, listener);
                util.recordLog(Constants.LOGFILE, "request location");
            } catch (IllegalArgumentException e) {
                // see http://code.google.com/p/android/issues/detail?id=21237
                Log.w(getClass().getSimpleName(),
                        "Exception requesting updates -- may be emulator issue",
                        e);
                util.recordLog(Constants.LOGFILE, "exception requestlocation");
                quit();
            }
        }

        private void broadCastFailureMessage() {
            Intent toBroadcast = createIntentToBroadcastOnCompletion();

            toBroadcast.putExtra(LocationPollerResult.ERROR_KEY, Constants.LOCATIONPOLLER_TIMEOUT);
            Location location = locationManager.getLastKnownLocation(getCurrentProvider());
            toBroadcast.putExtra(LocationPollerResult.LASTKNOWN_LOCATION_KEY, location);
            sendBroadcast(toBroadcast);
        }

        private String getCurrentProvider() {
            String currentProvider = locationPollerParameter.getProviders()[currentLocationProviderIndex];
            return currentProvider;
        }

        private boolean isTriedAllProviders() {
            return currentLocationProviderIndex == locationPollerParameter
                    .getProviders().length - 1;
        }

        private Intent createIntentToBroadcastOnCompletion() {
            Intent toBroadcast = new Intent(locationPollerParameter.getIntentToBroadcastOnCompletion());
            toBroadcast.setPackage(getPackageName());
            return toBroadcast;
        }

        /**
         * Called when the Handler loop ends. Removes the
         * location listener.
         */
        @Override
        protected void onPostExecute() {
            Log.i("giskook", "PollerThread onPostExecute");
            locationManager.removeUpdates(listener);
            super.onPostExecute();
        }

        /**
         * Called when the WakeLock is completely unlocked.
         * Stops the service, so everything shuts down.
         */
        @Override
        protected void onUnlocked() {
            Log.i("giskook", "PollerThread onUnlocked");
            stopSelf();
        }
    }
}
