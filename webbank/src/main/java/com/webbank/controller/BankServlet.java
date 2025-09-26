package com.webbank.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.webbank.model.Account;
import com.webbank.model.User;
import com.webbank.service.BankService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/webbank/*")
public class BankServlet extends HttpServlet {
    private BankService bankService=new BankService();
    private ObjectMapper objectMapper=new ObjectMapper();
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        String pathInfo=request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> result=new HashMap<>();

        try{
            if("/register".equals(pathInfo)){
                handleRegister(request,response,result);
            }else if("/login".equals(pathInfo)){
                handleLogin(request,response,result);
            }else if("/changePassword".equals(pathInfo)){
                handleChangePassword(request,response,result);
            }else if("/createAccount".equals(pathInfo)){
                handleCreateAccount(request,response,result);
            }else if("/deposit".equals(pathInfo)){
                handleDeposit(request,response,result);
            }else if("/withdraw".equals(pathInfo)){
                handleWithdraw(request,response,result);
            }else {
                result.put("success", false);
                result.put("message","无效的请求路径");
            }
        }catch(Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            e.printStackTrace();
        }
        objectMapper.writeValue(response.getWriter(),result);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        String pathInfo=request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> result=new HashMap<>();

        try{
            if("/getAccount".equals(pathInfo)){
                handleGetAccount(request,response,result);
            }else if("/getAllAccounts".equals(pathInfo)){
                handleGetAllAccounts(request,response,result);
            }else if("/logout".equals(pathInfo)){
                handleLogout(request,response,result);
            }else if("/checkLogin".equals(pathInfo)){
                handleCheckLogin(request,response,result);
            }else {
                result.put("success",false);
                result.put("message","无效的请求路径");
            }
        }catch(Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            e.printStackTrace();
        }
        objectMapper.writeValue(response.getWriter(),result);
    }

    public void handleRegister(HttpServletRequest request,HttpServletResponse response,Map<String,Object> result){
        String userNumber=request.getParameter("userNumber");
        String userPassword=request.getParameter("userPassword");
        if(userNumber==null||userPassword==null||userNumber.isEmpty()||userPassword.isEmpty()){
            result.put("success",false);
            result.put("message","用户名或密码不能为空");
            return ;
        }
        boolean success=bankService.registerUser(userNumber,userPassword);
        result.put("success",success);
        result.put("message",success?"注册成功":"注册失败");
    }

    public void handleLogin(HttpServletRequest request,HttpServletResponse response,Map<String,Object>result){
        String userNumber=request.getParameter("userNumber");
        String userPassword=request.getParameter("userPassword");
        if(userNumber==null||userPassword==null||userNumber.isEmpty()||userPassword.isEmpty()){
            result.put("success",false);
            result.put("message","用户名或密码不能为空");
            return ;
        }
        boolean success=bankService.loginUser(userNumber,userPassword);
        if(success){
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedIn", true);
            session.setAttribute("userNumber", userNumber);
        }
        result.put("success",success);
        result.put("message",success?"登录成功":"登录失败");
    }

    public void handleChangePassword(HttpServletRequest request,HttpServletResponse response,Map<String,Object>result){
        HttpSession session = request.getSession(false);
        if(session==null){
            result.put("success",false);
            result.put("message","请先登录");
            return ;
        }
        String userNumber=(String)session.getAttribute("userNumber");
        String oldUserPassword=request.getParameter("oldUserPassword");
        String newUserPassword=request.getParameter("newUserPassword");
        if(userNumber==null||oldUserPassword==null||newUserPassword==null||userNumber.isEmpty()||oldUserPassword.isEmpty()||newUserPassword.isEmpty()){
            result.put("success",false);
            result.put("message","用户名或密码不能为空");
            return ;
        }
        boolean success=bankService.changeUserPassword(userNumber,oldUserPassword,newUserPassword);
        result.put("success",success);
        result.put("message",success?"修改密码成功":"修改密码失败");
    }

    public void handleCreateAccount(HttpServletRequest request,HttpServletResponse response,Map<String,Object>result){
        String accountNumber=request.getParameter("accountNumber");
        HttpSession session = request.getSession(false);

        if(accountNumber==null||accountNumber.isEmpty()){
            result.put("success",false);
            result.put("message","账户号不能为空");
            return ;
        }
        String userNumber=(String)session.getAttribute("userNumber");
        User user=bankService.getUser(userNumber);
        boolean success=bankService.createAccount(user,accountNumber,0);
        result.put("success",success);
        result.put("message",success?"创建账户成功":"创建账户失败");
    }

    public void handleDeposit(HttpServletRequest request,HttpServletResponse response,Map<String,Object>result){
        HttpSession session = request.getSession(false);
        if(session==null||session.getAttribute("loggedIn").equals(false)){
            result.put("success",false);
            result.put("message","请先登录");
            return ;
        }
        String accountNumber=request.getParameter("accountNumber");
        Account account=bankService.getAccount(accountNumber);
        String amount=request.getParameter("amount");
        if(accountNumber==null||amount==null||accountNumber.isEmpty()||amount.isEmpty()){
            result.put("success",false);
            result.put("message","账户号或金额不能为空");
            result.put("message","账户号或金额不能为空");
            return ;
        }
        boolean success=bankService.deposit(account,Double.parseDouble(amount));
        result.put("success",success);
        result.put("message",success?"存款成功":"存款失败");
        if(success){result.put("newAccountBalance",account.getAccountBalance());}
    }

    public void handleWithdraw(HttpServletRequest request,HttpServletResponse response,Map<String,Object>result){
        HttpSession session = request.getSession(false);
        if(session==null||session.getAttribute("loggedIn").equals(false)){
            result.put("success",false);
            result.put("message","请先登录");
            return ;
        }
        String accountNumber=request.getParameter("accountNumber");
        Account account=bankService.getAccount(accountNumber);
        String amount=request.getParameter("amount");
        if(accountNumber==null||amount==null||accountNumber.isEmpty()||amount.isEmpty()){
            result.put("success",false);
            result.put("message","账户号或金额不能为空");
            return ;
        }
        boolean success=bankService.withdraw(account,Double.parseDouble(amount));
        result.put("success",success);
        result.put("message",success?"取款成功":"取款失败");
        if(success){result.put("newAccountBalance",account.getAccountBalance());}
    }

    public void handleGetAccount(HttpServletRequest request,HttpServletResponse response,Map<String,Object>result){

        String accountNumber=request.getParameter("accountNumber");
        Account account=bankService.getAccount(accountNumber);
        boolean success=account!=null;
        result.put("success",success);
        result.put("message",account);
    }

    public void handleGetAllAccounts(HttpServletRequest request,HttpServletResponse response,Map<String,Object>result){
        HttpSession session = request.getSession(false);
        if(session==null||session.getAttribute("loggedIn").equals(false)){
            result.put("success",false);
            result.put("message","请先登录");
            return ;
        }
        String userNumber=(String)session.getAttribute("userNumber");
        List<Account> allAccounts=bankService.getAllAccounts(userNumber);
        result.put("success",true);
        result.put("allAccounts",allAccounts);
    }

    public void handleLogout(HttpServletRequest request,HttpServletResponse response,Map<String,Object>result){
        HttpSession session=request.getSession(false);
        if(session!=null){
            session.invalidate();
        }
        result.put("success",true);
        result.put("message","注销成功");
    }

    public void handleCheckLogin(HttpServletRequest request,HttpServletResponse response,Map<String,Object>result){
        HttpSession session=request.getSession(false);
        if(session==null||session.getAttribute("loggedIn")==null){
            result.put("loggedIn",false);
            result.put("message","未登录");
            return ;
        }
        result.put("loggedIn",true);
        result.put("message","已登录");
        result.put("userNumber",session.getAttribute("userNumber"));
    }



}