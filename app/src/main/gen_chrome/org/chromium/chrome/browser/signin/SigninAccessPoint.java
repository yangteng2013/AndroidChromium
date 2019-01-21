
// Copyright 2019 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// This file is autogenerated by
//     java_cpp_enum.py
// From
//     ../../components/signin/core/browser/signin_metrics.h

package org.chromium.chrome.browser.signin;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
    SigninAccessPoint.START_PAGE, SigninAccessPoint.NTP_LINK, SigninAccessPoint.MENU,
    SigninAccessPoint.SETTINGS, SigninAccessPoint.SUPERVISED_USER,
    SigninAccessPoint.EXTENSION_INSTALL_BUBBLE, SigninAccessPoint.EXTENSIONS,
    SigninAccessPoint.APPS_PAGE_LINK, SigninAccessPoint.BOOKMARK_BUBBLE,
    SigninAccessPoint.BOOKMARK_MANAGER, SigninAccessPoint.AVATAR_BUBBLE_SIGN_IN,
    SigninAccessPoint.USER_MANAGER, SigninAccessPoint.DEVICES_PAGE, SigninAccessPoint.CLOUD_PRINT,
    SigninAccessPoint.CONTENT_AREA, SigninAccessPoint.SIGNIN_PROMO, SigninAccessPoint.RECENT_TABS,
    SigninAccessPoint.UNKNOWN, SigninAccessPoint.PASSWORD_BUBBLE,
    SigninAccessPoint.AUTOFILL_DROPDOWN, SigninAccessPoint.NTP_CONTENT_SUGGESTIONS,
    SigninAccessPoint.RESIGNIN_INFOBAR, SigninAccessPoint.TAB_SWITCHER,
    SigninAccessPoint.FORCE_SIGNIN_WARNING, SigninAccessPoint.SAVE_CARD_BUBBLE,
    SigninAccessPoint.MANAGE_CARDS_BUBBLE, SigninAccessPoint.MAX
})
@Retention(RetentionPolicy.SOURCE)
public @interface SigninAccessPoint {
  int START_PAGE = 0;
  int NTP_LINK = 1;
  int MENU = 2;
  int SETTINGS = 3;
  int SUPERVISED_USER = 4;
  int EXTENSION_INSTALL_BUBBLE = 5;
  int EXTENSIONS = 6;
  int APPS_PAGE_LINK = 7;
  int BOOKMARK_BUBBLE = 8;
  int BOOKMARK_MANAGER = 9;
  int AVATAR_BUBBLE_SIGN_IN = 10;
  int USER_MANAGER = 11;
  int DEVICES_PAGE = 12;
  int CLOUD_PRINT = 13;
  int CONTENT_AREA = 14;
  int SIGNIN_PROMO = 15;
  int RECENT_TABS = 16;
  int UNKNOWN = 17;
  int PASSWORD_BUBBLE = 18;
  int AUTOFILL_DROPDOWN = 19;
  int NTP_CONTENT_SUGGESTIONS = 20;
  int RESIGNIN_INFOBAR = 21;
  int TAB_SWITCHER = 22;
  int FORCE_SIGNIN_WARNING = 23;
  int SAVE_CARD_BUBBLE = 24;
  int MANAGE_CARDS_BUBBLE = 25;
  int MAX = 26;
}
