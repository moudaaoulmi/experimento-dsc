/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package ish.ecletex.wordnet.jwnl;

import ish.ecletex.ecletexPlugin;
import ish.ecletex.preferences.TeXPreferencePage;
import ish.ecletex.preferences.TexExternalToolsPreferencePage;
import ish.ecletex.wordnet.jwnl.data.Adjective;
import ish.ecletex.wordnet.jwnl.data.PointerType;
import ish.ecletex.wordnet.jwnl.data.VerbFrame;
import ish.ecletex.wordnet.jwnl.dictionary.Dictionary;
import ish.ecletex.wordnet.jwnl.util.ResourceBundleSet;
import ish.ecletex.wordnet.jwnl.util.factory.Element;
import ish.ecletex.wordnet.jwnl.util.factory.NameValueParam;
import ish.ecletex.wordnet.jwnl.util.factory.Param;
import ish.ecletex.wordnet.jwnl.util.factory.ParamList;
import ish.ecletex.wordnet.jwnl.util.factory.ValueParam;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/** Contains system info as well as JWNL properties. */
public final class JWNL_NEW {
	// OS types
	public static final OS WINDOWS = new OS("windows");
	public static final OS UNIX = new OS("unix");
	public static final OS MAC = new OS("mac");
	public static final OS UNDEFINED = new OS("undefined");

	public static final OS[] DEFINED_OS_ARRAY = {WINDOWS, UNIX, MAC};
	public static final String OS_PROPERTY_NAME = "os.name";

	private static final String JAVA_VERSION_PROPERTY = "java.version";
	private static final String CORE_RESOURCE = "JWNLResource";

	// initialization stages
	private static final int UNINITIALIZED = 0;
	private static final int START = 1;
	private static final int DICTIONARY_PATH_SET = 2;
	private static final int VERSION_SET = 3;
	private static final int INITIALIZED = 4;

	private static Version _version;
	private static ResourceBundleSet _bundle;
	private static OS _currentOS = UNDEFINED;
	private static int _initStage = UNINITIALIZED;

	static {
		createResourceBundle();
		// set the OS
		String os = System.getProperty(OS_PROPERTY_NAME);
		for (int i = 0; i < DEFINED_OS_ARRAY.length; i++)
			if (DEFINED_OS_ARRAY[i].matches(os))
				_currentOS = DEFINED_OS_ARRAY[i];
	}

	private JWNL_NEW() {
	}

	// tag names
	private static final String VERSION_TAG = "version";
	private static final String DICTIONARY_TAG = "dictionary";
	private static final String PARAM_TAG = "param";
	private static final String RESOURCE_TAG = "resource";

	// attribute names
	private static final String LANGUAGE_ATTRIBUTE = "language";
	private static final String COUNTRY_ATTRIBUTE = "country";
	private static final String CLASS_ATTRIBUTE = "class";
	private static final String NAME_ATTRIBUTE = "name";
	private static final String VALUE_ATTRIBUTE = "value";
	private static final String PUBLISHER_ATTRIBUTE = "publisher";
	private static final String NUMBER_ATTRIBUTE = "number";

	public static void initialize() throws JWNLException {
		checkInitialized(UNINITIALIZED);
		String dictionaryPath = ecletexPlugin.getDefault().getPreferenceStore().getString(
				TexExternalToolsPreferencePage.WN_DICT_DIR);
		System.out.println("Set Dictionary Path: "+dictionaryPath);
		if(dictionaryPath.equals("") || !(new File(dictionaryPath)).exists()){
			return;
		}
		_initStage = START;


		// set the locale
		_bundle.setLocale(new Locale("en", ""));
		_bundle.addResource("PrincetonResource");			
		System.out.println("Set Locale");
		// initialize bundle-dependant resources
		PointerType.initialize();
		Adjective.initialize();
		VerbFrame.initialize();
		System.out.println("Initialized bundle-dependant resources");
		// parse version information		

		_initStage = DICTIONARY_PATH_SET;

		String number = "2.0";
		
		
		_version = new Version(
				"Princeton",
                Double.parseDouble(number),
                new Locale("en", ""));

		_initStage = VERSION_SET;
		System.out.println("Set Version");
		// parse dictionary
		
		Param[] params = new Param[3];
		System.out.println("Setting Paramerters");
		NameValueParam one = new NameValueParam("morphological_processor","ish.ecletex.wordnet.jwnl.dictionary.morph.DefaultMorphologicalProcessor");
			ParamList one_one = new ParamList("operations");
				ValueParam one_one_one = new ValueParam("ish.ecletex.wordnet.jwnl.dictionary.morph.LookupExceptionsOperation");
				ValueParam one_one_two = new ValueParam("ish.ecletex.wordnet.jwnl.dictionary.morph.DetachSuffixesOperation");
					NameValueParam one_one_two_one = new NameValueParam("noun","|s=|ses=s|xes=x|zes=z|ches=ch|shes=sh|men=man|ies=y|");
					NameValueParam one_one_two_two = new NameValueParam("verb","|s=|ies=y|es=e|es=|ed=e|ed=|ing=e|ing=|");
					NameValueParam one_one_two_three = new NameValueParam("adjective","|er=|est=|er=e|est=e");
					ParamList one_one_two_four = new ParamList("operations");
						ValueParam one_one_two_four_one = new ValueParam("ish.ecletex.wordnet.jwnl.dictionary.morph.LookupIndexWordOperation");
						ValueParam one_one_two_four_two = new ValueParam("ish.ecletex.wordnet.jwnl.dictionary.morph.LookupExceptionsOperation");
				ValueParam one_one_three = new ValueParam("ish.ecletex.wordnet.jwnl.dictionary.morph.TokenizerOperation");
					ParamList one_one_three_one = new ParamList("delimiters");
						ValueParam one_one_three_one_one = new ValueParam(" ");
						ValueParam one_one_three_one_two = new ValueParam("-");
					ParamList one_one_three_two = new ParamList("token_operations");
						ValueParam one_one_three_two_one = new ValueParam("ish.ecletex.wordnet.jwnl.dictionary.morph.LookupIndexWordOperation");
						ValueParam one_one_three_two_two = new ValueParam("ish.ecletex.wordnet.jwnl.dictionary.morph.LookupExceptionsOperation");
						ParamList one_one_three_two_three = new ParamList("ish.ecletex.wordnet.jwnl.dictionary.morph.DetachSuffixesOperation");
							NameValueParam one_one_three_two_three_one = new NameValueParam("noun","|s=|ses=s|xes=x|zes=z|ches=ch|shes=sh|men=man|ies=y|");
							NameValueParam one_one_three_two_three_two = new NameValueParam("verb","|s=|ies=y|es=e|es=|ed=e|ed=|ing=e|ing=|");
							NameValueParam one_one_three_two_three_three = new NameValueParam("adjective","|er=|est=|er=e|est=e");
							ParamList one_one_three_two_three_four = new ParamList("operations");
								ValueParam one_one_three_two_three_four_one = new ValueParam("ish.ecletex.wordnet.jwnl.dictionary.morph.LookupIndexWordOperation");
								ValueParam one_one_three_two_three_four_two = new ValueParam("ish.ecletex.wordnet.jwnl.dictionary.morph.LookupExceptionsOperation");
							
		NameValueParam two = new NameValueParam("dictionary_element_factory","ish.ecletex.wordnet.jwnl.princeton.data.PrincetonWN17FileDictionaryElementFactory");
		
		NameValueParam three = new NameValueParam("file_manager","ish.ecletex.wordnet.jwnl.dictionary.file_manager.FileManagerImpl");
			NameValueParam three_one = new NameValueParam("file_type","ish.ecletex.wordnet.jwnl.princeton.file.PrincetonRandomAccessDictionaryFile");
			NameValueParam three_two = new NameValueParam("dictionary_path",dictionaryPath)	;
		
		one.addParam(one_one);
			one_one.addParam(one_one_one);
			one_one.addParam(one_one_two);
				one_one_two.addParam(one_one_two_one);
				one_one_two.addParam(one_one_two_two);
				one_one_two.addParam(one_one_two_three);
				one_one_two.addParam(one_one_two_four);
					one_one_two_four.addParam(one_one_two_four_one);
					one_one_two_four.addParam(one_one_two_four_two);
			one_one.addParam(one_one_three);
				one_one_three.addParam(one_one_three_one);
					one_one_three_one.addParam(one_one_three_one_one);
					one_one_three_one.addParam(one_one_three_one_two);
				one_one_three.addParam(one_one_three_two);
					one_one_three_two.addParam(one_one_three_two_one);
					one_one_three_two.addParam(one_one_three_two_two);
					one_one_three_two.addParam(one_one_three_two_three);
						one_one_three_two_three.addParam(one_one_three_two_three_one);
						one_one_three_two_three.addParam(one_one_three_two_three_two);
						one_one_three_two_three.addParam(one_one_three_two_three_three);
						one_one_three_two_three.addParam(one_one_three_two_three_four);
							one_one_three_two_three_four.addParam(one_one_three_two_three_four_one);
							one_one_three_two_three_four.addParam(one_one_three_two_three_four_two);
						
		three.addParam(three_one);
		three.addParam(three_two);
		
		params[0] = one;
		params[1] = two;
		params[2] = three;
		System.out.println("Set Paramerters");
		Element e = new Element("ish.ecletex.wordnet.jwnl.dictionary.FileBackedDictionary", params);
		e.install();

		_initStage = INITIALIZED;
	}

	private static void createResourceBundle() {
		_bundle = new ResourceBundleSet(CORE_RESOURCE);
	}

	

	private static Locale getLocale(String language, String country) {
		if (language == null) {
			return Locale.getDefault();
		} else if (country == null) {
			return new Locale(language, "");
		} else {
			return new Locale(language, country);
		}
	}

	public static boolean isInitialized() {
		return _initStage == INITIALIZED;
	}

	/** Get the current OS. */
	public static OS getOS() {
		return _currentOS;
	}

	public static double getJavaVersion() {
		String versionStr = System.getProperty(JAVA_VERSION_PROPERTY);
		return Double.parseDouble(versionStr.substring(0, 3));
	}

	/** Get the current WordNet version */
	public static Version getVersion() {
		checkInitialized(VERSION_SET);
		return _version;
	}

	public static ResourceBundle getResourceBundle() {
		return _bundle;
	}

	/** Resolve <var>msg</var> in one of the resource bundles used by the system */
	public static String resolveMessage(String msg) {
		return resolveMessage(msg, new Object[0]);
	}

	/**
	 * Resolve <var>msg</var> in one of the resource bundles used by the system.
	 * @param obj parameter to insert into the resolved message
	 */
	public static String resolveMessage(String msg, Object obj) {
		return resolveMessage(msg, new Object[]{obj});
	}

	/**
	 * Resolve <var>msg</var> in one of the resource bundles used by the system
	 * @param params parameters to insert into the resolved message
	 */
	public static String resolveMessage(String msg, Object[] params) {
		checkInitialized(UNINITIALIZED);
		return insertParams(_bundle.getString(msg), params);
	}

	private static String insertParams(String str, Object[] params) {
		StringBuffer buf = new StringBuffer();
		int startIndex = 0;
		for (int i = 0; i < params.length && startIndex <= str.length(); i++) {
			int endIndex = str.indexOf("{" + i, startIndex);
			if (endIndex != -1) {
				buf.append(str.substring(startIndex, endIndex));
				buf.append(params[i] == null ? null : params[i].toString());
				startIndex = endIndex + 3;
			}
		}
		buf.append(str.substring(startIndex, str.length()));
		return buf.toString();
	}

	public static void checkInitialized(int requiredStage) {
		if (requiredStage > _initStage) {
            throw new JWNLRuntimeException("JWNL_EXCEPTION_007");
        }
	}

	public static void shutdown() {
		_initStage = UNINITIALIZED;
		Dictionary.uninstall();
		_version = null;
		createResourceBundle();
	}

	/** Used to create constants that represent the major categories of operating systems. */
	public static final class OS {
		private String _name;

		protected OS(String name) {
			_name = name;
		}

		public String toString() {
			return resolveMessage("JWNL_TOSTRING_001", _name);
		}

		/**
		 * Returns true if <var>testOS</var> is a version of this OS. For example, calling
		 * WINDOWS.matches("Windows 95") returns true.
		 */
		public boolean matches(String test) {
			return test.toLowerCase().indexOf(_name.toLowerCase()) >= 0;
		}
	}

	/** Represents a version of WordNet. */
	public static final class Version {
		private static final String UNSPECIFIED = "unspecified";

		private String _publisher;
		private double _number;
		private Locale _locale;

		public Version(String publisher, double number, Locale locale) {
			if (publisher == null) {
				publisher = UNSPECIFIED;
			}
			_publisher = publisher;
			_number = number;
			_locale = locale;
		}

		public String getPublisher() {
			return _publisher;
		}

		public double getNumber() {
			return _number;
		}

		public Locale getLocale() {
			return _locale;
		}

		public boolean equals(Object obj) {
			return (obj instanceof Version)
			    && _publisher.equals(((Version) obj)._publisher)
			    && _number == ((Version) obj)._number
			    && _locale.equals(((Version) obj)._locale);
		}

		public String toString() {
			return resolveMessage("JWNL_TOSTRING_002", new Object[]{_publisher, new Double(_number), _locale});
		}

		public int hashCode() {
			return _publisher.hashCode() ^ (int) (_number * 100);
		}
	}
}