
// Copyright 2019 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// This file is autogenerated by
//     java_cpp_enum.py
// From
//     ../../chrome/browser/vr/ui_test_input.h

package org.chromium.chrome.browser.vr;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
    VrUiTestActivityResult.UNREPORTED, VrUiTestActivityResult.QUIESCENT,
    VrUiTestActivityResult.TIMEOUT_NO_START, VrUiTestActivityResult.TIMEOUT_NO_END
})
@Retention(RetentionPolicy.SOURCE)
public @interface VrUiTestActivityResult {
  /**
   * menu
   */
  int UNREPORTED = 0;
  int QUIESCENT = 1;
  int TIMEOUT_NO_START = 2;
  int TIMEOUT_NO_END = 3;
}
