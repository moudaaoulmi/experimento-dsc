/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sourceforge.texlipse.texparser.node;

import net.sourceforge.texlipse.texparser.analysis.*;

@SuppressWarnings("nls")
public final class TCsection extends Token
{
    public TCsection()
    {
        super.setText("\\section");
    }

    public TCsection(int line, int pos)
    {
        super.setText("\\section");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TCsection(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTCsection(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TCsection text.");
    }
}
