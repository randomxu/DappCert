package com.sdt.dapp.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.sdt.dapp.controller.BaseController;
import com.sdt.dapp.entity.system.RootcaMng;
import com.sdt.dapp.entity.system.YHBank;
import com.sdt.dapp.entity.system.YHUser;
import com.sdt.dapp.repository.YHBankRepository;
import com.sdt.dapp.repository.YHUserRepository;
import com.sdt.dapp.service.system.YHBankService;
import com.sdt.dapp.service.system.YHUserService;
import com.sdt.dapp.utils.PageRequestUtil;
import com.sdt.dapp.utils.PagingResult;
import com.sdt.dapp.utils.ResponseResult;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/YHUser")
public class YHUserController extends BaseController {
    @Autowired
    private PageRequestUtil pageRequestUtil;

    @Autowired
    private YHUserService service;

    @Autowired
    private YHUserRepository repository;

    @ResponseBody
    @GetMapping(value = "/findAll")
    public PagingResult<YHUser> findAll(@RequestParam Map<String, Object> params) {
        Map map = getMap(params);
        Map advQuery = (Map) map.get("advQuery");
        advQuery.put("id", "");
        PageRequest pageRequest = pageRequestUtil.genericPageRequestByRequest(map);
        return service.findYHUser(pageRequest, advQuery, "");
    }

    @ResponseBody
    @PostMapping(value = "/addUser")
    public ResponseResult addUser(@RequestBody YHUser bankUser) {
        System.out.printf("YHUser in controller :",bankUser);
        return service.addUser(bankUser);
    }

    @ResponseBody
    @PostMapping(value = "/delete")
    public ResponseResult delete(String isStr) {
        System.out.printf("delete :", isStr);
        return service.deleteUser(isStr);
    }

    @ResponseBody
    @RequestMapping(value = "/importReq")
    public ModelMap importReq(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        ModelMap map = new ModelMap();
        if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(RootcaMng.REQPATH, file.getOriginalFilename())));
                out.write(file.getBytes());
                out.flush();
                out.close();
                map.put("code", 0);
                return map;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                map.put("code", -3);
                return map;
            } catch (IOException e) {
                e.printStackTrace();
                map.put("code", -4);
                return map;
            }
        } else {
            map.put("code", -1);
            return map;
        }
    }

    @ResponseBody
    @GetMapping(value = "/genCert/{userName}")
    public ModelMap genCert(@RequestParam Map<String, Object> params,@PathVariable("userName") String userName,HttpServletRequest request) throws SQLException {
        System.out.println(userName);
        ModelMap map = new ModelMap();
        try{
            String bankid = (String) request.getSession().getAttribute("username");
            File file = new File(RootcaMng.REQPATH + userName + "_sig.req");
            if (!file.exists()) {
                map.put("code", -1);
                map.put("count", 0);
                map.put("msg", "证书请求文件不存在");
                map.put("data", null);
                return map;
            } else {
                Runtime run = Runtime.getRuntime();

                BufferedReader br = new BufferedReader(new InputStreamReader(run.exec(RootcaMng.CMD_START + RootcaMng.GEN_PEM + userName + "_sig.req -out " + userName + "_sig.pem -noemailDN -batch & "
                        + RootcaMng.GEN_ENCPEM + userName + "_enc.req -out " + userName + "_enc.pem -noemailDN -batch & copy " + userName + "_sig.pem " + RootcaMng.STORE_PATH + " & copy "+ userName + "_enc.pem " + RootcaMng.STORE_PATH
                        ).getInputStream()));
                String line = null;
                StringBuffer b = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    b.append(line + "\n");
                }

                YHUser bankinfo = repository.findByUserName(userName);

                Security.addProvider(new BouncyCastleProvider());
                CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
                FileInputStream in = new FileInputStream(RootcaMng.STORE_PATH + userName + "_sig.pem");
                X509Certificate Cert = (X509Certificate) cf.generateCertificate(in);
                FileInputStream enc = new FileInputStream(RootcaMng.STORE_PATH + userName + "_enc.pem");
                X509Certificate encCert = (X509Certificate) cf.generateCertificate(enc);

                bankinfo.setUserSigCert(new String(Base64.encode(Cert.getEncoded())));
                bankinfo.setUserSigPk(Cert.getPublicKey().toString());
                bankinfo.setUserEncCert(new String(Base64.encode(encCert.getEncoded())));
                bankinfo.setUserEncPk(encCert.getPublicKey().toString());
                bankinfo.setUserRegDate(new Timestamp(Cert.getNotBefore().getTime()));
                bankinfo.setBankEndDate(new Timestamp(Cert.getNotAfter().getTime()));
                bankinfo.setUserBankID(bankid);
                repository.save(bankinfo);
                map.put("code", 0);
                map.put("count", 1);
                map.put("msg", "生成证书成功");
                map.put("data", null);
                return map;
            }
        }catch (IOException | CertificateException | NoSuchProviderException e) {
            e.printStackTrace();
            map.put("code", -1);
            map.put("count", 1);
            map.put("msg", e);
            map.put("data", null);
            return map;
        }
    }

    @ResponseBody
    @GetMapping(value = "/exportCert/{userName}")
    public ModelMap exportCert(@RequestParam Map<String, Object> params,@PathVariable("userName") String userName) throws SQLException {
        System.out.println(userName);
        ModelMap map = new ModelMap();
        try{
            File file1 = new File(RootcaMng.STORE_PATH + userName + "_sig.pem");
            File file2 = new File(RootcaMng.STORE_PATH + userName + "_enc.pem");
            if (!file1.exists() || !file2.exists()) {
                map.put("code", -1);
                map.put("count", 0);
                map.put("msg", "证书文件不存在");
                map.put("data", null);
                return map;
            } else {
                Runtime run = Runtime.getRuntime();

                BufferedReader br = new BufferedReader(new InputStreamReader(run.exec(RootcaMng.CMD_START + " copy " + userName + "_sig.pem " + RootcaMng.EXPORT_PATH
                        + " & copy " + userName + "_enc.pem " + RootcaMng.EXPORT_PATH).getInputStream()));
                String line = null;
                StringBuffer b = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    b.append(line + "\n");
                }
                map.put("code", 0);
                map.put("count", 1);
                map.put("msg", "导出证书到桌面成功");
                map.put("data", null);
                return map;
            }
        }catch (IOException  e) {
            e.printStackTrace();
            map.put("code", -1);
            map.put("count", 1);
            map.put("msg", e);
            map.put("data", null);
            return map;
        }
    }

    @ResponseBody
    @PostMapping(value = "/cerinfo")
    public void cerinfo(@RequestBody YHUser bankUser, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter wirte = null;
        JSONObject json = new JSONObject();
        try{
            YHUser bankcert = service.getcerinfo(bankUser.getUserName());
            Security.addProvider(new BouncyCastleProvider());
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            FileInputStream in = new FileInputStream(RootcaMng.STORE_PATH + bankUser.getUserName() + "_sig.pem");
            X509Certificate Cert = (X509Certificate) cf.generateCertificate(in);
            wirte = response.getWriter();

            json.put("certdn", Cert.getSubjectDN().getName());
            json.put("sigcert", bankcert.getUserSigCert());
            json.put("enccert", bankcert.getUserEncCert());
            json.put("encpk", bankcert.getUserEncPk());
            json.put("sigpk", bankcert.getUserSigPk());
        } catch (CertificateException | NoSuchProviderException | FileNotFoundException e) {
            e.printStackTrace();
        } finally{
            wirte.print(json);
            wirte.flush();
            wirte.close();
        }response.getWriter().print(json);
    }

    //批量导出
    @ResponseBody
    @GetMapping(value = "/export")
    public ModelMap export(@RequestParam Map<String, Object> params) throws SQLException {
        ModelMap map = new ModelMap();
        try{
            File file = new File(RootcaMng.STORE_PATH);
            if (!file.exists() ) {
                map.put("code", -1);
                map.put("count", 0);
                map.put("msg", "证书文件夹不存在");
                map.put("data", null);
                return map;
            } else {
                String[] filePath = file.list();
                for (int i = 0; i < filePath.length; i++) {
                    File oldFile = new File(RootcaMng.STORE_PATH);
                    File newfile = new File(RootcaMng.EXPORT_PATH);
                    FileInputStream in = new FileInputStream(oldFile + file.separator+  filePath[i]);
                    FileOutputStream out = new FileOutputStream(newfile + file.separator+ filePath[i]);;

                    byte[] buffer=new byte[2097152];
                    int readByte = 0;
                    while((readByte = in.read(buffer)) != -1){
                        out.write(buffer, 0, readByte);
                    }

                    in.close();
                    out.close();
                }

                map.put("code", 0);
                map.put("count", 1);
                map.put("msg", "导出证书到桌面成功");
                map.put("data", null);
                return map;
            }
        }catch (IOException  e) {
            e.printStackTrace();
            map.put("code", -1);
            map.put("count", 1);
            map.put("msg", e);
            map.put("data", null);
            return map;
        }
    }

    @ResponseBody
    @GetMapping(value = "/searchUser")
    public ModelMap searchUser(@RequestParam Map map) throws SQLException {
        String startTime = (String) map.get("startTime");
        String endTime = (String) map.get("endTime");
        String userID = (String) map.get("userID");
        String userName = (String) map.get("userName");
        String userBankID = (String) map.get("userBank");

        List<YHUser> bankUser;
        if(startTime.equals("") && endTime.equals("") && userID.equals("") && userName.equals("") && userBankID.equals("")  ){
            bankUser = repository.findAll();
        }else{
            bankUser = repository.search(startTime,endTime,userID,userName,userBankID);
        }

        ModelMap model = new ModelMap();
        model.put("data", bankUser);
        model.put("code", 0);
        model.put("count", bankUser.size());
        System.out.println(model);
        return model;
    }
}
