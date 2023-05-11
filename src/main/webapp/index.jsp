<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<jsp:include page = "view/header.jsp" />

<%  %>

<c:set var="pageN" value="${param.page}"/>

<c:choose>
    <c:when test="${pageN == 1}">
        <jsp:include page = "view/users/register.jsp" />
    </c:when>
    
    <c:when test="${pageN == 2}">
        <jsp:include page = "view/users/register.jsp" />
    </c:when>
    
    <c:when test="${pageN == 3}">
        <jsp:include page = "view/instanciar/terminal.jsp" />
    </c:when>

    <c:when test="${pageN == 4}">
        <jsp:include page = "view/project.html" />
    </c:when>
    
    <c:when test="${pageN == 6}">
        <jsp:include page = "view/users/login.jsp" />
    </c:when>
    
    <c:when test="${pageN == 5}">
        <jsp:include page = "view/instanciar/instanciar.jsp" />
    </c:when>

    <c:otherwise>
        <jsp:include page = "view/main.jsp" />
    </c:otherwise>
</c:choose> 

<jsp:include page = "view/footer.jsp" />