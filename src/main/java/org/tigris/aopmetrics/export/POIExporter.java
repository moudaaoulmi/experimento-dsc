package org.tigris.aopmetrics.export;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.tigris.aopmetrics.results.Measurement;
import org.tigris.aopmetrics.results.MeasurementsVisitor;
import org.tigris.aopmetrics.results.PackageMeasurements;
import org.tigris.aopmetrics.results.ProjectMeasurements;
import org.tigris.aopmetrics.results.TypeMeasurements;

/**
 * Excel XLS exporter for aopmetrics. Uses jakarta POI.
 * 
 * @author <a href="mailto:misto@e-informatyka.pl">Michal Stochmialek</a>
 * @version CVS $Id: POIExporter.java,v 1.1 2006/04/20 23:37:20 misto Exp $
 */
public class POIExporter implements Exporter {
	public String export(ProjectMeasurements project) throws ExporterException {
		throw new ExporterException(
				"XLS exported doesn't support console output. Specify file.");
	}

	public void export(ProjectMeasurements project, String file)
			throws ExporterException {
		ExportMeasurementsVisitor visitor = new ExportMeasurementsVisitor();
		project.traverse(visitor);
		internalExport(file, visitor);
	}

	private void internalExport(String file, ExportMeasurementsVisitor visitor)
			throws ExporterException {
		FileOutputStream stream = new FileOutputStream(file);
		visitor.workbook.write(stream);
		stream.close();
	}

	class ExportMeasurementsVisitor extends MeasurementsVisitor {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet packageSheet, typeSheet;
		HSSFCellStyle headingStyle;

		Map<String, Integer> typeMetricsToColMapping = new HashMap();
		Map<String, Integer> pkgMetricsToColMapping = new HashMap();

		int packagesCount;
		int typesCount;

		public void visit(ProjectMeasurements project) {
			headingStyle = workbook.createCellStyle();
			headingStyle.setFillForegroundColor(HSSFColor.PLUM.index);
			headingStyle.setFillPattern(HSSFCellStyle.FINE_DOTS);
			headingStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			headingStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			packageSheet = workbook.createSheet("packages");
			HSSFRow pkgHeadingsRow = packageSheet.createRow(0);
			setCellStringValue(pkgHeadingsRow, 0, "Package name", headingStyle);

			typeSheet = workbook.createSheet("types");
			HSSFRow typeHeadingsRow = typeSheet.createRow(0);
			setCellStringValue(typeHeadingsRow, 0, "Package name", headingStyle);
			setCellStringValue(typeHeadingsRow, 1, "Type name", headingStyle);
			setCellStringValue(typeHeadingsRow, 2, "Type kind", headingStyle);
		}

		public void visit(PackageMeasurements pkg, ProjectMeasurements project) {
			HSSFRow row = packageSheet.createRow(1 + packagesCount);

			HSSFCell pkgNameCell = row.createCell((short) 0);
			pkgNameCell.setCellValue(pkg.getName());

			for (Measurement m : pkg.getMeasurements()) {
				int col = getColumnNameForPkg(m.getName());
				setCellMeasurementValue(row, col + 1, m);
			}

			packagesCount++;
		}

		private int getColumnNameForPkg(String name) {
			if (pkgMetricsToColMapping.containsKey(name))
				return pkgMetricsToColMapping.get(name);
			else {
				int column = pkgMetricsToColMapping.size();
				pkgMetricsToColMapping.put(name, column);
				setCellStringValue(packageSheet, 0, column + 1, name,
						headingStyle);
				return column;
			}
		}

		public void visit(TypeMeasurements type, PackageMeasurements pkg) {
			HSSFRow row = typeSheet.createRow(typesCount + 1);
			setCellStringValue(row, 0, pkg.getName());
			setCellStringValue(row, 1, type.getName());
			setCellStringValue(row, 2, type.getKind());

			for (Measurement m : type.getMeasurements()) {
				int col = getColumnNameForType(m.getName());
				setCellMeasurementValue(row, col + 3, m);
			}

			typesCount++;
		}

		private int getColumnNameForType(String name) {
			if (typeMetricsToColMapping.containsKey(name))
				return typeMetricsToColMapping.get(name);
			else {
				int column = typeMetricsToColMapping.size();
				typeMetricsToColMapping.put(name, column);
				setCellStringValue(typeSheet, 0, column + 3, name, headingStyle);
				return column;
			}
		}

		private void setCellMeasurementValue(HSSFRow row, int col, Measurement m) {
			HSSFCell cell = row.createCell((short) col);
			if (m.isFloatValue())
				cell.setCellValue(m.getValuef());
			else
				cell.setCellValue(m.getValuei());
		}

		private void setCellStringValue(HSSFSheet sheet, int rownr, int colnr,
				String value, HSSFCellStyle style) {
			setCellStringValue(sheet.getRow(rownr), colnr, value, style);
		}

		private void setCellStringValue(HSSFRow row, int colnr, String value) {
			setCellStringValue(row, colnr, value, null);
		}

		private void setCellStringValue(HSSFRow row, int colnr, String value,
				HSSFCellStyle style) {
			HSSFCell cell = row.createCell((short) colnr);
			cell.setCellValue(value);
			if (style != null)
				cell.setCellStyle(style);
		}

	}
}
