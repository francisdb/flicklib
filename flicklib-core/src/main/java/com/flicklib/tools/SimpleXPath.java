package com.flicklib.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.Element;

public class SimpleXPath {
    List<Element> root;

    public SimpleXPath() {
        this.root = new ArrayList();
    }

    public SimpleXPath(List<Element> root) {
        this.root = root;
    }

    public SimpleXPath(Element element) {
        this();
        this.root.add(element);
    }

    public void add(Element el) {
        this.root.add(el);
    }

    public void addAll(Collection<Element> el) {
        this.root.addAll(el);
    }

    public SimpleXPath getTags(String tagName) {
        SimpleXPath xp = new SimpleXPath();
        for (Element e : root) {
            xp.addAll(e.getAllElements(tagName));
        }
        return xp;
    }

    public SimpleXPath getAllTagByAttributes(String attribName, String attribValue) {
        SimpleXPath xp = new SimpleXPath();
        for (Element e : root) {
            xp.addAll(e.getAllElements(attribName, attribValue, true));
        }
        return xp;
    }
    
    public SimpleXPath filter(String attribName, String attribValue) {
        SimpleXPath xp = new SimpleXPath();
        if (attribValue != null) {
            for (Element e : root) {
                if (attribValue.equals(e.getAttributeValue(attribName))) {
                    xp.add(e);
                }
            }
        } else {
            for (Element e : root) {
                if (e.getAttributeValue(attribName)==null) {
                    xp.add(e);
                }
            }
        }
        return xp;
    }
    
    public SimpleXPath filterTagName(String tagName) {
        SimpleXPath xp = new SimpleXPath();
        for (Element e : root) {
            if (tagName.equals(e.getName())) {
                xp.add(e);
            }
        }
        return xp;
    }
    
    public SimpleXPath parentTagName(String tagName) {
        SimpleXPath xp = new SimpleXPath();
        for (Element e : root) {
            if (tagName.equals(e.getParentElement().getName())) {
                xp.add(e);
            }
        }
        return xp;
    }
    
    public SimpleXPath children() {
        SimpleXPath xp = new SimpleXPath();
        for (Element e : root) {
            xp.addAll(e.getChildElements());
        }
        return xp;
    }

    public SimpleXPath parent() {
        SimpleXPath xp = new SimpleXPath();
        for (Element e : root) {
            xp.add(e.getParentElement());
        }
        return xp;
    }

    public SimpleXPath first() {
        if (root.size() > 1) {
            return new SimpleXPath(root.get(0));
        }
        return this;

    }

    public boolean isEmpty() {
        return root.isEmpty();
    }

    /**
     * @return
     * @see java.util.List#size()
     */
    public int size() {
        return root.size();
    }

    /**
     * @return
     * @see java.util.List#iterator()
     */
    public Iterator<Element> iterator() {
        return root.iterator();
    }

    public List<Element> toList() {
        return root;
    }
    
    /**
     * 
     * @see java.util.List#clear()
     */
    public void clear() {
        root.clear();
    }

    /**
     * @param index
     * @return
     * @see java.util.List#get(int)
     */
    public Element get(int index) {
        return root.get(index);
    }

    @Override
    public String toString() {

        return "XPath[" + root.toString() + "]";
    }

}
