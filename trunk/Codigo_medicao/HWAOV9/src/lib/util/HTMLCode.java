package lib.util;

import healthwatcher.Constants;

/**
 *
 * Classe HTMLCode - possui métodos para facilitar a 
 * impressão dos códigos html de saída dos servlets
 *
 */
public class HTMLCode {

	private static final int REGULAR_PAGE = 1;

	private static final int QUERIES_PAGE = 2;

	private static final int ADMINISTRATOR_PAGE = 3;

	private static String open1() {
		return "<HTML><HEAD><TITLE>";
	}

	private static String open2() {
		return "</TITLE></HEAD>";
	}

	public static String open(String title, String bgColor) {
		return HTMLCode.open1() + title + HTMLCode.open2() + "<body bgcolor=\"" + bgColor + "\">";
	}

	public static String open(String title) {
		return HTMLCode.open("Health-Watcher - 2006 - " + title, "white");
	}

	public static String open() {
		return HTMLCode.open("");
	}

	public static String close() {
		return "<p><center>" + HTMLCode.foot();
	}

	public static String closeAdministrator() {
		return "<p><center>" + HTMLCode.linkAdministrator() + " - " + HTMLCode.foot();
	}

	public static String closeQueries() {
		return "<p><center>" + HTMLCode.linkQueries() + " - " + HTMLCode.foot();
	}

	private static String foot() {
		return "<a href=\"" + Constants.SYSTEM_INDEX + "\">" + "Main menu</a>"
				+ "<p><small>Health-Watcher - 2006</small></center></body></html>";
	}

	private static String linkAdministrator() {
		return link(Constants.SYSTEM_INDEX_ADMINISTRATOR, "Employee's menu");
	}

	private static String linkQueries() {
		return link(Constants.SYSTEM_QUERIES, "Queries' menu");
	}

	public static String link(String url, String description) {
		return "<a href=\"" + url + "\">" + description + "</a>";
	}

	public static String errorPage(String message) {
		return HTMLCode.htmlPage("Error message", message);
	}

	public static String errorPageAdministrator(String message) {
		return HTMLCode.htmlPage("Administrator - Error message", message, ADMINISTRATOR_PAGE);
	}

	public static String errorPageQueries(String message) {
		return HTMLCode.htmlPage("Queries - Error message", message, QUERIES_PAGE);
	}

	public static String htmlPage(String title, String text) {
		return htmlPage(title, text, REGULAR_PAGE);
	}

	public static String htmlPageQueries(String title, String text) {
		return htmlPage(title, text, QUERIES_PAGE);
	}

	public static String htmlPageAdministrator(String title, String text) {
		return htmlPage(title, text, ADMINISTRATOR_PAGE);
	}

	public static String htmlPage(String title, String text, int pageType) {

		StringBuffer pagina = new StringBuffer();

		pagina.append(HTMLCode.open(title, "white"));
		pagina.append("<center>");
		pagina.append("<font face=\"Arial\" color=\"black\" size=+1>" + title + "</font></td>");
		pagina.append("</center>");

		pagina.append("<font face=\"Arial\" color=\"black\"><small>");
		pagina.append("<p align=\"center\">");
		pagina.append(text);
		pagina.append("</small></font>");
		switch (pageType) {
		case QUERIES_PAGE:
			pagina.append(HTMLCode.closeQueries());
			break;
		case ADMINISTRATOR_PAGE:
			pagina.append(HTMLCode.closeAdministrator());
			break;
		default:
			pagina.append(HTMLCode.close());
		}
		return pagina.toString();
	}

}