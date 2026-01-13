package com.example.studyflow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TasksPagerAdapter extends FragmentStateAdapter {

    public TasksPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public TasksPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return TaskListFragment.newInstance(0);
        } else {
            return TaskListFragment.newInstance(1);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}