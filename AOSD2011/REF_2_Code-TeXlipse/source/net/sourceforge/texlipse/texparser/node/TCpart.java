/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sourceforge.texlipse.texparser.node;

import net.sourceforge.texlipse.texparser.analysis.*;

@SuppressWarnings("nls")
public final class TCpart extends Token
{
    public TCpart()
    {
        super.setText("\\part");
    }

    public TCpart(int line, int pos)
    {
        super.setText("\\part");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TCpart(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTCpart(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TCpart text.");
    }
}
