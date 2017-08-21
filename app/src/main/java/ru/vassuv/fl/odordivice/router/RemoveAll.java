package ru.vassuv.fl.odordivice.router;

import ru.terrakok.cicerone.commands.Command;

class RemoveAll implements Command {
    private String name;

    RemoveAll(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
