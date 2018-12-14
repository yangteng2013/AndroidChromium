// Copyright 2015 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.preferences;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;

import org.chromium.base.ContextUtils;
import org.chromium.base.VisibleForTesting;
import org.chromium.chrome.R;
import org.chromium.chrome.browser.ChromeFeatureList;
import org.chromium.chrome.browser.autofill.PersonalDataManager;
import org.chromium.chrome.browser.contextual_suggestions.EnabledStateMonitor;
import org.chromium.chrome.browser.net.spdyproxy.DataReductionProxySettings;
import org.chromium.chrome.browser.partnercustomizations.HomepageManager;
import org.chromium.chrome.browser.preferences.datareduction.DataReductionPreferences;
import org.chromium.chrome.browser.search_engines.TemplateUrl;
import org.chromium.chrome.browser.search_engines.TemplateUrlService;
import org.chromium.chrome.browser.signin.SigninManager;
import org.chromium.chrome.browser.util.FeatureUtilities;

import java.util.HashMap;
import java.util.Map;

/**
 * The main settings screen, shown when the user first opens Settings.
 */
public class MainPreferences extends PreferenceFragment
        implements SigninManager.SignInStateObserver, TemplateUrlService.LoadListener {
    public static final String PREF_ACCOUNT_SECTION = "account_section";
    //public static final String PREF_SIGN_IN = "sign_in";
    public static final String PREF_SYNC_AND_SERVICES = "sync_and_services";
    public static final String PREF_AUTOFILL_SETTINGS = "autofill_settings";
    public static final String PREF_STANDARD_SEARCH_ENGINE = "standard_search_engine";
    public static final String PREF_PRIVATE_SEARCH_ENGINE = "private_search_engine";
    public static final String PREF_SAVED_PASSWORDS = "saved_passwords";
    public static final String PREF_CONTEXTUAL_SUGGESTIONS = "contextual_suggestions";
    public static final String PREF_HOMEPAGE = "homepage";
    //public static final String PREF_DATA_REDUCTION = "data_reduction";
    public static final String PREF_NOTIFICATIONS = "notifications";
    public static final String PREF_LANGUAGES = "languages";
    public static final String PREF_DOWNLOADS = "downloads";

    private final ManagedPreferenceDelegate mManagedPreferenceDelegate;
    private final Map<String, Preference> mAllPreferences = new HashMap<>();
    //private SignInPreference mSignInPreference;

    public MainPreferences() {
        setHasOptionsMenu(true);
        mManagedPreferenceDelegate = createManagedPreferenceDelegate();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPreferences();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mSignInPreference.onPreferenceFragmentDestroyed();
    }

    @Override
    public void onStart() {
        super.onStart();
        /*if (SigninManager.get().isSigninSupported()) {
            SigninManager.get().addSignInStateObserver(this);
            mSignInPreference.registerForUpdates();
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        /*if (SigninManager.get().isSigninSupported()) {
            SigninManager.get().removeSignInStateObserver(this);
            mSignInPreference.unregisterForUpdates();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePreferences();
    }

    private void createPreferences() {
        PreferenceUtils.addPreferencesFromResource(this, R.xml.main_preferences);
        cachePreferences();

        if (ChromeFeatureList.isEnabled(ChromeFeatureList.UNIFIED_CONSENT)) {
            //mSignInPreference.setOnStateChangedCallback(this::onSignInPreferenceStateChanged);
        } else {
            getPreferenceScreen().removePreference(findPreference(PREF_ACCOUNT_SECTION));
            getPreferenceScreen().removePreference(findPreference(PREF_SYNC_AND_SERVICES));
        }

        setManagedPreferenceDelegateForPreference(PREF_STANDARD_SEARCH_ENGINE);
        setManagedPreferenceDelegateForPreference(PREF_PRIVATE_SEARCH_ENGINE);
        setManagedPreferenceDelegateForPreference(PREF_AUTOFILL_SETTINGS);
        setManagedPreferenceDelegateForPreference(PREF_SAVED_PASSWORDS);
        //setManagedPreferenceDelegateForPreference(PREF_DATA_REDUCTION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // If we are on Android O+ the Notifications preference should lead to the Android
            // Settings notifications page, not to Chrome's notifications settings page.
            Preference notifications = findPreference(PREF_NOTIFICATIONS);
            notifications.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE,
                        ContextUtils.getApplicationContext().getPackageName());
                startActivity(intent);
                // We handle the click so the default action (opening NotificationsPreference)
                // isn't triggered.
                return true;
            });
        } else if (!ChromeFeatureList.isEnabled(
                           ChromeFeatureList.CONTENT_SUGGESTIONS_NOTIFICATIONS)) {
            // The Notifications Preferences page currently only contains the Content Suggestions
            // Notifications setting and a link to per-website notification settings. The latter can
            // be access through Site Settings, so if the Content Suggestions Notifications feature
            // isn't enabled we don't show the Notifications Preferences page.

            // This checks whether the Content Suggestions Notifications *feature* is enabled on the
            // user's device, not whether the user has Content Suggestions Notifications themselves
            // enabled (which is what the user can toggle on the Notifications Preferences page).
            getPreferenceScreen().removePreference(findPreference(PREF_NOTIFICATIONS));
        }

        // This checks whether the Languages Preference *feature* is enabled on the user's device.
        // If not, remove the languages preference.
        if (!ChromeFeatureList.isEnabled(ChromeFeatureList.LANGUAGES_PREFERENCE)) {
            getPreferenceScreen().removePreference(findPreference(PREF_LANGUAGES));
        }

        if (!TemplateUrlService.getInstance().isLoaded()) {
            TemplateUrlService.getInstance().registerLoadListener(this);
            TemplateUrlService.getInstance().load();
        }

        // This checks whether the flag for Downloads Preferences is enabled.
        if (!ChromeFeatureList.isEnabled(ChromeFeatureList.DOWNLOADS_LOCATION_CHANGE)) {
            getPreferenceScreen().removePreference(findPreference(PREF_DOWNLOADS));
        }
    }

    /**
     * Stores all preferences in memory so that, if they needed to be added/removed from the
     * PreferenceScreen, there would be no need to reload them from 'main_preferences.xml'.
     */
    private void cachePreferences() {
        int preferenceCount = getPreferenceScreen().getPreferenceCount();
        for (int index = 0; index < preferenceCount; index++) {
            Preference preference = getPreferenceScreen().getPreference(index);
            mAllPreferences.put(preference.getKey(), preference);
        }
        //mSignInPreference = (SignInPreference) mAllPreferences.get(PREF_SIGN_IN);
    }

    private void setManagedPreferenceDelegateForPreference(String key) {
        ChromeBasePreference chromeBasePreference = (ChromeBasePreference) mAllPreferences.get(key);
        chromeBasePreference.setManagedPreferenceDelegate(mManagedPreferenceDelegate);
    }

    private void updatePreferences() {
        /*if (SigninManager.get().isSigninSupported()) {
            addPreferenceIfAbsent(PREF_SIGN_IN);
        } else {
            removePreferenceIfPresent(PREF_SIGN_IN);
        }*/

        updateSearchEnginePreference(PREF_STANDARD_SEARCH_ENGINE);
        updateSearchEnginePreference(PREF_PRIVATE_SEARCH_ENGINE);

        if (HomepageManager.shouldShowHomepageSetting()) {
            Preference homepagePref = addPreferenceIfAbsent(PREF_HOMEPAGE);
            if (FeatureUtilities.isNewTabPageButtonEnabled()) {
                homepagePref.setTitle(R.string.options_startup_page_title);
            }
            setOnOffSummary(homepagePref, HomepageManager.getInstance().getPrefHomepageEnabled());
        } else {
            removePreferenceIfPresent(PREF_HOMEPAGE);
        }

        if (FeatureUtilities.areContextualSuggestionsEnabled(getActivity())
                && EnabledStateMonitor.shouldShowSettings()) {
            Preference contextualSuggesitons = addPreferenceIfAbsent(PREF_CONTEXTUAL_SUGGESTIONS);
            setOnOffSummary(contextualSuggesitons, EnabledStateMonitor.getEnabledState());
        } else {
            removePreferenceIfPresent(PREF_CONTEXTUAL_SUGGESTIONS);
        }

        /*ChromeBasePreference dataReduction =
                (ChromeBasePreference) findPreference(PREF_DATA_REDUCTION);
        dataReduction.setSummary(DataReductionPreferences.generateSummary(getResources()));*/
    }

    private Preference addPreferenceIfAbsent(String key) {
        Preference preference = getPreferenceScreen().findPreference(key);
        if (preference == null) getPreferenceScreen().addPreference(mAllPreferences.get(key));
        return mAllPreferences.get(key);
    }

    private void removePreferenceIfPresent(String key) {
        Preference preference = getPreferenceScreen().findPreference(key);
        if (preference != null) getPreferenceScreen().removePreference(preference);
    }

    private void updateSearchEnginePreference(String prefSearchName) {
        if (!TemplateUrlService.getInstance().isLoaded()) {
            ChromeBasePreference searchEnginePref =
                    (ChromeBasePreference) findPreference(prefSearchName);
            searchEnginePref.setEnabled(false);
            return;
        }

        String defaultSearchEngineName = TemplateUrlService.getInstance().getDefaultSearchEngineName(prefSearchName.equals(PREF_PRIVATE_SEARCH_ENGINE));
        Preference searchEnginePreference = findPreference(prefSearchName);
        searchEnginePreference.setEnabled(true);
        searchEnginePreference.setSummary(defaultSearchEngineName);
    }

    private void setOnOffSummary(Preference pref, boolean isOn) {
        pref.setSummary(getResources().getString(isOn ? R.string.text_on : R.string.text_off));
    }

    // SigninManager.SignInStateObserver implementation.
    @Override
    public void onSignedIn() {
        // After signing in or out of a managed account, preferences may change or become enabled
        // or disabled.
        new Handler().post(() -> updatePreferences());
    }

    @Override
    public void onSignedOut() {
        updatePreferences();
    }

    private void onSignInPreferenceStateChanged() {
        // Remove "Account" section header if the personalized sign-in promo is shown.
        /*if (mSignInPreference.getState() == SignInPreference.State.PERSONALIZED_PROMO) {
            removePreferenceIfPresent(PREF_ACCOUNT_SECTION);
        } else {
            addPreferenceIfAbsent(PREF_ACCOUNT_SECTION);
        }*/
    }

    // TemplateUrlService.LoadListener implementation.
    @Override
    public void onTemplateUrlServiceLoaded() {
        TemplateUrlService.getInstance().unregisterLoadListener(this);
        updateSearchEnginePreference(PREF_STANDARD_SEARCH_ENGINE);
        updateSearchEnginePreference(PREF_PRIVATE_SEARCH_ENGINE);
    }

    @VisibleForTesting
    ManagedPreferenceDelegate getManagedPreferenceDelegateForTest() {
        return mManagedPreferenceDelegate;
    }

    private ManagedPreferenceDelegate createManagedPreferenceDelegate() {
        return new ManagedPreferenceDelegate() {
            @Override
            public boolean isPreferenceControlledByPolicy(Preference preference) {
                if (PREF_AUTOFILL_SETTINGS.equals(preference.getKey())) {
                    return PersonalDataManager.isAutofillProfileManaged()
                            || PersonalDataManager.isAutofillCreditCardManaged();
                }
                if (PREF_SAVED_PASSWORDS.equals(preference.getKey())) {
                    return PrefServiceBridge.getInstance().isRememberPasswordsManaged();
                }
                /*if (PREF_DATA_REDUCTION.equals(preference.getKey())) {
                    return DataReductionProxySettings.getInstance().isDataReductionProxyManaged();
                }
                if (PREF_STANDARD_SEARCH_ENGINE.equals(preference.getKey())) {
                    return TemplateUrlService.getInstance().isDefaultSearchManaged();
                }*/
                return false;
            }

            @Override
            public boolean isPreferenceClickDisabledByPolicy(Preference preference) {
                if (PREF_AUTOFILL_SETTINGS.equals(preference.getKey())) {
                    // The whole "Autofill and payments" page is disabled by policy if profiles and
                    // credit cards are both disabled by policy.
                    return PersonalDataManager.isAutofillProfileManaged()
                            && PersonalDataManager.isAutofillCreditCardManaged()
                            && !PersonalDataManager.isAutofillProfileEnabled()
                            && !PersonalDataManager.isAutofillCreditCardEnabled();
                }
                if (PREF_SAVED_PASSWORDS.equals(preference.getKey())) {
                    PrefServiceBridge prefs = PrefServiceBridge.getInstance();
                    return prefs.isRememberPasswordsManaged()
                            && !prefs.isRememberPasswordsEnabled();
                }
                /*if (PREF_DATA_REDUCTION.equals(preference.getKey())) {
                    DataReductionProxySettings settings = DataReductionProxySettings.getInstance();
                    return settings.isDataReductionProxyManaged()
                            && !settings.isDataReductionProxyEnabled();
                }
                if (PREF_STANDARD_SEARCH_ENGINE.equals(preference.getKey())) {
                    return TemplateUrlService.getInstance().isDefaultSearchManaged();
                }*/
                return isPreferenceControlledByPolicy(preference)
                        || isPreferenceControlledByCustodian(preference);
            }
        };
    }
}