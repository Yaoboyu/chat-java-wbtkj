package com.wbtkj.chat;

import com.wbtkj.chat.utils.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.mail.HtmlEmail;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
class WbtApplicationTests {
    public static int SendMail(String EmailAdd,String ToAdd,String Au) throws Exception{
        EmailAdd = ToAdd = "1712683212@qq.com";
        Au = "ezjcvxhvxxvmfajg";
        Random r = new Random(System.currentTimeMillis());
        int code = Math.abs(r.nextInt() % 10000);
        HtmlEmail email = new HtmlEmail();
        email.setHostName("smtp.qq.com");
        email.setCharset("utf-8");
        email.addTo(ToAdd);
        email.setFrom(EmailAdd);
        email.setAuthentication(EmailAdd,Au);
        email.setSubject("乌邦图科技:您好,请查收您的注册验证码");//设置发送主题
        email.setMsg("您的验证码是" + code + "请注意查收.");//设置发送内容
        email.send();//进行发送
        return code;
    }
    public static void main(String[] args) throws Exception{
        /*//System.out.println(WbtApplicationTests.SendMail("","",""));
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));*/
        Map<String,Object> claims = new HashMap<String,Object>();
        claims.put("email","17126832121@qq.com");
        claims.put("pwd","44652096433b8268");
        String jwt = generateJwt(claims);
        System.out.println(jwt);
    }

    public static String generateJwt(Map<String, Object> claims) throws Exception{
        claims = new HashMap<String,Object>();
        claims.put("email","17126832121@qq.com");
        claims.put("pwd","44652096433b8268");
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, JwtUtils.SIGN_KEY)
                .setExpiration(new Date(System.currentTimeMillis() / 1000 + JwtUtils.EXPIRE))
                .compact();
        return jwt;
    }

}
