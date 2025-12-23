// SimulatorBackend.java - AVEC DOCUMENTATION COMPLÈTE
package motorola6809.core;

import motorola6809.assembler.Assembler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * API PRINCIPALE DU SIMULATEUR 6809 - BACKEND
 * ===========================================
 * 
 * Cette classe est le cœur du simulateur. Elle fournit :
 * 1. L'assembleur intégré
 * 2. L'exécution du code machine
 * 3. L'accès à la mémoire et aux registres
 * 4. Le contrôle d'exécution (start/pause/stop/step)
 * 
 * UTILISATION :
 * SimulatorBackend backend = SimulatorBackend.getInstance();
 * 
 * @version 1.0
 * @author Motorola6809 Simulator
 */
public class SimulatorBackend {
    
    // Singleton instance
    private static SimulatorBackend instance;
    
    // Composants du simulateur
    private CPU cpu;
    private Assembler assembler;
    
    // État d'exécution
    private boolean isRunning = false;
    private boolean isPaused = false;
    private ScheduledExecutorService executor;
    
    // Données du programme
    private byte[] currentProgram;
    private int programStartAddress = 0x1000;
    private List<String> executionLog;
    private int instructionCount = 0;
    private List<Runnable> updateListeners = new ArrayList<>();
    
    // ==================== CONSTRUCTEUR & SINGLETON ====================
    
    /**
     * Constructeur privé (Singleton)
     */
    private SimulatorBackend() {
        this.cpu = new CPU();
        this.assembler = new Assembler();
        this.executionLog = new ArrayList<>();
        initialize();
    }
    
    /**
     * Obtient l'instance unique du simulateur (Singleton)
     * @return Instance de SimulatorBackend
     */
    public static synchronized SimulatorBackend getInstance() {
        if (instance == null) {
            instance = new SimulatorBackend();
        }
        return instance;
    }
    
    // ==================== INITIALISATION ====================
    
    /**
     * Initialise/réinitialise le simulateur
     * - Réinitialise le CPU
     * - Réinitialise la mémoire
     * - Réinitialise les logs
     */
    public void initialize() {
        cpu.reset();
        executionLog.clear();
        instructionCount = 0;
        isRunning = false;
        isPaused = false;
        
        log("Simulateur initialisé");
        notifyUpdate();
    }
    
    // ==================== ASSEMBLAGE ====================
    
    /**
     * Assemble du code source 6809
     * @param sourceCode Code source en assembleur 6809
     * @return true si succès, false si erreur
     */
    public boolean assemble(String sourceCode) {
        try {
            log("Début de l'assemblage...");
            
            // Assembler le code
            currentProgram = assembler.assemble(sourceCode);
            
            // Charger en mémoire
            cpu.loadProgram(currentProgram, programStartAddress);
            
            log("Programme assemblé : " + currentProgram.length + " octets");
            log("Adresse de départ : $" + String.format("%04X", programStartAddress));
            
            notifyUpdate();
            return true;
            
        } catch (Exception e) {
            log("Erreur d'assemblage : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Assemble et exécute immédiatement
     * @param sourceCode Code source
     * @return true si succès
     */
    public boolean assembleAndRun(String sourceCode) {
        if (assemble(sourceCode)) {
            start();
            return true;
        }
        return false;
    }
    
    // ==================== CONTRÔLE D'EXÉCUTION ====================
    
    /**
     * Démarre l'exécution du programme
     */
    public void start() {
        if (currentProgram == null || currentProgram.length == 0) {
            log("Erreur : Aucun programme chargé");
            return;
        }
        
        if (isRunning) return;
        
        isRunning = true;
        isPaused = false;
        cpu.start();
        
        log("Exécution démarrée");
        
        // Planifier l'exécution
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            if (!isRunning || isPaused) return;
            
            try {
                cpu.step();
                instructionCount++;
                log("Instruction exécutée : PC=$" + 
                    String.format("%04X", cpu.getRegisters().getPC()));
                notifyUpdate();
                
            } catch (Exception e) {
                log("Erreur d'exécution : " + e.getMessage());
                stop();
            }
        }, 0, 100, TimeUnit.MILLISECONDS); // 10 instructions/seconde
        
        notifyUpdate();
    }
    
    /**
     * Met en pause/reprend l'exécution
     */
    public void pause() {
        isPaused = !isPaused;
        if (isPaused) {
            cpu.stop();
            log("Exécution en pause");
        } else {
            cpu.start();
            log("Exécution reprise");
        }
        notifyUpdate();
    }
    
    /**
     * Arrête l'exécution
     */
    public void stop() {
        isRunning = false;
        isPaused = false;
        cpu.stop();
        
        if (executor != null) {
            executor.shutdown();
        }
        
        log("Exécution arrêtée");
        notifyUpdate();
    }
    
    /**
     * Exécute une seule instruction (pas à pas)
     */
    public void step() {
        if (currentProgram == null) {
            log("Erreur : Aucun programme chargé");
            return;
        }
        
        try {
            cpu.step();
            instructionCount++;
            log("Pas à pas exécuté : PC=$" + 
                String.format("%04X", cpu.getRegisters().getPC()));
            notifyUpdate();
            
        } catch (Exception e) {
            log("Erreur pas à pas : " + e.getMessage());
        }
    }
    
    /**
     * Réinitialise complètement le simulateur
     */
    public void reset() {
        stop();
        initialize();
        
        // Recharger le programme si existant
        if (currentProgram != null) {
            cpu.loadProgram(currentProgram, programStartAddress);
        }
        
        log("Simulateur réinitialisé");
        notifyUpdate();
    }
    
    // ==================== ACCÈS MEMOIRE ====================
    
    /**
     * Lit un octet en mémoire
     * @param address Adresse (0x0000 - 0xFFFF)
     * @return Valeur de l'octet
     */
    public byte readMemory(int address) {
        return (byte) cpu.getMemory().readByte(address);
    }
    
    /**
     * Écrit un octet en mémoire
     * @param address Adresse
     * @param value Valeur à écrire
     */
    public void writeMemory(int address, byte value) {
        cpu.getMemory().writeByte(address, value);
        notifyUpdate();
    }
    
    /**
     * Lit un mot (16 bits) en mémoire
     * @param address Adresse
     * @return Valeur du mot
     */
    public int readMemoryWord(int address) {
        return cpu.getMemory().readWord(address);
    }
    
    /**
     * Écrit un mot (16 bits) en mémoire
     * @param address Adresse
     * @param value Valeur à écrire
     */
    public void writeMemoryWord(int address, int value) {
        cpu.getMemory().writeWord(address, value);
        notifyUpdate();
    }
    
    /**
     * Obtient une plage de mémoire
     * @param start Adresse de début
     * @param length Nombre d'octets
     * @return Tableau d'octets
     */
    public byte[] getMemoryRange(int start, int length) {
        byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            data[i] = (byte) cpu.getMemory().readByte(start + i);
        }
        return data;
    }
    
    /**
     * Écrit une plage de mémoire
     * @param start Adresse de début
     * @param data Données à écrire
     */
    public void writeMemoryRange(int start, byte[] data) {
        for (int i = 0; i < data.length; i++) {
            cpu.getMemory().writeByte(start + i, data[i] & 0xFF);
        }
        notifyUpdate();
    }
    
    // ==================== ACCÈS REGISTRES ====================
    
    /**
     * Obtient le Program Counter
     * @return Valeur du PC
     */
    public int getPC() {
        return cpu.getRegisters().getPC();
    }
    
    /**
     * Définit le Program Counter
     * @param value Nouvelle valeur
     */
    public void setPC(int value) {
        cpu.getRegisters().setPC(value);
        notifyUpdate();
    }
    
    /**
     * Obtient l'accumulateur A
     * @return Valeur de A
     */
    public int getA() {
        return cpu.getRegisters().getA();
    }
    
    /**
     * Définit l'accumulateur A
     * @param value Nouvelle valeur
     */
    public void setA(int value) {
        cpu.getRegisters().setA(value);
        notifyUpdate();
    }
    
    /**
     * Obtient l'accumulateur B
     * @return Valeur de B
     */
    public int getB() {
        return cpu.getRegisters().getB();
    }
    
    /**
     * Définit l'accumulateur B
     * @param value Nouvelle valeur
     */
    public void setB(int value) {
        cpu.getRegisters().setB(value);
        notifyUpdate();
    }
    
    /**
     * Obtient le registre double D (A:B)
     * @return Valeur de D
     */
    public int getD() {
        return cpu.getRegisters().getD();
    }
    
    /**
     * Définit le registre double D (A:B)
     * @param value Nouvelle valeur
     */
    public void setD(int value) {
        cpu.getRegisters().setD(value);
        notifyUpdate();
    }
    
    /**
     * Obtient le registre X
     * @return Valeur de X
     */
    public int getX() {
        return cpu.getRegisters().getX();
    }
    
    /**
     * Définit le registre X
     * @param value Nouvelle valeur
     */
    public void setX(int value) {
        cpu.getRegisters().setX(value);
        notifyUpdate();
    }
    
    /**
     * Obtient le registre Y
     * @return Valeur de Y
     */
    public int getY() {
        return cpu.getRegisters().getY();
    }
    
    /**
     * Définit le registre Y
     * @param value Nouvelle valeur
     */
    public void setY(int value) {
        cpu.getRegisters().setY(value);
        notifyUpdate();
    }
    
    /**
     * Obtient le Stack Pointer (S)
     * @return Valeur de S
     */
    public int getS() {
        return cpu.getRegisters().getS();
    }
    
    /**
     * Définit le Stack Pointer (S)
     * @param value Nouvelle valeur
     */
    public void setS(int value) {
        cpu.getRegisters().setS(value);
        notifyUpdate();
    }
    
    /**
     * Obtient le User Stack Pointer (U)
     * @return Valeur de U
     */
    public int getU() {
        return cpu.getRegisters().getU();
    }
    
    /**
     * Définit le User Stack Pointer (U)
     * @param value Nouvelle valeur
     */
    public void setU(int value) {
        cpu.getRegisters().setU(value);
        notifyUpdate();
    }
    
    /**
     * Obtient le Direct Page Register (DP)
     * @return Valeur de DP
     */
    public int getDP() {
        return cpu.getRegisters().getDP();
    }
    
    /**
     * Définit le Direct Page Register (DP)
     * @param value Nouvelle valeur
     */
    public void setDP(int value) {
        cpu.getRegisters().setDP(value);
        notifyUpdate();
    }
    
    // ==================== ACCÈS FLAGS ====================
    
    /**
     * Obtient tous les flags
     * @return Tableau de flags [E,F,H,I,N,Z,V,C]
     */
    public boolean[] getFlags() {
        StatusFlags flags = cpu.getFlags();
        return new boolean[] {
            flags.getEntire(),
            flags.getFIRQMask(),
            flags.getHalfCarry(),
            flags.getIRQMask(),
            flags.getNegative(),
            flags.getZero(),
            flags.getOverflow(),
            flags.getCarry()
        };
    }
    
    /**
     * Obtient un flag spécifique
     * @param flagName Nom du flag (E,F,H,I,N,Z,V,C)
     * @return Valeur du flag
     */
    public boolean getFlag(String flagName) {
        StatusFlags flags = cpu.getFlags();
        switch (flagName.toUpperCase()) {
            case "E": return flags.getEntire();
            case "F": return flags.getFIRQMask();
            case "H": return flags.getHalfCarry();
            case "I": return flags.getIRQMask();
            case "N": return flags.getNegative();
            case "Z": return flags.getZero();
            case "V": return flags.getOverflow();
            case "C": return flags.getCarry();
            default: return false;
        }
    }
    
    /**
     * Définit un flag
     * @param flagName Nom du flag
     * @param value Nouvelle valeur
     */
    public void setFlag(String flagName, boolean value) {
        StatusFlags flags = cpu.getFlags();
        switch (flagName.toUpperCase()) {
            case "E": flags.setEntire(value); break;
            case "F": flags.setFIRQMask(value); break;
            case "H": flags.setHalfCarry(value); break;
            case "I": flags.setIRQMask(value); break;
            case "N": flags.setNegative(value); break;
            case "Z": flags.setZero(value); break;
            case "V": flags.setOverflow(value); break;
            case "C": flags.setCarry(value); break;
        }
        notifyUpdate();
    }
    
    // ==================== ÉVÉNEMENTS ====================
    
    /**
     * Ajoute un listener pour les mises à jour
     * @param listener Callback à appeler lors des updates
     */
    public void addUpdateListener(Runnable listener) {
        updateListeners.add(listener);
    }
    
    /**
     * Retire un listener
     * @param listener Listener à retirer
     */
    public void removeUpdateListener(Runnable listener) {
        updateListeners.remove(listener);
    }
    
    /**
     * Notifie tous les listeners
     */
    private void notifyUpdate() {
        for (Runnable listener : updateListeners) {
            listener.run();
        }
    }
    
    // ==================== LOGGING ====================
    
    /**
     * Ajoute un message au log
     * @param message Message à logger
     */
    private void log(String message) {
        executionLog.add(message);
        // Garde les 100 derniers messages
        if (executionLog.size() > 100) {
            executionLog.remove(0);
        }
    }
    
    /**
     * Obtient les logs d'exécution
     * @return Liste des messages de log
     */
    public List<String> getExecutionLog() {
        return new ArrayList<>(executionLog);
    }
    
    /**
     * Efface les logs
     */
    public void clearLog() {
        executionLog.clear();
        notifyUpdate();
    }
    
    // ==================== GETTERS/SETTERS ====================
    
    /**
     * Obtient le CPU interne
     * @return Instance du CPU
     */
    public CPU getCPU() {
        return cpu;
    }
    
    /**
     * Obtient le programme courant
     * @return Code machine courant
     */
    public byte[] getCurrentProgram() {
        return currentProgram;
    }
    
    /**
     * Obtient l'adresse de départ du programme
     * @return Adresse de départ
     */
    public int getProgramStartAddress() {
        return programStartAddress;
    }
    
    /**
     * Définit l'adresse de départ du programme
     * @param address Nouvelle adresse
     */
    public void setProgramStartAddress(int address) {
        this.programStartAddress = address & 0xFFFF;
    }
    
    /**
     * Vérifie si le simulateur est en cours d'exécution
     * @return true si en cours d'exécution
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Vérifie si le simulateur est en pause
     * @return true si en pause
     */
    public boolean isPaused() {
        return isPaused;
    }
    
    /**
     * Obtient le nombre d'instructions exécutées
     * @return Nombre d'instructions
     */
    public int getInstructionCount() {
        return instructionCount;
    }
    
    /**
     * Obtient les cycles CPU consommés
     * @return Nombre de cycles
     */
    public long getCycleCount() {
        return cpu.getCycleCount();
    }
    
    /**
     * Obtient l'état complet du CPU
     * @return String décrivant l'état
     */
    public String getCPUState() {
        return cpu.toString();
    }
    
    // ==================== UTILITAIRES ====================
    
    /**
     * Formate une valeur en hexadécimal 16 bits
     */
    public static String formatHex16(int value) {
        return String.format("%04X", value & 0xFFFF);
    }
    
    /**
     * Formate une valeur en hexadécimal 8 bits
     */
    public static String formatHex8(int value) {
        return String.format("%02X", value & 0xFF);
    }
    
    /**
     * Parse une chaîne hexadécimale
     */
    public static int parseHex(String hex) {
        if (hex.startsWith("$") || hex.startsWith("0x")) {
            hex = hex.substring(hex.startsWith("$") ? 1 : 2);
        }
        return Integer.parseInt(hex, 16);
    }
}