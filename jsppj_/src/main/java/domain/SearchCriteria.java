package domain;

public class SearchCriteria extends Criteria{

	//상속받은 변수에 2개의 멤버변수를 추가한다
	private String searchType;
	private String keyword; 
	
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}	
}
