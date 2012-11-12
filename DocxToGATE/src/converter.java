import java.util.*;
import java.io.*;

public class converter {

    // Main Method
    public static void main(String [] args) {
	String commentsFile, textFile, outFile;
	ArrayList<annCom> annComs = new ArrayList<annCom>();
	document doc = new document();
	if (args.length == 3) {
	    commentsFile = args[0];
	    textFile = args[1];
	    outFile = args[2];
	} else {
	    commentsFile = "comments.xml";
	    textFile = "document.xml";
	    outFile = "out.xml";
	}

	parseComments(commentsFile, annComs);
	parseDocument(textFile, doc);
	setAnnComs(annComs, doc);
	createGATEXML(outFile, annComs, doc);
    }

    // Parses the Annontations/Comments from the .docx comments file
    private static void parseComments(String commentsFile, ArrayList<annCom> annComs) {
	try {
	    FileInputStream fstream = new FileInputStream(commentsFile);
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strLine, label, type;
	    while ((strLine = br.readLine()) != null) {
		while (strLine.length() > 0) {

		    // find the id/label of a comment
		    if (strLine.indexOf("w:id=") != -1) {
			strLine = strLine.substring(strLine.indexOf("w:id=") + 6);
			if (strLine.indexOf("\"") != -1) {
			    label = strLine.substring(0, strLine.indexOf("\""));
			    strLine = strLine.substring(strLine.indexOf("\"") + 1);

			    // find the text of the comment
			    if (strLine.indexOf("<w:t>") != -1) {
				strLine = strLine.substring(strLine.indexOf("<w:t>") + 5);
				if (strLine.indexOf("</w:t>") != -1) {
				    type = strLine.substring(0, strLine.indexOf("</w:t>"));
				    strLine = strLine.substring(strLine.indexOf("</w:t>") + 6);

				    // add label/text pair to the list of comments
				    annComs.add(new annCom(label, type));
				} else {
				    strLine = "";
				}
			    } else {
				strLine = "";
			    }
			} else {
			    strLine = "";
			}
		    } else {
			strLine = "";
		    }
		}
	    }
	    in.close();
	} catch (Exception e) {
	    System.err.println("Error: " + e.getMessage());
	}
    }

    // Parses the Document and comment posisitions from the .docx document file
    private static void parseDocument(String textFile, document doc) {
	try {
	    FileInputStream fstream = new FileInputStream(textFile);
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strLine, text, label;
	    while ((strLine = br.readLine()) != null) {
		while (strLine.length() > 0) {

		    // If text tag is found
		    if (strLine.startsWith("<w:t>") || strLine.startsWith("<w:t ")) {
			strLine = strLine.substring(4);
			if (strLine.indexOf(">") != -1) {
			    strLine = strLine.substring(strLine.indexOf(">") + 1);
			    if (strLine.indexOf("</w:t>") != -1) {
				text = strLine.substring(0, strLine.indexOf("</w:t>"));
				
				// add the text to the document text
				doc.addText(text);
				strLine = strLine.substring(strLine.indexOf("</w:t>") + 6);
			    }
			}
		    }

		    // Deal with runs and paragraphs
		    // If an end of a run is found
		    if (strLine.startsWith("</w:r>")) {
			doc.addText(" ");
		    }
		    // If a end of paragraph is found
		    if (strLine.startsWith("</w:p>")) {
			doc.addText(". ");
		    }

		    // Deal with tables
		    // If a column is found
		    if (strLine.startsWith("</w:tc>")) {
			doc.addText(" ");
		    }
		    // If a row is found
		    if (strLine.startsWith("</w:tr>")) {
			doc.addText(". ");
		    }

		    // If start tag is found
		    if (strLine.startsWith("<w:commentRangeStart w:id=\"")) {
			strLine = strLine.substring(27);
			if (strLine.indexOf("\"") != -1) {
			    label = strLine.substring(0, strLine.indexOf("\""));
			    
			    // add the start label at the current index of the text
			    doc.addLabel(label, true);
			    strLine = strLine.substring(strLine.indexOf("\"") + 1);
			}
		    }

		    // If end tag is found
		    if (strLine.startsWith("<w:commentRangeEnd w:id=\"")) {
			strLine = strLine.substring(25);
			if (strLine.indexOf("\"") != -1) {
			    label = strLine.substring(0, strLine.indexOf("\""));

			    // add the end label at the current index of the text
			    doc.addLabel(label, false);
			    strLine = strLine.substring(strLine.indexOf("\"") + 1);
			}
		    }

		    if (strLine.length() > 0) {
			strLine = strLine.substring(1);
		    }
		}
	    }
	    in.close();
	} catch (Exception e) {
	    System.err.println("Error: " + e.getMessage());
	}
    }	

    // Sets the annotations/comments to have the correct position
    private static void setAnnComs(ArrayList<annCom> annComs, document doc) {
	ArrayList<comLabel> labels = new ArrayList<comLabel>();
	labels.addAll(doc.getLabels());
	for (int i = 0; i < annComs.size(); i++) {
	    for (int j = 0; j < labels.size(); j++) {
		if (annComs.get(i).getLabel().equals(labels.get(j).getLabel())) {
		    if (labels.get(j).isStart()) {
			annComs.get(i).setStart(labels.get(j).getPosition());
		    } else {
			annComs.get(i).setEnd(labels.get(j).getPosition());
		    }
		}
	    }
	}
    }

    // Creates the GATE ready XML
    private static void createGATEXML(String outFile, ArrayList<annCom> annComs, document doc) {
	ArrayList<comLabel> labels = new ArrayList<comLabel>();
	labels.addAll(doc.getLabels());
	ArrayList<Integer> nodes = new ArrayList<Integer>();
	int nextNode = 0;
	for (int i = 0; i < labels.size(); i++) {
	    nodes.add(labels.get(i).getPosition());
	}
	sortSet(nodes);
	try {
	    FileWriter fstream = new FileWriter(outFile);
	    BufferedWriter out = new BufferedWriter(fstream);

	    // write header
	    out.write("<?xml version='1.0' encoding='MacRoman'?>\n<GateDocument>\n");
	    
	    // write document features
	    out.write("<GateDocumentFeatures>\n");
	    out.write("<Feature>\n");
	    out.write("<Name className=\"java.lang.String\">MimeType</Name>\n");
	    out.write("<Value className=\"java.lang.String\">text/plain</Value>\n");
	    out.write("</Feature>\n");
	    out.write("<Feature>\n");
	    out.write("<Name className=\"java.lang.String\">docNewLineType</Name>\n");
	    out.write("<Value className=\"java.lang.String\">LF</Value>\n");
	    out.write("</Feature>\n");
	    out.write("</GateDocumentFeatures>\n");

	    // write text with nodes
	    out.write("<TextWithNodes>");
	    out.write("<Node id=\"0\" />");
	    if (nodes.get(nextNode) == 0) {
		nextNode++;
	    }
	    for (int i = 0; i < doc.getText().size(); i++) {
		out.write(doc.getText().get(i));
		if (i + 1 == nodes.get(nextNode)) {
		    out.write("<Node id=\"" + (i + 1) + "\" />");
		    nextNode++;
		    if (nextNode >= nodes.size()) {
			nextNode--;
		    }
		}
	
	    }
	    out.write("</TextWithNodes>\n");

	    // write annotation set
	    out.write("<AnnotationSet>\n");
	    for (int i = 0; i < annComs.size(); i++) {
		out.write("<Annotation Id=\"" + annComs.get(i).getLabel() + "\" Type=\"" +
			  annComs.get(i).getType() + "\" StartNode=\"" + annComs.get(i).getStart() +
			  "\" EndNode=\"" + annComs.get(i).getEnd() + "\">\n</Annotation>\n");
	    }
	    out.write("</AnnotationSet>\n");

	    // write footer
	    out.write("</GateDocument>");

	    out.close();
	} catch (Exception e) {
	    System.err.println("Error: " + e.getMessage());
	}
    }

    // Turns an arraylist of Integers into a sorted set
    private static void sortSet(ArrayList<Integer> set) {
	
	// turn into a set
	for (int i = 0; i < set.size(); i++) {
	    if (set.lastIndexOf(set.get(i)) != i) {
		set.remove(i);
		i--;
	    }
	}

	// sort
	int min, temp;
	for (int i = 0; i < set.size(); i++) {
	    min = i;
	    for (int j = i + 1; j < set.size(); j++) {
		if (set.get(j) < set.get(min)) {
		    min = j;
		}
	    }
	    if (min != i) {
		temp = set.get(i);
		set.set(i, min);
		set.set(min, temp);
	    }
	}
    }
}