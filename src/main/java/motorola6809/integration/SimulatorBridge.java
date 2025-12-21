package motorola6809.integration;

import motorola6809.assembler.Assembler;
import motorola6809.core.CPU;
import motorola6809.ui.ArchitectureWindow;
import motorola6809.ui.ProgrammeWindow;
import motorola6809.ui.RAMWindow;
import motorola6809.ui.ROMWindow;
import motorola6809.ui.EditeurWindow;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulatorBridge {
    
    private CPU cpu;
    private EditeurWindow editorWindow;
    private Thread executionThread;
    private AtomicBoolean running;
    private AtomicBoolean programLoaded;
    
    public SimulatorBridge(EditeurWindow editorWindow) {
        this.cpu = new CPU();
        this.cpu.reset();
        this.editorWindow = editorWindow;
        this.running = new AtomicBoolean(false);
        this.programLoaded = new AtomicBoolean(false);
    }
    
    public boolean compileAndLoad() {
        try {
            String code = editorWindow.getText();
            
            Assembler assembler = new Assembler();
            byte[] machineCode = assembler.assemble(code);
            
            System.out.println("Code machine généré (" + machineCode.length + " octets):");
            for (int i = 0; i < machineCode.length; i++) {
                System.out.print(String.format("%02X ", machineCode[i] & 0xFF));
            }
            System.out.println();
            
            // Charge en mémoire ROM
            cpu.loadProgram(machineCode, 0x1400);
            cpu.getRegisters().setPC(0x1400);
            
            // Initialise les vecteurs d'interruption
            cpu.getMemory().writeWord(0xFFFA, 0xFFFF); // Vecteur SWI
            
            // Met à jour l'interface ROM
            updateROMDisplay(machineCode);
            
            // Met à jour l'interface
            updateUI();
            
            programLoaded.set(true);
            System.out.println("Programme chargé à 0x1400");
            System.out.println("PC initial = " + String.format("%04X", cpu.getRegisters().getPC()));
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de compilation");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            });
            return false;
        }
    }
    
    public boolean executeStep() {
        if (cpu.isHalted() || !programLoaded.get()) {
            return false;
        }
        
        try {
            int pcBefore = cpu.getRegisters().getPC();
            int cycles = cpu.executeInstruction();
            
            System.out.println("Instruction exécutée à PC=" + 
                String.format("%04X", pcBefore) + ", cycles=" + cycles);
            System.out.println("Nouveau PC=" + String.format("%04X", cpu.getRegisters().getPC()));
            
            updateUI();
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur d'exécution");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            });
            return false;
        }
    }
    
    public void executeAll() {
        if (executionThread != null && executionThread.isAlive()) {
            return;
        }
        
        running.set(true);
        executionThread = new Thread(() -> {
            try {
                while (running.get() && !cpu.isHalted()) {
                    executeStep();
                    Thread.sleep(200); // 200ms entre les instructions
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                running.set(false);
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Exécution terminée");
                    alert.setHeaderText(null);
                    alert.setContentText("Le programme s'est terminé normalement.");
                    alert.showAndWait();
                });
            }
        });
        executionThread.start();
    }
    
    public void stop() {
        running.set(false);
        if (executionThread != null) {
            executionThread.interrupt();
        }
    }
    
    public void reset() {
        stop();
        cpu.reset();
        programLoaded.set(false);
        running.set(false);
        updateUI();
    }
    
    public boolean isRunning() {
        return running.get();
    }
    
    public boolean isProgramLoaded() {
        return programLoaded.get();
    }
    
    private void updateUI() {
        Platform.runLater(() -> {
            // Met à jour les registres
            ArchitectureWindow.setPC(String.format("%04X", cpu.getRegisters().getPC()));
            ArchitectureWindow.setA(String.format("%02X", cpu.getRegisters().getA()));
            ArchitectureWindow.setB(String.format("%02X", cpu.getRegisters().getB()));
            ArchitectureWindow.setX(String.format("%04X", cpu.getRegisters().getX()));
            ArchitectureWindow.setY(String.format("%04X", cpu.getRegisters().getY()));
            ArchitectureWindow.setS(String.format("%04X", cpu.getRegisters().getS()));
            ArchitectureWindow.setU(String.format("%04X", cpu.getRegisters().getU()));
            ArchitectureWindow.setDP(String.format("%02X", cpu.getRegisters().getDP()));
            
            // Met à jour les flags
            ArchitectureWindow.setC(cpu.getFlags().getCarry() ? "1" : "0");
            ArchitectureWindow.setV(cpu.getFlags().getOverflow() ? "1" : "0");
            ArchitectureWindow.setZ(cpu.getFlags().getZero() ? "1" : "0");
            ArchitectureWindow.setN(cpu.getFlags().getNegative() ? "1" : "0");
            ArchitectureWindow.setI(cpu.getFlags().getIRQMask() ? "1" : "0");
            ArchitectureWindow.setH(cpu.getFlags().getHalfCarry() ? "1" : "0");
            ArchitectureWindow.setF(cpu.getFlags().getFIRQMask() ? "1" : "0");
            ArchitectureWindow.setE(cpu.getFlags().getEntire() ? "1" : "0");
            
            // Affiche l'instruction dans ProgrammeWindow
            int pc = cpu.getRegisters().getPC();
            if (pc > 0) {
                int opcode = cpu.getMemory().readByte(pc - 1);
                ProgrammeWindow.displayInstructions(pc - 1, 
                    String.format("%02X", opcode));
            }
            
            // Met à jour la mémoire RAM
            updateRAMDisplay();
        });
    }
    
    private void updateROMDisplay(byte[] machineCode) {
        Platform.runLater(() -> {
            for (int i = 0; i < machineCode.length && i < 1024; i++) {
                ROMWindow.setData(i, String.format("%02X", machineCode[i] & 0xFF));
            }
        });
    }
    
    private void updateRAMDisplay() {
        Platform.runLater(() -> {
            // Affiche les 16 premières adresses de RAM
            for (int i = 0; i < 16; i++) {
                int value = cpu.getMemory().readByte(i);
                RAMWindow.setData(String.format("%02X", value), i);
            }
        });
    }
}