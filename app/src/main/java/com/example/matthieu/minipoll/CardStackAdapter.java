package com.example.matthieu.minipoll;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentManager;

public class CardStackAdapter extends FragmentStatePagerAdapter
{
    public CardStackAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new CardStackFragment();
    }

    @Override
    public int getCount() {
        return 5;
    }
}
