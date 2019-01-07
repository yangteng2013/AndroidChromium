// Copyright 2018 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.customtabs;

import org.chromium.chrome.browser.metrics.PageLoadMetrics;
import org.chromium.chrome.browser.tab.Tab;
import org.chromium.content_public.browser.WebContents;

/**
 * Notifies the provided {@link CustomTabObserver} when first meaningful paint occurs.
 */
/* package */ class FirstMeaningfulPaintObserver implements PageLoadMetrics.Observer {
    private final Tab mTab;
    private final CustomTabObserver mCustomTabObserver;

    /* package */ FirstMeaningfulPaintObserver(CustomTabObserver tabObserver, Tab tab) {
        mCustomTabObserver = tabObserver;
        mTab = tab;
    }

    @Override
    public void onFirstMeaningfulPaint(WebContents webContents, long navigationId,
            long navigationStartTick, long firstContentfulPaintMs) {
        if (webContents != mTab.getWebContents()) return;

        mCustomTabObserver.onFirstMeaningfulPaint(mTab);
    }
}
