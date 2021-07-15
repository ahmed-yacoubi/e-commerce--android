package ahmed.yacoubi.e_commerce.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import java.util.List;

public class IntroViewPagerAdapter extends FragmentStateAdapter {
    Context context;
    List<Fragment> fragments;

    public IntroViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Context context, List<Fragment> fragments) {
        super(fragmentActivity);
        this.context = context;
        this.fragments = fragments;


    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
