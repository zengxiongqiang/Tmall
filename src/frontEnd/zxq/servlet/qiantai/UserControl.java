package frontEnd.zxq.servlet.qiantai;

import frontEnd.zxq.dao.TmuserDao;
import frontEnd.zxq.daoImp.TmpersonalInformationDaoImp;
import frontEnd.zxq.daoImp.TmshoppingAddressDaoImp;
import frontEnd.zxq.daoImp.TmuserDaoImp;
import frontEnd.zxq.entity.TmPersonalInformation;
import frontEnd.zxq.entity.TmShoppingAddress;
import frontEnd.zxq.entity.TmUser;
import javafx.scene.chart.PieChart;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Random;

@WebServlet("*.user")
public class UserControl extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset= utf-8");
        String uri = req.getRequestURI();
        PrintWriter printWriter = resp.getWriter();
        System.out.println("hhh");
       // System.out.println("sk"+uri);
        TmuserDao tmuserDao = new TmuserDaoImp();
        //保存发送的邮件验证码
       String  Verification = "";



        if(uri.indexOf("login")>=0){
            String uname = req.getParameter("name");
            String password = req.getParameter("password");

            TmshoppingAddressDaoImp tmshoppingAddressDaoImp  = new TmshoppingAddressDaoImp();
            TmUser tmUser  = new TmUser();
            tmUser.setName(uname);
            tmUser.setPassword(password);

            try {
                if (tmuserDao.login(tmUser)){
                    //用户信息和收货地址存入session中
                    req.getSession().setAttribute("user",tmUser);
                    List<TmShoppingAddress> list = tmshoppingAddressDaoImp.finddress(uname);
                    req.getSession().setAttribute("list",list);
                    resp.sendRedirect("registerSuccess.jsp");
                }else {
                    req.setAttribute("error_msg" ,"用户名密码错误");
                    req.getRequestDispatcher("login.jsp").forward(req,resp);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }else if(uri.indexOf("reg")>=0){
            String uname = req.getParameter("name");
            String password = req.getParameter("password");
            TmUser tmUser = new TmUser(uname,password);

            try {
                tmuserDao.reg(tmUser);
                System.out.println("注册成功");
                resp.sendRedirect("registerSuccess.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendRedirect("register3.0.jsp");
            }

        }else if (uri.indexOf("checkname")>=0){
            System.out.println("检测面试者");
            String  name = req.getParameter("name");
            try {
                if (!tmuserDao.checkUserName(name)){
                    printWriter.write("msg用户名已存在！");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }else if (uri.indexOf("out")>=0){
            req.getSession().setAttribute("user",null);
            resp.sendRedirect("frontDesk/zxq/login.jsp");

        }else if (uri.indexOf("sendemail")>=0){
            //向用户发送邮件，用以验证
            String name = req.getParameter("name");
            String choose = req.getParameter("choose");

            try {
                //拿到用户id
                TmUser tmUser = tmuserDao.findByName(name);
                int tmuid = tmUser.getTmuid();


                //拿到邮件接受人邮件向号，判断是否是使用已保存的邮箱号
                String email;
                email = req.getParameter("email");
                //如果没有拿到Email，就从个人信息表中取Email
                if(email.equals(null)){
                    TmpersonalInformationDaoImp tmpersonalInformationDaoImp = new TmpersonalInformationDaoImp();
                    TmPersonalInformation tmPersonalInformation = tmpersonalInformationDaoImp.findInfo(tmuid);
                    email = tmPersonalInformation.getEmail();
                }

                //生成将发送的邮件
                    int i = new Random().nextInt(10);
                    Main main = new Main();

                    main.setReceiveMailAccount(email);
                    main.setI(i);
                    Verification = main.getyanzhengma();
                System.out.println("发送有劲啊"+Verification);
                    try {
                        main.send();
                        req.getSession().setAttribute("Verification",Verification);
                        printWriter.write("msg发送成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            } catch (SQLException e) {
                System.out.println("msg用户相关信息获取失败！！！！！！");
                e.printStackTrace();
            }


        }else if (uri.indexOf("findpassword")>=0){
            //设置新密码
            //拿到用户id
            String name = req.getParameter("name");
           String password = req.getParameter("password");
            try {
                TmUser tmUser = tmuserDao.findByName(name);
                tmUser.setPassword(password);
                tmuserDao.updataTmuser(tmUser);
                resp.sendRedirect("frontDesk/zxq/registerSuccess.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
                printWriter.write("msg操作失败！！！！！！！");
            }

        }else if (uri.indexOf("yanzheng")>=0){
            System.out.println("进行核对验证码");
            //核对验证码是否正确
            String yanzheng = req.getParameter("yanzheng");
            System.out.println(yanzheng+"其那段");
            Verification = (String)req.getSession().getAttribute("Verification");
            System.out.println(Verification+"后天");
            if (Verification.equals(yanzheng)){
                printWriter.write("msg成功yes");
                System.out.println("yanzhengcg");
            }else {
                printWriter.write("msg失败no");
                System.out.println("yanzhaneshibai ");
            }
            System.out.println("进行核对验证码完成");
        }else if (uri.indexOf("personalInstall")>=0){
            resp.sendRedirect("frontDesk/zxq/PersonalInstall.jsp");
        }else if (uri.indexOf("updataPassword")>=0){
            String password = req.getParameter("password");
            TmUser tmUser= (TmUser)req.getSession().getAttribute("user");
            tmUser.setPassword(password);
            try {
                tmuserDao.updataTmuser(tmUser);
                req.getSession().setAttribute("user",tmUser);
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }else if (uri.indexOf("checkpassword")>=0){
            String oldpassword = req.getParameter("checkpassword");
            TmUser tmUser= (TmUser)req.getSession().getAttribute("user");
           tmUser.setPassword(oldpassword);
            try {
                tmuserDao.login(tmUser);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


    }
}
