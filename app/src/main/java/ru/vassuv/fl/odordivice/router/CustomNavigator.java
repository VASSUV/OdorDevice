package ru.vassuv.fl.odordivice.router;

import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.commands.Back;
import ru.terrakok.cicerone.commands.BackTo;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Forward;
import ru.terrakok.cicerone.commands.Replace;
import ru.terrakok.cicerone.commands.SystemMessage;

public abstract class CustomNavigator implements Navigator {
    private List<String> screenNames = new ArrayList<>();
    private FragmentManager fragmentManager;
    private int containerId;

    protected CustomNavigator(FragmentManager fragmentManager, int containerId, final OnChangeFragmentListener listener) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        if (listener != null)
            fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    listener.onChangeFragment();
                }
            });
    }

    @Override
    synchronized public void applyCommand(Command command) {
        final String lastScreenKey = screenNames.isEmpty() ? "" : screenNames.get(screenNames.size() - 1);
        if (command instanceof Forward) {
            final Forward forward = (Forward) command;
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(getEnterAnimation(lastScreenKey, forward.getScreenKey()),
                            getExitAnimation(lastScreenKey, forward.getScreenKey()),
                            getPopEnterAnimation(lastScreenKey, forward.getScreenKey()),
                            getPopExitAnimation(lastScreenKey, forward.getScreenKey()))
                    .replace(containerId, createFragment(forward.getScreenKey(), forward.getTransitionData()))
                    .addToBackStack(forward.getScreenKey())
                    .commit();
            screenNames.add(((Forward) command).getScreenKey());
        } else if (command instanceof Back) {
            if (fragmentManager.getBackStackEntryCount() > 0)
                fragmentManager.popBackStackImmediate();
            else exit();

            if (screenNames.size() > 0)
                screenNames.remove(screenNames.size() - 1);
        } else if (command instanceof BackWithResult) {
            if (fragmentManager.getBackStackEntryCount() > 0)
                fragmentManager.popBackStackImmediate();
            else exit();

            if (screenNames.size() > 0)
                screenNames.remove(screenNames.size() - 1);

            if (fragmentManager.getBackStackEntryCount() > 0) {
                final ExtFragment fragment = (ExtFragment) fragmentManager.findFragmentById(containerId);
                if (fragment != null) fragment.setResult(((BackWithResult) command).result);
            }
        } else if (command instanceof Replace) {
            final Replace replace = (Replace) command;
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(getEnterAnimation(lastScreenKey, replace.getScreenKey()),
                                getExitAnimation(lastScreenKey, replace.getScreenKey()),
                                getPopEnterAnimation(lastScreenKey, replace.getScreenKey()),
                                getPopExitAnimation(lastScreenKey, replace.getScreenKey()))
                        .replace(containerId, createFragment(replace.getScreenKey(), replace.getTransitionData()))
                        .addToBackStack(replace.getScreenKey())
                        .commit();
            } else {
                fragmentManager.popBackStackImmediate();
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(getEnterAnimation(lastScreenKey, replace.getScreenKey()),
                                getExitAnimation(lastScreenKey, replace.getScreenKey()),
                                getPopEnterAnimation(lastScreenKey, replace.getScreenKey()),
                                getPopExitAnimation(lastScreenKey, replace.getScreenKey()))
                        .replace(containerId, createFragment(replace.getScreenKey(), replace.getTransitionData()))
                        .addToBackStack(replace.getScreenKey())
                        .commit();
            }

            if (screenNames.size() > 0)
                screenNames.remove(screenNames.size() - 1);
            screenNames.add(((Replace) command).getScreenKey());
        } else if (command instanceof BackTo) {
            final String key = ((BackTo) command).getScreenKey();

            if (key == null) {
                backToRoot();
                screenNames.clear();
            } else {
                boolean hasScreen = false;
                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                    if (key.equals(fragmentManager.getBackStackEntryAt(i).getName())) {
                        fragmentManager.popBackStackImmediate(key, 0);
                        hasScreen = true;
                        break;
                    }
                }
                if (!hasScreen) {
                    backToUnexisting();
                }
                if (screenNames.size() > 0)
                    screenNames = new ArrayList<>(screenNames.subList(0,
                            fragmentManager.getBackStackEntryCount() + 1));
            }
        } else if (command instanceof CustomSystemMessage) {
            showSystemMessage(((CustomSystemMessage) command).getMessage(), ((CustomSystemMessage) command).getType());
            return;
        }

        printScreensScheme();
    }

    private void printScreensScheme() {
        final StringBuilder str = new StringBuilder("[");
        boolean isFirst = true;

        for (String name : screenNames) {
            if (isFirst) {
                str.append(name);
                isFirst = false;
            } else str.append(" ➔ ")
                    .append(name);
        }

        str.append("]");
        Log.d("Cicerone-ext","Screen chain:" + str.toString());
    }

    public List<String> getScreenNames() {
        return screenNames;
    }

    public void setScreenNames(List<String> value) {
        screenNames = value;
//        printScreensScheme();
    }

    private void backToRoot() {
//        if (fragmentManager.getBackStackEntryCount() == 0) return;

        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }

        fragmentManager.executePendingTransactions();
    }

    protected abstract Fragment createFragment(String screenKey, Object data);

    protected abstract void showSystemMessage(String message, int type);

    protected abstract void exit();

    private void backToUnexisting() {
        backToRoot();
    }

    public interface OnChangeFragmentListener {
        void onChangeFragment();
    }

    @AnimRes
    protected abstract int getEnterAnimation(String oldScreenKey, String newScreenKey);

    @AnimRes
    protected abstract int getExitAnimation(String oldScreenKey, String newScreenKey);

    @AnimRes
    protected abstract int getPopEnterAnimation(String oldScreenKey, String newScreenKey);

    @AnimRes
    protected abstract int getPopExitAnimation(String oldScreenKey, String newScreenKey);
}
