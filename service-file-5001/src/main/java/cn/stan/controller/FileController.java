package cn.stan.controller;

import cn.stan.common.result.GraceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * @author zyh
 * @date 2023-08-07 下午 06:04
 */
@Slf4j
@RestController
@RequestMapping("file")
public class FileController {

    @GetMapping("hello")
    public GraceResult hello() {
        return GraceResult.ok("hello, file service");
    }

    /**
     * 上传头像
     * @param file
     * @param userId
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("uploadFace")
    public GraceResult uploadFace(@RequestParam("file") MultipartFile file, String userId, HttpServletRequest request) throws IOException {
        String filename = file.getOriginalFilename();
        String fileType = filename.substring(filename.lastIndexOf("."));
        String newFileName = userId + fileType;

        String rootPath = "E:/temp/face";
        String filePath = rootPath + File.separator + newFileName;
        File newFile = new File(filePath);
        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
        }
        file.transferTo(newFile);
        return GraceResult.ok();
    }
}
