/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sourceforge.texlipse.bibparser.node;

import java.util.*;
import net.sourceforge.texlipse.bibparser.analysis.*;

public final class ABibeBibEntry extends PBibEntry
{
    private PEntry _entry_;

    public ABibeBibEntry()
    {
    }

    public ABibeBibEntry(
        PEntry _entry_)
    {
        setEntry(_entry_);

    }
    public Object clone()
    {
        return new ABibeBibEntry(
            (PEntry) cloneNode(_entry_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseABibeBibEntry(this);
    }

    public PEntry getEntry()
    {
        return _entry_;
    }

    public void setEntry(PEntry node)
    {
        if(_entry_ != null)
        {
            _entry_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _entry_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_entry_);
    }

    void removeChild(Node child)
    {
        if(_entry_ == child)
        {
            _entry_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_entry_ == oldChild)
        {
            setEntry((PEntry) newChild);
            return;
        }

    }
}
