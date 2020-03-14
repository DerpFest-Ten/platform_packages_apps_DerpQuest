/*
 * Copyright (C) 2015-2020 Android Open Source Illusion Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.derpquest.settings.statusbar;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import java.util.ArrayList;
import java.util.List;

@SearchIndexable
public class StatusbarHolder extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private static final String KEY_STATUS_BAR_LOGO = "status_bar_logo";

    private ListPreference mLogoStyle;
    private SwitchPreference mShowDerpLogo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.statusbar);

        mShowDerpLogo = (SwitchPreference) findPreference(KEY_STATUS_BAR_LOGO);
        mShowDerpLogo.setChecked((Settings.System.getInt(getContentResolver(),
             Settings.System.STATUS_BAR_LOGO, 0) == 1));
        mShowDerpLogo.setOnPreferenceChangeListener(this);

        mLogoStyle = (ListPreference) findPreference("status_bar_logo_style");
        mLogoStyle.setOnPreferenceChangeListener(this);
        int logoStyle = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.STATUS_BAR_LOGO_STYLE,
                0, UserHandle.USER_CURRENT);
        mLogoStyle.setValue(String.valueOf(logoStyle));
        mLogoStyle.setSummary(mLogoStyle.getEntry());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if  (preference == mShowDerpLogo) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_LOGO, value ? 1 : 0);
            return true;
        } else if (preference.equals(mLogoStyle)) {
            int logoStyle = Integer.parseInt(((String) objValue).toString());
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.STATUS_BAR_LOGO_STYLE, logoStyle, UserHandle.USER_CURRENT);
            int index = mLogoStyle.findIndexOfValue((String) objValue);
            mLogoStyle.setSummary(
                    mLogoStyle.getEntries()[index]);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.DERP;
    }

    /**
     * For Search.
     */
    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider() {
            @Override
            public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean enabled) {
                ArrayList<SearchIndexableResource> result =
                    new ArrayList<SearchIndexableResource>();
                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.statusbar;
                    result.add(sir);
                    return result;
            }

            @Override
            public List<String> getNonIndexableKeys(Context context) {
                List<String> keys = super.getNonIndexableKeys(context);
                return keys;
            }
        };
}
