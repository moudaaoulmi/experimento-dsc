package org.tigris.aopmetrics.export;

import java.io.IOException;

import org.tigris.aopmetrics.export.POIExporter.ExportMeasurementsVisitor;
import org.tigris.aopmetrics.results.ProjectMeasurements;

import com.thoughtworks.xstream.XStream;

import exception.ExceptionHandler;
@ExceptionHandler
public privileged aspect ExportHandler {

	pointcut internalExportHandler(): execution(private void POIExporter.internalExport(String, ExportMeasurementsVisitor));

	pointcut internalExportHandler2(): execution(private void XStreamExporter.internalExport(ProjectMeasurements, String,XStream));

	declare soft: IOException: internalExportHandler()||internalExportHandler2();
	
//	//Não está conseguindo usar o thisEnclosingJoinPointStaticPart
//	void around() throws ExporterException: internalExportHandler() || internalExportHandler2() {
//		int id = thisEnclosingJoinPointStaticPart.getId();
//		try {
//			proceed();
//		} catch (IOException e) {
//			String message = "";
//			switch(id){
//				case 1: message = "while exporting to xml";
//				break;
//				case 2: message = "error while writing resultfile.";
//				break;
//			}
//			throw new ExporterException(message, e);
//		}
//	}
	
	void around() throws ExporterException: internalExportHandler2() {
		try {
			proceed();
		} catch (IOException e) {
			throw new ExporterException("while exporting to xml", e);
		}
	}

	void around() throws ExporterException: internalExportHandler(){
		try {
			proceed();
		} catch (IOException e) {
			throw new ExporterException("error while writing resultfile.", e);
		}
	}

}
