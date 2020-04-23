package org.edumips64.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import jsinterop.annotations.JsType;

import org.edumips64.core.*;
import org.edumips64.core.is.BUBBLE;
import org.edumips64.core.is.HaltException;
import org.edumips64.core.is.InstructionBuilder;
import org.edumips64.core.parser.Parser;
import org.edumips64.utils.ConfigStore;
import org.edumips64.utils.InMemoryConfigStore;
import org.edumips64.utils.io.FileUtils;
import org.edumips64.utils.io.NullFileUtils;

import java.util.logging.Logger;

class RegisterJSO extends JavaScriptObject {
  protected RegisterJSO() {}
  public final native String getName()/*-{
    return this.name;
}-*/; 

public final native String getValue()/*-{
    return this.value;
}-*/;

public final native void setValue(String value)/*-{
    this.value = value;
}-*/;

public final native void setName(String name)/*-{
    this.name = name;
}-*/;
}

@JsType(namespace = "jsedumips64")
public class WebUi implements EntryPoint {
  private CPU cpu;
  private Parser parser;
  private SymbolTable symTab;
  private Memory memory;
  private Dinero dinero;
   
  private Logger logger = Logger.getLogger("simulator");

  // Executes the program. Returns an empty string on success, or an error message.
  public String runProgram(String code) {
    logger.info("Running program: " + code);
    try {
      cpu.reset();
      dinero.reset();
      symTab.reset();
      logger.info("About to parse it.");
      parser.doParsing(code);
      dinero.setDataOffset(memory.getInstructionsNumber()*4);
      logger.info("Parsed. Running.");
      cpu.setStatus(CPU.CPUStatus.RUNNING);
      while (true) {
        cpu.step();
      }
    } catch (HaltException e) {
      logger.info("All done.");
      return "";
    } catch (Exception e) {
      logger.warning("Error: " + e.toString());
      return e.toString();
    }
  }

  public String getMemory() {
    return memory.toString();
  }

  public JsArray<RegisterJSO> getRegisters() {
    JsArray<RegisterJSO> registers = JavaScriptObject.createArray().cast();

    try {
      for(Register r : cpu.getRegisters()) {
        RegisterJSO register = (RegisterJSO)JavaScriptObject.createObject().cast();
        register.setName(r.getName());
        register.setValue(r.getHexString());
        registers.push(register);
      }
    } catch (Exception e) {
      logger.warning("Error fetching registers: " + e.toString());
    }
    return registers;
  }

  public String getStatistics() {
    // Ugly, but GWT does not support String.format.
    return cpu.getCycles() + " cycles executed\n" +
        cpu.getInstructions() + " instructions executed\n" +
        cpu.getRAWStalls() + " RAW Stalls\n" +
        cpu.getWAWStalls() + " WAW Stalls\n" +
        cpu.getStructuralStallsDivider() + " structural stalls (divider not available)\n" +
        cpu.getStructuralStallsMemory() + " structural stalls (Memory not available)\n" +
        "Code Size: " + (memory.getInstructionsNumber() * 4) + " Bytes";
  }

  @Override
  public void onModuleLoad() {}

  public void init() {
    // Simulator initialization.
    ConfigStore config = new InMemoryConfigStore(ConfigStore.defaults);
    memory = new Memory();
    symTab = new SymbolTable(memory);
    FileUtils fu = new NullFileUtils();
    IOManager iom = new IOManager(fu, memory);
    cpu = new CPU(memory, config, new BUBBLE());
    dinero = new Dinero();
    InstructionBuilder instructionBuilder = new InstructionBuilder(memory, iom, cpu, dinero, config);
    parser = new Parser(fu, symTab, memory, instructionBuilder);
  }
}
