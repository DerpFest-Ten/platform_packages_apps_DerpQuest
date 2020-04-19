/*
 *  Copyright (C) 2015-2020 Android Open Source Illusion Project
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

package com.derpquest.settings.lockscreen;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.ServiceManager;
import android.provider.SearchIndexableResource;
import androidx.preference.SwitchPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import android.provider.Settings;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import java.util.ArrayList;
import java.util.List;

@SearchIndexable
public class LockscreenHolder extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private static final String LOCK_CLOCK_FONT_STYLE = "lock_clock_font_style";

    private ListPreference mLockClockFonts;

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.DERP;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.lockscreen);

        // Lockscren Clock Fonts
        mLockClockFonts = (ListPreference) findPreference(LOCK_CLOCK_FONT_STYLE);
        mLockClockFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.LOCK_CLOCK_FONT_STYLE, 0)));
        mLockClockFonts.setSummary(mLockClockFonts.getEntry());
        mLockClockFonts.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mLockClockFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCK_CLOCK_FONT_STYLE,
                    Integer.valueOf((String) newValue));
            mLockClockFonts.setValue(String.valueOf(newValue));
            mLockClockFonts.setSummary(mLockClockFonts.getEntry());
            return true;
        }
        return false;
    }

    public static void reset(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        LockScreenVisualizer.reset(mContext);
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
                    sir.xmlResId = R.xml.lockscreen;
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

