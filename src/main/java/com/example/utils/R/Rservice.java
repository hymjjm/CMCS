package com.example.utils.R;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.example.common.Constants;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import static org.math.R.RLogSlf4j.LOGGER;

@Service
public class Rservice {

    /*
    * 雨林图
    * */
    public String getYLxlsx(MultipartFile file) {
//        String rdataName = rdata.substring(0, rdata.length() - 5);
       return runRScriptYL(Constants.RPath + "mutClusterCode.R",file );


    }
    public String runRScriptYL(String RScript, MultipartFile file) {

        RConnection conn = null;
        File tempFile = null;
        try {
//            // 将MultipartFile对象保存为临时文件
//            File tempFile = File.createTempFile("temp", ".xlsx");
            // 指定临时文件的保存目录
//            tempFile = File.createTempFile("temp", ".xlsx", new File("E:\\jjm\\tmp"));
             tempFile = File.createTempFile("YLPicTemp", ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }
            String filePath = tempFile.getAbsolutePath();
            filePath = filePath.replace("\\","/");
            System.out.println("___________________filePath:"+filePath);

            // 获取连接
            conn = new RConnection();
            // 加载R文件
            conn.assign("RScript", RScript);//将RScript（.R的路径）添加到R里面
            conn.eval("source(RScript)");//用source，根据路径加载R文件
            // 画图指令
            String cmd = "plot_genome_data(" + "\"" +filePath + "\"" +")";
            System.out.println("cmd_______________________:"+cmd);
            // 开始执行
//             REXP rResponseObject = conn.eval(cmd);
            REXP rResponseObject = conn.parseAndEval("try(eval(" + cmd + "),silent=TRUE)");
            if (rResponseObject.inherits("try-error")) {
                LOGGER.error("R Serve Eval Exception : " + rResponseObject.asString());
            }
            // 获取返回的base64字符串
            String base64String = rResponseObject.asString();
            System.out.println("___________________________base64String:"+base64String);
            //删除临时文件
            return base64String;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            assert conn != null;
            conn.close();
            if (tempFile != null && tempFile.exists()) {
                try {
                    Files.delete(tempFile.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
