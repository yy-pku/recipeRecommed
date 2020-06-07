<%@ page import="com.sun.vo.RecipeVo" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.sun.dao.DisDAO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: sunyang
  Date: 2016/12/8
  Time: 下午5:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%  List<Map.Entry<Integer,Double>> top =(List<Map.Entry<Integer,Double>>)(request.getAttribute("list"));
    String id=request.getAttribute("id").toString();
    int topNum=20;//指定显示前多少条
    int[] list=new int[topNum];
    double[] list1=new double[topNum];
    for (int j = 0; j < topNum; j++) {
        list[j]=top.get(j).getKey();
        list1[j]=top.get(j).getValue();
    }
    DisDAO disdao=new DisDAO();
    ArrayList<RecipeVo> l=new ArrayList<RecipeVo>();
    l=disdao.display(list);
%>
<head>
    <title>推荐结果</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/css/bootstrap.min.css">
	<script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
	<script src="https://cdn.staticfile.org/popper.js/1.15.0/umd/popper.min.js"></script>
	<script src="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
</head>
<script type="text/javascript">
    function back(){
        window.location.href="rec_sys.jsp?page=1&id="+<%=id%>;
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

<body>
	<div class="jumbotron text-center" style="margin-bottom:0">
	  <h1>美食菜谱推荐系统</h1>
	  <p>Welcome to the food Recommender System！</p>
	</div>
	<table id = "main" class = "container">
		<table class = "table">
				<thead class="thead-dark">
					<tr>
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
			<%for(int i = 0 ; i<l.size();i++) {
				RecipeVo re = l.get(i);%>
			<tr align="center" >
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
	<div align="center">
		<input type="button" class="btn btn-info" value="返回" id="return" onclick="back()" >
	</div>
</body>
</html>
