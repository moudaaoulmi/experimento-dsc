/*
 * $Id: ReferenceEntry.java,v 1.5 2006/10/27 17:08:48 borisvl Exp $
 *
 * Copyright (c) 2004-2005 by the TeXlapse Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package net.sourceforge.texlipse.model;

import java.io.File;


/**
 * A class for containing LaTeX references (\label and BibTeX)
 * 
 * @author Oskar Ojala
 */
public final class ReferenceEntry extends AbstractEntry {

    /**
     * A descriptive text of the reference
     */
    public String info;
    /**
     * The end line of the reference declaration (used for BibTeX editing)
     */
    public int endLine;
    // FIXME
    public String author;
    public String journal;
    public String year;
    
    /**
     * The document of the reference declaration (used for BibTeX viewing)
     */
    public File refFile;
    
    /**
     * Constructs a new entry with the given key (reference key/name)
     * 
     * @param key Reference key
     */
    public ReferenceEntry(String key) {
        this.key = key;
    }
    
    /**
     * Constructs a new entry with the given key (Reference key/name)
     * and a descriptive text telling something about the reference
     * (used for BibTeX).
     * 
     * @param key Reference key
     * @param info A descriptive text of the reference
     */
    public ReferenceEntry(String key, String info) {
        this.key = key;
        this.info = info;
    }
        
    /**
     * Returns a shallow copy of this reference
     * 
     * @return A copy of this reference
     */
    public AbstractEntry copy() {
        ReferenceEntry re = new ReferenceEntry(key, info);
        re.startLine = startLine;
        re.endLine = endLine;
        re.author = author;
        re.journal = journal;
        re.year = year;
        re.refFile = refFile;
        re.fileName = fileName;
        re.position = position;
        return re;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return key;
    }
}
