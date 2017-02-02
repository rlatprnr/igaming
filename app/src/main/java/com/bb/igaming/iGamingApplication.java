/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.bb.igaming;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.http.ParseHttpRequest;
import com.parse.http.ParseHttpResponse;
import com.parse.http.ParseNetworkInterceptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class iGamingApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

      Parse.enableLocalDatastore(this);
      Parse.addParseNetworkInterceptor(new ParseLogInterceptor());
      Parse.initialize(this);

      ParseACL defaultACL = new ParseACL();
      defaultACL.setPublicReadAccess(true);
      defaultACL.setPublicWriteAccess(false);
      ParseACL.setDefaultACL(defaultACL, true);
  }

  public class ParseLogInterceptor implements ParseNetworkInterceptor {
    @Override
    public ParseHttpResponse intercept(Chain chain) throws IOException {
      ParseHttpRequest request = chain.getRequest();

      ParseHttpResponse response = chain.proceed(request);

      // Consume the response body
      ByteArrayOutputStream responseBodyByteStream = new ByteArrayOutputStream();
      int n;
      byte[] buffer = new byte[1024];
      while ((n = response.getContent().read(buffer, 0, buffer.length)) != -1) {
        responseBodyByteStream.write(buffer, 0, n);
      }
      final byte[] responseBodyBytes = responseBodyByteStream.toByteArray();

      // Make a new response before return the response
      response = new ParseHttpResponse.Builder(response)
              .setContent(new ByteArrayInputStream(responseBodyBytes))
              .build();

      return response;
    }
  }
}
