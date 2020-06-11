package com.uowm.skidrow.eok.adapters;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.uowm.skidrow.eok.fragments.MessageHistoryFragment;
import com.uowm.skidrow.eok.fragments.MessageSendFragment;
import com.uowm.skidrow.eok.fragments.MessageViewFragment;

public class TabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    public Bundle bdl;
    int totalTabs;

    public TabAdapter(Context context, FragmentManager fm, int totalTabs, Bundle bdl) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.bdl = bdl;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MessageViewFragment messageViewFragment = new MessageViewFragment();
                messageViewFragment.setArguments(bdl);
                return messageViewFragment;
            case 1:
                MessageSendFragment messageSendFragment = new MessageSendFragment();
                messageSendFragment.setArguments(bdl);
                return messageSendFragment;
            case 2:
                MessageHistoryFragment messageHistoryFragment = new MessageHistoryFragment();
                messageHistoryFragment.setArguments(bdl);
                return messageHistoryFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}