package org.dkohl.wdp.spectrogram;

import org.dkohl.wdp.io.Properties;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class AudioMNIST {

    private static String[] files() {
        JFrame frame = new JFrame();
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.showOpenDialog(frame);
        File[] files = chooser.getSelectedFiles();
        String[] filePath = new String[files.length];
        for(int i = 0; i < filePath.length; i++) {
            filePath[i] = files[i].getAbsolutePath();
        }
        return filePath;
    }

    public static void main(String ... args) throws Exception {
        Properties prop = Properties.defaultProperties();
        String[] files = files();
        ArrayList<Annotation> annotations = new ArrayList<>();
        AudioStream s = new AudioStream(files, prop.getBuffer());
        double audio[] = s.next();

        SpectrogramParams params = new SpectrogramParams(prop.getFftWin(), prop.getFftStep());
        Spectrogram spec = new Spectrogram(audio, params);

        InfoComponent info = new InfoComponent(annotations, s, prop.getMnistWin());
        info.setPreferredSize(new Dimension(300, 600));

        AudioComponent audioCmp = new AudioComponent(audio);
        audioCmp.setPreferredSize(new Dimension(500, 100));

        SpectrogramComponent specCmp = new SpectrogramComponent(spec, prop.getMnistWin());
        specCmp.setPreferredSize(new Dimension(500, 500));

        SpectrogramController controller = new SpectrogramController(s, spec, params, specCmp, audioCmp, info, prop.getMnistWin(), prop.getMnistStep(), annotations);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(specCmp,  BorderLayout.CENTER);
        panel.add(audioCmp, BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setBackground(Color.WHITE);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        panel2.add(panel,  BorderLayout.CENTER);
        panel2.add(info, BorderLayout.EAST);
        panel2.setBackground(Color.WHITE);

        JFrame frame = new JFrame("Audio MNIST");
        frame.setBackground(Color.WHITE);
        frame.addKeyListener(controller);
        frame.add(panel2);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
