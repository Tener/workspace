import java.util.*;
import java.io.*;

public class document {
    ArrayList<Character> text;
    ArrayList<comLabel> labels;
    
    // Constructor
    public document() {
	text = new ArrayList<Character>();
	labels = new ArrayList<comLabel>();
    }
    
    // Getters and Setters
    public ArrayList<Character> getText() {
	return text;
    }
    
    public ArrayList<comLabel> getLabels() {
	return labels;
    }
    
    public void setText(ArrayList<Character> text) {
	this.text.clear();
	this.text.addAll(text);
    }
    
    public void setLabels(ArrayList<comLabel> labels) {
	this.labels.clear();
	this.labels.addAll(labels);
    }

    // To String
    public String toString() {
	String out = "Text = ";
	for (int i = 0; i < text.size(); i++) {
	    out += text.get(i);
	}
	out += "\nLabels = ";
	for (int i = 0; i < labels.size(); i++) {
	    out += labels.get(i) + ",\n";
	}
	return out;
    }
    
    // Adder classes
    public void addText(String newText) {
	for (int i = 0; i < newText.length(); i++) {
	    text.add(new Character(newText.charAt(i)));
	}
    }
    
    public void addLabel(String label, boolean start) {
	labels.add(new comLabel(label, text.size(), start));
    }
}