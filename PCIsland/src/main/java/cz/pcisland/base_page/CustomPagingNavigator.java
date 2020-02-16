package cz.pcisland.base_page;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

/**
 *	Upravená třída navigace stránkováním
 */

public class CustomPagingNavigator extends PagingNavigator {

	private static final long serialVersionUID = 1L;

	public CustomPagingNavigator(final String id, final IPageable pageable) {
		this(id, pageable, null);	
	}

	public CustomPagingNavigator(final String id, final IPageable pageable, final IPagingLabelProvider labelProvider) {
		super(id,pageable,labelProvider);
	}
	
}
