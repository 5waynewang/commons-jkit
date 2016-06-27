package org.until.keyword;


import org.until.keyword.criterion.CharStandardization;
import org.until.keyword.decorate.TextDecorator;
import org.until.keyword.dictionary.Darts;
import org.until.keyword.dictionary.DartsDictionaryBuilder;

/**
 * @author mega
 */
public class KeywordFilter {

	private TextDecorator decorator;

	public KeywordFilter() throws Exception {
		this(DartsDictionaryBuilder.dicFile);
	}

	public KeywordFilter(String dicFile) throws Exception {
		Darts darts = DartsDictionaryBuilder.getDartsDictionary(dicFile);
		decorator = new TextDecorator(darts);
	}

	public TextDecorator getTextDecorator() {
		return decorator;
	}

	public void replaceDarts(String dicFile) throws Exception {
		Darts darts = DartsDictionaryBuilder.getDartsDictionary(dicFile);
		replaceDarts(darts);
	}

	public void replaceDarts(Darts darts) {
		decorator.setDarts(darts);
	}

	public boolean containKeyword(String content) {
		return decorator.containKeyword(content, true);
	}
	
	public String getKeyword(String content) {
		return decorator.getKeyword(content, true);
	}

	public String filterText(String content) {
		return decorator.decorateText(content, true);
	}

	public String standardiseText(String content) {
		return CharStandardization.compositeTextConvert(content, true, true, false, false, false, true, true);
	}
}
