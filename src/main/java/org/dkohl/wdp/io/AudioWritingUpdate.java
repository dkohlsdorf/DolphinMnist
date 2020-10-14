package org.dkohl.wdp.io;

public interface AudioWritingUpdate {
    public void progress(int percentageDone, String filename);
}