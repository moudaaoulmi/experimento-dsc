/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sourceforge.texlipse.bibparser.node;

import net.sourceforge.texlipse.bibparser.analysis.*;

public final class TEntryName extends Token
{
    public TEntryName(String text)
    {
        setText(text);
    }

    public TEntryName(String text, int line, int pos)
    {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TEntryName(getText(), getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTEntryName(this);
    }
}