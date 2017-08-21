package ru.vassuv.fl.odordivice.router;

import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import ru.terrakok.cicerone.BaseRouter;
import ru.terrakok.cicerone.commands.Back;
import ru.terrakok.cicerone.commands.BackTo;
import ru.terrakok.cicerone.commands.Forward;
import ru.terrakok.cicerone.commands.Replace;
import ru.terrakok.cicerone.commands.SystemMessage;

public class CustomRouter extends BaseRouter {
    private OnNewRootScreenListener onNewRootScreenListener;
    private OnBackScreenListener onBackScreenListener;

    public CustomRouter() {
        super();
    }

    public void navigateTo(String screenKey) {
        navigateTo(screenKey, null);
    }

    public void navigateTo(String screenKey, Object data) {
        executeCommand(new Forward(screenKey, data));
    }

    public void newScreenChain(String screenKey) {
        newScreenChain(screenKey, null);
    }

    public void newScreenChain(String screenKey, Object data) {
        executeCommand(new BackTo(null));
        executeCommand(new Forward(screenKey, data));
    }

    public void newRootScreen(String screenKey) {
        newRootScreen(screenKey, null);
    }

    synchronized public void newRootScreen(String screenKey, Object data) {
        executeCommand(new BackTo(null));
        executeCommand(new Replace(screenKey, data));

        if (onNewRootScreenListener != null)
            onNewRootScreenListener.onChangeRootScreen(screenKey);
    }

    public void replaceScreen(String screenKey) {
        replaceScreen(screenKey, null);
    }

    public void replaceScreen(String screenKey, Object data) {
        executeCommand(new Replace(screenKey, data));
    }

    public void backTo(String screenKey) {
        executeCommand(new BackTo(screenKey));
    }

    public void exit() {
        if (onBackScreenListener != null)
            onBackScreenListener.onBackScreen();
        executeCommand(new Back());
    }

    public void exitWithResult(Bundle bundle) {
        if (onBackScreenListener != null)
            onBackScreenListener.onBackScreen();
        executeCommand(new BackWithResult(bundle));
    }

    public void exitWithMessage(String message, int type) {
        executeCommand(new Back());
        executeCommand(new CustomSystemMessage(message, type));
    }

    public void showSystemMessage(String message, int type) {
        executeCommand(new CustomSystemMessage(message, type));
    }

    public void setOnNewRootScreenListener(OnNewRootScreenListener onNewRootScreenListener) {
        this.onNewRootScreenListener = onNewRootScreenListener;
    }

    public void setOnBackScreenListener(OnBackScreenListener onBackScreenListener) {
        this.onBackScreenListener = onBackScreenListener;
    }

    public interface OnNewRootScreenListener {
        void onChangeRootScreen(String screenKey);
    }

    public interface OnBackScreenListener {
        void onBackScreen();
    }
}
