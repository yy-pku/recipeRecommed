<%@ page import="com.sun.vo.RecipeVo" %>
<%@ page import="com.sun.dao.DisDAO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>收藏列表</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/css/bootstrap.min.css">
	<script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
	<script src="https://cdn.staticfile.org/popper.js/1.15.0/umd/popper.min.js"></script>
	<script src="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
	<script src="http://libs.baidu.com/jquery/2.1.1/jquery.min.js"></script>
</head>

<%  ArrayList<Integer> list=(ArrayList<Integer>)request.getAttribute("list");
    String id=request.getAttribute("id").toString();

    DisDAO disdao=new DisDAO();
    ArrayList<RecipeVo> l=new ArrayList<RecipeVo>();
    l=disdao.display(list);
%>

<script>
    function del(){
        var list =new Array();
        var check=document.getElementsByName("like");
        for(var i=0;i<check.length;i++){
            if(check[i].checked==true){//检查checkbox是否已选中
                list.push(check[i].value);
            }
        }
        $.ajax({//通过ajax传给一个servlet处理
            type:"POST", //请求方式
            url:"/likeList", //请求路径
            data:{"list":list,
                "id":<%=id
                %>},
            traditional: true,//加上这个就可以传数组
            dataType : 'json',
            success:function(result){
            }
        });
        window.location.href="likeList?id="+<%=id%>;
    }
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
				<%for(int i = 0 ; i<l.size();i++) {
				RecipeVo re = l.get(i);%>
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

	<div>
		<table align="center">
		<tr>
			<td><input type ="button" class="btn btn-info" value ="删除" onclick="del()"/></td>
			<td><input type ="button" class="btn btn-info" value ="返回" onclick="back()"/></td>
		</tr>
		</table>
	</div>
</body>
</html>
