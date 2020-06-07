package com.sun.servlet;

import com.sun.dao.DisDAO;
import com.sun.dao.LikeDAO;
import com.sun.dao.SimDAO;
import com.sun.rec_engin.handle.mix_rec;
import com.sun.rec_engin.novelty;
import com.sun.rec_engin.similarity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "recommend")
public class recommend extends HttpServlet {
    LikeDAO likedao=new LikeDAO();
    similarity sim=new similarity();
    DisDAO disdao=new DisDAO();
    SimDAO simdao=new SimDAO();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id=Integer.parseInt(request.getParameter("id"));
        List<Map.Entry<Integer,Double>> infoId = new ArrayList<Map.Entry<Integer,Double>>();
//        yuanliao yl=new yuanliao();
//        mingcheng mc=new mingcheng();
//        zuofa zf=new zuofa();

//        infoId=yl.countSim(yl.getSimList(id));

        infoId= mix_rec.countSim(id);

        List<Map.Entry<Integer,Double>> infoIds = new ArrayList<Map.Entry<Integer,Double>>();
        List<Map.Entry<Integer,Double>> novel = novelty.getRes(novelty.getNovelty(id));
        /*
        给用户添加新颖度
         */
        for (int i = 0; i < 20-novel.size(); i++) {
            infoIds.add(infoId.get(i));
        }
        for (int i = 0; i < novel.size(); i++) {
            infoIds.add(novel.get(i));
        }



        request.setAttribute("list", infoIds);
        request.setAttribute("id",id);
        request.getRequestDispatcher("/Rec_list.jsp").forward(request, response);
    }
}
