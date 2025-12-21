package motorola6809.io;

import java.util.HashMap;
import java.util.Map;

public class IOManager {
    
    private Map<Integer, InputDevice> inputDevices;
    private Map<Integer, OutputDevice> outputDevices;
    private InterruptController interruptController;
    
    public IOManager(InterruptController interruptController) {
        this.inputDevices = new HashMap<>();
        this.outputDevices = new HashMap<>();
        this.interruptController = interruptController;
    }
    
    /**
     * Enregistre un périphérique d'entrée à une adresse
     */
    public void registerInputDevice(int address, InputDevice device) {
        inputDevices.put(address, device);
    }
    
    /**
     * Enregistre un périphérique de sortie à une adresse
     */
    public void registerOutputDevice(int address, OutputDevice device) {
        outputDevices.put(address, device);
    }
    
    /**
     * Lit depuis un périphérique d'entrée
     */
    public int read(int address) {
        InputDevice device = inputDevices.get(address);
        if (device != null && device.hasData()) {
            return device.read();
        }
        return 0xFF; // Valeur par défaut si pas de périphérique
    }
    
    /**
     * Écrit vers un périphérique de sortie
     */
    public void write(int address, int data) {
        OutputDevice device = outputDevices.get(address);
        if (device != null) {
            device.write(data);
        }
    }
    
    /**
     * Vérifie si un périphérique d'entrée a des données
     */
    public boolean hasInput(int address) {
        InputDevice device = inputDevices.get(address);
        return device != null && device.hasData();
    }
    
    /**
     * Réinitialise tous les périphériques
     */
    public void reset() {
        inputDevices.values().forEach(InputDevice::reset);
        outputDevices.values().forEach(OutputDevice::reset);
    }
}