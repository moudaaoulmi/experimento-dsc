package ish.ecletex.properties;

import org.eclipse.core.runtime.CoreException;

public privileged aspect PropertiesHandler {

	pointcut internalAddSecondSectionHandler(): execution(private void ecletexProjectProperties.internalAddSecondSection());

	pointcut internalAddBibtexOptionHandler(): execution(private void ecletexProjectProperties.internalAddBibtexOption());

	pointcut internalAddPSOptionHandler(): execution(private void ecletexProjectProperties.internalAddPSOption());

	pointcut internalAddPDFOptionHandler(): execution(private void ecletexProjectProperties.internalAddPDFOption());

	pointcut internalAddLanguageOptionHandler(): execution(private String texFileProperties.internalAddLanguageOption(String));

	pointcut internalAddAlternateOptionHandler(): execution(private void texFileProperties.internalAddAlternateOption());

	declare soft: CoreException: internalAddSecondSectionHandler() || internalAddBibtexOptionHandler() || internalAddPSOptionHandler() || internalAddPDFOptionHandler() || internalAddLanguageOptionHandler() || internalAddAlternateOptionHandler();

	void around(texFileProperties tex): internalAddAlternateOptionHandler() && this(tex){
		try {
			proceed(tex);
		} catch (CoreException e) {
			if (tex.DEFAULT_ALTERNATE_SUPPORT.equals(tex.CHECKED)) {
				tex.alternateChecked.setSelection(true);
			} else {
				tex.alternateChecked.setSelection(false);
			}
		}
	}

	String around(texFileProperties tex): internalAddLanguageOptionHandler() && this(tex) {
		try {
			return proceed(tex);
		} catch (CoreException e) {
			return tex.DEFAULT_DICTIONARY;
		}
	}

	void around(ecletexProjectProperties ecle): internalAddPDFOptionHandler() && this(ecle) {
		try {
			proceed(ecle);
		} catch (CoreException e) {
			if (ecle.DEFAULT_PDF_ACTION.equals(ecle.CHECKED)) {
				ecle.pdfChecked.setSelection(true);
			} else {
				ecle.pdfChecked.setSelection(false);
			}
		}
	}

	void around(ecletexProjectProperties ecle): internalAddPSOptionHandler() && this(ecle){
		try {
			proceed(ecle);
		} catch (CoreException e) {
			if (ecle.DEFAULT_PS_ACTION.equals(ecle.CHECKED)) {
				ecle.psChecked.setSelection(true);
			} else {
				ecle.psChecked.setSelection(false);
			}
		}
	}

	void around(ecletexProjectProperties ecle): internalAddBibtexOptionHandler() && this(ecle){
		try {
			proceed(ecle);
		} catch (CoreException e) {
			if (ecle.DEFAULT_BIBTEX_COMPLIER_ACTION.equals(ecle.CHECKED)) {
				ecle.bibtexCheck.setSelection(true);
			} else {
				ecle.bibtexCheck.setSelection(false);
			}
		}
	}

	void around(ecletexProjectProperties ecle): internalAddSecondSectionHandler() && this(ecle) {
		try {
			proceed(ecle);
		} catch (CoreException e) {
			ecle.ownerText.setText(ecle.DEFAULT_FILE);
		}
	}

}
