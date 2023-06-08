package com.onemosys.gfx.tool.game.booster.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.onemosys.gfx.tool.game.booster.views.fragment.Dashboard;
import com.onemosys.gfx.tool.game.booster.views.fragment.PrimaryGFX;
import com.onemosys.gfx.tool.game.booster.views.fragment.SecondaryGFX;
import com.onemosys.gfx.tool.game.booster.views.fragment.Stats;

public class MainPageViewAdapter extends FragmentStatePagerAdapter {
    public MainPageViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Dashboard();
            case 1:
                return new PrimaryGFX();
            case 2:
                return new SecondaryGFX();
           case 3:
                return new Stats();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
