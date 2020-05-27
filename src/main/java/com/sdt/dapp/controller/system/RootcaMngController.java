package com.sdt.dapp.controller.system;

import com.sdt.dapp.controller.BaseController;
import com.sdt.dapp.entity.system.RootcaMng;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

@Controller
@RequestMapping("/RootcaMng")
public class RootcaMngController extends BaseController {

    @ResponseBody
    @GetMapping(value = "/findRootca")
    public ModelMap findRootca(@RequestParam Map<String, Object> params) {
        try {
            ModelMap map = new ModelMap();
            File file = new File(RootcaMng.PATH);
            if (!file.exists()) {
                map.put("code", 0);
                map.put("count", 0);
                map.put("data", null);
            } else {
                RootcaMng cert = new RootcaMng();
                // 使用BC解析X.509证书
                Security.addProvider(new BouncyCastleProvider());
                CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
                FileInputStream in = new FileInputStream(RootcaMng.PATH);
                X509Certificate Cert = (X509Certificate) cf.generateCertificate(in);

                Date issueTime = Cert.getNotBefore();
                Date endTime = Cert.getNotAfter();

                PublicKey pkVer = Cert.getPublicKey();
                SM3Digest digest = new SM3Digest();
                digest.update(pkVer.getEncoded(), 0, pkVer.getEncoded().length);
                byte[] hash = new byte[digest.getDigestSize()];
                digest.doFinal(hash, 0);
                String result = ByteUtils.toHexString(hash);

                cert.setIssueTime(issueTime);
                cert.setEndTime(endTime);
                cert.setCertName("央行根证书");
                cert.setPkVer(result);
                List<RootcaMng> list = new ArrayList<>();
                list.add(cert);
                map.put("code", 0);
                map.put("count", 1);
                map.put("data", list);
            }
            return map;
        } catch (CertificateException | FileNotFoundException | NoSuchProviderException e) {
            e.printStackTrace();
            ModelMap map = new ModelMap();
            map.put("code", 0);
            map.put("count", 0);
            map.put("data", null);
            return map;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/importRootca")
    public ModelMap importRootca(@RequestParam("file") MultipartFile file) {
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
            } catch (IOException e) {
                map.put("code", -1);
                return map;
            }
        } else {
            map.put("code", -1);
            return map;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/delete")
    public ModelMap delete(String isStr) {
        ModelMap map = new ModelMap();
        try {
            if (isStr != null && !isStr.equals("")) {
                File file = new File(RootcaMng.PATH);
                // 路径为文件且不为空则进行删除
                if (file.isFile() && file.exists()) {
                    System.gc();//启动jvm垃圾回收
                    file.delete();
                }
            }
            map.put("code", 0);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", -1);
            return map;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/export")
    public ModelMap export(@RequestParam(required=false) HttpServletRequest request) {
        ModelMap map = new ModelMap();
        try {
            File file = new File(RootcaMng.PATH);
            if (!file.exists()) {
                map.put("code", -1);
                map.put("count", 0);
                map.put("msg", "文件不存在");
                map.put("data", null);
                return map;
            } else {
                Runtime run = Runtime.getRuntime();

                BufferedReader br = new BufferedReader(new InputStreamReader(run.exec("cmd /c e: & cd E:\\1_ca_new & copy cacert.pem "+ RootcaMng.EXPORT_PATH ).getInputStream()));
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

}




