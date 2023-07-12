package hu.ditservices.handlers;

import hu.ditservices.commands.SaveCommand;

public class SaveHandler implements Runnable{
    @Override
    public void run() {
        SaveCommand.Run();
    }
}
