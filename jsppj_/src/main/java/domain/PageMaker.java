package domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PageMaker {
	
	private int displayPageNum =10;   //   < 1 2 3 4 5 6 7 8 9 10 >
	private int startPage;    //페이징에 시작점
	private int endPage;    //페이징의 현재의 끝점
	private int totalCount;   //전체 게시물 수
	
	private boolean prev;   //이전버튼
    private boolean next;   //다음버튼
    
    private SearchCriteria scri;       // 페이지 객체

	public int getDisplayPageNum() {
		return displayPageNum;
	}

	public void setDisplayPageNum(int displayPageNum) {
		this.displayPageNum = displayPageNum;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;			
		calcData();		//계산식
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public SearchCriteria getSearchCri() {
		return scri;
	}

	public void setSearchCri(SearchCriteria scri) {
		this.scri = scri;
	}
    
    public void calcData() {
    	//시작페이지 번호
    	//끝페이지 번호
    	//prev next을 보여준다
    	
    	//끝페이지 번호
    	endPage = (int)(Math.ceil(scri.getPage()/(double)displayPageNum)* displayPageNum) ;
    	//시작페이지 번호
    	startPage = (endPage - displayPageNum)+1;
    	
    	//전체 페이지 수
    	int tempEndPage = (int)(Math.ceil(totalCount/(double)scri.getPerPageNum()));
    	
    	if (endPage > tempEndPage) {
    		endPage = tempEndPage;
    	}
    	//이전버튼
    	prev = (startPage ==1 ? false:true);
    	next = (endPage*scri.getPerPageNum()  >= totalCount ? false:true);
    	    	
    }
    
    //한글키워드 인코딩을 위한 메소드
    public String encoding(String keyword) {
    	String str = "";    	
    	try {
			str = URLEncoder.encode(keyword, "UTF-8");
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}   	    	
    	return str;
    }
    

}
