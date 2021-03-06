/*
 * Copyright (C) 2012-2014 Neo Visionaries Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.neovisionaries.android.app;


import android.app.Activity;
import android.view.KeyEvent;
import com.neovisionaries.android.util.L;


public final class ActivityHelper
{
    private ActivityHelper()
    {
    }


    /**
     * Mark this application as stopping and finish the given Activity.
     *
     * <p>
     * {@link App}{@code .}{@link App#getInstance() getInstance()}{@code
     * .}{@link App#setStopping(boolean) setStopping}{@code (true)}
     * is called and then {@link Activity#finish() finish()} is called
     * on the given Activity.
     * </p>
     *
     * <p>
     * {@code BaseXxxActivity} classes can use this method to implement
     * {@code exit()} method. See the implementation of {@link
     * BaseActivity#exit()} as an example.
     * </p>
     */
    public static void exit(Activity self)
    {
        // App instance.
        App app = App.getInstance();

        // Emit a log message related to the application's life cycle.
        L.d(self, "== APPLICATION '%s' (version = %s) STOPPING ==",
            app.getApplicationLabel(), app.getVersionName());

        // Make the application's state as "stopping".
        app.setStopping(true);

        // Close the Activity. After the Activity is closed,
        // another Activity's onResume() will be called. It is
        // expected the implementation of the onResume() checks
        // the return value of App.getInstance().getStopping()
        // and returns without doing anything if the return
        // value is true. This chain mechanism terminates the
        // application gracefully without destroying the life
        // cycle of Activities.
        self.finish();
    }


    /**
     * Exit this application by calling {@link #exit(Activity)
     * exit}{@code (true)} if the given key code is {@link
     * KeyEvent#KEYCODE_BACK}. Otherwise, nothing is done and
     * false is returned.
     *
     * <p>
     * {@code BottomXxxActivity} classes can use this method to implement
     * {@code onKeyDown(int, android.view.KeyEvent)} method.
     * See the implementation of {@link BottomActivity#onKeyDown(int, KeyEvent)}
     * as an example.
     * </p>
     *
     * @param self
     * @param keyCode
     * @param event
     * @return
     *         True if the key code was {@code KEYCODE_BACK}.
     */
    public static boolean exitOnBackKeyDown(Activity self, int keyCode, KeyEvent event)
    {
        // If the given key code is 'back'.
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            // Terminate this application.
            exit(self);

            // The key event was consumed.
            return true;
        }

        // The given key code is not 'back'.
        return false;
    }


    /**
     * Mark this application as restarting and finish the given Activity.
     *
     * <p>
     * {@link App}{@code .}{@link App#getInstance() getInstance()}{@code
     * .}{@link App#setRestarting(boolean) setRestarting}{@code (true)}
     * is called and then {@link #exit(Activity) exit}{@code (self)}
     * is called.
     * </p>
     *
     * <p>
     * {@code BaseXxxActivity} classes can use this method to implement
     * {@code restart()} method. See the implementation of {@link
     * BaseActivity#restart()} as an example.
     * </p>
     *
     * @since 1.7
     */
    public static void restart(Activity self)
    {
        // App instance.
        App app = App.getInstance();

        // Make the application's state as "restarting".
        app.setRestarting(true);

        // Terminate this application.
        exit(self);
    }
}
