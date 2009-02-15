package com.flicklib.service.movie.ofdb;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.HTMLElementName;

import com.flicklib.api.Parser;
import com.flicklib.domain.MoviePage;
import com.flicklib.service.Source;

public class OfdbParser implements Parser{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OfdbParser.class);


	@Override
	public void parse(Source source, MoviePage page) {

		au.id.jericho.lib.html.Source jerichoSource = new au.id.jericho.lib.html.Source(source.getContent());
        jerichoSource.fullSequentialParse();
        
        // <span class="movieMainTitle">The Matrix</span>
        @SuppressWarnings("unchecked")
        List<Element> h2Elements = jerichoSource.findAllElements(HTMLElementName.H2);
        for(Element h2Element: h2Elements){
        	// TODO get all titles
        	String germanTitle = h2Element.getContent().getTextExtractor().toString();
        	germanTitle = OfdbTools.handleType(germanTitle, page);
        	page.setAlternateTitle(germanTitle);
        }
        
//        <tr valign="top">
//        <td nowrap><font face="Arial,Helvetica,sans-serif" size="2" class="Normal">Originaltitel:</font></td>
//        <td>&nbsp;&nbsp;</td>
//        <td width="99%"><font face="Arial,Helvetica,sans-serif" size="2" class="Daten"><b>Dune</b></font></td>
//        </tr>
        
    	@SuppressWarnings("unchecked")
        List<Element> fontElements = jerichoSource.findAllElements(HTMLElementName.FONT);
        Iterator<Element> fontIterator = fontElements.iterator();

        while(fontIterator.hasNext()){
        	Element fontElement = fontIterator.next();
        	String txt = fontElement.getContent().getTextExtractor().toString();
        	if("Originaltitel:".equals(txt)){
        		fontElement = fontIterator.next();
                String title = fontElement.getContent().getTextExtractor().toString().trim();
        		page.setTitle(title);
                page.setOriginalTitle(title);
        	}else if("Erscheinungsjahr:".equals(txt)){
        		fontElement = fontIterator.next();
        		try{
    				page.setYear(Integer.parseInt(fontElement.getContent().getTextExtractor().toString()));
    			}catch(NumberFormatException ex){
    				LOGGER.warn("Could not parse year: "+ex.getMessage());
    			}
        	}else if("Regie:".equals(txt)){
        		fontElement = fontIterator.next();
        		page.setDirector(fontElement.getContent().getTextExtractor().toString());
        	}
        }


        // score
        int noteIndex = source.getContent().indexOf("Note: ");
        int nextBr = source.getContent().indexOf("<br>", noteIndex);
        String score = source.getContent().substring(noteIndex, nextBr);
        //System.out.println(score);
        String actual = score.substring("Note: ".length(), score.indexOf("&nbsp;"));
        double dScore = Double.valueOf(actual);
        page.setScore((int)Math.round(dScore * 10));
        
        
        // Inhalt:</b> Vincent Vega und Jules Winnfield holen für ihren Boss Marsellus Wallace eine schwarze Aktentasche aus einer Wohnung ab. Drei Jungs, die ihnen dabei im... <a href="plot/1050,
        // TODO fetch the extra info page and get the text from there
        int inhaltIndex = source.getContent().indexOf("Inhalt:</b> ");
        int nextAHref = source.getContent().indexOf("<a href", inhaltIndex);
        String inhalt = source.getContent().substring(inhaltIndex + "Inhalt:</b> ".length(), nextAHref);
        page.setPlot(inhalt);
        page.setDescription(inhalt);
        // TODO get genres, length, alternative titles
	}

}
