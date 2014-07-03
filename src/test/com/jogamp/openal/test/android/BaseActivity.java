/**
 * Copyright 2013 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */
package com.jogamp.openal.test.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {
   boolean isDelegatedActivity;
   Activity rootActivity;
   boolean setThemeCalled = false;

   public BaseActivity() {
       super();
       isDelegatedActivity = false;
       rootActivity = this;
   }

   public void setRootActivity(final Activity rootActivity) {
       this.rootActivity = rootActivity;
       this.isDelegatedActivity = this != rootActivity;
   }

   public final boolean isDelegatedActivity() {
       return isDelegatedActivity;
   }

   public final Activity getActivity() {
       return rootActivity;
   }

   @Override
   public void onCreate(final Bundle savedInstanceState) {
       Log.d(MD.TAG, "onCreate");
       if(!isDelegatedActivity()) {
           super.onCreate(savedInstanceState);
       }
       jogamp.common.os.android.StaticContext.init(rootActivity.getApplicationContext());
   }

   @Override
   public void onStart() {
     Log.d(MD.TAG, "onStart");
     if(!isDelegatedActivity()) {
         super.onStart();
     }
   }

   @Override
   public void onRestart() {
     Log.d(MD.TAG, "onRestart");
     if(!isDelegatedActivity()) {
         super.onRestart();
     }
   }

   @Override
   public void onResume() {
     Log.d(MD.TAG, "onResume");
     if(!isDelegatedActivity()) {
         super.onResume();
     }
   }

   @Override
   public void onPause() {
     Log.d(MD.TAG, "onPause");
     if(!isDelegatedActivity()) {
         super.onPause();
     }
   }

   @Override
   public void onStop() {
     Log.d(MD.TAG, "onStop");
     if(!isDelegatedActivity()) {
         super.onStop();
     }
   }

   @Override
   public void onDestroy() {
     Log.d(MD.TAG, "onDestroy");
     jogamp.common.os.android.StaticContext.clear();
     if(!isDelegatedActivity()) {
         super.onDestroy();
     }
   }
}
