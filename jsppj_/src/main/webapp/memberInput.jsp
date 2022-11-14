<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입페이지</title>

<script type="text/javascript">
function check(){	
//	var memberId = document.frm.memberId.value;
//	alert(memberId);	
	var fm = document.frm;	
	if (fm.memberId.value == "" ){
		alert("아이디를 입력하세요");
		fm.memberId.focus();
		return;
	}else if (fm.memberPwd.value == ""){
		alert("비밀번호를 입력하세요");
		fm.memberPwd.focus();
		return;
	}else if (fm.memberPwd2.value == ""){
		alert("비밀번호 확인를 입력하세요");
		fm.memberPwd2.focus();
		return;
	}else if (fm.memberPwd.value != fm.memberPwd2.value){
		alert("비밀번호가 일치하지 않습니다.");
		fm.memberPwd2.value = "";
		fm.memberPwd2.focus();
		return;
	}else if (fm.memberName.value == ""){
		alert("이름을 입력하세요");
		fm.memberName.focus();
		return;
	}else if (fm.memberBirth.value == ""){
		alert("생년월일을 입력하세요");
		fm.memberBirth.focus();
		return;
	}else if (fm.memberGender.value == ""){
		alert("성별을 입력하세요");
		fm.memberGender.focus();
		return;
	}else if (fm.memberArea.value == ""){
		alert("지역을 입력하세요");
		fm.memberArea.focus();
		return;
	}
	
	fm.action = "<%=request.getContextPath()%>/member/memberInputAction.do";
	fm.method="post";
	fm.submit();
	return;
}


</script>
</head>
<body>
♠회원가입
<form name="frm">
<table style="width:800px;" border=1>
<tr>
<td>아이디</td>
<td><input type="text"  name="memberId" ></td>
</tr>
<tr>
<td>비밀번호</td>
<td><input type="password" name="memberPwd" ></td>
</tr>
<tr>
<td>비밀번호 확인</td>
<td><input type="password" name="memberPwd2" ></td>
</tr>
<tr>
<td>이름</td>
<td><input type="text" name="memberName" ></td>
</tr>
<tr>
<td>생년월일</td>
<td><input type="text" name="memberBirth" ></td>
</tr>
<tr>
<td>성별</td>
<td>
	<input type="radio" name="memberGender"  value="0">남성
	<input type="radio" name="memberGender" value="1">여성
</td>
</tr>
<tr>
<td>지역</td>
<td>
	<select name="memberArea">
		<option value="">선택</option>
		<option value="서울">서울</option>
		<option value="전주">전주</option>
	</select>
</td>
</tr>
<tr>
<td colspan=2>
 <input type="button" name="btn" value="확인" onclick="check();">
 <input type="reset" name="reset" value="취소">
  </td>
  </tr>
</table>
</form>
</body>
</html>