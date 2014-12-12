<%@ include file="taglib.jsp"%>
<div class="header">
<div class="innertop">&nbsp;</div>
<div class="innertop1"><h1>Talent Manager</h1>
	<div class="logout-section">
		 <div>
		 	<ul class="drop">
				  <li><img src="view/images/profile-pic.png" alt="" border="0" width="40"  height="43" />
				    <ul>
				      <li><a href="changePassword.htm" style="display: block;color: #fff;text-decoration: none;">Change Password</a></li>
				      <li><a href="<c:url value="j_spring_security_logout"/>" style="display: block;color: #fff;text-decoration: none;">Logout</a></li>
				    </ul>
				  </li>
				</ul>
		 </div>
	</div>
	<span class="welcome">Welcome <security:authentication property="principal.userName"/></span>
</div>
</div>