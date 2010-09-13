package org.tigris.aopmetrics.export;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.tigris.aopmetrics.results.Measurement;
import org.tigris.aopmetrics.results.PackageMeasurements;
import org.tigris.aopmetrics.results.ProjectMeasurements;
import org.tigris.aopmetrics.results.TypeMeasurements;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class XStreamExporter implements Exporter {
	public String export(ProjectMeasurements project) throws ExporterException {
		XStream xstream = setup();
		return xstream.toXML(project);
	}

	public void export(ProjectMeasurements project, String file)
			throws ExporterException {
		XStream xstream = setup();
		internalExport(project, file, xstream);
	}

	private void internalExport(ProjectMeasurements project, String file,
			XStream xstream) throws ExporterException {
		Writer writer = new FileWriter(file);
		xstream.toXML(project, writer);
		writer.close();
	}

	private XStream setup() {
		XStream xstream = new XStream(new XppDriver());

		xstream.alias("project", ProjectMeasurements.class);
		xstream.alias("package", PackageMeasurements.class);
		xstream.alias("type", TypeMeasurements.class);
		xstream.alias("metric", Measurement.class);

		xstream.registerConverter(
				new AttributeAwareReflectionConverter(xstream), -10);
		xstream.registerConverter(new MeasurementConverter());

		xstream.addImplicitCollection(ProjectMeasurements.class, "packages");
		xstream.addImplicitCollection(PackageMeasurements.class, "types");
		xstream
				.addImplicitCollection(ProjectMeasurements.class,
						"measurements");
		xstream
				.addImplicitCollection(PackageMeasurements.class,
						"measurements");
		xstream.addImplicitCollection(TypeMeasurements.class, "measurements");

		return xstream;
	}

	class MeasurementConverter implements Converter {
		public boolean canConvert(Class clazz) {
			return Measurement.class.isAssignableFrom(clazz);
		}

		public void marshal(Object obj, HierarchicalStreamWriter writer,
				MarshallingContext context) {
			Measurement m = (Measurement) obj;
			writer.addAttribute("name", m.getName());
			writer.addAttribute("value", m.getValueAsString());
		}

		public Object unmarshal(HierarchicalStreamReader reader,
				UnmarshallingContext context) {
			throw new RuntimeException("not implemented");
		}
	}
}
