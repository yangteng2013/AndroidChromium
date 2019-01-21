
// Copyright 2019 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// This file is autogenerated by
//     java_cpp_enum.py
// From
//     ../../chrome/browser/android/preferences/prefs.h

package org.chromium.chrome.browser.preferences;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
    Pref.ALLOW_DELETING_BROWSER_HISTORY, Pref.CONTEXTUAL_SUGGESTIONS_ENABLED,
    Pref.INCOGNITO_MODE_AVAILABILITY, Pref.NTP_ARTICLES_SECTION_ENABLED,
    Pref.NTP_ARTICLES_LIST_VISIBLE, Pref.READER_FOR_ACCESSIBILITY_ENABLED,
    Pref.PROMPT_FOR_DOWNLOAD_ANDROID, Pref.SHOW_MISSING_SD_CARD_ERROR_ANDROID,
    Pref.CAN_MAKE_PAYMENT_ENABLED, Pref.CONTEXTUAL_SEARCH_ENABLED, Pref.AUTOFILL_PROFILE_ENABLED,
    Pref.AUTOFILL_CREDIT_CARD_ENABLED, Pref.PREF_NUM_PREFS
})
@Retention(RetentionPolicy.SOURCE)
public @interface Pref {
  int ALLOW_DELETING_BROWSER_HISTORY = 0;
  int CONTEXTUAL_SUGGESTIONS_ENABLED = 1;
  int INCOGNITO_MODE_AVAILABILITY = 2;
  int NTP_ARTICLES_SECTION_ENABLED = 3;
  int NTP_ARTICLES_LIST_VISIBLE = 4;
  int READER_FOR_ACCESSIBILITY_ENABLED = 5;
  int PROMPT_FOR_DOWNLOAD_ANDROID = 6;
  int SHOW_MISSING_SD_CARD_ERROR_ANDROID = 7;
  int CAN_MAKE_PAYMENT_ENABLED = 8;
  int CONTEXTUAL_SEARCH_ENABLED = 9;
  int AUTOFILL_PROFILE_ENABLED = 10;
  int AUTOFILL_CREDIT_CARD_ENABLED = 11;
  /**
   * PREF_NUM_PREFS must be the last entry.
   */
  int PREF_NUM_PREFS = 12;
}
