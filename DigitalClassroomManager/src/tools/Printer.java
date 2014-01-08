
package tools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JPanel;

/**
 *
 * @author Abdul Hamid
 */
public class Printer implements Printable, ActionListener {

    JPanel printedPanel;
    private Paper paper;
    String types = "";

    public Printer(JPanel panel) {
        printedPanel = panel;
        paper = new Paper();
    }

    public void setDocumentType(String type){
        this.types = type;
        if(type.equalsIgnoreCase("Grade Card")){
            paper.setSize(612, 936);
            paper.setImageableArea(0, 0, 612,936);
      
        }else if(type.equalsIgnoreCase("Inventory")){
            paper.setSize(612, 936);
            paper.setImageableArea(0, 0, 612,936);
        }
        else {
            paper.setSize(612, 936);
            paper.setImageableArea(0, 0, 612,936);
        }
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D)graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        /* Now print the window and its visible contents */
        printedPanel.printAll(graphics);

        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }

    public void actionPerformed(ActionEvent e) {
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

        /*
        System.out.println("WIDTH : " + pf.getWidth());
        System.out.println("HEIGHT : " + pf.getHeight());
        System.out.println("ImeageableX : " + pf.getImageableX());
        System.out.println("ImgaeableY : " + pf.getImageableY());
        System.out.println("ImageableWidth : " + pf.getImageableWidth());
        System.out.println("ImageableHeiht : " + pf.getImageableHeight());
        */
         paper.setSize(612, 936);
            paper.setImageableArea(0, 0, 612,936);
        boolean ok = job.printDialog(aset);
        if(types.equalsIgnoreCase("Grade Card")){
            PageFormat pf = job.defaultPage();
            pf.setOrientation(PageFormat.PORTRAIT);
            
            pf.setPaper(paper);
            
            System.out.println("HEIGHT : " + pf.getHeight()+"  d ");
            System.out.println("WIDTH : " + pf.getWidth());
            job.setPrintable(this,pf);
            
        } else {
            PageFormat pf = job.getPageFormat(aset);
            pf.setPaper(paper);
            job.setPrintable(this,pf);
        }
        
        

        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                System.out.println(ex);
            }
        }
    } // end method
    
    
} // emd class
