package com.example.jimec.javascriptshell;

import android.support.design.widget.TabLayout;

public class ShellTabTraverser implements TabLayout.OnTabSelectedListener {
    
    private final EditorTab mEditorTab;
    private final RunTab mRunTab;
    
    public ShellTabTraverser(ShellPagerAdapter adapter) {
        mEditorTab = adapter.getEditorTab();
        mRunTab = adapter.getRunTab();
    }
    
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case ShellPagerAdapter.FRAGMENT_POSITION_RUN:
                // Enter run tab:
                mRunTab.clearOutput();
                mRunTab.launchV8(mEditorTab.getEditor().toString());
        }
    }
    
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case ShellPagerAdapter.FRAGMENT_POSITION_RUN:
                // Exit run tab:
                System.out.println("STOP V8");
                mRunTab.stopV8();
        }
    }
    
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
    
}
