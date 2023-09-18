package com.picoinnov.ballentine.service;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

import javax.print.PrintService;
import javax.print.attribute.standard.PrinterName;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.Orientation;
import org.apache.pdfbox.printing.PDFPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.picoinnov.ballentine.controller.PrinterController;

@Service
public class PrinterService {
	private Logger logger = LoggerFactory.getLogger(PrinterController.class);
	
	/**
	 * 프린터 명칭으로 프린트서비스 검색
	 * @param printer
	 * @return PrinterService
	 */
    public PrintService getPrintServiceForName(String printer) {
        PrintService res = null;
        PrintService[ ] services = PrinterJob.lookupPrintServices();

        for (PrintService ps : services) {
            PrinterName name = ps.getAttribute(PrinterName.class);
            if (name.getValue().equalsIgnoreCase(printer)) {
                res = ps;
                break;
            }
        }
        return res;
    }
    
    /**
     * 파일명으로 PDDocument 생성
     * @param filename
     * @return PDDocument
     */
    public PDDocument getDocument(String filename) {
        PDDocument document = null;
        try {
            document = PDDocument.load(new File(filename));
        } catch (IOException e) {
        	logger.error(e.getMessage(), e.fillInStackTrace());
            e.printStackTrace();
        }
        return document;
    }
    
    /**
     * byte 배열로 PDDocument 생성
     * @param data : byte배열
     * @return PDDocument
     * @throws IOException 
     */
    public PDDocument getDocument(byte[] data) throws IOException {
        PDDocument document = null;
        try {
            document = PDDocument.load(data);
        } catch (IOException e) {
        	logger.error(e.getMessage(), e.fillInStackTrace());
            throw e;
        }
        return document;
    }

    /**
     * 인쇄
     * @param document : PDDocument
     * @param printer : 프린터명칭
     */
    public void print(PDDocument document, String printer) {
        PrintService service = this.getPrintServiceForName(printer);
        if(service == null) {
        	logger.error("Printer is not valid : " + printer);
        	return;
        }

        PrinterJob job = PrinterJob.getPrinterJob();
        try {
            job.setPrintService(service);
//            job.setPageable(new PDFPageable(document));
            job.setPageable(new PDFPageable(document, Orientation.LANDSCAPE));
            job.print();
        } catch (PrinterException e) {
            logger.error(e.getMessage(), e.fillInStackTrace());
            e.printStackTrace();
        }
    }
    
    /**
     * 인쇄
     * @param document : PDDocument
     * @param printer : 프린트 서비스
     * @throws PrinterException 
     */
    private void print(PDDocument document, PrintService printer) throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        try {
            job.setPrintService(printer);
            job.setPageable(new PDFPageable(document));
//            job.setPageable(new PDFPageable(document, Orientation.LANDSCAPE));
            job.print();
        } catch (PrinterException e) {
        	logger.error(e.getMessage(), e.fillInStackTrace());
            throw e;
        }
    }
    
    /**
     * 인쇄
     * @param document : PDDocument
     * @param printer : 프린트 서비스
     * @throws PrinterException 
     */
    private void print(PDDocument document, PrintService printer, String orientation) throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        try {
            job.setPrintService(printer);
            job.setPageable(new PDFPageable(document));
            
            if (orientation == null)
            	job.setPageable(new PDFPageable(document));
            else if (orientation.equalsIgnoreCase("LANDSCAPE"))
            	job.setPageable(new PDFPageable(document, Orientation.LANDSCAPE));
            else if (orientation.equalsIgnoreCase("PORTRAIT"))
            	job.setPageable(new PDFPageable(document, Orientation.PORTRAIT));
            else
            	job.setPageable(new PDFPageable(document));
            
            job.print();
        } catch (PrinterException e) {
        	logger.error(e.getMessage(), e.fillInStackTrace());
            throw e;
        }
    }
    
    /**
     * 인쇄
     * @param data : byte 배열
     * @param printer : 프린터 명칭
     * @throws Exception 
     */
    public void print(byte[] data, String printer, String orientation) throws Exception {
        PrintService service = this.getPrintServiceForName(printer);
        if(service == null) {
        	String str = "Printer is not valid : " + printer;
        	logger.error(str);
        	throw new Exception(str);
        }
        
        PDDocument document = this.getDocument(data);
        if(document == null) {
        	String str = "Document is not valid.";
        	logger.error(str);
        	throw new Exception(str);
        }        
    	
        this.print(document, service, orientation);
        
    	try {    		
			document.close();
		} catch (IOException e) {
			String str = "Document cannot close.";
        	logger.error(str, e.fillInStackTrace());
        	throw new Exception(str);			
		}
    }

}
