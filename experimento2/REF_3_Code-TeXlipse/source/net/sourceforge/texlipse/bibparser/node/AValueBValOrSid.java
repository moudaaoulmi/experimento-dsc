/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sourceforge.texlipse.bibparser.node;

import net.sourceforge.texlipse.bibparser.analysis.Analysis;

public final class AValueBValOrSid extends PValOrSid
{
    private TStringLiteral _stringLiteral_;

    public AValueBValOrSid()
    {
    }

    public AValueBValOrSid(
        TStringLiteral _stringLiteral_)
    {
        setStringLiteral(_stringLiteral_);

    }
    public Object clone()
    {
        return new AValueBValOrSid(
            (TStringLiteral) cloneNode(_stringLiteral_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAValueBValOrSid(this);
    }

    public TStringLiteral getStringLiteral()
    {
        return _stringLiteral_;
    }

    public void setStringLiteral(TStringLiteral node)
    {
        if(_stringLiteral_ != null)
        {
            _stringLiteral_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _stringLiteral_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_stringLiteral_);
    }

    void removeChild(Node child)
    {
        if(_stringLiteral_ == child)
        {
            _stringLiteral_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_stringLiteral_ == oldChild)
        {
            setStringLiteral((TStringLiteral) newChild);
            return;
        }

    }
}