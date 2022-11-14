package domain;

public class Criteria {

	private int page;    //페이지 
	private int perPageNum;   //화면에 출력되는 리스트 수
	
	public Criteria() {
		this.page = 1;
		this.perPageNum = 15;
	}	
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPerPageNum() {
		return perPageNum;
	}
	public void setPerPageNum(int perPageNum) {
		this.perPageNum = perPageNum;
	}
	
	
	
	
	
}
