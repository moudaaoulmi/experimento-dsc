package ish.ecletex.editors.tex.spelling.util;

import java.util.Iterator;
import java.util.List;

public class VectorUtility {
  public static List addAll(List dest, List src) {
    return addAll(dest, src, true);
  }

  public static List addAll(List dest, List src, boolean allow_duplicates) {
    for (Iterator e = src.iterator(); e.hasNext();) {
      Object o = e.next();
      if (allow_duplicates || !dest.contains(o))
        dest.add(o);
    }
    return dest;
  }
}
