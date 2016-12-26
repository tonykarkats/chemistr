package ximeio;

import java.awt.*;
import javax.swing.*;
import java.awt.print.*;

public class PrintableDocument implements Printable {
	private Component compent;

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
	
	public static void printComponent(Component c) {
		new PrintableDocument(c).print();
	}
	
	public PrintableDocument(Component compent) {
		this.compent = compent;
	}
	
	public void print() {
		PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(this);

		if(printJob.printDialog())
			try {
			printJob.print();
		}
		catch(PrinterException pe) {
			System.out.println("Error printing: " + pe);
		}
	}
	
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		if (pageIndex > 0) {
			return(NO_SUCH_PAGE);
		}
		else {
			Graphics2D graph = (Graphics2D)g;
                         // pageFormat.setOrientation(pageFormat.LANDSCAPE);
			graph.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                        //System.out.println("BEF"+graph.getClipBounds());
                        //graph.scale(0.60, 0.60);

                        graph.scale(864.0/(double)dim.width, 486.0/(double)dim.height);
                        

                        
                       
			disableBuffering(compent);
			compent.paint(graph);
			enableBuffering(compent);
			return(PAGE_EXISTS);
		}
	}
	
	public static void disableBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(false);
	}
	
	public static void enableBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(true);
	}
}

