<%@ include file="taglib.jsp"%>
<div class="header">
<div class="innertop">&nbsp;</div>
<div class="innertop1"><h1>Talent Manager</h1>
<span class="welcome">Welcome <security:authentication property="principal.userName"/>! <a href="<c:url value="j_spring_security_logout"/>">Logout</a></span>
</div>
</div>