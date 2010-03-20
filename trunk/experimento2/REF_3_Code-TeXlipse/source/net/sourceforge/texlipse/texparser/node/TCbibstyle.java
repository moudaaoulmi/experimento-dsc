/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sourceforge.texlipse.texparser.node;

import net.sourceforge.texlipse.texparser.analysis.Analysis;

@SuppressWarnings("nls")
public final class TCbibstyle extends Token
{
    public TCbibstyle()
    {
        super.setText("\\bibliographystyle");
    }

    public TCbibstyle(int line, int pos)
    {
        super.setText("\\bibliographystyle");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TCbibstyle(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTCbibstyle(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TCbibstyle text.");
    }
}
