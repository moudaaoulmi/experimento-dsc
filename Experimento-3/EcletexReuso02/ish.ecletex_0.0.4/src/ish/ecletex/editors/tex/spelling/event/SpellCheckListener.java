package ish.ecletex.editors.tex.spelling.event;

import java.util.EventListener;

/**
 * This is the event based listener interface.
 *
 * @author Jason Height (jheight@chariot.net.au)
 */
public interface SpellCheckListener extends EventListener {
  public void spellingError(SpellCheckEvent event);
}
