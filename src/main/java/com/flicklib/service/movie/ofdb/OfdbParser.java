package com.flicklib.service.movie.ofdb;

import java.util.List;

import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.HTMLElementName;

import com.flicklib.api.Parser;
import com.flicklib.domain.MoviePage;
import com.flicklib.service.Source;

public class OfdbParser implements Parser{

	@Override
	public void parse(Source source, MoviePage page) {

		au.id.jericho.lib.html.Source jerichoSource = new au.id.jericho.lib.html.Source(source.getContent());
        jerichoSource.fullSequentialParse();
        
        // <span class="movieMainTitle">The Matrix</span>
        @SuppressWarnings("unchecked")
        List<Element> h2Elements = jerichoSource.findAllElements(HTMLElementName.H2);
        for(Element h2Element: h2Elements){
        	page.setTitle(h2Element.getContent().getTextExtractor().toString());
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
        page.setDescription(inhalt);
        // TODO get genre, runtime, director, length, alternative titles
	}

}
