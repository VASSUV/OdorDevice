package ru.vassuv.fl.odordivice.router;

import android.os.Bundle;

import ru.terrakok.cicerone.commands.Command;

class BackWithResult implements Command {
    Bundle result;

    BackWithResult(Bundle bundle) {
        result = bundle;
    }
}
