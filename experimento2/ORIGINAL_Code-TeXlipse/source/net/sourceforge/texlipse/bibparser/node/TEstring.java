/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sourceforge.texlipse.bibparser.node;

import net.sourceforge.texlipse.bibparser.analysis.*;

@SuppressWarnings("nls")
public final class TEstring extends Token
{
    public TEstring(String text)
    {
        setText(text);
    }

    public TEstring(String text, int line, int pos)
    {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TEstring(getText(), getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTEstring(this);
    }
}
