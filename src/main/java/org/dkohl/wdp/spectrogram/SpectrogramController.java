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

    private AudioReader stream;
    private SpectrogramParams params;
    private SpectrogramComponent spectrogramView;
    private InfoComponent info;
    private AudioComponent audioView;
    private Spectrogram spectrogram;
    private int position;
    private int width;
    private int speed;

    public SpectrogramController(AudioReader stream, Spectrogram s, SpectrogramParams params, SpectrogramComponent spectrogramView, AudioComponent audioView, InfoComponent info, int width, int speed, ArrayList<Annotation> annotations) {
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

    private void fwd() throws Exception {
        boolean didMove = stream.fwd();
        double x[] = stream.getData();
        spectrogram = new Spectrogram(x, params);
        spectrogramView.setSpectrogram(spectrogram);
        audioView.setAudio(x);
        if(didMove) position = 0;
        spectrogramView.repaint();
        audioView.repaint();
        spectrogramView.setPosition(position);
    }

    private void bwd() throws Exception {
        boolean didMove = stream.bwd();
        double x[] = stream.getData();
        spectrogram = new Spectrogram(x, params);
        spectrogramView.setSpectrogram(spectrogram);
        audioView.setAudio(x);
        if(didMove) {
            position = spectrogram.getTime() - width;
        } else {
            position = 0;
        }
        spectrogramView.repaint();
        audioView.repaint();
        spectrogramView.setPosition(position);
    }

    private void right() throws Exception {
        if(position + speed >= spectrogram.getTime()) {
            fwd();
        } else {
            position += speed;
            spectrogramView.setPosition(position);
        }
    }

    private void left() throws Exception{
        if(position - speed <= 0) {
            bwd();
        } else {
            position -= speed;
            spectrogramView.setPosition(position);
        }
    }

    private void play() {
        StdAudio.play(stream.current(params.sample(position), params.sample(position + width)));
    }

    private void handle(KeyEvent e) throws Exception {
        switch (e.getKeyChar()) {
            case '1': addAnnotation(Labels.BP_FAST); break;
            case '2': addAnnotation(Labels.BP_MED); break;
            case '3': addAnnotation(Labels.EC_FAST); break;
            case '4': addAnnotation(Labels.EC_MED); break;
            case '5': addAnnotation(Labels.EC_SLOW); break;
            case '6': addAnnotation(Labels.WSTL_UP); break;
            case '7': addAnnotation(Labels.WSTL_DOWN); break;
            case '8': addAnnotation(Labels.WSTL_CONV); break;
            case '9': addAnnotation(Labels.WSTL_CONC); break;
            case '0': addAnnotation(Labels.NOISE); break;
            case 'd': right(); break;
            case 'a': left(); break;
            case 's': save(); break;
            case 'f': fwd(); break;
            case 'b': bwd(); break;
            case 'p': play(); break;
            case 'x': addAnnotation(null); break;
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
            position = params.fftSample((int) match.getStart() - stream.getCurrentOffset());
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
