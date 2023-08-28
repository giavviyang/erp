package com.erp.sales.controller;

import com.FTSafe.Dongle;
import com.erp.common.core.domain.AjaxResult;
import com.erp.common.utils.sign.Base64;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-11-07 12:33
 */
@RestController
@RequestMapping("/dongle")
public class DongleController {

    @PostMapping(value = "/add")
    private AjaxResult addEncryptionData(String json) {
        //设备数目
        int[] count = new int[1];
        //加密锁句柄
        long[] handle = new long[1];
        //返回结果校验码
        int nRet = 0;
        Dongle dongle = new Dongle();
        nRet = dongle.Dongle_Enum(null, count);
        if (nRet != Dongle.DONGLE_SUCCESS) {
            System.out.printf("Dongle_Enum error. error code: 0x%08X .\n ", nRet);
            return AjaxResult.error("请插入UKEY加密狗！");
        }
        //打开锁
        nRet = dongle.Dongle_Open(handle, 0);
        if (nRet != Dongle.DONGLE_SUCCESS) {
            System.out.printf("Dongle_Open error. error code: 0x%08X .\n ", nRet);
            return AjaxResult.error("打开加密锁失败，请联系管理员！");
        }
        //验证开发商密码
        int[] nRemain = new int[1];
        String strPin = "FFFFFFFFFFFFFFFF"; //默认开发商密码
        nRet = dongle.Dongle_VerifyPIN(handle[0], dongle.FLAG_ADMINPIN, strPin, nRemain);
        if (nRet != Dongle.DONGLE_SUCCESS) {
            System.out.printf("Dongle_VerifyPIN error [remain cout: %d]. error code: 0x%08X .\n ", nRemain[0], nRet);
            dongle.Dongle_Close(handle[0]);
            return AjaxResult.error("开发商密码验证失败，请联系管理员！");
        }
        //对录入信息进行加密
        String encode = Base64.encode(json.getBytes());
        byte [] data = encode.getBytes();
        //创建数据文件
        byte []licBuffer = new byte[100];
        byte []attrBuffer = new byte[200];
        int []licBufferLen = new int[1];
        int []attrBufferLen = new int[1];
        nRet = dongle.Convert_DATA_LIC_To_Buffer((short)0, (short)1, licBuffer, licBufferLen);
        nRet = dongle.Convert_DATA_FILE_ATTR_To_Buffer(data.length, licBuffer, licBufferLen[0], attrBuffer, attrBufferLen);
        nRet = dongle.Dongle_CreateFile(handle[0], dongle.FILE_DATA, 0x1100, attrBuffer);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            System.out.printf("Dongle_CreateFile error . error code: 0x%08X .\n ", nRet);
            dongle.Dongle_Close(handle[0]);
            if (String.format("0x%08X", nRet).equals("0xF000000E")) {
                return AjaxResult.error("UKEY已经录入过信息，无法再次录入！");
            }
            return AjaxResult.error("创建数据文件失败，请联系管理员！");
        }
        //写文件
        nRet = dongle.Dongle_WriteFile(handle[0],dongle.FILE_DATA,0x1100,0,data, data.length);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            System.out.printf("Dongle_WriteFile error . error code: 0x%08X .\n ", nRet);
            dongle.Dongle_Close(handle[0]);
            return AjaxResult.error("写入文件失败，请联系管理员！");
        }
        //关闭锁
        nRet = dongle.Dongle_Close(handle[0]);
        if (nRet != Dongle.DONGLE_SUCCESS) {
            System.out.printf("Dongle_Close error. error code: 0x%08X \n", nRet);
            return AjaxResult.error("关锁失败，请联系管理员！");
        }
        return AjaxResult.success("信息录入成功！");
    }

    @PostMapping(value = "/check")
    private AjaxResult checkEncryptionData() {
        //设备数目
        int[] count = new int[1];
        //加密锁句柄
        long[] handle = new long[1];
        //返回结果校验码
        int nRet = 0;
        Dongle dongle = new Dongle();
        nRet = dongle.Dongle_Enum(null, count);
        if (nRet != Dongle.DONGLE_SUCCESS) {
            System.out.printf("Dongle_Enum error. error code: 0x%08X .\n ", nRet);
            return new AjaxResult(444,"请插入UKEY加密狗！");
        }
        //打开锁
        nRet = dongle.Dongle_Open(handle, 0);
        if (nRet != Dongle.DONGLE_SUCCESS) {
            System.out.printf("Dongle_Open error. error code: 0x%08X .\n ", nRet);
            return new AjaxResult(444,"打开加密锁失败，请联系管理员！");
        }
        //验证开发商密码
        int[] nRemain = new int[1];
        String strPin = "FFFFFFFFFFFFFFFF"; //默认开发商密码
        nRet = dongle.Dongle_VerifyPIN(handle[0], dongle.FLAG_ADMINPIN, strPin, nRemain);
        if (nRet != Dongle.DONGLE_SUCCESS) {
            System.out.printf("Dongle_VerifyPIN error [remain cout: %d]. error code: 0x%08X .\n ", nRemain[0], nRet);
            dongle.Dongle_Close(handle[0]);
            return new AjaxResult(444,"开发商密码验证失败，请联系管理员！");
        }
        //列数据文件
        byte []fileList = new byte[1024];
        int []fileListLen = new int[1];
        fileListLen[0] = 1024;
        nRet = dongle.Dongle_ListFile(handle[0], dongle.FILE_DATA, fileList, fileListLen);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            System.out.printf("Dongle_ListFile error . error code: 0x%08X .\n ", nRet);
            dongle.Dongle_Close(handle[0]);
            return new AjaxResult(444,"读取数据文件失败，请联系管理员！");
        }
        int index = 0;
        short []fileID = new short[1];
        int  []fileSize = new int[1];
        short []readPriv = new short[1];
        short []writePriv = new short[1];
        while(true)
        {
            nRet = dongle.Get_DATA_FILE_LIST_Info(fileList, fileListLen[0], index, fileID, fileSize, readPriv, writePriv);
            if(nRet != Dongle.DONGLE_SUCCESS)
            {
                if(nRet == Dongle.DONGLE_INVALID_PARAMETER)
                {
                    break;
                }
                else
                {
                    System.out.printf("Get_DATA_FILE_LIST_Info . error code: 0x%08X.\n ", nRet);
                    dongle.Dongle_Close(handle[0]);
                    return new AjaxResult(444,"获取文件信息失败，请联系管理员！");
                }
            }
            System.out.printf("  >>FileSize :%04d.\n", fileSize[0]);
            index++;
        }
        //读文件
        byte []outData = new byte[fileSize[0]];
        nRet = dongle.Dongle_ReadFile(handle[0], 0x1100, 0, outData, fileSize[0]);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            System.out.printf("Dongle_ReadFile error . error code: 0x%08X .\n ", nRet);
            dongle.Dongle_Close(handle[0]);
            return new AjaxResult(444, "获取文件信息失败，请联系管理员！");
        }
        //解码
        byte[] decode = Base64.decode(new String(outData));
        String string = new String(decode);
        //关闭锁
        nRet = dongle.Dongle_Close(handle[0]);
        if (nRet != Dongle.DONGLE_SUCCESS) {
            System.out.printf("Dongle_Close error. error code: 0x%08X \n", nRet);
            return new AjaxResult(444,"关锁失败，请联系管理员！");
        }
        return AjaxResult.success("UKEY认证通过！", string);
    }

    @PostMapping(value = "/del")
    private AjaxResult delEncryptionData() {
        //设备数目
        int[] count = new int[1];
        //加密锁句柄
        long[] handle = new long[1];
        //返回结果校验码
        int nRet = 0;
        Dongle dongle = new Dongle();
        nRet = dongle.Dongle_Enum(null, count);
        if (nRet != Dongle.DONGLE_SUCCESS) {
            System.out.printf("Dongle_Enum error. error code: 0x%08X .\n ", nRet);
            return AjaxResult.error("请插入UKEY加密狗！");
        }
        //打开锁
        nRet = dongle.Dongle_Open(handle, 0);
        if (nRet != Dongle.DONGLE_SUCCESS) {
            System.out.printf("Dongle_Open error. error code: 0x%08X .\n ", nRet);
            return AjaxResult.error("打开加密锁失败，请联系管理员！");
        }
        //验证开发商密码
        int[] nRemain = new int[1];
        String strPin = "FFFFFFFFFFFFFFFF"; //默认开发商密码
        nRet = dongle.Dongle_VerifyPIN(handle[0], dongle.FLAG_ADMINPIN, strPin, nRemain);
        if (nRet != Dongle.DONGLE_SUCCESS) {
            System.out.printf("Dongle_VerifyPIN error [remain cout: %d]. error code: 0x%08X .\n ", nRemain[0], nRet);
            dongle.Dongle_Close(handle[0]);
            return AjaxResult.error("开发商密码验证失败，请联系管理员！");
        }
        //删除文件
        nRet = dongle.Dongle_DeleteFile(handle[0], dongle.FILE_DATA, 0x1100);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            System.out.printf("Dongle_DeleteFile error . error code: 0x%08X .\n ", nRet);
            dongle.Dongle_Close(handle[0]);
            if (String.format("0x%08X", nRet).equals("0xF000000F")) {
                return AjaxResult.error("未找到录入信息！");
            }
            return AjaxResult.error("删除旧文件失败，请联系管理员！");
        }
        //关闭锁
        nRet = dongle.Dongle_Close(handle[0]);
        if (nRet != Dongle.DONGLE_SUCCESS) {
            System.out.printf("Dongle_Close error. error code: 0x%08X \n", nRet);
            return AjaxResult.error("关锁失败，请联系管理员！");
        }
        return AjaxResult.success("删除加密文件成功！");
    }

}
