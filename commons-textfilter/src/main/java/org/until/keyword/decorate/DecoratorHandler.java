package org.until.keyword.decorate;

/**
 * @author mega
 */
public interface DecoratorHandler {

	String decorate(String filterContent);

	int getUpperLimit();

	String getReplaceText();

}
