/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sourceforge.texlipse.bibparser.node;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.texlipse.bibparser.analysis.Analysis;

public final class AKeyvalDecl extends PKeyvalDecl
{
    private TIdentifier _identifier_;
    private PValOrSid _valOrSid_;
    private final LinkedList _concat_ = new TypedLinkedList(new Concat_Cast());

    public AKeyvalDecl()
    {
    }

    public AKeyvalDecl(
        TIdentifier _identifier_,
        PValOrSid _valOrSid_,
        List _concat_)
    {
        setIdentifier(_identifier_);

        setValOrSid(_valOrSid_);

        {
            this._concat_.clear();
            this._concat_.addAll(_concat_);
        }

    }
    public Object clone()
    {
        return new AKeyvalDecl(
            (TIdentifier) cloneNode(_identifier_),
            (PValOrSid) cloneNode(_valOrSid_),
            cloneList(_concat_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAKeyvalDecl(this);
    }

    public TIdentifier getIdentifier()
    {
        return _identifier_;
    }

    public void setIdentifier(TIdentifier node)
    {
        if(_identifier_ != null)
        {
            _identifier_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _identifier_ = node;
    }

    public PValOrSid getValOrSid()
    {
        return _valOrSid_;
    }

    public void setValOrSid(PValOrSid node)
    {
        if(_valOrSid_ != null)
        {
            _valOrSid_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _valOrSid_ = node;
    }

    public LinkedList getConcat()
    {
        return _concat_;
    }

    public void setConcat(List list)
    {
        _concat_.clear();
        _concat_.addAll(list);
    }

    public String toString()
    {
        return ""
            + toString(_identifier_)
            + toString(_valOrSid_)
            + toString(_concat_);
    }

    void removeChild(Node child)
    {
        if(_identifier_ == child)
        {
            _identifier_ = null;
            return;
        }

        if(_valOrSid_ == child)
        {
            _valOrSid_ = null;
            return;
        }

        if(_concat_.remove(child))
        {
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

        if(_valOrSid_ == oldChild)
        {
            setValOrSid((PValOrSid) newChild);
            return;
        }

        for(ListIterator i = _concat_.listIterator(); i.hasNext();)
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

    private class Concat_Cast implements Cast
    {
        public Object cast(Object o)
        {
            PConcat node = (PConcat) o;

            if((node.parent() != null) &&
                (node.parent() != AKeyvalDecl.this))
            {
                node.parent().removeChild(node);
            }

            if((node.parent() == null) ||
                (node.parent() != AKeyvalDecl.this))
            {
                node.parent(AKeyvalDecl.this);
            }

            return node;
        }
    }
}
