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
        for (int i = 0; i < filePath.length; i++) {
            filePath[i] = files[i].getAbsolutePath();
        }
        return filePath;
    }

    public static void main(String... args)  throws Exception {
        SwingUtilities.invokeLater(() -> {
            try {
                Properties prop = Properties.defaultProperties();
                String[] files = files();
                ArrayList<Annotation> annotations = new ArrayList<>();
                if(files.length == 1 && files[0].endsWith(".csv")) {
                    annotations = Annotation.fromFile(files[0]);
                    files = Annotation.files(annotations);
                }
                if(files.length == 0) {
                    System.exit(0);
                }

                KeyMap keymap = new KeyMap();
                ArrayList<Measurment> measurments = new ArrayList<>();
                AudioReader s = new AudioReader(files, prop.getBuffer());
                double audio[] = s.getData();
                if(audio != null) {
                    GrayscaleImager grayscaleImager = new GrayscaleImager();

                    SpectrogramParams params = new SpectrogramParams(prop.getFftWin(), prop.getFftStep(), 0.25);
                    Spectrogram spec = new Spectrogram(audio, params);

                    InfoComponent info = new InfoComponent(annotations, s, params, keymap, prop.getMnistWinDFT());
                    info.setPreferredSize(new Dimension(300, 600));

                    AudioComponent audioCmp = new AudioComponent(audio, 100);
                    audioCmp.setPreferredSize(new Dimension(500, 100));

                    AnnotationPlot annotationPlot = new AnnotationPlot(params, annotations, s, keymap);
                    RegionImager windowPlot = new RegionImager(params, annotations, s, keymap);

                    SpectrogramComponent specCmp = new SpectrogramComponent(spec, params, windowPlot, annotationPlot, grayscaleImager, prop.getMnistWinDFT());
                    specCmp.setPreferredSize(new Dimension(500, 500));

                    AdjustableComponent gainComponent = new AdjustableComponent(specCmp, params);
                    AdjustableComponent brightnessComponent = new AdjustableComponent(specCmp, grayscaleImager);

                    SpectrogramController controller = new SpectrogramController(s, spec, params, specCmp, audioCmp, info, prop.getMnistWinDFT(), prop.getMnistStepDFT(), annotations, keymap);
                    specCmp.addMouseListener(controller);
                    specCmp.addMouseMotionListener(controller);

                    FrequencyComponent freq = new FrequencyComponent(params.fftBins());
                    freq.setPreferredSize(new Dimension(100, 500));

                    JPanel panel = new JPanel();
                    panel.setLayout(new BorderLayout());
                    panel.add(freq, BorderLayout.WEST);
                    panel.add(specCmp, BorderLayout.CENTER);
                    panel.add(audioCmp, BorderLayout.SOUTH);
                    panel.setPreferredSize(new Dimension(800, 600));
                    panel.setBackground(Color.WHITE);

                    JPanel infoCtrl = new JPanel();
                    infoCtrl.setPreferredSize(new Dimension(300, 600));
                    infoCtrl.setBackground(Color.WHITE);
                    infoCtrl.setLayout(new BoxLayout(infoCtrl, 1));
                    infoCtrl.add(info);
                    infoCtrl.add(gainComponent);
                    infoCtrl.add(brightnessComponent);

                    JPanel panel2 = new JPanel();
                    panel2.setLayout(new BorderLayout());
                    panel2.add(panel, BorderLayout.CENTER);
                    panel2.add(infoCtrl, BorderLayout.EAST);
                    panel2.setBackground(Color.WHITE);

                    JFrame frame = new JFrame("Audio MNIST");
                    frame.setFocusable(true);
                    frame.setBackground(Color.WHITE);
                    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                    manager.addKeyEventDispatcher(controller);
                    frame.add(panel2);
                    frame.setPreferredSize(new Dimension(800, 600));
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
