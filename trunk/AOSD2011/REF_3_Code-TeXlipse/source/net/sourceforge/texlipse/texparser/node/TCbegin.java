/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sourceforge.texlipse.texparser.node;

import net.sourceforge.texlipse.texparser.analysis.Analysis;

@SuppressWarnings("nls")
public final class TCbegin extends Token
{
    public TCbegin()
    {
        super.setText("\\begin");
    }

    public TCbegin(int line, int pos)
    {
        super.setText("\\begin");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TCbegin(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTCbegin(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TCbegin text.");
    }
}
