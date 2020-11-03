package org.dkohl.wdp.spectrogram;

import org.dkohl.wdp.io.Audio;
import org.dkohl.wdp.io.StdAudio;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class SpectrogramController implements KeyListener, MouseListener {

    private ArrayList<Annotation> annotations;

    private AudioStream stream;
    private SpectrogramParams params;
    private SpectrogramComponent spectrogramView;
    private InfoComponent info;
    private AudioComponent audioView;
    private Spectrogram spectrogram;
    private int position;
    private int width;
    private int speed;

    public SpectrogramController(AudioStream stream, Spectrogram s, SpectrogramParams params, SpectrogramComponent spectrogramView, AudioComponent audioView, InfoComponent info, int width, int speed, ArrayList<Annotation> annotations) {
        this.stream = stream;
        this.params = params;
        this.spectrogramView = spectrogramView;
        this.audioView = audioView;
        this.width = width;
        this.spectrogram = s;
        position = 0;
        this.annotations = annotations;
        this.speed = speed;
        this.info = info;
    }

    private void addAnnotation(Labels label) {
        Annotation match = Annotation.findAnnotation(annotations, params, stream, position, position + width);
        if(label == null && match != null) {
            annotations.remove(match);
        }
        if(label != null){
            if (match == null) {
                Annotation a = stream.getAnnotation(label, params.sample(position), params.sample(position + width));
                annotations.add(a);
            } else {
                match.setAnnotation(label);
            }
        }
    }

    private void save() {
        Thread th = new Thread() {
            @Override
            public void run() {
                JFrame f = new JFrame();
                JFileChooser fc = new JFileChooser();
                Date date = new Date();
                String filename = date.toString().replaceAll("[^a-zA-Z0-9:-]+", "_");
                fc.setSelectedFile(new File(String.format("~/%s.wav", filename)));
                int returnVal = fc.showSaveDialog(f);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    String pathWav = file.getAbsolutePath();
                    String pathCsv = pathWav.replace("wav", "csv");
                    try {
                        Audio.write(pathWav, pathCsv, annotations, info);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        th.run();
    }

    private void seek() throws Exception {
        double x[] = stream.next();
        if(x == null) {
            save();
            System.exit(0);
        }
        spectrogram = new Spectrogram(x, params);
        spectrogramView.setSpectrogram(spectrogram);
        audioView.setAudio(x);
        position = 0;
        spectrogramView.repaint();
        audioView.repaint();
        spectrogramView.setPosition(position);
    }

    private void right() throws Exception {
        if(position + width >= spectrogram.getTime()) {
            seek();
        } else {
            position += speed;
            spectrogramView.setPosition(position);
        }
    }

    private void left() {
        if(position > 0) position -= speed;
        spectrogramView.setPosition(position);
    }

    private void play() {
        StdAudio.play(stream.current(params.sample(position), params.sample(position + width)));
    }

    private void handle(KeyEvent e) throws Exception {
        switch (e.getExtendedKeyCode()) {
            case KeyEvent.VK_1: addAnnotation(Labels.BP_FAST); break;
            case KeyEvent.VK_2: addAnnotation(Labels.BP_MED); break;
            case KeyEvent.VK_3: addAnnotation(Labels.EC_FAST); break;
            case KeyEvent.VK_4: addAnnotation(Labels.EC_MED); break;
            case KeyEvent.VK_5: addAnnotation(Labels.EC_SLOW); break;
            case KeyEvent.VK_6: addAnnotation(Labels.WSTL_UP); break;
            case KeyEvent.VK_7: addAnnotation(Labels.WSTL_DOWN); break;
            case KeyEvent.VK_8: addAnnotation(Labels.WSTL_CONV); break;
            case KeyEvent.VK_9: addAnnotation(Labels.WSTL_CONC); break;
            case KeyEvent.VK_0: addAnnotation(Labels.NOISE); break;
            case KeyEvent.VK_D: right(); break;
            case KeyEvent.VK_A: left(); break;
            case KeyEvent.VK_S: save(); break;
            case KeyEvent.VK_F: seek(); break;
            case KeyEvent.VK_P: play(); break;
            case KeyEvent.VK_X: addAnnotation(null); break;
        }
        info.refresh(position);
        spectrogramView.repaint();
    }

    @Override
    public void keyTyped(KeyEvent event) {
        try {
            handle(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        position = (e.getX() * spectrogram.getTime() / spectrogramView.getSize().width);
        Annotation match = null;
        for(Annotation annotation : annotations) {
            if(annotation.in(stream.sample(params.sample(position)))) {
                match = annotation;
            }
        }
        if(match != null) {
            System.out.println(position);
            position = params.fftSample((int) match.getStart() - stream.getCurrentOffset());
            System.out.println(position);
        }
        spectrogramView.setPosition(position);
        spectrogramView.repaint();
        info.refresh(position);
    }

    @Override
    public void keyPressed(KeyEvent event) { }

    @Override
    public void keyReleased(KeyEvent event) { }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}
