/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sourceforge.texlipse.texparser.node;

import net.sourceforge.texlipse.texparser.analysis.Analysis;

@SuppressWarnings("nls")
public final class TCsssection extends Token
{
    public TCsssection()
    {
        super.setText("\\subsubsection");
    }

    public TCsssection(int line, int pos)
    {
        super.setText("\\subsubsection");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TCsssection(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTCsssection(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TCsssection text.");
    }
}
