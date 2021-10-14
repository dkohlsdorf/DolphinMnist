package org.dkohl.wdp.spectrogram;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KeyMap {
    private static String DEFAULT_KEYMAP = "default_keymapping.yml";

    private Map<Character, String> keymap;
    private Map<String, Character> reverse;

    private Character[] characters;


    public KeyMap(String path) throws Exception {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(DEFAULT_KEYMAP);
        Map<Integer, String> map = yaml.load(inputStream);
        keymap = new HashMap<>();
        reverse = new HashMap<>();
        for(Integer c : map.keySet()) {
            keymap.put((char) (c + 48), map.get(c));
            reverse.put(map.get(c), (char) (c + 48));
        }
        if(!allLabels()) {
            throw new Exception("Bad Label Range: labels not in [0...9]");
        }
        Object[] obj = keymap.keySet().toArray();
        characters = new Character[obj.length];
        for(int i = 0; i < obj.length; i++) {
            characters[i] = (Character) obj[i];
        }
        Arrays.sort(characters);
        System.out.println(keymap);
    }

    private boolean allLabels() {
        for(Character c : keymap.keySet()) {
            int i = c - 48;
            if (i > 9 || i < 0) {
                return false;
            }
        }
        return true;
    }

    public KeyMap() throws Exception{
        this(DEFAULT_KEYMAP);
    }

    public String getLabel(char keycode) {
        return keymap.get(keycode);
    }

    public Character getKey(String entry) {
        return reverse.get(entry);
    }

    public Character[] getValues() {
        return characters;
    }

    public boolean hasKey(char c) {
        return keymap.containsKey(c);
    }

}
