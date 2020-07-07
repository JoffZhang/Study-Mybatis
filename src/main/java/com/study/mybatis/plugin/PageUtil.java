package com.study.mybatis.plugin;

/**
 * @author zy
 * @date 2020/7/7 16:03
 */
public class PageUtil {

	private static final ThreadLocal<Page> LOCAL_PAGE = new ThreadLocal<Page>();

	public static void page(int offset,int limit){
		Page page = new Page(offset,limit);
		LOCAL_PAGE.set(page);
	}

	public static void removePageParam(){
		LOCAL_PAGE.remove();
	}

	public static Page getPageParam(){
		return LOCAL_PAGE.get();
	}
	public static class Page{
		private int offset;
		private int limit;

		public Page(int offset, int limit) {
			this.offset = offset;
			this.limit = limit;
		}

		public int getOffset() {
			return offset;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		public int getLimit() {
			return limit;
		}

		public void setLimit(int limit) {
			this.limit = limit;
		}
	}
}
