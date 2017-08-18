package ru.vassuv.fl.odordivice.router;

import android.os.Bundle;

import ru.terrakok.cicerone.commands.Command;

class BackToWithResult implements Command {
    private String screenKey;
    private Bundle result;

    BackToWithResult(String screenKey, Bundle result) {
        this.screenKey = screenKey;
        this.result = result;
    }

    Bundle getResult() {
        return result;
    }

    String getScreenKey() {
        return screenKey;
    }
}