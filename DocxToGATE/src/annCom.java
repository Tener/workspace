import java.util.*;
import java.io.*;

public class annCom {
    String label, type;
    int start, end;
    
    // Constructor
    public annCom(String label, String type) {
	this.label = label;
	this.type = type;
	start = -1;
	end = -1;
    }
    
    // Getters and setters
    public String getLabel() {
	return label;
    }
    
    public String getType() {
	return type;
    }
    
    public int getStart() {
	return start;
    }
    
    public int getEnd() {
	return end;
    }
    
    public void setLabel(String label) {
	this.label = label;
    }
    
    public void setType(String type) {
	this.type = type;
    }
    
    public void setStart(int start) {
	this.start = start;
    }
    
    public void setEnd(int end) {
	this.end = end;
    }

    public String toString() {
	return "{" + label + ", " + type + ", (" + start + ", " + end + ")}";
    }
}