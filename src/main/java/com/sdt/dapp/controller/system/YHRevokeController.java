package com.sdt.dapp.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.sdt.dapp.controller.BaseController;
import com.sdt.dapp.entity.system.RootcaMng;
import com.sdt.dapp.entity.system.YHRevoke;
import com.sdt.dapp.entity.system.YHUser;
import com.sdt.dapp.repository.YHUserRepository;
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
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/YHRevoke")
public class YHRevokeController extends BaseController {
    @Autowired
    private YHUserRepository repository;

    @ResponseBody
    @GetMapping(value = "/findAll")
    public ModelMap findAll(@RequestParam Map<String, Object> params) {
        ModelMap map = new ModelMap();
        try {
            List<YHUser> userList = repository.findAll();
            List<YHRevoke> revokeList = new ArrayList<>();
            for (YHUser item : userList) {
                YHRevoke revoke = new YHRevoke();
                revoke.setId(item.getId());
                revoke.setUserBankID(item.getUserBankID());
                revoke.setUserID(item.getUserID());
                revoke.setUserName(item.getUserName());
                revoke.setUserPhone(item.getUserPhone());
                revoke.setUserRegDate(item.getUserRegDate());
                revoke.setBankEndDate(item.getBankEndDate());
                File file = new File(RootcaMng.REQPATH + item.getUserName() + "_enc.pem");
                if (!file.exists()) {
                    revoke.setState("1");
                } else {
                    revoke.setState("0");
                }
                Timestamp now = new Timestamp(System.currentTimeMillis());
                int judgeTime = now.compareTo(item.getBankEndDate());
                if (judgeTime < 0) {
                    revoke.setJudge("0");
                } else {
                    revoke.setJudge("1");
                }
                revokeList.add(revoke);
            }
            map.put("data", revokeList);
            map.put("code", 0);
            map.put("msg", "查询成功");
            map.put("count", revokeList.size());
            return map;
        } catch (Exception ignored) {
            map.put("data", null);
            map.put("code", -1);
            map.put("msg", ignored);
            map.put("count", 0);
            return map;
        }
    }

    @ResponseBody
    @GetMapping(value = "/searchUser")
    public ModelMap searchUser(@RequestParam Map map) throws SQLException {
        String startTime = (String) map.get("startTime");
        String endTime = (String) map.get("endTime");
        String userID = (String) map.get("userID");
        String userBankID = (String) map.get("userBankID");

        List<YHUser> bankUser;
        if (startTime.equals("") && endTime.equals("") && userID.equals("") && userBankID.equals("")) {
            bankUser = repository.findAll();
        } else {
            bankUser = repository.searchNoUserName(startTime, endTime, userID, userBankID);
        }
        ModelMap model = new ModelMap();
        List<YHRevoke> revokeList = new ArrayList<>();
        for (YHUser item : bankUser) {
            YHRevoke revoke = new YHRevoke();
            revoke.setId(item.getId());
            revoke.setUserBankID(item.getUserBankID());
            revoke.setUserID(item.getUserID());
            revoke.setUserName(item.getUserName());
            revoke.setUserPhone(item.getUserPhone());
            revoke.setUserRegDate(item.getUserRegDate());
            revoke.setBankEndDate(item.getBankEndDate());
            File file = new File(RootcaMng.REQPATH + item.getUserName() + "_enc.pem");
            if (!file.exists()) {
                revoke.setState("1");
            } else {
                revoke.setState("0");
            }
            Timestamp now = new Timestamp(System.currentTimeMillis());
            int judgeTime = now.compareTo(item.getBankEndDate());
            if (judgeTime < 0) {
                revoke.setJudge("0");
            } else {
                revoke.setJudge("1");
            }
            revokeList.add(revoke);
        }
        model.put("data", revokeList);
        model.put("code", 0);
        model.put("msg", "查询成功");
        model.put("count", revokeList.size());
        return model;
    }

    @ResponseBody
    @PostMapping(value = "/revoke")
    public ResponseResult revoke(@RequestBody Map map) throws IOException {

        String userName = map.get("userName").toString();
        Runtime run = Runtime.getRuntime();

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(run.exec(RootcaMng.CMD_START + " del .\\" + userName + "_enc.pem & del .\\" + userName + "_sig.pem").getInputStream()));
            String line = null;
            StringBuffer b = new StringBuffer();
            while ((line = br.readLine()) != null) {
                b.append(line + "\n");
            }
            return new ResponseResult("撤销证书成功", null, ResponseResult.getSUCCESS());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult("撤销证书失败", e, ResponseResult.getFAIL());
        }
    }
}
