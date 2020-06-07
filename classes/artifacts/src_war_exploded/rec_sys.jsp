<%--@ page import="com.sun.dao.DisDAO" --%>
<%--@ page import="com.sun.vo.RecipeVo" --%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.sun.dao.DisDAO" %>
<%@ page import="com.sun.vo.RecipeVo" %>
<%--
  Created by IntelliJ IDEA.
  User: sunyang
  Date: 2016/12/8
  Time: 下午1:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>推荐系统</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/css/bootstrap.min.css">
	<script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
	<script src="https://cdn.staticfile.org/popper.js/1.15.0/umd/popper.min.js"></script>
	<script src="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
	<script src="http://libs.baidu.com/jquery/2.1.1/jquery.min.js"></script>
</head>
<body>
	<%  String id =request.getParameter("id");
		int pagenum=Integer.parseInt(request.getParameter("page"));
		System.out.println(pagenum);
	%>

	<%  DisDAO dd=new DisDAO();
		ArrayList<RecipeVo> list=dd.display(pagenum);
		int totalpage=dd.gettotalPage();
	%>
	<script type="text/javascript">
        function jumpTo(maxPage){
            like();
            var page = document.getElementById("jump").value;
            if(page > maxPage || page < 1 ) {
                alert('请输入正确的页码！');
                return;
            }else {
                window.location.href="rec_sys.jsp?page="+page+"&id="+<%=id%>;
            }

        }
        function prePage(){
            like();
            var page = <%=pagenum%>;
            if(page == 1 ) {
                alert('已经到达第一页');
                return;
            }else {
                window.location.href="rec_sys.jsp?page="+(page-1)+"&id="+<%=id%>;
            }
        }
        function nextPage(){
            like();
            var page = <%=pagenum%>;
            if(page == <%=totalpage%> ) {
                alert('已经到达最后一页');
                return;
            }else {
                window.location.href="rec_sys.jsp?page="+(page+1)+"&id="+<%=id%>;
            }
        }
        function like(){//这个方法是将DOM对象里面名字为like的都找出来.然后被选中的加入到json传递.用ajax交互
            var list =new Array();
            var check=document.getElementsByName("like");
            for(var i=0;i<check.length;i++){
                if(check[i].checked==true){//检查checkbox是否已选中
                    list.push(check[i].value);
                }
            }
            $.ajax({//通过ajax传给一个servlet处理
                type:"POST", //请求方式
                url:"/addLike", //请求路径
                data:{"list":list,
                    "id":<%=id
                %>},
                traditional: true,//加上这个就可以传数组
                dataType : 'json',
                success:function(result){
                }
            });
        }
        function reconmmend(){
            like();
            window.location.href="recommend?id="+<%=id%>;
        }
        function likeList(){
            like();
            window.location.href="likeList?id="+<%=id%>;
        }
        /*
        下面的代码是负责实现"显示部分内容,点击...显示全部"功能
        针对每个div实现
         */
        function cutStr(odiv){
            var str=odiv.innerHTML;
            var ospan=document.createElement("span");
            var olink=document.createElement("a");
            ospan.innerHTML=str.substring(0,20);
            olink.innerHTML=str.length>20?"...":"";
            olink.href="###";
            olink.onclick=function(){
                if(olink.innerHTML=="..."){
                    olink.innerHTML="收起";
                    ospan.innerHTML=str;
                }
                else{
                    olink.innerHTML="...";
                    ospan.innerHTML=str.substring(0,20);
                }
            }
            odiv.innerHTML="";
            odiv.appendChild(ospan);
            odiv.appendChild(olink);
        };
        /*
        加载上面的函数,指定哪些div需要实现这一功能
         */
        function loadCut(divname){
            var div=document.getElementsByName(divname);
            for(var i=0;i<div.length;i++){
                cutStr(div[i]);
            }
        }
        /*
        页面直接调用这个函数实现"部分显示"功能
         */
        window.onload=function(){
            loadCut("thediv0");
            loadCut("thediv1");
            loadCut("thediv2");
            loadCut("thediv3");
            loadCut("thediv4");
        }
	</script>
	<div class="jumbotron text-center" style="margin-bottom:0">
	  <h1>美食菜谱推荐系统</h1>
	  <p>Welcome to the food Recommender System！</p>
	</div>
	<table id = "main" class = "container">
		<table class = "table">
			<thead class="thead-dark">
			<tr>
				<th>感兴趣</th>
				<th>菜谱ID</th>
				<th>菜谱名称</th>
				<th width="400">做法</th>
				<th>特性</th>
				<th>提示</th>
				<th>调料</th>
				<th>原料</th>
			</tr>
			</thead>
			<tbody>
			<%for(int i = 0 ; i<list.size();i++) {
				RecipeVo re = list.get(i);%>
			<tr align="center" >
				<td><input type = "checkbox"  value ='<%=re.getId() %>' name="like" /></td>
				<td><%=re.getId() %></td>
				<td><%=re.getName() %></td>
				<td width="400"><div name="thediv0"><%=re.getZuofa() %></div></td>
				<td><div name="thediv1"><%=re.getTexing() %></div></td>
				<td><div name="thediv2"><%=re.getTishi() %></div></td>
				<td><div name="thediv3"><%=re.getTiaoliao() %></div></td>
				<td><div name="thediv4"><%=re.getYuanliao() %></div></td>
			</tr>
			<%
				}
			%>
			</tbody>
		</table>
	</table>

	<div id="pageControl" align="center">
	<!-- 上一页 按钮 -->
	<input type="button" value="上一页" class="btn btn-info" id="prePage" onclick="prePage()" >

	<!-- 下一页 按钮 -->
	<input type="button" value="下一页"  class="btn btn-info" id="nextPage" onclick="nextPage()" >

	<!-- 直接跳转 -->
	共<%=totalpage%>页 当前<%=pagenum%>页 -向<input type="text" id="jump" />页 <input type="button" value="跳转" onclick="jumpTo(<%=totalpage%>)" />
</div>
	<div>
		<table align="center">
			<tr>
				<td><input type ="button" value ="推荐结果" class="btn btn-info" onclick="reconmmend()"/></td>
				<td><input type ="button" value ="收藏列表" class="btn btn-info" onclick="likeList()"/></td>
			</tr>
		</table>
	</div>
</body>
</html>
