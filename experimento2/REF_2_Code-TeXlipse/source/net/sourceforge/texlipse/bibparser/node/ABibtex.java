/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sourceforge.texlipse.bibparser.node;

import java.util.*;
import net.sourceforge.texlipse.bibparser.analysis.*;

public final class ABibtex extends PBibtex
{
    private final LinkedList _bibEntry_ = new TypedLinkedList(new BibEntry_Cast());

    public ABibtex()
    {
    }

    public ABibtex(
        List _bibEntry_)
    {
        {
            this._bibEntry_.clear();
            this._bibEntry_.addAll(_bibEntry_);
        }

    }
    public Object clone()
    {
        return new ABibtex(
            cloneList(_bibEntry_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseABibtex(this);
    }

    public LinkedList getBibEntry()
    {
        return _bibEntry_;
    }

    public void setBibEntry(List list)
    {
        _bibEntry_.clear();
        _bibEntry_.addAll(list);
    }

    public String toString()
    {
        return ""
            + toString(_bibEntry_);
    }

    void removeChild(Node child)
    {
        if(_bibEntry_.remove(child))
        {
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        for(ListIterator i = _bibEntry_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set(newChild);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

    }

    private class BibEntry_Cast implements Cast
    {
        public Object cast(Object o)
        {
            PBibEntry node = (PBibEntry) o;

            if((node.parent() != null) &&
                (node.parent() != ABibtex.this))
            {
                node.parent().removeChild(node);
            }

            if((node.parent() == null) ||
                (node.parent() != ABibtex.this))
            {
                node.parent(ABibtex.this);
            }

            return node;
        }
    }
}