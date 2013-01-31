/**
 * Copyright 2011 JogAmp Community. All rights reserved.
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

import com.jogamp.openal.test.manual.OpenALTest;

import android.os.Bundle;
import android.util.Log;

public class OpenALTestActivity extends BaseActivity {
   static String TAG = "OpenALTestActivity";
   
   OpenALTest demo = null;
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
       Log.d(TAG, "onCreate - 0");
       super.onCreate(savedInstanceState);
       
       demo = new OpenALTest();
       try {
           demo.init();
       } catch (Exception e) {
           e.printStackTrace();
           demo.dispose();
           demo = null;
       }
       
       Log.d(TAG, "onCreate - X");
   }
   
   @Override
   public void onResume() {
     Log.d(MD.TAG, "onResume");
     super.onResume();
     if( null != demo ) {
         demo.play();
     }
   }

   @Override
   public void onPause() {
     Log.d(MD.TAG, "onPause");
     if( null != demo ) {
         demo.pause();
     }
     super.onPause();
   }

   @Override
   public void onDestroy() {
     Log.d(MD.TAG, "onDestroy");
     if( null != demo ) {
         demo.dispose();
         demo = null;
     }
     super.onDestroy(); 
   }   
   
}
