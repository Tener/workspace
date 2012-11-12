import java.util.*;
import java.io.*;

public class comLabel {
    String label;
    int position;
    boolean start;
    
    // Constructor
    public comLabel (String label, int position, boolean start) {
	this.label = label;
	this.position = position;
	this.start = start;
    }
    
    // Getters and Setters
    public String getLabel() {
	return label;
    }
    
    public int getPosition() {
	return position;
    }
    
    public boolean isStart() {
	return start;
    }
    
    public void setLabel(String label) {
	this.label = label;
    }
    
    public void setPosition(int position) {
	this.position = position;
    }
    
    public void setStart(boolean start) {
	this.start = start;
    }

    public String toString() {
	return "{" + label + ", " + position + ", " + start + "}";
    }
}