package com.sdt.dapp.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sdt.dapp.controller.BaseController;
import com.sdt.dapp.entity.system.RootcaMng;

import com.sdt.dapp.entity.system.YHBank;
import com.sdt.dapp.repository.YHBankRepository;
import com.sdt.dapp.service.system.YHBankService;
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
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.*;



@Controller
@RequestMapping("/YHBank")
public class YHBankController extends BaseController {
    @Autowired
    private PageRequestUtil pageRequestUtil;

    @Autowired
    private YHBankService service;

    @Autowired
    private YHBankRepository repository;

    @ResponseBody
    @GetMapping(value = "/findAll")
    public PagingResult<YHBank> findAll(@RequestParam Map<String, Object> params) {
        Map map = getMap(params);
        Map advQuery = (Map) map.get("advQuery");
        advQuery.put("id", "");
        PageRequest pageRequest = pageRequestUtil.genericPageRequestByRequest(map);
        return service.findYHBank(pageRequest, advQuery, "");
    }


    @ResponseBody
    @GetMapping(value = "/genCertReq")
    public ModelMap genCertReq(HttpServletRequest request) {
        ModelMap map = new ModelMap();
        try {
            String bankid = (String) request.getSession().getAttribute("username");
            Runtime run = Runtime.getRuntime();
            BufferedReader br = new BufferedReader(new InputStreamReader(run.exec(RootcaMng.CMD_START + RootcaMng.GEN_SIG_SK + bankid + "_sig.key & " +
                    RootcaMng.GEN_CER_REQ1 + bankid + "_sig.req -key " + bankid + "_sig.key" + RootcaMng.GEN_CER_REQ2 + " & " + RootcaMng.GEN_ENC_SK + bankid + "_enc.key & " +
                    RootcaMng.GEN_ENCER_REQ1 + bankid + "_enc.req -key " + bankid + "_enc.key" + RootcaMng.GEN_CER_REQ2).getInputStream()));
            String line = null;
            StringBuffer b = new StringBuffer();
            while ((line = br.readLine()) != null) {
                b.append(line + "\n");
            }

            List<YHBank> yhBank = new ArrayList<>();
            repository.deleteByBankid(bankid);
            YHBank bankinfo = new YHBank();
            bankinfo.setBankID(bankid);

            //读取证书申请
            File file = new File(RootcaMng.REQPATH + bankid + "_sig.req");
            BufferedReader reader = null;
            StringBuffer sbf = new StringBuffer();
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();

            bankinfo.setBankSigReq(sbf.toString());
            bankinfo.setBankName("待注册银行");
            bankinfo.setBankType("0");
            bankinfo.setBankEncIndex(-1);
            repository.save(bankinfo);

            map.put("code", 0);
            map.put("count", 1);
            map.put("msg", "生成证书申请成功");
            map.put("data", yhBank);
            return map;
        } catch (IOException e) {
            map.put("code", -1);
            map.put("msg", e);
            return map;
        }
    }

    @ResponseBody
    @PostMapping(value = "/delete")
    public ResponseResult delete(String isStr) {
        System.out.printf("delete :", isStr);
        System.out.println("\n");
        return service.deleteUser(isStr);
    }

    @ResponseBody
    @RequestMapping(value = "/export")
    public ModelMap export(HttpServletRequest request) {
        ModelMap map = new ModelMap();
        try {
            String bankid = (String) request.getSession().getAttribute("username");
            File file = new File(RootcaMng.REQPATH + bankid + "_sig.req");
            if (!file.exists()) {
                map.put("code", -1);
                map.put("count", 0);
                map.put("msg", "文件不存在");
                map.put("data", null);
                return map;
            } else {
                Runtime run = Runtime.getRuntime();

                BufferedReader br = new BufferedReader(new InputStreamReader(run.exec("cmd /c e: & cd E:\\1_ca_new & copy " + bankid + "_sig.req " + RootcaMng.EXPORT_PATH
                        + " & copy " + bankid + "_enc.req " + RootcaMng.EXPORT_PATH).getInputStream()));
                String line = null;
                StringBuffer b = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    b.append(line + "\n");
                }
                map.put("code", 0);
                map.put("count", 1);
                map.put("msg", "导出文件到桌面成功");
                map.put("data", null);
                return map;
            }

        } catch (IOException e) {
            map.put("code", -1);
            map.put("msg", e);
            return map;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/importCert")
    public ModelMap importCert(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        ModelMap map = new ModelMap();
        if (!file.isEmpty()) {
            try {
                String bankid = (String) request.getSession().getAttribute("username");
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(RootcaMng.REQPATH, file.getOriginalFilename())));
                out.write(file.getBytes());
                out.flush();
                out.close();

                YHBank bankinfo = repository.findByBankID(bankid);

                Security.addProvider(new BouncyCastleProvider());
                CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
                FileInputStream in = new FileInputStream(RootcaMng.REQPATH + bankid + "_sig.pem");
                X509Certificate Cert = (X509Certificate) cf.generateCertificate(in);
                FileInputStream enc = new FileInputStream(RootcaMng.REQPATH + bankid + "_enc.pem");
                X509Certificate encCert = (X509Certificate) cf.generateCertificate(enc);

                bankinfo.setBankSigCert(new String(Base64.encode(Cert.getEncoded())));
                bankinfo.setBankSigPk(Cert.getPublicKey().toString());
                bankinfo.setBankEncCert(new String(Base64.encode(encCert.getEncoded())));
                bankinfo.setBankEncPk(encCert.getPublicKey().toString());
                bankinfo.setBankRegDate(new Timestamp(Cert.getNotBefore().getTime()));
                bankinfo.setBankEndDate(new Timestamp(Cert.getNotAfter().getTime()));
                bankinfo.setBankName("中国工商银行");
                bankinfo.setBankType("0");
                bankinfo.setBankEncIndex(1);
                repository.save(bankinfo);
                map.put("code", 0);
                return map;
            } catch (CertificateException e) {
                e.printStackTrace();
                map.put("code", -2);
                return map;
            } catch (NoSuchProviderException | FileNotFoundException e) {
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
    @PostMapping(value = "/cerinfo")
    public void cerinfo(@RequestBody YHBank bankUser, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter wirte = null;
        JSONObject json = new JSONObject();
        try{
            YHBank bankcert = service.getcerinfo(bankUser.getBankID());
            Security.addProvider(new BouncyCastleProvider());
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            FileInputStream in = new FileInputStream(RootcaMng.REQPATH + bankUser.getBankID() + "_sig.pem");
            X509Certificate Cert = (X509Certificate) cf.generateCertificate(in);
            wirte = response.getWriter();

            json.put("certdn", Cert.getSubjectDN().getName());
            json.put("sigcert", bankcert.getBankSigCert());
            json.put("enccert", bankcert.getBankEncCert());
            json.put("encpk", bankcert.getBankEncPk());
            json.put("encskenv", bankcert.getBankSigCert());
            json.put("sigreq", bankcert.getBankSigReq());
        } catch (CertificateException | NoSuchProviderException | FileNotFoundException e) {
            e.printStackTrace();
        } finally{
            wirte.print(json);
            wirte.flush();
            wirte.close();
        }response.getWriter().print(json);
    }
}
