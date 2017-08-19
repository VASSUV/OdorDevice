package ru.vassuv.fl.odordivice.router;

import ru.terrakok.cicerone.commands.SystemMessage;

public class CustomSystemMessage extends SystemMessage {
    private int type;

    CustomSystemMessage(String message, int type) {
        super(message);
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
