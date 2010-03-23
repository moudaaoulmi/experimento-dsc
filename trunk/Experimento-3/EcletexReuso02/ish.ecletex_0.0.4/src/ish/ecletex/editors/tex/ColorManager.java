/*
 * Created on 23-Sep-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.editors.tex;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * @author ish
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ColorManager {

	private Map _colorMap;
	private IPreferenceStore _store;

	public ColorManager(IPreferenceStore store) {
		_colorMap = new HashMap();
		_store = store;
	}

	public Color getColor(String type) {
		if(_colorMap == null)
			_colorMap = new HashMap();
		RGB prefColor = PreferenceConverter.getColor(_store, type);
		Color color = null;
		if (_colorMap.containsKey(type)
			&& (color = (Color) _colorMap.get(type)).getRGB().equals(prefColor)) {
			color = (Color) _colorMap.get(type);
		} else {
			color = new Color(Display.getDefault(), prefColor);
			_colorMap.put(type, color);
		}
		return color;
	}

	public void dispose() {
		Collection colors = _colorMap.values();
		for (Iterator iter = colors.iterator(); iter.hasNext();) {
			Color color = (Color) iter.next();
			_colorMap.remove(color);
			color.dispose();
		}
		_colorMap = null;
	}
}
