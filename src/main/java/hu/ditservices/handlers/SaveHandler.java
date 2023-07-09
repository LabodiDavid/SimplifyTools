package hu.ditservices.handlers;

import hu.ditservices.commands.SaveCmd;

public class SaveHandler implements Runnable{
    @Override
    public void run() {
        SaveCmd.Run();
    }
}
